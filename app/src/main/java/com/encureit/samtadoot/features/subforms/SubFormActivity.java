package com.encureit.samtadoot.features.subforms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.encureit.samtadoot.Helpers.GPSTracker;
import com.encureit.samtadoot.Helpers.GlobalHelper;
import com.encureit.samtadoot.Helpers.PermissionsUtil;
import com.encureit.samtadoot.R;
import com.encureit.samtadoot.adapters.DropDownArrayAdapter;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.custom.HeaderTextView;
import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.databinding.ActivitySubFormBinding;
import com.encureit.samtadoot.databinding.SingleAddAnotherItemBinding;
import com.encureit.samtadoot.databinding.SingleAddAnotherParentItemBinding;
import com.encureit.samtadoot.databinding.SingleCheckBoxesLayoutBinding;
import com.encureit.samtadoot.databinding.SingleDropDownListLayoutBinding;
import com.encureit.samtadoot.databinding.SingleDropDownMultiSelectListLayoutBinding;
import com.encureit.samtadoot.databinding.SingleFotoViewBinding;
import com.encureit.samtadoot.databinding.SingleInputBoxLayoutBinding;
import com.encureit.samtadoot.databinding.SingleLinkedChildDataBinding;
import com.encureit.samtadoot.databinding.SingleMultipleInputBoxLayoutBinding;
import com.encureit.samtadoot.databinding.SingleMultipleInputBoxParentLayoutBinding;
import com.encureit.samtadoot.databinding.SingleRadioButtonsLayoutBinding;
import com.encureit.samtadoot.features.dashboard.DashboardActivity;
import com.encureit.samtadoot.lib.AppKeys;
import com.encureit.samtadoot.lib.ScreenHelper;
import com.encureit.samtadoot.models.CandidateDetails;
import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;
import com.encureit.samtadoot.models.QuestionOption;
import com.encureit.samtadoot.models.SurveyQuestion;
import com.encureit.samtadoot.models.SurveyQuestionWithData;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.SurveyType;
import com.encureit.samtadoot.models.contracts.SubFormContract;
import com.encureit.samtadoot.presenter.SubFormPresenter;
import com.encureit.samtadoot.utils.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import static com.encureit.samtadoot.utils.CommonUtils.getCurrentDate;
import static com.encureit.samtadoot.utils.CommonUtils.getRandomAlphaNumericString;

import javax.xml.validation.Validator;

public class SubFormActivity extends BaseActivity implements SubFormContract.ViewModel {
    private ActivitySubFormBinding mBinding;
    private SubFormPresenter mPresenter;
    private SingleAddAnotherItemBinding[] mBindingChild;
    private List<SurveyQuestionWithData> list;
    private int i = 0;
    private final List<HashMap<String, AppCompatEditText>> editTexts = new ArrayList<>();
    private final List<HashMap<String, HashMap<String, AppCompatEditText>>> optionEditTexts = new ArrayList<>();
    private final List<HashMap<String, Spinner>> spinners = new ArrayList<>();
    private final List<HashMap<String, MultiSpinnerSearch>> multiSpinnerSearches = new ArrayList<>();
    private final List<HashMap<String, RadioButton>> radioButtons = new ArrayList<>();
    private final List<HashMap<String, CheckBox>> checkBoxes = new ArrayList<>();
    private final List<HashMap<String, String>> multiEditTextValues = new ArrayList<>();
    //private List<Object> requiredFields = new ArrayList<>();
    private LinearLayout mainLinear;
    private SurveySection section;
    private SurveyType surveyType;
    private String formId;
    private LocationManager locationManager;
    private double latitude, longitude;
    private String start_date;
    private String end_date;
    private GlobalHelper helper;

    private File fileImage = null;
    private Uri uri;
    private ActivityResultLauncher<Uri> openCamera;
    private boolean hasFoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySubFormBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Intent intent = mActivity.getIntent();
        if (intent.hasExtra(AppKeys.SURVEY_SECTION)) {
            section = intent.getParcelableExtra(AppKeys.SURVEY_SECTION);
            surveyType = intent.getParcelableExtra(AppKeys.SURVEY_TYPE);
        }
        helper = new GlobalHelper(SubFormActivity.this);
        mPresenter = new SubFormPresenter(SubFormActivity.this, this);
        mBinding.setPresenter(mPresenter);
        mPresenter.startSubForm(section);

        formId = getRandomAlphaNumericString();
        start_date = getCurrentDate();

        mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(SubFormActivity.this).create();
                dialog.setTitle(getResources().getString(R.string.message_on_save_button_pressed));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (validateData()) {
                            saveData();
                        } else {
                            ScreenHelper.showErrorSnackBar(mBinding.getRoot(), getResources().getString(R.string.invalid_entry));
                        }
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private boolean validateData() {
        if (hasFoto) {
            if (fileImage != null) {
                return true;
            } else {
                return false;
            }
        } else {
            getAllChildView();

            int filled_edittext_count = 0;
            for (int j = 0; j < editTexts.size(); j++) {
                HashMap<String, AppCompatEditText> map = editTexts.get(j);
                for (Map.Entry<String, AppCompatEditText> entry : map.entrySet()) {
                    String str_question = entry.getKey();
                    SurveyQuestion question = DatabaseUtil.on().getSurveyQuestionFromQuestion(str_question);
                    if (question.getRequired() != null && question.getRequired().equalsIgnoreCase("true")) {
                        AppCompatEditText editText = entry.getValue();
                        if (!TextUtils.isEmpty(editText.getText().toString())) {
                            filled_edittext_count++;
                        }
                    } else {
                        filled_edittext_count++;
                    }
                }
            }

            int filled_option_edittext_count = 0;
            for (int j = 0; j < optionEditTexts.size(); j++) {
                HashMap<String, HashMap<String,AppCompatEditText>> map = optionEditTexts.get(j);
                for (Map.Entry<String, HashMap<String,AppCompatEditText>> entry : map.entrySet()) {
                    for (Map.Entry<String,AppCompatEditText> entry1 : entry.getValue().entrySet()) {
                        String str_question = entry.getKey();
                        SurveyQuestion question = DatabaseUtil.on().getSurveyQuestionFromQuestion(str_question);
                        if (question != null && question.getRequired() != null && question.getRequired().equalsIgnoreCase("true")) {
                            AppCompatEditText editText = entry1.getValue();
                            if (!TextUtils.isEmpty(editText.getText().toString())) {
                                filled_option_edittext_count++;
                            }
                        } else {
                            filled_option_edittext_count++;
                        }
                    }
                }
            }

            int filled_radio_count = 0;
            for (int j = 0; j < radioButtons.size(); j++) {
                HashMap<String, RadioButton> map = radioButtons.get(j);
                for (Map.Entry<String, RadioButton> entry : map.entrySet()) {
                    String str_question = entry.getKey();
                    SurveyQuestion question = DatabaseUtil.on().getSurveyQuestionFromQuestion(str_question);
                    if (question.getRequired() != null && question.getRequired().equalsIgnoreCase("true")) {
                        RadioButton radioButton = entry.getValue();
                        if (radioButton.isChecked()) {
                            filled_radio_count++;
                        }
                    } else {
                        filled_radio_count++;
                    }
                }
            }

            return filled_edittext_count == editTexts.size() && filled_option_edittext_count == optionEditTexts.size() && filled_radio_count == radioButtons.size();
        }

    }

