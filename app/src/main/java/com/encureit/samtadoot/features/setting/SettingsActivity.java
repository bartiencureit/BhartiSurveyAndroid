package com.encureit.samtadoot.features.setting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.encureit.samtadoot.Helpers.GlobalHelper;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.databinding.ActivitySettingsBinding;
import com.encureit.samtadoot.features.dashboard.DashboardActivity;
import com.encureit.samtadoot.lib.ScreenHelper;
import com.encureit.samtadoot.models.AssignDetails;
import com.encureit.samtadoot.models.OtherValues;
import com.encureit.samtadoot.models.QuestionOption;
import com.encureit.samtadoot.models.QuestionType;
import com.encureit.samtadoot.models.QuestionValidation;
import com.encureit.samtadoot.models.SurveyQuestion;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.SurveyType;
import com.encureit.samtadoot.models.contracts.SettingsContract;
import com.encureit.samtadoot.network.responsemodel.OtherValuesResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionOptionResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionValidationResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyQuestionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveySectionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.UserAssignedDetailsResponseModel;
import com.encureit.samtadoot.presenter.SettingsPresenter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static com.encureit.samtadoot.utils.CommonUtils.getCurrentDate;

public class SettingsActivity extends BaseActivity implements SettingsContract.ViewModel {
    public ActivitySettingsBinding mBinding;
    private SettingsPresenter mPresenter;
    public GlobalHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        helper = new GlobalHelper(this);
        mPresenter = new SettingsPresenter(SettingsActivity.this, this);
        mBinding.setPresenter(mPresenter);
        mPresenter.setUpData(helper);
        mBinding.toolbar.backArrow.setVisibility(View.VISIBLE);
        mBinding.toolbar.backArrow.setOnClickListener(view -> startActivityOnTop(DashboardActivity.class,true));
    }

    @Override
    public void syncFinished() {
        startProgressDialog(mBinding.getRoot());
        mPresenter.getSurveySectionFields();
    }

    @Override
    public void syncFormsFinished() {
        DatabaseUtil.on().getCandidateSurveyStatusDetailsDao().nukeTable();
        helper.getSharedPreferencesHelper().setLastSyncTimeCandidateData(getCurrentDate());
        ScreenHelper.showGreenSnackBar(mBinding.getRoot(),"Sync Forms Finished");
        mPresenter.setUpData(helper);
    }

    @Override
    public void logoutFinished() {
        Snackbar.make(mBinding.getRoot(),"Successfully logged out", BaseTransientBottomBar.LENGTH_LONG).show();

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(startMain);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mActivity.finishAffinity();
        } else {
            mActivity.finish();
        }

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void getSurveySectionFieldsResponse(SurveySectionResponseModel surveySectionResponseModel) {
        addSurveySectionToDb(surveySectionResponseModel.getSurvey_section());
    }

    /**
     * @date 3-3-2022
     * Empty Survey section table and
     * Add Survey Section to db
     * @param survey_section
     */
    private void addSurveySectionToDb(List<SurveySection> survey_section) {
        DatabaseUtil.on().getSurveySectionDao().nukeTable();
        DatabaseUtil.on().insertAllSurveySection(survey_section);
        mPresenter.getSurveyQuestionField();
    }

    @Override
    public void getSurveyQuestionFieldResponse(SurveyQuestionResponseModel surveyQuestionResponseModel) {
        addSurveyQuestionToDb(surveyQuestionResponseModel.getSurvey_question_list());
    }

    /**
     * @date 3-3-2022
     * Empty Survey Question table and
     * Add Survey Question to db
     * @param survey_question_list
     */
    private void addSurveyQuestionToDb(List<SurveyQuestion> survey_question_list) {
        DatabaseUtil.on().getSurveyQuestionDao().nukeTable();
        DatabaseUtil.on().insertAllSurveyQuestion(survey_question_list);
        mPresenter.getQuestionOptionField();
    }

    @Override
    public void getQuestionOptionFieldResponse(QuestionOptionResponseModel questionOptionResponseModel) {
        addQuestionOptionToDb(questionOptionResponseModel.getSurvey_question_list());
    }

    /**
     * @date 3-3-2022
     * Empty Question Option table and
     * Add Question Option List to db
     * @param questionOptions
     */
    private void addQuestionOptionToDb(List<QuestionOption> questionOptions) {
        DatabaseUtil.on().getQuestionOptionDao().nukeTable();
        DatabaseUtil.on().insertAllQuestionOption(questionOptions);
        mPresenter.getQuestionTypesField();
    }

    @Override
    public void getQuestionTypesFieldResponse(QuestionTypeResponseModel questionTypeResponseModel) {
        addQuestionTypesToDb(questionTypeResponseModel.getQuestion_type());
    }

    /**
     * @date 3-3-2022
     * Empty Question types table and
     * Add Question types List to db
     * @param questionTypes
     */
    private void addQuestionTypesToDb(List<QuestionType> questionTypes) {
        DatabaseUtil.on().getQuestionTypeDao().nukeTable();
        DatabaseUtil.on().insertAllQuestionType(questionTypes);
        mPresenter.getQuestionValidationFields();
    }

    @Override
    public void getQuestionValidationFieldsResponse(QuestionValidationResponseModel questionValidationResponseModel) {
        addQuestionValidationsToDb(questionValidationResponseModel.getQues_validation_list());
    }

    /**
     * @date 3-3-2022
     * Empty Question validation table and
     * Add Question validation List to db
     * @param questionValidations
     */
    private void addQuestionValidationsToDb(List<QuestionValidation> questionValidations) {
        DatabaseUtil.on().getQuestionValidationDao().nukeTable();
        DatabaseUtil.on().insertAllQuestionValidation(questionValidations);
        mPresenter.getUserAssignedDetails(helper.getSharedPreferencesHelper().getLoginUserId());
    }

    @Override
    public void getUserAssignedDetails(UserAssignedDetailsResponseModel userAssignedDetailsResponseModel) {
        addUserAssignedDetailsToDb(userAssignedDetailsResponseModel.getAssign_details());
    }

    /**
     * @date 3-3-2022
     * Empty UserAssignedDetails table and
     * Add UserAssignedDetails List to db
     * @param assignDetails
     */
    private void addUserAssignedDetailsToDb(List<AssignDetails> assignDetails) {
        DatabaseUtil.on().getAssignDetailsDao().nukeTable();
        DatabaseUtil.on().insertAllUserAssignedDetails(assignDetails);
        mPresenter.getSurveyMaster();
    }

    @Override
    public void getSurveyMasterResponse(SurveyTypeResponseModel surveyTypeResponseModel) {
        addSurveyMasterToDb(surveyTypeResponseModel.getSurvey_type());
    }

    @Override
    public void getOtherValuesResponse(OtherValuesResponseModel otherValuesResponseModel) {
        addOtherValuesToDb(otherValuesResponseModel.getOther_values());
    }

    private void addOtherValuesToDb(List<OtherValues> other_values) {
        helper.getSharedPreferencesHelper().setLastSyncTimeAllData(getCurrentDate());
        DatabaseUtil.on().getOtherValuesDao().nukeTable();
        DatabaseUtil.on().insertAllOtherValues(other_values);
        dismissProgressDialog();
        mPresenter.setUpData(helper);
    }

    /**
     * @date 3-3-2022
     * Empty SurveyMaster table and
     * Add SurveyMaster List to db
     * @param surveyTypes
     */
    private void addSurveyMasterToDb(List<SurveyType> surveyTypes) {
        DatabaseUtil.on().getSurveyTypeDao().nukeTable();
        DatabaseUtil.on().insertAllSurveyTypes(surveyTypes);
        mPresenter.getOtherValues();
    }


    @Override
    public void showResponseFailed(String error) {
        ScreenHelper.showErrorSnackBar(mBinding.getRoot(),error);
    }

    @Override
    public void showResponseNoData(String message) {
        helper.getSharedPreferencesHelper().setLastSyncTimeCandidateData(getCurrentDate());
        ScreenHelper.showGreenSnackBar(mBinding.getRoot(),message);
        mPresenter.setUpData(helper);
    }
}