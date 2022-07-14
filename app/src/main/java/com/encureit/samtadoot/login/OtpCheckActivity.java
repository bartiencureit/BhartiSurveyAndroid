package com.encureit.samtadoot.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.encureit.samtadoot.Helpers.GlobalHelper;
import com.encureit.samtadoot.R;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.databinding.ActivityOtpCheckBinding;
import com.encureit.samtadoot.features.dashboard.DashboardActivity;
import com.encureit.samtadoot.lib.AppKeys;
import com.encureit.samtadoot.lib.ScreenHelper;
import com.encureit.samtadoot.listeners.MessageListener;
import com.encureit.samtadoot.models.AssignDetails;
import com.encureit.samtadoot.models.OtherValues;
import com.encureit.samtadoot.models.QuestionOption;
import com.encureit.samtadoot.models.QuestionType;
import com.encureit.samtadoot.models.QuestionValidation;
import com.encureit.samtadoot.models.SurveyQuestion;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.SurveyType;
import com.encureit.samtadoot.network.responsemodel.OtherValuesResponseModel;
import com.encureit.samtadoot.network.responsemodel.OtpCheckResponseModel;
import com.encureit.samtadoot.models.contracts.OtpContract;
import com.encureit.samtadoot.network.responsemodel.QuestionOptionResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionValidationResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyQuestionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveySectionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.UserAssignedDetailsResponseModel;
import com.encureit.samtadoot.presenter.OtpPresenter;

import java.util.List;

public class OtpCheckActivity extends BaseActivity implements OtpContract.ViewModel, MessageListener {
    public ActivityOtpCheckBinding mBinding;
    private String loginUserId;
    private OtpPresenter mPresenter;
    private GlobalHelper helper;
    private static final String TAG = "OtpCheckActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mBinding = ActivityOtpCheckBinding.inflate(getLayoutInflater());
            setContentView(mBinding.getRoot());
            Intent intent = getIntent();
            if(intent.hasExtra(AppKeys.loginUserId)) {
                loginUserId = intent.getStringExtra(AppKeys.loginUserId);
            }
            mBinding.pinview.setValue("1111");
            helper = new GlobalHelper(this);
            mPresenter = new OtpPresenter(this,OtpCheckActivity.this);
            mPresenter.rootView = mBinding.getRoot();
            mPresenter.loginId = loginUserId;
            mBinding.setPresenter(mPresenter);
        } catch (Exception e) {
            Log.e(TAG, "onCreate: "+e.getMessage());
        }
    }

    @Override
    public void getOtp(OtpCheckResponseModel otpCheckResponseModel) {
        //save date to shared preference
        try {
            helper.getSharedPreferencesHelper().setLoginDateTimeData(otpCheckResponseModel.getLoginDate());
            if(isInternetConnected()) {
                //startProgressDialog(mBinding.getRoot());
                mPresenter.getSurveySectionFields();
            } else {
                showNotInternetConnected(new OnInternetConnectedListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "getOtp: "+e.getMessage());
        }
    }

    /**
     * @commentedBy Swapna
     * Tried to disable view when snackbar shows and enable view when snack bar dismiss
     * but it's not working
     * */
//    public void enableForm() {
//        mBinding.pinview.setEnabled(true);
//        mBinding.btnSubmit.setEnabled(true);
//    }
//
//    public void disableForm() {
//        mBinding.pinview.setEnabled(false);
//        mBinding.btnSubmit.setEnabled(false);
//    }

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
        mPresenter.getUserAssignedDetails(loginUserId);
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
        DatabaseUtil.on().getOtherValuesDao().nukeTable();
        DatabaseUtil.on().insertAllOtherValues(other_values);
        dismissProgressDialog();
        ScreenHelper.showGreenSnackBar(mBinding.getRoot(),getResources().getString(R.string.sucessfull_login));
        goToDashboard();
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

    /**
     * @date 3-3-2022
     * navigates to dashboard activity
     */
    private void goToDashboard() {
        startActivityOnTop(DashboardActivity.class,false);
    }

    @Override
    public void showOtpFailed(String error) {
        dismissProgressDialog();
        ScreenHelper.showErrorSnackBar(mBinding.getRoot(),error);
    }

    @Override
    public void messageReceived(String message) {
        Log.i("OTPCA::", "OTP_: " + message);
        if (!message.equalsIgnoreCase("")) {
            if (message.contains(":")) {
                String[] data = message.split(":");
                message = data[1];
                mBinding.pinview.setValue(message);
            }
            Log.i("OCA:","OTP Received : " + message);
        } else {
            Log.i("OCA:","OTP not Received : ");
        }
    }
}