    /**
     * @date 10-3-2022
     * Save data to sqlite
     */
    private void saveData() {
        end_date = getCurrentDate();
        //getAllChildView();
        uploadData();
    }

    /**
     * @date 10-3-2022
     * Save form to sqlite
     */
    private void uploadData() {
        //save data to candidate details survey status
        saveCandidateSurveyStatusDetails();
        if (hasFoto) {
            saveFotoToDb();
            ScreenHelper.showGreenSnackBar(mBinding.getRoot(), getResources().getString(R.string.data_sync_finished_successfully));
            startActivityOnTop(DashboardActivity.class, false);
        } else {
            saveInputBoxDataToDb();
            saveOptionInputBoxDataToDb();
            saveDropDownDataToDb();
            saveRadioButtonDataToDb();
            saveCheckBoxDataToDb();
            saveMultiInputBoxDataToDb();
            ScreenHelper.showGreenSnackBar(mBinding.getRoot(), getResources().getString(R.string.data_sync_finished_successfully));
            startActivityOnTop(DashboardActivity.class, false);
        }
    }

    private void saveFotoToDb() {
        CandidateDetails candidateDetails = new CandidateDetails();
        candidateDetails.setSurvey_master_id(surveyType.getForm_unique_id());
        candidateDetails.setSurvey_section_id(section.getSurveySection_ID());
        candidateDetails.setSurvey_que_id("");
        candidateDetails.setSurvey_que_option_id("0");
        candidateDetails.setSurvey_que_values(fileImage.getAbsolutePath());
        candidateDetails.setFormID(formId);
        candidateDetails.setCurrent_Form_Status("GY");
        candidateDetails.setAge_value("0");
        candidateDetails.setSurvey_StartDate(start_date);
        candidateDetails.setSurvey_EndDate(end_date);
        candidateDetails.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
        candidateDetails.setLatitude(Double.toString(latitude));
        candidateDetails.setLongitude(Double.toString(longitude));
        candidateDetails.setHasImage(true);
        DatabaseUtil.on().getCandidateDetailsDao().insert(candidateDetails);
    }

    private void saveMultiInputBoxDataToDb() {
        List<CandidateDetails> multiInputCandidateDetails = new ArrayList<>();

        //render through multi input hashmap list
        for (int j = 0; j < multiEditTextValues.size(); j++) {
            HashMap<String, String> map = multiEditTextValues.get(j);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String str_question = entry.getKey();
                String value = entry.getValue();
                //Question was saved in hashmap i.e header text value as key
                //We have get question id using question text from database
                //QuestionOption questionOption = DatabaseUtil.on().getQuestionIdFromQuestionOptionText(str_question);
                QuestionOption questionOption = DatabaseUtil.on().getQuestionOptionDao().getQuestionOptionByID(str_question);
                if (questionOption != null) {
                    CandidateDetails candidateDetails = new CandidateDetails();
                    candidateDetails.setSurvey_master_id(surveyType.getForm_unique_id());
                    candidateDetails.setSurvey_section_id(section.getSurveySection_ID());
                    candidateDetails.setSurvey_que_id(questionOption.getSurveyQuestion_ID());
                    candidateDetails.setSurvey_que_option_id(questionOption.getQNAOption_ID());
                    candidateDetails.setSurvey_que_values(value);
                    candidateDetails.setFormID(formId);
                    candidateDetails.setCurrent_Form_Status("GY");
                    candidateDetails.setAge_value("0");
                    candidateDetails.setSurvey_StartDate(start_date);
                    candidateDetails.setSurvey_EndDate(end_date);
                    candidateDetails.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
                    candidateDetails.setLatitude(Double.toString(latitude));
                    candidateDetails.setLongitude(Double.toString(longitude));
                    multiInputCandidateDetails.add(candidateDetails);
                }
            }
        }
        DatabaseUtil.on().insertAllCandidateDetails(multiInputCandidateDetails);
    }

    /**
     * @date 10-3-2022
     * Save CandidateSurveyStatusDetails to room db
     */
    private void saveCandidateSurveyStatusDetails() {
        CandidateSurveyStatusDetails candidateSurveyStatusDetails = new CandidateSurveyStatusDetails();
        candidateSurveyStatusDetails.setFormID(formId);
        candidateSurveyStatusDetails.setSurvey_section_id(section.getSurveySection_ID());
        if (DatabaseUtil.on().isLastSurveySection(section.getSurveySection_ID(), formId)) {
            candidateSurveyStatusDetails.setSurvey_status(getResources().getString(R.string.completed));
        } else {
            candidateSurveyStatusDetails.setSurvey_status(getResources().getString(R.string.pending));
        }
        candidateSurveyStatusDetails.setStart_date(start_date);
        candidateSurveyStatusDetails.setStart_date(end_date);
        DatabaseUtil.on().getCandidateSurveyStatusDetailsDao().insert(candidateSurveyStatusDetails);
    }

    /**
     * @date 10-3-2022
     * save edittext data to sqlite db
     */
    private void saveInputBoxDataToDb() {
        List<CandidateDetails> inputCandidateDetails = new ArrayList<>();
        //render through edittext hashmap list
        for (int j = 0; j < editTexts.size(); j++) {
            HashMap<String, AppCompatEditText> map = editTexts.get(j);
            for (Map.Entry<String, AppCompatEditText> entry : map.entrySet()) {
                String str_question = entry.getKey();
                AppCompatEditText editText = entry.getValue();
                //Question was saved in hashmap i.e header text value as key
                //We have get question id using question text from database
                String questionId = DatabaseUtil.on().getQuestionIdFromQuestion(str_question);
                String questionValue = editText.getText().toString();
                CandidateDetails candidateDetails = new CandidateDetails();
                candidateDetails.setSurvey_master_id(surveyType.getForm_unique_id());
                candidateDetails.setSurvey_section_id(section.getSurveySection_ID());
                candidateDetails.setSurvey_que_id(questionId);
                candidateDetails.setSurvey_que_option_id("0");
                candidateDetails.setSurvey_que_values(questionValue);
                candidateDetails.setFormID(formId);
                candidateDetails.setCurrent_Form_Status("GY");
                candidateDetails.setAge_value("0");
                candidateDetails.setSurvey_StartDate(start_date);
                candidateDetails.setSurvey_EndDate(end_date);
                candidateDetails.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
                candidateDetails.setLatitude(Double.toString(latitude));
                candidateDetails.setLongitude(Double.toString(longitude));
                if (!(editText.getTag().toString().equalsIgnoreCase("-1"))) {
                    candidateDetails.setIndex_if_linked_question(Integer.parseInt(editText.getTag().toString()));
                }
                inputCandidateDetails.add(candidateDetails);
            }
        }
        DatabaseUtil.on().insertAllCandidateDetails(inputCandidateDetails);
    }

    /**
     * @date 10-6-2022
     * save option edittext data to sqlite db
     */
    private void saveOptionInputBoxDataToDb() {
        List<CandidateDetails> inputCandidateDetails = new ArrayList<>();

        //render through edittext hashmap list
        for (int j = 0; j < optionEditTexts.size(); j++) {
            HashMap<String, HashMap<String, AppCompatEditText>> map = optionEditTexts.get(j);
            for (Map.Entry<String, HashMap<String, AppCompatEditText>> entry : map.entrySet()) {
                String str_question = entry.getKey();
                HashMap<String, AppCompatEditText> editTextMap = entry.getValue();

                for (Map.Entry<String, AppCompatEditText> edtEntry : editTextMap.entrySet()) {
                    //Question was saved in hashmap i.e header text value as key
                    //We have get question id using question text from database
                    AppCompatEditText editText = edtEntry.getValue();
                    String questionId = DatabaseUtil.on().getQuestionIdFromQuestion(str_question);
                    String questionValue = editText.getText().toString();

                    CandidateDetails candidateDetails = new CandidateDetails();
                    candidateDetails.setSurvey_master_id(surveyType.getForm_unique_id());
                    candidateDetails.setSurvey_section_id(section.getSurveySection_ID());
                    candidateDetails.setSurvey_que_id(questionId);
                    candidateDetails.setSurvey_que_option_id(edtEntry.getKey());
                    candidateDetails.setSurvey_que_values(questionValue);
                    candidateDetails.setFormID(formId);
                    candidateDetails.setCurrent_Form_Status("GY");
                    candidateDetails.setAge_value("0");
                    candidateDetails.setSurvey_StartDate(start_date);
                    candidateDetails.setSurvey_EndDate(end_date);
                    candidateDetails.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
                    candidateDetails.setLatitude(Double.toString(latitude));
                    candidateDetails.setLongitude(Double.toString(longitude));
                    if (!(editText.getTag().toString().equalsIgnoreCase("-1"))) {
                        candidateDetails.setIndex_if_linked_question(Integer.parseInt(editText.getTag().toString()));
                    }
                    inputCandidateDetails.add(candidateDetails);
                }
            }
        }
        DatabaseUtil.on().insertAllCandidateDetails(inputCandidateDetails);
    }

    /**
     * @date 10-3-2022
     * save drop down data to sqlite db
     */
    private void saveDropDownDataToDb() {
        List<CandidateDetails> dropDownCandidateDetails = new ArrayList<>();

        //render through spinner hashmap list
        for (int j = 0; j < spinners.size(); j++) {
            HashMap<String, Spinner> map = spinners.get(j);
            for (Map.Entry<String, Spinner> entry : map.entrySet()) {
                String str_question = entry.getKey();
                Spinner spinner = entry.getValue();
                //Question was saved in hashmap i.e header text value as key
                //We have get question id using question text from database
                String questionId = DatabaseUtil.on().getQuestionIdFromQuestion(str_question);
                String questionValue = getSpinnerValue(spinner);
                String option_id = DatabaseUtil.on().getOptionId(str_question, questionValue);
                CandidateDetails candidateDetails = new CandidateDetails();
                candidateDetails.setSurvey_master_id(surveyType.getForm_unique_id());
                candidateDetails.setSurvey_section_id(section.getSurveySection_ID());
                candidateDetails.setSurvey_que_id(questionId);
                candidateDetails.setSurvey_que_option_id(option_id);
                candidateDetails.setSurvey_que_values(questionValue);
                candidateDetails.setFormID(formId);
                candidateDetails.setCurrent_Form_Status("GY");
                candidateDetails.setAge_value("0");
                candidateDetails.setSurvey_StartDate(start_date);
                candidateDetails.setSurvey_EndDate(end_date);
                candidateDetails.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
                candidateDetails.setLatitude(Double.toString(latitude));
                candidateDetails.setLongitude(Double.toString(longitude));
                if (!(spinner.getTag().toString().equalsIgnoreCase("-1"))) {
                    candidateDetails.setIndex_if_linked_question(Integer.parseInt(spinner.getTag().toString()));
                }
                dropDownCandidateDetails.add(candidateDetails);
            }
        }
        DatabaseUtil.on().insertAllCandidateDetails(dropDownCandidateDetails);
    }

    /**
     * @param spinner
     * @return
     * @date 10-3-2022
     * Calculate spinner value
     */
    private String getSpinnerValue(Spinner spinner) {
        QuestionOption option = (QuestionOption) spinner.getSelectedItem();
        return option.getQNA_Values();
    }

    /**
     * @date 10-3-2022
     * save radio button data to sqlite db
     */
    private void saveRadioButtonDataToDb() {
        List<CandidateDetails> radioButtonCandidateDetails = new ArrayList<>();

        //render through radio button hashmap list
        for (int j = 0; j < radioButtons.size(); j++) {
            HashMap<String, RadioButton> map = radioButtons.get(j);
            for (Map.Entry<String, RadioButton> entry : map.entrySet()) {
                String str_question = entry.getKey();
                RadioButton radioButton = entry.getValue();
                //Question was saved in hashmap i.e header text value as key
                //We have get question id using question text from database
                String questionId = DatabaseUtil.on().getQuestionIdFromQuestion(str_question);
                String questionValue = radioButton.getText().toString();
                String option_id = DatabaseUtil.on().getOptionId(str_question, questionValue);

                CandidateDetails candidateDetails = new CandidateDetails();
                candidateDetails.setSurvey_master_id(surveyType.getForm_unique_id());
                candidateDetails.setSurvey_section_id(section.getSurveySection_ID());
                candidateDetails.setSurvey_que_id(questionId);
                candidateDetails.setSurvey_que_option_id(option_id);
                candidateDetails.setSurvey_que_values(questionValue);
                candidateDetails.setFormID(formId);
                candidateDetails.setCurrent_Form_Status("GY");
                candidateDetails.setAge_value("0");
                candidateDetails.setSurvey_StartDate(start_date);
                candidateDetails.setSurvey_EndDate(end_date);
                candidateDetails.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
                candidateDetails.setLatitude(Double.toString(latitude));
                candidateDetails.setLongitude(Double.toString(longitude));
                if (!(radioButton.getTag().toString().equalsIgnoreCase("-1"))) {
                    candidateDetails.setIndex_if_linked_question(Integer.parseInt(radioButton.getTag().toString()));
                }
                radioButtonCandidateDetails.add(candidateDetails);
            }
        }
        DatabaseUtil.on().insertAllCandidateDetails(radioButtonCandidateDetails);
    }

    /**
     * @date 10-3-2022
     * save check boxes data to sqlite db
     */
    private void saveCheckBoxDataToDb() {
        List<CandidateDetails> checkBoxCandidateDetails = new ArrayList<>();

        //render through radio button hashmap list
        for (int j = 0; j < checkBoxes.size(); j++) {
            HashMap<String, CheckBox> map = checkBoxes.get(j);
            for (Map.Entry<String, CheckBox> entry : map.entrySet()) {
                String str_question = entry.getKey();
                CheckBox checkBox = entry.getValue();
                //Question was saved in hashmap i.e header text value as key
                //We have get question id using question text from database
                String questionId = DatabaseUtil.on().getQuestionIdFromQuestion(str_question);
                String questionValue = checkBox.getText().toString();
                String option_id = DatabaseUtil.on().getOptionId(str_question, questionValue);

                CandidateDetails candidateDetails = new CandidateDetails();
                candidateDetails.setSurvey_master_id(surveyType.getForm_unique_id());
                candidateDetails.setSurvey_section_id(section.getSurveySection_ID());
                candidateDetails.setSurvey_que_id(questionId);
                candidateDetails.setSurvey_que_option_id(option_id);
                candidateDetails.setSurvey_que_values(questionValue);
                candidateDetails.setFormID(formId);
                candidateDetails.setCurrent_Form_Status("GY");
                candidateDetails.setAge_value("0");
                candidateDetails.setSurvey_StartDate(start_date);
                candidateDetails.setSurvey_EndDate(end_date);
                candidateDetails.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
                candidateDetails.setLatitude(Double.toString(latitude));
                candidateDetails.setLongitude(Double.toString(longitude));
                if (!(checkBox.getTag().toString().equalsIgnoreCase("-1"))) {
                    candidateDetails.setIndex_if_linked_question(Integer.parseInt(checkBox.getTag().toString()));
                }
                checkBoxCandidateDetails.add(candidateDetails);
            }
        }
        DatabaseUtil.on().insertAllCandidateDetails(checkBoxCandidateDetails);
    }

    @Override
    public void setupSubForms(List<SurveyQuestionWithData> list, SurveySection surveySection) {
        this.list = list;
        //set up form tittle
        mBinding.toolbar.tvToolbarTitle.setText(surveySection.getSectionDescription());
        addChildViews(surveySection);
    }

    private void addChildViews(SurveySection surveySection) {
        //Add Survey Section as a header
        HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
        headerTextView.setText(surveySection.getSectionDescription());
        mBinding.llFormList.addView(headerTextView);

        if (surveySection.getSectionDescription().contains("फोटो")) {
            hasFoto = true;
            addPhotoView(mBinding.llFormList);
        }

        mBindingChild = new SingleAddAnotherItemBinding[list.size()];

        //add child views and linked views to linear layout
        for (i = 0; i < list.size(); i++) {
            SurveyQuestionWithData subForm = list.get(i);
            populateQuestion(subForm);
            for (int j = 0; j < subForm.getChildQuestions().size(); j++) {
                populateQuestion(subForm.getChildQuestions().get(j));
            }
            if (subForm.getLinkedQuestions().size() > 0) {
                mBindingChild[i] = SingleAddAnotherItemBinding.inflate(getLayoutInflater());
                int finalI = i;
                addLinkedQuestion(subForm,finalI);
                mBindingChild[i].btnAddAnother.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addLinkedQuestion(subForm,finalI);
                    }
                });
                mBinding.llFormList.addView(mBindingChild[i].getRoot());
            }
        }

        mainLinear = mBinding.llFormList;
    }

    private void addPhotoView(LinearLayout llFormList) {
        requestPermissions();
        SingleFotoViewBinding binding = SingleFotoViewBinding.inflate(getLayoutInflater());
        binding.imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fileImage = createImageFile();
                    uri =  FileProvider.getUriForFile(SubFormActivity.this, "com.encureit.samtadoot.fileprovider", fileImage);
                    openCamera.launch(uri);
                } catch (Exception e) {
                    Toast.makeText(SubFormActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        openCamera = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            if(fileImage != null) {
                                Bitmap bitmap = BitmapFactory.decodeFile(fileImage.getAbsolutePath());
                                binding.imgViewFoto.setImageBitmap(bitmap);
                            }
                        } else {
                            fileImage = null;
                        }
                    }
                }
        );

        llFormList.addView(binding.getRoot());
    }

    private File createImageFile() throws IOException {
        File image = File.createTempFile("image", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        this.fileImage = new File(image.getAbsolutePath());
        return image;
    }

    private void addSingleChildViews(SurveyQuestionWithData subForm, LinearLayout linearLayout, View parent) {
        populateChildQuestionInput(subForm, linearLayout);
        if (subForm.getChildQuestions() != null) {
            for (int j = 0; j < subForm.getChildQuestions().size(); j++) {
                populateChildQuestionInput(subForm.getChildQuestions().get(j), linearLayout);
            }
        }
        Button btnAddAnother = parent.findViewById(R.id.btn_add_another);
        if (subForm.getLinkedQuestions() != null && subForm.getLinkedQuestions().size() > 0) {

            if (btnAddAnother != null) {
                addChildLinkedQuestion(subForm, linearLayout, (LinearLayout) parent);
                btnAddAnother.setVisibility(View.VISIBLE);
                btnAddAnother.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addChildLinkedQuestion(subForm, linearLayout, (LinearLayout) parent);
                    }
                });
            }
        } else {
            btnAddAnother.setVisibility(View.GONE);
        }
    }

    /**
     * Check if section has linked question or not
     *
     * @return
     */
    private boolean sectionHasLinkedQuestion() {
        boolean hasLinkedQuestion = false;
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).getLinkedQuestions().size() > 0) {
                hasLinkedQuestion = true;
            }
        }

        return hasLinkedQuestion;
    }

    private void addLinkedQuestion(SurveyQuestionWithData subForm,int index) {
        //inflating layout single_add_another_parent_item
        SingleAddAnotherParentItemBinding binding = SingleAddAnotherParentItemBinding.inflate(getLayoutInflater());
        for (int j = 0; j < subForm.getLinkedQuestions().size(); j++) {
            SurveyQuestionWithData linkedQuestion = subForm.getLinkedQuestions().get(j);
            populateChildQuestionInput(linkedQuestion, binding.llAddAnotherChild);
        }
        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBindingChild[index].llAddAnotherSingleView.removeView(binding.llAddAnother);
            }
        });
        mBindingChild[index].llAddAnotherSingleView.addView(binding.getRoot());
    }

    private void addChildLinkedQuestion(SurveyQuestionWithData subForm, LinearLayout rootView, LinearLayout parent) {
        //inflating layout single_add_another_parent_item
        SingleAddAnotherParentItemBinding binding = SingleAddAnotherParentItemBinding.inflate(getLayoutInflater());
        for (int j = 0; j < subForm.getLinkedQuestions().size(); j++) {
            SurveyQuestionWithData linkedQuestion = subForm.getLinkedQuestions().get(j);
            populateChildQuestionInput(linkedQuestion, binding.llAddAnotherChild);
        }
        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootView.removeView(binding.getRoot());
            }
        });
        rootView.addView(binding.getRoot());
    }

    /**
     * @param subForm
     * @date 7-3-2022
     * populate question
     */
    private void populateQuestion(SurveyQuestionWithData subForm) {
        switch (subForm.getQuestionType().getQuestionTypes()) {
            case AppKeys.INPUT_TEXT:
                populateInputText(subForm);
                break;
            case AppKeys.RADIO_BUTTON:
                populateRadioButton(subForm);
                break;
            case AppKeys.CHECKBOX:
                populateCheckBox(subForm);
                break;
            case AppKeys.DROPDOWNLIST:
                populateDropDown(subForm);
                break;
            case AppKeys.LABEL_TEXT:
                populateLabelText(subForm);
                break;
            case AppKeys.HEADER_TEXT:
                populateHeaderText(subForm);
                break;
            case AppKeys.DROPDOWNMULTISELECT:
                populateDropDownMultiSelect(subForm);
                break;
        }
    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert Multi Select Dropdown
     */
    private void populateDropDownMultiSelect(SurveyQuestionWithData subForm) {
        SingleDropDownMultiSelectListLayoutBinding binding = SingleDropDownMultiSelectListLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        // Pass true If you want searchView above the list. Otherwise false. default = true.
        binding.multiSelectSpinnerWithSearch.setSearchEnabled(true);

        // A text that will display in search hint.
        binding.multiSelectSpinnerWithSearch.setSearchHint(subForm.getQuestions());

        // Set text that will display when search result not found...
        binding.multiSelectSpinnerWithSearch.setEmptyTitle("Not Data Found!");

        // If you will set the limit, this button will not display automatically.
        binding.multiSelectSpinnerWithSearch.setShowSelectAllButton(true);

        //A text that will display in clear text button
        binding.multiSelectSpinnerWithSearch.setClearText("Close & Clear");

        List<KeyPairBoolData> listArray1 = populateMultiSelectList(subForm.getQuestionOptions());

        // Removed second parameter, position. Its not required now..
        // If you want to pass preselected items, you can do it while making listArray,
        // Pass true in setSelected of any item that you want to preselect
        binding.multiSelectSpinnerWithSearch.setItems(listArray1, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("TAG", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                    }
                }
            }
        });

        mBinding.llFormList.addView(binding.getRoot());
    }

    /**
     * @param questionOptions
     * @return List<KeyPairBoolData> keyPairBoolData
     * @date 8-3-2022
     * Creates multiselect list data
     */
    private List<KeyPairBoolData> populateMultiSelectList(List<QuestionOption> questionOptions) {
        List<KeyPairBoolData> keyPairBoolDataList = new ArrayList<>();

        for (int i = 0; i < questionOptions.size(); i++) {
            KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
            keyPairBoolData.setId(i);
            keyPairBoolData.setName(questionOptions.get(i).getQNA_Values());
            keyPairBoolData.setObject(questionOptions.get(i));
            keyPairBoolDataList.add(keyPairBoolData);
        }

        return keyPairBoolDataList;
    }

    private void populateHeaderText(SurveyQuestionWithData subForm) {
        HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
        if (subForm.getRequired() != null && subForm.getRequired().equalsIgnoreCase("true")) {
            headerTextView.setText(subForm.getQuestions() + " *");
        } else {
            headerTextView.setText(subForm.getQuestions());
        }
        mBinding.llFormList.addView(headerTextView);
    }

    private void populateLabelText(SurveyQuestionWithData subForm) {
        if (!TextUtils.isEmpty(subForm.getLabelHeader().trim())) {
            //add multiple edittext
            populateMultiInputBox(subForm,mBinding.llFormList);
        } else {
            HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
            headerTextView.setText(subForm.getQuestions());
            mBinding.llFormList.addView(headerTextView);
        }
    }

    private void populateMultiInputBox(SurveyQuestionWithData subForm, LinearLayout linearLayout) {
        HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
        headerTextView.setText(subForm.getQuestions());
        linearLayout.addView(headerTextView);

        SingleMultipleInputBoxParentLayoutBinding binding = SingleMultipleInputBoxParentLayoutBinding.inflate(getLayoutInflater());

        addLabelHeader(subForm, binding.llMultiInputParent);
        for (int j = 0; j < subForm.getQuestionOptions().size(); j++) {
            addInputOption(subForm.getInputValidation(),subForm.getQuestionOptions().get(j), binding.llMultiInputParent);
        }
        linearLayout.addView(binding.getRoot());
    }

    private void addInputOption(int validation,QuestionOption questionOption, LinearLayout llMultiInputParent) {
        SingleMultipleInputBoxLayoutBinding binding = SingleMultipleInputBoxLayoutBinding.inflate(getLayoutInflater());
        binding.setOption(questionOption);

        int tot_input_boxes = Integer.parseInt(questionOption.getDisplayTypeCount());

        for (int j = 0; j < tot_input_boxes; j++) {
            AppCompatEditText textView = new AppCompatEditText(SubFormActivity.this);
            int ten_dp = CommonUtils.dip2pix(SubFormActivity.this, 8);
            textView.setPadding(ten_dp, ten_dp, ten_dp, ten_dp);
            textView.setInputType(validation);
            textView.setTag(questionOption.getQNAOption_ID());
            textView.setBackground(getResources().getDrawable(R.drawable.balck_border_rectangle));
            int width = CommonUtils.dip2pix(SubFormActivity.this, getResources().getDimensionPixelSize(R.dimen.multi_input_width));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);

            binding.llInputBox.addView(textView);
        }

        llMultiInputParent.addView(binding.getRoot());
    }

    private void addLabelHeader(SurveyQuestionWithData subForm, LinearLayout llMultiInputParent) {
        String label_header = subForm.getLabelHeader().replaceAll("\".*\"", "");
        List<String> labels = Arrays.asList(label_header.split(","));

        SingleMultipleInputBoxLayoutBinding binding = SingleMultipleInputBoxLayoutBinding.inflate(getLayoutInflater());
        binding.setOption(null);

        for (int j = 0; j < labels.size(); j++) {
            //add Header To Layout
            HeaderTextView textView = new HeaderTextView(SubFormActivity.this);
            int ten_dp = CommonUtils.dip2pix(SubFormActivity.this, 10);
            textView.setPadding(ten_dp, ten_dp, ten_dp, ten_dp);
            textView.setBackground(getResources().getDrawable(R.drawable.balck_border_rectangle));
            int width = CommonUtils.dip2pix(SubFormActivity.this, getResources().getDimensionPixelSize(R.dimen.multi_input_width));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setText(labels.get(j));

            binding.llInputBox.addView(textView);
        }

        llMultiInputParent.addView(binding.getRoot());
    }


    /**
     * @param subForm
     * @date 8-3-2022
     * Insert check box in layout
     */
    private void populateCheckBox(SurveyQuestionWithData subForm) {
        SingleCheckBoxesLayoutBinding binding = SingleCheckBoxesLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            CheckBox checkBox = new CheckBox(SubFormActivity.this);
            checkBox.setText(questionOption.getQNA_Values());
            binding.rgCheckboxQuestionOptions.addView(checkBox, i);
        }
        mBinding.llFormList.addView(binding.getRoot());
    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert DropDown in layout
     */
    private void populateDropDown(SurveyQuestionWithData subForm) {
        SingleDropDownListLayoutBinding binding = SingleDropDownListLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        DropDownArrayAdapter adapter = new DropDownArrayAdapter(SubFormActivity.this, R.layout.single_drop_down_item, subForm.getQuestionOptions());
        binding.sprQuestionOption.setAdapter(adapter);
        binding.edtDropDownItar.setVisibility(View.GONE);
        binding.sprQuestionOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    QuestionOption questionOption = subForm.getQuestionOptions().get(i);
                    if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                        binding.llChildQuestion.removeAllViews();
                        String childQuesId = questionOption.getChildQuestionId();
                        if (childQuesId.contains(",")) {
                            List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                            for (int j = 0; j < childQuesIds.size(); j++) {
                                SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesIds.get(j));
                                if (childQues != null) {
//                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                }
                                binding.llChildQuestion.addView(singleBinding.getRoot());
                            }
                            binding.llChildQuestion.setVisibility(View.VISIBLE);
                            binding.edtDropDownItar.setVisibility(View.GONE);
                        } else {
                            SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesId);
                            if (childQues != null) {
//                                populateChildQuestionInput(childQues,binding.llChildQuestion);
                                addSingleChildViews(childQues, binding.llChildQuestion, binding.getRoot());
                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                                binding.edtDropDownItar.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        if (DatabaseUtil.on().isPresentInOtherValues(questionOption)) {
                            binding.llChildQuestion.removeAllViews();
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.edtDropDownItar.setVisibility(View.VISIBLE);
                        } else {
                            binding.llChildQuestion.removeAllViews();
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.edtDropDownItar.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(SubFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mBinding.llFormList.addView(binding.getRoot());
    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert radio button in layout
     */
    private void populateRadioButton(SurveyQuestionWithData subForm) {
        SingleRadioButtonsLayoutBinding binding = SingleRadioButtonsLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            RadioButton radioButton = new RadioButton(SubFormActivity.this);
            radioButton.setText(questionOption.getQNA_Values());
            binding.rgQuestionOptions.addView(radioButton, i);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("null") && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                            //Toast.makeText(SubFormActivity.this, "Id : "+questionOption.getChildQuestionId(), Toast.LENGTH_SHORT).show();
                            binding.llChildQuestion.removeAllViews();
                            String childQuesId = questionOption.getChildQuestionId();
                            if (childQuesId.contains(",")) {
                                //binding.btnAddAnother.setVisibility(View.GONE);
                                List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                                for (int j = 0; j < childQuesIds.size(); j++) {
                                    SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                    SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesIds.get(j));
                                    if (childQues != null) {
//                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                                        addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                    }
                                    binding.llChildQuestion.addView(singleBinding.getRoot());
                                }
                            } else {
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesId);
                                if (childQues != null) {
//                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    addSingleChildViews(childQues, binding.llChildQuestion, binding.getRoot());
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.llChildQuestion.removeAllViews();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SubFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        mBinding.llFormList.addView(binding.getRoot());
    }

    /**
     * @param subForm
     * @date 7-3-2022
     * Insert EditText with Label in layout
     */
    private void populateInputText(SurveyQuestionWithData subForm) {
        SingleInputBoxLayoutBinding binding = SingleInputBoxLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);
        ScreenHelper.setEditTextValidation(subForm,binding.edtHeader);
        mBinding.llFormList.addView(binding.getRoot());
    }

    /**
     * @param subForm
     * @date 7-3-2022
     * populate linked question
     */
    private void populateChildQuestionInput(SurveyQuestionWithData subForm, LinearLayout rootView) {
        switch (subForm.getQuestionType().getQuestionTypes()) {
            case AppKeys.INPUT_TEXT:
                populateChildInputText(subForm, rootView);
                break;
            case AppKeys.RADIO_BUTTON:
                populateChildRadioButton(subForm, rootView);
                break;
            case AppKeys.CHECKBOX:
                populateChildCheckBox(subForm, rootView);
                break;
            case AppKeys.DROPDOWNLIST:
                populateChildDropDown(subForm, rootView);
                break;
            case AppKeys.LABEL_TEXT:
                populateChildLabelText(subForm, rootView);
                break;
            case AppKeys.HEADER_TEXT:
                populateChildHeaderText(subForm, rootView);
                break;
            case AppKeys.DROPDOWNMULTISELECT:
                populateChildDropDownMultiSelect(subForm, rootView);
                break;
        }

    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert Multi Select Dropdown
     */
    private void populateChildDropDownMultiSelect(SurveyQuestionWithData subForm, LinearLayout rootView) {
        SingleDropDownMultiSelectListLayoutBinding binding = SingleDropDownMultiSelectListLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        // Pass true If you want searchView above the list. Otherwise false. default = true.
        binding.multiSelectSpinnerWithSearch.setSearchEnabled(true);

        // A text that will display in search hint.
        binding.multiSelectSpinnerWithSearch.setSearchHint(subForm.getQuestions());

        // Set text that will display when search result not found...
        binding.multiSelectSpinnerWithSearch.setEmptyTitle(getResources().getString(R.string.no_data_found));

        // If you will set the limit, this button will not display automatically.
        binding.multiSelectSpinnerWithSearch.setShowSelectAllButton(true);

        //A text that will display in clear text button
        binding.multiSelectSpinnerWithSearch.setClearText("Close & Clear");

        List<KeyPairBoolData> listArray1 = populateChildMultiSelectList(subForm.getQuestionOptions());

        // Removed second parameter, position. Its not required now..
        // If you want to pass preselected items, you can do it while making listArray,
        // Pass true in setSelected of any item that you want to preselect
        binding.multiSelectSpinnerWithSearch.setItems(listArray1, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("TAG", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                    }
                }
            }
        });

        rootView.addView(binding.getRoot());
    }

    /**
     * @param questionOptions
     * @return List<KeyPairBoolData> keyPairBoolData
     * @date 8-3-2022
     * Creates multiselect list data
     */
    private List<KeyPairBoolData> populateChildMultiSelectList(List<QuestionOption> questionOptions) {
        List<KeyPairBoolData> keyPairBoolDataList = new ArrayList<>();

        for (int i = 0; i < questionOptions.size(); i++) {
            KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
            keyPairBoolData.setId(i);
            keyPairBoolData.setName(questionOptions.get(i).getQNA_Values());
            keyPairBoolData.setObject(questionOptions.get(i));
            keyPairBoolDataList.add(keyPairBoolData);
        }

        return keyPairBoolDataList;
    }

    private void populateChildHeaderText(SurveyQuestionWithData subForm, LinearLayout rootView) {
        HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
        if (subForm.getRequired() != null && subForm.getRequired().equalsIgnoreCase("true")) {
            headerTextView.setText(subForm.getQuestions() + " *");
        } else {
            headerTextView.setText(subForm.getQuestions());
        }
        rootView.addView(headerTextView);
    }

    private void populateChildLabelText(SurveyQuestionWithData subForm, LinearLayout rootView) {
        if (!TextUtils.isEmpty(subForm.getLabelHeader().trim())) {
            //add multiple edittext
            populateMultiInputBox(subForm,rootView);
        } else {
            HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
            headerTextView.setText(subForm.getQuestions());
            rootView.addView(headerTextView);
        }

    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert check box in layout
     */
    private void populateChildCheckBox(SurveyQuestionWithData subForm, LinearLayout rootView) {
        SingleCheckBoxesLayoutBinding binding = SingleCheckBoxesLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            CheckBox checkBox = new CheckBox(SubFormActivity.this);
            checkBox.setText(questionOption.getQNA_Values());
            binding.rgCheckboxQuestionOptions.addView(checkBox, i);
        }
        rootView.addView(binding.getRoot());
    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert DropDown in layout
     */
    private void populateChildDropDown(SurveyQuestionWithData subForm, LinearLayout rootView) {
        SingleDropDownListLayoutBinding binding = SingleDropDownListLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        DropDownArrayAdapter adapter = new DropDownArrayAdapter(SubFormActivity.this, R.layout.single_drop_down_item, subForm.getQuestionOptions());
        binding.sprQuestionOption.setAdapter(adapter);
        binding.edtDropDownItar.setVisibility(View.GONE);
        binding.sprQuestionOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    QuestionOption questionOption = subForm.getQuestionOptions().get(i);

                    if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                        binding.llChildQuestion.removeAllViews();
                        String childQuesId = questionOption.getChildQuestionId();
                        if (childQuesId.contains(",")) {
                            List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                            for (int j = 0; j < childQuesIds.size(); j++) {
                                SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesIds.get(j));
                                if (childQues != null) {
//                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                }
                                binding.llChildQuestion.addView(singleBinding.getRoot());
                            }
                            binding.llChildQuestion.setVisibility(View.VISIBLE);
                            binding.edtDropDownItar.setVisibility(View.GONE);
                        } else {
                            SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesId);
                            if (childQues != null) {
//                                populateChildQuestionInput(childQues,binding.llChildQuestion);
                                addSingleChildViews(childQues, binding.llChildQuestion, binding.getRoot());
                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                                binding.edtDropDownItar.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        if (DatabaseUtil.on().isPresentInOtherValues(questionOption)) {
                            binding.llChildQuestion.removeAllViews();
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.edtDropDownItar.setVisibility(View.VISIBLE);
                        } else {
                            binding.llChildQuestion.removeAllViews();
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.edtDropDownItar.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(SubFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rootView.addView(binding.getRoot());
    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert radio button in layout
     */
    private void populateChildRadioButton(SurveyQuestionWithData subForm, LinearLayout rootView) {
        SingleRadioButtonsLayoutBinding binding = SingleRadioButtonsLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);
        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            RadioButton radioButton = new RadioButton(SubFormActivity.this);
            radioButton.setText(questionOption.getQNA_Values());
            binding.rgQuestionOptions.addView(radioButton, i);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("null") && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                            //Toast.makeText(SubFormActivity.this, "Id : "+questionOption.getChildQuestionId(), Toast.LENGTH_SHORT).show();
                            binding.llChildQuestion.removeAllViews();
                            String childQuesId = questionOption.getChildQuestionId();
                            if (childQuesId.contains(",")) {
                                List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                                for (int j = 0; j < childQuesIds.size(); j++) {
                                    SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                    SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesIds.get(j));
                                    if (childQues != null) {
//                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                                        addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                    }
                                    binding.llChildQuestion.addView(singleBinding.getRoot());
                                }
                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                            } else {
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesId);
                                if (childQues != null) {
//                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    addSingleChildViews(childQues, binding.llChildQuestion, binding.getRoot());
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.llChildQuestion.removeAllViews();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SubFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        rootView.addView(binding.getRoot());
    }

    /**
     * @param subForm
     * @param rootView
     * @date 7-3-2022
     * Insert EditText with Label in layout
     */
    private void populateChildInputText(SurveyQuestionWithData subForm, LinearLayout rootView) {
        SingleInputBoxLayoutBinding binding = SingleInputBoxLayoutBinding.inflate(getLayoutInflater());
        if (subForm != null) {
            binding.setSubForm(subForm);
            /*binding.edtHeader.setInputType(subForm.getInputValidation());*/
            ScreenHelper.setEditTextValidation(subForm,binding.edtHeader);
            rootView.addView(binding.getRoot());
        }
    }

    /**
     * @date 9-3-2022 and 10-3-2022
     * get all child views
     */
    private void getAllChildView() {
        emptyAllLists();
        //get the count of main linear layout
        int count = mainLinear.getChildCount();
        for (int j = 0; j < count; j++) {
            //get all child views in main linear layout
            View view = mainLinear.getChildAt(j);
            //as our header text is not child of main linear so here we have checked for instanceof LinearLayout (this is ll_input_box layout)
            if (view instanceof LinearLayout) {
                LinearLayout linearLayout = ((LinearLayout) view);
                if (linearLayout.getId() == R.id.ll_add_another_parent) {
                    //linked view
                    getLinkedView(linearLayout);
                } else {
                    //simple child view
                    getSimpleChildView(linearLayout, -1);
                }
            } else if (view instanceof HorizontalScrollView) {
                HorizontalScrollView horizontalScrollView = ((HorizontalScrollView) view);
                View hsv_child = horizontalScrollView.getChildAt(0);
                if (hsv_child instanceof LinearLayout) {
                    LinearLayout ll_hsv_child = (LinearLayout) hsv_child;
                    int tot_count = ll_hsv_child.getChildCount();
                    for (int k = 1; k < tot_count; k++) {
                        View view_input_box = ll_hsv_child.getChildAt(k);
                        if (view_input_box instanceof LinearLayout) {
                            LinearLayout ll_input_box = (LinearLayout) view_input_box;
                            getMultiChildView(ll_input_box);
                        }
                    }
                }
                //getSimpleChildView(linearLayout,-1);
            }
        }
    }

    private void emptyAllLists() {
        editTexts.clear();
        optionEditTexts.clear();
        spinners.clear();
        multiSpinnerSearches.clear();
        radioButtons.clear();
        checkBoxes.clear();
        multiEditTextValues.clear();
    }

    private void getMultiChildView(LinearLayout linearLayout) {
        int ll_count = linearLayout.getChildCount();
        String option = "";
        String optionId = "";
        StringBuffer stringBuffer = new StringBuffer();

        for (int k = 0; k < ll_count; k++) {
            View child_view = linearLayout.getChildAt(k);
            //Check option label
            if (child_view instanceof HeaderTextView) {
                HeaderTextView headerTextView = (HeaderTextView) child_view;
                option = headerTextView.getText().toString().replace("*", "").trim();
                Log.e("Text", option);
            }
            if (child_view instanceof AppCompatEditText) {
                AppCompatEditText editText = (AppCompatEditText) child_view;
                optionId = (String) editText.getTag();
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    if (k == (ll_count - 1)) {
                        stringBuffer.append("0");
                    } else {
                        stringBuffer.append("0,");
                    }
                } else {
                    if (k == (ll_count - 1)) {
                        stringBuffer.append(editText.getText().toString());
                    } else {
                        stringBuffer.append(editText.getText().toString()).append(",");
                    }
                }
            }
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(optionId, stringBuffer.toString());
        multiEditTextValues.add(map);
    }

    private void getSimpleChildView(LinearLayout linearLayout, int index_of_child) {
        int ll_count = linearLayout.getChildCount();

        for (int k = 0; k < ll_count; k++) {
            View child_view = linearLayout.getChildAt(k);
            //Check question label
            if (!(child_view instanceof RadioGroup) && child_view instanceof LinearLayout) {
                getSimpleChildView((LinearLayout) child_view, index_of_child);
            } else if (child_view instanceof HeaderTextView) {
                HeaderTextView headerTextView = (HeaderTextView) child_view;
                int view_next_to_header_id = linearLayout.indexOfChild(child_view) + 1;
                View next_view = linearLayout.getChildAt(view_next_to_header_id);

                if (next_view != null) {
                    //check if view next to header is edittext
                    if (next_view instanceof AppCompatEditText) {
                        addViewToEdittext(headerTextView, next_view, index_of_child);
                    }

                    //Checks value of editext in case of itar
                    if (next_view instanceof Spinner) {
                        addViewToSpinner(headerTextView, next_view, view_next_to_header_id, linearLayout, index_of_child);
                    }

                    if (next_view instanceof MultiSpinnerSearch) {
                        addViewToMultiSpinner(headerTextView, next_view, index_of_child);
                    }

                    if (next_view instanceof RadioGroup) {
                        addViewToRadioButtonAndCheckBox(headerTextView, next_view, linearLayout, index_of_child);
                    }
                }
            } else if (child_view instanceof HorizontalScrollView) {
                HorizontalScrollView horizontalScrollView = ((HorizontalScrollView) child_view);
                View hsv_child = horizontalScrollView.getChildAt(0);
                if (hsv_child instanceof LinearLayout) {
                    LinearLayout ll_hsv_child = (LinearLayout) hsv_child;
                    int tot_count = ll_hsv_child.getChildCount();
                    for (int j = 0; j < tot_count; j++) {
                        View view_input_box = ll_hsv_child.getChildAt(j);
                        if (view_input_box instanceof LinearLayout) {
                            LinearLayout ll_input_box = (LinearLayout) view_input_box;
                            getMultiChildView(ll_input_box);
                        }
                    }
                }
                //getSimpleChildView(linearLayout,-1);
            }
        }
    }

    private void addViewToRadioButtonAndCheckBox(HeaderTextView headerTextView, View next_view, LinearLayout linearLayout, int index_of_child) {
        RadioGroup radioGroup = (RadioGroup) next_view;
        View radio_child_view = radioGroup.getChildAt(0);
        if (radio_child_view instanceof RadioButton) {
            int checked_radio_button_id = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = linearLayout.findViewById(checked_radio_button_id);
            radioButton.setTag(index_of_child);

            HashMap<String, RadioButton> map = new HashMap<>();
            map.put(headerTextView.getText().toString().replace("*", "").trim(), radioButton);
            radioButtons.add(map);


        } else if (radio_child_view instanceof CheckBox) {
            //to get the count of all child checkboxes
            int cb_child_count = radioGroup.getChildCount();
            for (int l = 0; l < cb_child_count; l++) {
                View cb_child_view = radioGroup.getChildAt(l);
                if (cb_child_view instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) cb_child_view;
                    checkBox.setTag(index_of_child);
                    HashMap<String, CheckBox> map = new HashMap<>();
                    map.put(headerTextView.getText().toString().replace("*", "").trim(), checkBox);
                    checkBoxes.add(map);
                }
            }
        }
    }

    private void addViewToMultiSpinner(HeaderTextView headerTextView, View next_view, int index_of_child) {
        MultiSpinnerSearch multiSpinnerSearch = (MultiSpinnerSearch) next_view;
        multiSpinnerSearch.setTag(index_of_child);
        HashMap<String, MultiSpinnerSearch> map = new HashMap<>();
        map.put(headerTextView.getText().toString().replace("*", "").trim(), multiSpinnerSearch);
        multiSpinnerSearches.add(map);
    }

    private void addViewToSpinner(HeaderTextView headerTextView, View next_view, int view_next_to_header_id, LinearLayout linearLayout, int index_of_child) {
        Spinner spinner = (Spinner) next_view;
        QuestionOption questionOption = (QuestionOption) spinner.getSelectedItem();

        if (DatabaseUtil.on().isPresentInOtherValues(questionOption)) {
            int edit_text_next_to_spinner_id = view_next_to_header_id + 1;
            View edit_text_next_to_spinner = linearLayout.getChildAt(edit_text_next_to_spinner_id);

            if (edit_text_next_to_spinner instanceof AppCompatEditText) {
                AppCompatEditText editText = (AppCompatEditText) edit_text_next_to_spinner;
                editText.setTag(index_of_child);
                HashMap<String, HashMap<String, AppCompatEditText>> map = new HashMap<>();
                HashMap<String, AppCompatEditText> edtMap = new HashMap<>();
                edtMap.put(questionOption.getQNAOption_ID(), editText);
                map.put(headerTextView.getText().toString().replace("*", "").trim(), edtMap);
                optionEditTexts.add(map);
            }

        } else {
            spinner.setTag(index_of_child);

            HashMap<String, Spinner> map = new HashMap<>();
            map.put(headerTextView.getText().toString().replace("*", "").trim(), spinner);
            spinners.add(map);
        }
    }

    private void addViewToEdittext(HeaderTextView headerTextView, View next_view, int index_of_child) {
        AppCompatEditText editText = (AppCompatEditText) next_view;
        editText.setTag(index_of_child);
        HashMap<String, AppCompatEditText> map = new HashMap<>();
        map.put(headerTextView.getText().toString().replace("*", "").trim(), editText);
        editTexts.add(map);
    }

    private void getLinkedView(LinearLayout linearLayout) {
        LinearLayout ll_add_another_single_view = linearLayout.findViewById(R.id.ll_add_another_single_view);
        int single_view_child_count = ll_add_another_single_view.getChildCount();
        for (int j = 0; j < single_view_child_count; j++) {
            View view = ll_add_another_single_view.getChildAt(j);
            if (view instanceof LinearLayout) {
                LinearLayout ll_add_another = (LinearLayout) view;
                LinearLayout ll_add_another_child = null;
                try {
                    ll_add_another_child = ll_add_another.findViewById(R.id.ll_add_another_child);
                    if (ll_add_another_child != null) {
                        int count = ll_add_another_child.getChildCount();
                        for (int k = 0; k < count; k++) {
                            View ll_view = ll_add_another_child.getChildAt(k);
                            if (ll_view instanceof LinearLayout) {
                                getSimpleChildView((LinearLayout) ll_view, j);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void showResponseFailed(String error) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkGps();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SubFormActivity.this, QuesSectionListActivity.class);
        intent.putExtra(AppKeys.SURVEY_TYPE, surveyType);
        startActivityOnTop(true, intent);
    }

    public void checkGps() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GPSTracker gpsTracker = new GPSTracker(this);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //OnGPS();
            gpsTracker.showSettingsAlert();
        } else {
            //getLocation();
            if (gpsTracker.canGetLocation()) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                Log.e("TAG", "getLocation: " + "Your Location: " + "Latitude: " + latitude + "\t" + "Longitude: " + longitude);
                //Toast.makeText(this, "Your Location: " + "Latitude: " + latitude + "\t" + "Longitude: " + longitude,Toast.LENGTH_SHORT).show();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }

    /**
     * Request permissions.
     */
    /* access modifiers changed from: private */
    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, PermissionsUtil.permissions, 455);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!PermissionsUtil.permissionsGranted(grantResults)) {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle((int) R.string.missing_permissions).setMessage((int) R.string.you_have_to_grant_permissions).setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions();
            }
        }).setNegativeButton((int) R.string.no_close_the_app, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }

}