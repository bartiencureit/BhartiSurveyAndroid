package com.encureit.samtadoot.features.subforms;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.encureit.samtadoot.R;
import com.encureit.samtadoot.adapters.DropDownArrayAdapter;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.custom.HeaderTextView;
import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.databinding.ActivityEditFormBinding;
import com.encureit.samtadoot.databinding.SingleAddAnotherItemBinding;
import com.encureit.samtadoot.databinding.SingleAddAnotherParentItemBinding;
import com.encureit.samtadoot.databinding.SingleCheckBoxesLayoutBinding;
import com.encureit.samtadoot.databinding.SingleDropDownListLayoutBinding;
import com.encureit.samtadoot.databinding.SingleDropDownMultiSelectListLayoutBinding;
import com.encureit.samtadoot.databinding.SingleInputBoxLayoutBinding;
import com.encureit.samtadoot.databinding.SingleMultipleInputBoxLayoutBinding;
import com.encureit.samtadoot.databinding.SingleMultipleInputBoxParentLayoutBinding;
import com.encureit.samtadoot.databinding.SingleRadioButtonsLayoutBinding;
import com.encureit.samtadoot.lib.AppKeys;
import com.encureit.samtadoot.lib.ScreenHelper;
import com.encureit.samtadoot.models.CandidateDetails;
import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;
import com.encureit.samtadoot.models.QuestionOption;
import com.encureit.samtadoot.models.SurveyQuestion;
import com.encureit.samtadoot.models.SurveyQuestionWithData;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.SurveyType;
import com.encureit.samtadoot.models.contracts.EditFormContract;
import com.encureit.samtadoot.presenter.EditFormPresenter;
import com.encureit.samtadoot.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.encureit.samtadoot.utils.CommonUtils.getCurrentDate;

public class EditFormActivity extends BaseActivity implements EditFormContract.ViewModel {
    private ActivityEditFormBinding mBinding;
    private EditFormPresenter mPresenter;
    private SingleAddAnotherItemBinding[] mBindingChild;
    private List<SurveyQuestionWithData> list;
    private int i = 0;
    private List<HashMap<String, AppCompatEditText>> editTexts = new ArrayList<>();
    private List<HashMap<String,HashMap<String,AppCompatEditText>>> optionEditTexts = new ArrayList<>();
    private List<HashMap<String, Spinner>> spinners = new ArrayList<>();
    private List<HashMap<String, MultiSpinnerSearch>> multiSpinnerSearches = new ArrayList<>();
    private List<HashMap<String, RadioButton>> radioButtons = new ArrayList<>();
    private List<HashMap<String, CheckBox>> checkBoxes = new ArrayList<>();
    private List<HashMap<String,String>> multiEditTextValues = new ArrayList<>();
    //private List<Object> requiredFields = new ArrayList<>();
    private LinearLayout mainLinear;
    private SurveySection section;
    private SurveyType surveyType;
    private CandidateSurveyStatusDetails candidateSurveyStatusDetails;
    private String formId;
    private LocationManager locationManager;
    private double latitude, longitude;
    private String start_date;
    private String end_date;
    private GlobalHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityEditFormBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        Intent intent = mActivity.getIntent();
        if (intent.hasExtra(AppKeys.SURVEY_SECTION)) {
            section = intent.getParcelableExtra(AppKeys.SURVEY_SECTION);
            surveyType = intent.getParcelableExtra(AppKeys.SURVEY_TYPE);
            candidateSurveyStatusDetails = intent.getParcelableExtra(AppKeys.CANDIDATE_SURVEY_DETAILS);
            initData();
        }
    }

    private void initData() {
        if (candidateSurveyStatusDetails != null && candidateSurveyStatusDetails.getFormID() != null) {
            formId = candidateSurveyStatusDetails.getFormID();
        }
        helper = new GlobalHelper(EditFormActivity.this);
        mPresenter = new EditFormPresenter(EditFormActivity.this,this);
        startCircularProgressDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadViews();
            }
        }, 1000);
    }

    /**
     * @date 14-3-2022
     * Load Views according to database
     */
    private void loadViews() {
        mBinding.setPresenter(mPresenter);
        mPresenter.startSubForm(section,formId);
        start_date = candidateSurveyStatusDetails.getStart_date();
        mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(EditFormActivity.this).create();
                dialog.setTitle(getResources().getString(R.string.message_on_save_button_pressed));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(validateData()) {
                            saveData();
                        } else {
                            ScreenHelper.showErrorSnackBar(mBinding.getRoot(),getResources().getString(R.string.invalid_entry));
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

    @Override
    public void setupSubForms(List<SurveyQuestionWithData> list, SurveySection surveySection) {
        this.list = list;
        //set up form tittle
        mBinding.toolbar.tvToolbarTitle.setText(surveySection.getSectionDescription());
        addChildViews(surveySection);
    }

    private void addChildViews(SurveySection surveySection) {
        //Add Survey Section as a header
        HeaderTextView headerTextView = new HeaderTextView(EditFormActivity.this);
        headerTextView.setText(surveySection.getSectionDescription());
        mBinding.llFormList.addView(headerTextView);
        mBindingChild = new SingleAddAnotherItemBinding[list.size()];
        //add child views and linked views to linear layout
        for ( i = 0; i < list.size(); i++) {
            SurveyQuestionWithData subForm = list.get(i);
            populateQuestion(subForm);
            for (int j = 0; j < subForm.getChildQuestions().size(); j++) {
                populateQuestion(subForm.getChildQuestions().get(j));
            }
            int finalI = i;
            if(subForm.getLinkedQuestionInEdit() != null && subForm.getLinkedQuestionInEdit().size() > 0) {
                mBindingChild[i] = SingleAddAnotherItemBinding.inflate(getLayoutInflater());
                for (int j = 0; j < subForm.getLinkedQuestionInEdit().size(); j++) {
                    HashMap<Integer,List<SurveyQuestionWithData>> map = subForm.getLinkedQuestionInEdit().get(j);
                    for (Map.Entry<Integer,List<SurveyQuestionWithData>> entry : map.entrySet()) {
                        int index = entry.getKey();
                        List<SurveyQuestionWithData> linkedQuestions = entry.getValue();
                        addLinkedQuestion(linkedQuestions,index,finalI);
                        mBindingChild[i].btnAddAnother.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addLinkedQuestion(linkedQuestions,index,finalI);
                            }
                        });
                    }
                }
                mBinding.llFormList.addView(mBindingChild[i].getRoot());
            } else if(subForm.getLinkedQuestions().size() > 0) {
                mBindingChild[i] = SingleAddAnotherItemBinding.inflate(getLayoutInflater());
                addLinkedQuestion(subForm.getLinkedQuestions(),0,finalI);
                mBindingChild[i].btnAddAnother.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addLinkedQuestion(subForm.getLinkedQuestions(),0,finalI);
                    }
                });
                mBinding.llFormList.addView(mBindingChild[i].getRoot());
            }
        }

        mainLinear = mBinding.llFormList;
        dismissCircularProgressDialog();
    }

    private boolean validateData() {
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
                    if (question.getRequired() != null && question.getRequired().equalsIgnoreCase("true")) {
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

        if (filled_edittext_count == editTexts.size() && filled_option_edittext_count == optionEditTexts.size() && filled_radio_count == radioButtons.size()) {
            return true;
        } else {
            return false;
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
        editCandidateSurveyStatusDetails();
        editInputBoxDataToDb();
        editOptionInputBoxDataToDb();
        editDropDownDataToDb();
        editRadioButtonDataToDb();
        editCheckBoxDataToDb();
        editMultiInputBoxDataToDb();
        ScreenHelper.showGreenSnackBar(mBinding.getRoot(),getResources().getString(R.string.data_sync_finished_successfully));
        onBackPressed();
    }

    private void editMultiInputBoxDataToDb() {
        List<CandidateDetails> multiInputCandidateDetails = new ArrayList<>();

        //render through multi input hashmap list
        for (int j = 0; j < multiEditTextValues.size(); j++) {
            HashMap<String,String> map = multiEditTextValues.get(j);
            for (Map.Entry<String,String> entry : map.entrySet()) {
                String str_question = entry.getKey();
                String value = entry.getValue();
                //Question was saved in hashmap i.e header text value as key
                //We have get question id using question text from database
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
                    int id = DatabaseUtil.on().isCandidateDetailsPresent(candidateDetails);
                    if (id != -1) {
                        updateCandidate(candidateDetails, id);
                    } else {
                        DatabaseUtil.on().getCandidateDetailsDao().insert(candidateDetails);
                    }
                }
            }
        }
    }

    /**
     * @date 10-3-2022
     * Save CandidateSurveyStatusDetails to room db
     */
    private void editCandidateSurveyStatusDetails() {
        CandidateSurveyStatusDetails candidateSurveyStatusDetails = new CandidateSurveyStatusDetails();
        candidateSurveyStatusDetails.setFormID(formId);
        candidateSurveyStatusDetails.setSurvey_section_id(section.getSurveySection_ID());
        if (DatabaseUtil.on().isLastSurveySection(section.getSurveySection_ID(),formId)) {
            candidateSurveyStatusDetails.setSurvey_status(getResources().getString(R.string.completed));
        } else {
            candidateSurveyStatusDetails.setSurvey_status(getResources().getString(R.string.pending));
        }
        candidateSurveyStatusDetails.setEnd_date(end_date);
        DatabaseUtil.on().getCandidateSurveyStatusDetailsDao().update(candidateSurveyStatusDetails);
    }

    /**
     * @date 10-3-2022
     * save edittext data to sqlite db
     */
    private void editInputBoxDataToDb() {
        List<CandidateDetails> inputCandidateDetails = new ArrayList<>();

        //render through edittext hashmap list
        for (int j = 0; j < editTexts.size(); j++) {
            HashMap<String,AppCompatEditText> map = editTexts.get(j);
            for (Map.Entry<String,AppCompatEditText> entry : map.entrySet()) {
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
                inputCandidateDetails.add(candidateDetails);
                int id = DatabaseUtil.on().isCandidateDetailsPresent(candidateDetails);
                if( id != -1) {
                    updateCandidate(candidateDetails,id);
                } else {
                    DatabaseUtil.on().getCandidateDetailsDao().insert(candidateDetails);
                }
            }
        }
    }

    /**
     * @date 10-6-2022
     * save option edittext data to sqlite db
     */
    private void editOptionInputBoxDataToDb() {
        List<CandidateDetails> inputCandidateDetails = new ArrayList<>();

        //render through edittext hashmap list
        for (int j = 0; j < optionEditTexts.size(); j++) {
            HashMap<String,HashMap<String,AppCompatEditText>> map = optionEditTexts.get(j);
            for (Map.Entry<String,HashMap<String,AppCompatEditText>> entry : map.entrySet()) {
                String str_question = entry.getKey();
                HashMap<String,AppCompatEditText> editTextMap = entry.getValue();

                for (Map.Entry<String,AppCompatEditText> edtEntry : editTextMap.entrySet()) {
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
                    inputCandidateDetails.add(candidateDetails);
                    int id = DatabaseUtil.on().isCandidateDetailsPresent(candidateDetails);
                    if( id != -1) {
                        updateCandidate(candidateDetails,id);
                    } else {
                        DatabaseUtil.on().getCandidateDetailsDao().insert(candidateDetails);
                    }
                }
            }
        }
    }

    /**
     * @date 10-3-2022
     * save drop down data to sqlite db
     */
    private void editDropDownDataToDb() {
        List<CandidateDetails> dropDownCandidateDetails = new ArrayList<>();

        //render through spinner hashmap list
        for (int j = 0; j < spinners.size(); j++) {
            HashMap<String,Spinner> map = spinners.get(j);
            for (Map.Entry<String,Spinner> entry : map.entrySet()) {
                String str_question = entry.getKey();
                Spinner spinner = entry.getValue();
                //Question was saved in hashmap i.e header text value as key
                //We have get question id using question text from database
                String questionId = DatabaseUtil.on().getQuestionIdFromQuestion(str_question);
                String questionValue = getSpinnerValue(spinner);

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
                dropDownCandidateDetails.add(candidateDetails);

                int id = DatabaseUtil.on().isCandidateDetailsPresent(candidateDetails);
                if( id != -1) {
                    updateCandidate(candidateDetails, id);
                } else {
                    DatabaseUtil.on().getCandidateDetailsDao().insert(candidateDetails);
                }
            }
        }
    }

    /**
     * @date 10-3-2022
     * Calculate spinner value
     * @param spinner
     * @return
     */
    private String getSpinnerValue(Spinner spinner) {
        QuestionOption option = (QuestionOption) spinner.getSelectedItem();
        return option.getQNA_Values();
    }

    /**
     * @date 10-3-2022
     * save radio button data to sqlite db
     */
    private void editRadioButtonDataToDb() {
        List<CandidateDetails> radioButtonCandidateDetails = new ArrayList<>();

        //render through radio button hashmap list
        for (int j = 0; j < radioButtons.size(); j++) {
            HashMap<String,RadioButton> map = radioButtons.get(j);
            for (Map.Entry<String,RadioButton> entry : map.entrySet()) {
                String str_question = entry.getKey();
                RadioButton radioButton = entry.getValue();
                //Question was saved in hashmap i.e header text value as key
                //We have get question id using question text from database
                String questionId = DatabaseUtil.on().getQuestionIdFromQuestion(str_question);
                String questionValue = radioButton.getText().toString();
                String option_id = DatabaseUtil.on().getOptionId(str_question,questionValue);

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
                radioButtonCandidateDetails.add(candidateDetails);

                int id = DatabaseUtil.on().isCandidateDetailsPresent(candidateDetails);
                if( id != -1) {
                    updateCandidate(candidateDetails, id);
                } else {
                    DatabaseUtil.on().getCandidateDetailsDao().insert(candidateDetails);
                }
            }
        }
    }

    /**
     * @date 10-3-2022
     * save check boxes data to sqlite db
     */
    private void editCheckBoxDataToDb() {
        List<CandidateDetails> checkBoxCandidateDetails = new ArrayList<>();

        //render through radio button hashmap list
        for (int j = 0; j < checkBoxes.size(); j++) {
            HashMap<String,CheckBox> map = checkBoxes.get(j);
            for (Map.Entry<String,CheckBox> entry : map.entrySet()) {
                String str_question = entry.getKey();
                CheckBox checkBox = entry.getValue();
                //Question was saved in hashmap i.e header text value as key
                //We have get question id using question text from database
                String questionId = DatabaseUtil.on().getQuestionIdFromQuestion(str_question);
                String questionValue = checkBox.getText().toString();
                String option_id = DatabaseUtil.on().getOptionId(str_question,questionValue);

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
                checkBoxCandidateDetails.add(candidateDetails);

                int id = DatabaseUtil.on().isCandidateDetailsPresent(candidateDetails);
                if( id != -1) {
                    updateCandidate(candidateDetails, id);
                } else {
                    DatabaseUtil.on().getCandidateDetailsDao().insert(candidateDetails);
                }
            }
        }
    }

    /**
     * Check if section has linked question or not
     * @return
     */
    private boolean sectionHasLinkedQuestion() {
        boolean hasLinkedQuestion = false;
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).getLinkedQuestionInEdit() != null) {
                if (list.get(j).getLinkedQuestionInEdit().size() > 0) {
                    hasLinkedQuestion = true;
                }
            } else {
                if (list.get(j).getLinkedQuestions().size() > 0) {
                    hasLinkedQuestion = true;
                }
            }
        }
        return hasLinkedQuestion;
    }

    private void addLinkedQuestion(List<SurveyQuestionWithData> linkedQuestionList,int index,int finalI) {
        //inflating layout single_add_another_parent_item
        SingleAddAnotherParentItemBinding binding = SingleAddAnotherParentItemBinding.inflate(getLayoutInflater());
        for (int j = 0; j < linkedQuestionList.size(); j++) {
            SurveyQuestionWithData linkedQuestion = linkedQuestionList.get(j);
            populateChildQuestionInput(linkedQuestion,binding.llAddAnotherChild);
        }
        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(EditFormActivity.this).create();
                dialog.setTitle(getResources().getString(R.string.do_you_want_to_delete));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mBindingChild[finalI].llAddAnotherSingleView.removeView(binding.llAddAnother);
                        deleteLinkedQuestion(linkedQuestionList,index,formId);
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
        mBindingChild[finalI].llAddAnotherSingleView.addView(binding.getRoot());
    }

    private void deleteLinkedQuestion(List<SurveyQuestionWithData> linkedQuestionList, int index,String FormId) {
        for (int j = 0; j < linkedQuestionList.size(); j++) {
            CandidateDetails candidateDetails = DatabaseUtil.on().getCandidateById(linkedQuestionList.get(j).getSurveyQuestion_ID(),index,FormId);
            DatabaseUtil.on().deleteCandidate(candidateDetails);
        }
    }

    /**
     * @date 7-3-2022
     * populate question
     * @param subForm
     */
    private void populateQuestion(SurveyQuestionWithData subForm) {
        switch (subForm.getQuestionType().getQuestionTypes()) {
            case AppKeys.INPUT_TEXT:
                populateInputText(subForm,null);
                break;
            case AppKeys.RADIO_BUTTON:
                populateRadioButton(subForm,null);
                break;
            case AppKeys.CHECKBOX:
                populateCheckBox(subForm,null);
                break;
            case AppKeys.DROPDOWNLIST:
                populateDropDown(subForm,null);
                break;
            case AppKeys.LABEL_TEXT:
                populateLabelText(subForm,null);
                break;
            case AppKeys.HEADER_TEXT:
                populateHeaderText(subForm,null);
                break;
            case AppKeys.DROPDOWNMULTISELECT:
                populateDropDownMultiSelect(subForm,null);
                break;
        }
    }

    /**
     * @date 8-3-2022
     * Insert Multi Select Dropdown
     * @param subForm
     */
    private void populateDropDownMultiSelect(SurveyQuestionWithData subForm,CandidateDetails candidateDetails) {
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
     * @date 8-3-2022
     * Creates multiselect list data
     * @param questionOptions
     * @return List<KeyPairBoolData> keyPairBoolData
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

    private void populateHeaderText(SurveyQuestionWithData subForm,CandidateDetails candidateDetails) {
        HeaderTextView headerTextView = new HeaderTextView(EditFormActivity.this);
        headerTextView.setText(subForm.getQuestions());
        mBinding.llFormList.addView(headerTextView);
    }

    private void populateLabelText(SurveyQuestionWithData subForm,CandidateDetails candidateDetails) {
        if (!TextUtils.isEmpty(subForm.getLabelHeader().trim())) {
            //add multiple edittext
            populateMultiInputBox(subForm,mBinding.llFormList);
        } else {
            HeaderTextView headerTextView = new HeaderTextView(EditFormActivity.this);
            headerTextView.setText(subForm.getQuestions());
            mBinding.llFormList.addView(headerTextView);
        }
    }

    private void populateMultiInputBox(SurveyQuestionWithData subForm,LinearLayout linearLayout) {
        HeaderTextView headerTextView = new HeaderTextView(EditFormActivity.this);
        headerTextView.setText(subForm.getQuestions());
        linearLayout.addView(headerTextView);

        SingleMultipleInputBoxParentLayoutBinding binding = SingleMultipleInputBoxParentLayoutBinding.inflate(getLayoutInflater());

        addLabelHeader(subForm,binding.llMultiInputParent);
        for (int j = 0; j < subForm.getQuestionOptions().size(); j++) {
            addInputOption(subForm.getInputValidation(),subForm.getQuestionOptions().get(j),binding.llMultiInputParent);
        }
        linearLayout.addView(binding.getRoot());
    }

    private void addInputOption(int validation,QuestionOption questionOption, LinearLayout llMultiInputParent) {
        SingleMultipleInputBoxLayoutBinding binding = SingleMultipleInputBoxLayoutBinding.inflate(getLayoutInflater());
        binding.setOption(questionOption);

        int tot_input_boxes = Integer.parseInt(questionOption.getDisplayTypeCount());
        CandidateDetails candidateDetails = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionId(questionOption.getQNAOption_ID(),formId);
        if (candidateDetails != null && candidateDetails.getSurvey_que_values() != null) {
            String value = candidateDetails.getSurvey_que_values();
            List<String> values = Arrays.asList(value.split(","));

            for (int j = 0; j < tot_input_boxes; j++) {
                AppCompatEditText textView = new AppCompatEditText(EditFormActivity.this);
                int ten_dp = CommonUtils.dip2pix(EditFormActivity.this,8);
                textView.setPadding(ten_dp,ten_dp,ten_dp,ten_dp);
                textView.setInputType(validation);
                textView.setText(values.get(j));
                textView.setTag(questionOption.getQNAOption_ID());
                textView.setBackground(getResources().getDrawable(R.drawable.balck_border_rectangle));
                int width = CommonUtils.dip2pix(EditFormActivity.this,getResources().getDimensionPixelSize(R.dimen.multi_input_width));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);

                binding.llInputBox.addView(textView);
            }

            llMultiInputParent.addView(binding.getRoot());
        } else {
            for (int j = 0; j < tot_input_boxes; j++) {
                AppCompatEditText textView = new AppCompatEditText(EditFormActivity.this);
                int ten_dp = CommonUtils.dip2pix(EditFormActivity.this,8);
                textView.setPadding(ten_dp,ten_dp,ten_dp,ten_dp);
                textView.setInputType(validation);
                textView.setTag(questionOption.getQNAOption_ID());
                textView.setBackground(getResources().getDrawable(R.drawable.balck_border_rectangle));
                int width = CommonUtils.dip2pix(EditFormActivity.this,getResources().getDimensionPixelSize(R.dimen.multi_input_width));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);

                binding.llInputBox.addView(textView);
            }

            llMultiInputParent.addView(binding.getRoot());
        }
    }

    private void addLabelHeader(SurveyQuestionWithData subForm, LinearLayout llMultiInputParent) {
        String label_header = subForm.getLabelHeader().replaceAll("\".*\"","");
        List<String> labels = Arrays.asList(label_header.split(","));

        SingleMultipleInputBoxLayoutBinding binding = SingleMultipleInputBoxLayoutBinding.inflate(getLayoutInflater());
        binding.setOption(null);

        for (int j = 0; j < labels.size(); j++) {
            //add Header To Layout
            HeaderTextView textView = new HeaderTextView(EditFormActivity.this);
            int ten_dp = CommonUtils.dip2pix(EditFormActivity.this,10);
            textView.setPadding(ten_dp,ten_dp,ten_dp,ten_dp);
            textView.setBackground(getResources().getDrawable(R.drawable.balck_border_rectangle));
            int width = CommonUtils.dip2pix(EditFormActivity.this,getResources().getDimensionPixelSize(R.dimen.multi_input_width));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setText(labels.get(j));

            binding.llInputBox.addView(textView);
        }

        llMultiInputParent.addView(binding.getRoot());
    }

    /**
     * @date 8-3-2022
     * Insert check box in layout
     * @param subForm
     */
    private void populateCheckBox(SurveyQuestionWithData subForm,CandidateDetails candidateDetails) {
        SingleCheckBoxesLayoutBinding binding = SingleCheckBoxesLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            CheckBox checkBox = new CheckBox(EditFormActivity.this);
            checkBox.setText(questionOption.getQNA_Values());
            binding.rgCheckboxQuestionOptions.addView(checkBox,i);
            CandidateDetails details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionId(questionOption.getQNAOption_ID(),formId);
            if (details != null && questionOption.getQNA_Values().equalsIgnoreCase(details.getSurvey_que_values())) {
                checkBox.setChecked(true);
            }
        }
        mBinding.llFormList.addView(binding.getRoot());
    }

    /**
     * @date 8-3-2022
     * Insert DropDown in layout
     * @param subForm
     */
    private void populateDropDown(SurveyQuestionWithData subForm,CandidateDetails candidateDetails) {
        SingleDropDownListLayoutBinding binding = SingleDropDownListLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        DropDownArrayAdapter adapter = new DropDownArrayAdapter(EditFormActivity.this, R.layout.single_drop_down_item,subForm.getQuestionOptions());
        binding.sprQuestionOption.setAdapter(adapter);
        binding.sprQuestionOption.setSelection(getDropDownSelectedPosition(subForm));
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
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesIds.get(j),formId);
                                if (childQues != null) {
                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                    binding.edtDropDownItar.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesId,formId);
                            if (childQues != null) {
                                populateChildQuestionInput(childQues,binding.llChildQuestion);
                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                                binding.edtDropDownItar.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        if (DatabaseUtil.on().isPresentInOtherValues(questionOption)) {
                            binding.llChildQuestion.removeAllViews();
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.edtDropDownItar.setVisibility(View.VISIBLE);

                            CandidateDetails details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionIdFormId(subForm.getSurveyQuestion_ID(),formId);
                            if (details != null) {
                                binding.edtDropDownItar.setText(details.getSurvey_que_values());
                            }
                        } else {
                            binding.llChildQuestion.removeAllViews();
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.edtDropDownItar.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(EditFormActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mBinding.llFormList.addView(binding.getRoot());
    }

    /**
     * @date 8-3-2022
     * Insert radio button in layout
     * @param subForm
     */
    private void populateRadioButton(SurveyQuestionWithData subForm,CandidateDetails candidateDetails) {
        SingleRadioButtonsLayoutBinding binding = SingleRadioButtonsLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            RadioButton radioButton = new RadioButton(EditFormActivity.this);
            radioButton.setText(questionOption.getQNA_Values());
            binding.rgQuestionOptions.addView(radioButton,i);
            CandidateDetails details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionId(questionOption.getQNAOption_ID(),formId);
            if (details != null && questionOption.getQNA_Values().equalsIgnoreCase(details.getSurvey_que_values())) {
                radioButton.setChecked(true);

                try {
                    if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("null") && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                        //Toast.makeText(SubFormActivity.this, "Id : "+questionOption.getChildQuestionId(), Toast.LENGTH_SHORT).show();
                        binding.llChildQuestion.removeAllViews();
                        String childQuesId = questionOption.getChildQuestionId();
                        if (childQuesId.contains(",")) {
                            List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                            for (int j = 0; j < childQuesIds.size(); j++) {
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesIds.get(j),formId);
                                if (childQues != null) {
                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesId,formId);
                            if (childQues != null) {
                                populateChildQuestionInput(childQues,binding.llChildQuestion);
                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        binding.llChildQuestion.setVisibility(View.GONE);
                        binding.llChildQuestion.removeAllViews();
                    }
                } catch (Exception e) {
                    Toast.makeText(EditFormActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                            //Toast.makeText(SubFormActivity.this, "Id : "+questionOption.getChildQuestionId(), Toast.LENGTH_SHORT).show();
                            binding.llChildQuestion.removeAllViews();
                            String childQuesId = questionOption.getChildQuestionId();
                            if (childQuesId.contains(",")) {
                                List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                                for (int j = 0; j < childQuesIds.size(); j++) {
                                    SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesIds.get(j),formId);
                                    if (childQues != null) {
                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                                        binding.llChildQuestion.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesId,formId);
                                if (childQues != null) {
                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.llChildQuestion.removeAllViews();
                        }
                    } catch (Exception e) {
                        Toast.makeText(EditFormActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        mBinding.llFormList.addView(binding.getRoot());
    }

    /**
     * @date 7-3-2022
     * Insert EditText with Label in layout
     * @param subForm
     */
    private void populateInputText(SurveyQuestionWithData subForm,CandidateDetails candidateDetails) {
        SingleInputBoxLayoutBinding binding = SingleInputBoxLayoutBinding.inflate(getLayoutInflater());
        if(candidateDetails != null) {
            subForm.setValue(candidateDetails.getSurvey_que_values());
        } else {
            String quesId = subForm.getSurveyQuestion_ID();
            CandidateDetails details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionIdFormId(quesId,formId);
            if (details != null) {
                subForm.setValue(details.getSurvey_que_values());
            }
        }
        binding.setSubForm(subForm);
        mBinding.llFormList.addView(binding.getRoot());
    }

    /**
     * @date 7-3-2022
     * populate linked question
     * @param subForm
     */
    private void populateChildQuestionInput(SurveyQuestionWithData subForm, LinearLayout rootView) {
        switch (subForm.getQuestionType().getQuestionTypes()) {
            case AppKeys.INPUT_TEXT:
                populateChildInputText(subForm,rootView,null);
                break;
            case AppKeys.RADIO_BUTTON:
                populateChildRadioButton(subForm,rootView,null);
                break;
            case AppKeys.CHECKBOX:
                populateChildCheckBox(subForm,rootView,null);
                break;
            case AppKeys.DROPDOWNLIST:
                populateChildDropDown(subForm,rootView,null);
                break;
            case AppKeys.LABEL_TEXT:
                populateChildLabelText(subForm,rootView,null);
                break;
            case AppKeys.HEADER_TEXT:
                populateChildHeaderText(subForm,rootView,null);
                break;
            case AppKeys.DROPDOWNMULTISELECT:
                populateChildDropDownMultiSelect(subForm,rootView,null);
                break;
        }

    }

    /**
     * @date 8-3-2022
     * Insert Multi Select Dropdown
     * @param subForm
     */
    private void populateChildDropDownMultiSelect(SurveyQuestionWithData subForm,LinearLayout rootView,CandidateDetails candidateDetails) {
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
     * @date 8-3-2022
     * Creates multiselect list data
     * @param questionOptions
     * @return List<KeyPairBoolData> keyPairBoolData
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

    private void populateChildHeaderText(SurveyQuestionWithData subForm,LinearLayout rootView,CandidateDetails candidateDetails) {
        HeaderTextView headerTextView = new HeaderTextView(EditFormActivity.this);
        headerTextView.setText(subForm.getQuestions());
        rootView.addView(headerTextView);
    }

    private void populateChildLabelText(SurveyQuestionWithData subForm,LinearLayout rootView,CandidateDetails candidateDetails) {
        if (!TextUtils.isEmpty(subForm.getLabelHeader().trim())) {
            //add multiple edittext
            populateMultiInputBox(subForm,rootView);
        } else {
            HeaderTextView headerTextView = new HeaderTextView(EditFormActivity.this);
            headerTextView.setText(subForm.getQuestions());
            rootView.addView(headerTextView);
        }
    }

    /**
     * @date 8-3-2022
     * Insert check box in layout
     * @param subForm
     */
    private void populateChildCheckBox(SurveyQuestionWithData subForm,LinearLayout rootView,CandidateDetails candidateDetails) {
        SingleCheckBoxesLayoutBinding binding = SingleCheckBoxesLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            CheckBox checkBox = new CheckBox(EditFormActivity.this);
            checkBox.setText(questionOption.getQNA_Values());
            binding.rgCheckboxQuestionOptions.addView(checkBox,i);
            CandidateDetails details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionId(questionOption.getQNAOption_ID(),formId);
            if (details != null && questionOption.getQNA_Values().equalsIgnoreCase(details.getSurvey_que_values())) {
                checkBox.setChecked(true);
            }
        }
        rootView.addView(binding.getRoot());
    }

    /**
     * @date 8-3-2022
     * Insert DropDown in layout
     * @param subForm
     */
    private void populateChildDropDown(SurveyQuestionWithData subForm,LinearLayout rootView,CandidateDetails candidateDetails) {
        SingleDropDownListLayoutBinding binding = SingleDropDownListLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        DropDownArrayAdapter adapter = new DropDownArrayAdapter(EditFormActivity.this, R.layout.single_drop_down_item,subForm.getQuestionOptions());
        binding.sprQuestionOption.setAdapter(adapter);
        binding.sprQuestionOption.setSelection(getDropDownSelectedPosition(subForm));
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
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesIds.get(j),formId);
                                if (childQues != null) {
                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                    binding.edtDropDownItar.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesId,formId);
                            if (childQues != null) {
                                populateChildQuestionInput(childQues,binding.llChildQuestion);
                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                                binding.edtDropDownItar.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        if (DatabaseUtil.on().isPresentInOtherValues(questionOption)) {
                            binding.llChildQuestion.removeAllViews();
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.edtDropDownItar.setVisibility(View.VISIBLE);

                            CandidateDetails details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionIdFormId(subForm.getSurveyQuestion_ID(),formId);
                            if (details != null) {
                                binding.edtDropDownItar.setText(details.getSurvey_que_values());
                            }
                        } else {
                            binding.llChildQuestion.removeAllViews();
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.edtDropDownItar.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(EditFormActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rootView.addView(binding.getRoot());
    }

    private int getDropDownSelectedPosition(SurveyQuestionWithData subForm) {
        int position = 0;

        if (subForm != null && subForm.getQuestionOptions() != null && subForm.getQuestionOptions().size() > 0) {
            for (int j = 0; j < subForm.getQuestionOptions().size(); j++) {
                QuestionOption questionOption = subForm.getQuestionOptions().get(j);
                CandidateDetails candidateDetails = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionId(questionOption.getQNAOption_ID(),formId);
                if (candidateDetails != null) {
                    position = j;
                }
            }
        }

        return position;
    }

    /**
     * @date 8-3-2022
     * Insert radio button in layout
     * @param subForm
     */
    private void populateChildRadioButton(SurveyQuestionWithData subForm,LinearLayout rootView,CandidateDetails candidateDetails) {
        SingleRadioButtonsLayoutBinding binding = SingleRadioButtonsLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            RadioButton radioButton = new RadioButton(EditFormActivity.this);
            radioButton.setText(questionOption.getQNA_Values());
            binding.rgQuestionOptions.addView(radioButton,i);
            CandidateDetails details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionId(questionOption.getQNAOption_ID(),formId);
            if (details != null && questionOption.getQNA_Values().equalsIgnoreCase(details.getSurvey_que_values())) {
                radioButton.setChecked(true);

                try {
                    if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("null") && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                        //Toast.makeText(SubFormActivity.this, "Id : "+questionOption.getChildQuestionId(), Toast.LENGTH_SHORT).show();
                        binding.llChildQuestion.removeAllViews();
                        String childQuesId = questionOption.getChildQuestionId();
                        if (childQuesId.contains(",")) {
                            List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                            for (int j = 0; j < childQuesIds.size(); j++) {
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesIds.get(j),formId);
                                if (childQues != null) {
                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesId,formId);
                            if (childQues != null) {
                                populateChildQuestionInput(childQues,binding.llChildQuestion);
                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        binding.llChildQuestion.setVisibility(View.GONE);
                        binding.llChildQuestion.removeAllViews();
                    }
                } catch (Exception e) {
                    Toast.makeText(EditFormActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                            //Toast.makeText(SubFormActivity.this, "Id : "+questionOption.getChildQuestionId(), Toast.LENGTH_SHORT).show();
                            binding.llChildQuestion.removeAllViews();
                            String childQuesId = questionOption.getChildQuestionId();
                            if (childQuesId.contains(",")) {
                                List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                                for (int j = 0; j < childQuesIds.size(); j++) {
                                    SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesIds.get(j),formId);
                                    if (childQues != null) {
                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                                        binding.llChildQuestion.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromId(childQuesId,formId);
                                if (childQues != null) {
                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.llChildQuestion.removeAllViews();
                        }
                    } catch (Exception e) {
                        Toast.makeText(EditFormActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        rootView.addView(binding.getRoot());
    }

    /**
     * @date 7-3-2022
     * Insert EditText with Label in layout
     * @param subForm
     * @param rootView
     */
    private void populateChildInputText(SurveyQuestionWithData subForm, LinearLayout rootView,CandidateDetails candidateDetails) {
        SingleInputBoxLayoutBinding binding = SingleInputBoxLayoutBinding.inflate(getLayoutInflater());
        if(candidateDetails != null) {
            subForm.setValue(candidateDetails.getSurvey_que_values());
        } else {
            String quesId = subForm.getSurveyQuestion_ID();
            CandidateDetails details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionIdFormId(quesId,formId);
            if (details != null) {
                subForm.setValue(details.getSurvey_que_values());
            }
        }
        binding.setSubForm(subForm);
        rootView.addView(binding.getRoot());
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
                LinearLayout linearLayout = ((LinearLayout)view);
                if (linearLayout.getId() == R.id.ll_add_another_parent) {
                    //linked view
                    getLinkedView(linearLayout);
                } else {
                    //simple child view
                    getSimpleChildView(linearLayout);
                }
            } else if(view instanceof HorizontalScrollView) {
                HorizontalScrollView horizontalScrollView = ((HorizontalScrollView)view);
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

    private void getMultiChildView(LinearLayout linearLayout) {
        int ll_count = linearLayout.getChildCount();
        String option = "";
        String optionId = "";
        StringBuffer stringBuffer = new StringBuffer();

        for (int k = 0; k <ll_count; k++) {
            View child_view = linearLayout.getChildAt(k);
            //Check option label
            if (child_view instanceof HeaderTextView) {
                HeaderTextView headerTextView = (HeaderTextView) child_view;
                option = headerTextView.getText().toString();
                Log.e("Text",option);
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

    private void getSimpleChildView(LinearLayout linearLayout) {
        int ll_count = linearLayout.getChildCount();

        for (int k = 0; k <ll_count; k++) {
            View child_view = linearLayout.getChildAt(k);
            //Check question label
            if (!(child_view instanceof RadioGroup) && child_view instanceof LinearLayout) {
                getSimpleChildView((LinearLayout) child_view);
            } else if (child_view instanceof HeaderTextView) {
                HeaderTextView headerTextView = (HeaderTextView) child_view;
                int view_next_to_header_id = linearLayout.indexOfChild(child_view) + 1;
                View next_view = linearLayout.getChildAt(view_next_to_header_id);

                if( next_view != null) {
                    //check if view next to header is edittext
                    if (next_view instanceof AppCompatEditText) {
                        addViewToEdittext(headerTextView,next_view);
                    }

                    //Checks value of editext in case of itar
                    if (next_view instanceof Spinner) {
                        addViewToSpinner(headerTextView,next_view,view_next_to_header_id,linearLayout);
                    }

                    if (next_view instanceof MultiSpinnerSearch) {
                        addViewToMultiSpinner(headerTextView,next_view);
                    }

                    if (next_view instanceof RadioGroup) {
                        addViewToRadioButtonAndCheckBox(headerTextView,next_view,linearLayout);
                    }
                }
            }  else if(child_view instanceof HorizontalScrollView) {
                HorizontalScrollView horizontalScrollView = ((HorizontalScrollView)child_view);
                View hsv_child = horizontalScrollView.getChildAt(0);
                if (hsv_child instanceof LinearLayout) {
                    LinearLayout ll_hsv_child = (LinearLayout) hsv_child;
                    int tot_count = ll_hsv_child.getChildCount();
                    for (int j = 1; j < tot_count; j++) {
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

    private void addViewToRadioButtonAndCheckBox(HeaderTextView headerTextView, View next_view,LinearLayout linearLayout) {
        RadioGroup radioGroup = (RadioGroup) next_view;
        View radio_child_view = radioGroup.getChildAt(0);
        if(radio_child_view instanceof RadioButton) {
            int checked_radio_button_id = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = linearLayout.findViewById(checked_radio_button_id);

            HashMap<String, RadioButton> map = new HashMap<>();
            map.put(headerTextView.getText().toString(), radioButton);
            radioButtons.add(map);
        } else if (radio_child_view instanceof CheckBox) {
            //to get the count of all child checkboxes
            int cb_child_count = radioGroup.getChildCount();
            for (int l = 0; l < cb_child_count; l++) {
                View cb_child_view = radioGroup.getChildAt(l);
                if (cb_child_view instanceof CheckBox) {
                    HashMap<String,CheckBox> map = new HashMap<>();
                    map.put(headerTextView.getText().toString(),(CheckBox) cb_child_view);
                    checkBoxes.add(map);
                }
            }
        }
    }

    private void addViewToMultiSpinner(HeaderTextView headerTextView, View next_view) {
        HashMap<String,MultiSpinnerSearch> map = new HashMap<>();
        map.put(headerTextView.getText().toString(),(MultiSpinnerSearch) next_view);
        multiSpinnerSearches.add(map);
    }

    private void addViewToSpinner(HeaderTextView headerTextView, View next_view, int view_next_to_header_id, LinearLayout linearLayout) {
        /*Spinner spinner = (Spinner) next_view;
        QuestionOption questionOption = (QuestionOption) spinner.getSelectedItem();

        if (DatabaseUtil.on().isPresentInOtherValues(questionOption)) {
            int edit_text_next_to_spinner_id = view_next_to_header_id + 1;
            View edit_text_next_to_spinner = linearLayout.getChildAt(edit_text_next_to_spinner_id);

            if (edit_text_next_to_spinner instanceof AppCompatEditText) {
                HashMap<String,AppCompatEditText> map = new HashMap<>();
                map.put(headerTextView.getText().toString(),(AppCompatEditText) edit_text_next_to_spinner);
                editTexts.add(map);
            }

        } else {
            HashMap<String, Spinner> map = new HashMap<>();
            map.put(headerTextView.getText().toString(), (Spinner) next_view);
            spinners.add(map);
        }*/

        Spinner spinner = (Spinner) next_view;
        QuestionOption questionOption = (QuestionOption) spinner.getSelectedItem();

        if (DatabaseUtil.on().isPresentInOtherValues(questionOption)) {
            int edit_text_next_to_spinner_id = view_next_to_header_id + 1;
            View edit_text_next_to_spinner = linearLayout.getChildAt(edit_text_next_to_spinner_id);

            if (edit_text_next_to_spinner instanceof AppCompatEditText) {
                AppCompatEditText editText = (AppCompatEditText) edit_text_next_to_spinner;
                HashMap<String,HashMap<String,AppCompatEditText>> map = new HashMap<>();
                HashMap<String,AppCompatEditText> edtMap = new HashMap<>();
                edtMap.put(questionOption.getQNAOption_ID(),editText);
                map.put(headerTextView.getText().toString(),edtMap);
                optionEditTexts.add(map);
            }

        } else {
            HashMap<String, Spinner> map = new HashMap<>();
            map.put(headerTextView.getText().toString(), spinner);
            spinners.add(map);
        }
    }

    private void addViewToEdittext(HeaderTextView headerTextView, View next_view) {
        HashMap<String,AppCompatEditText> map = new HashMap<>();
        map.put(headerTextView.getText().toString(),(AppCompatEditText) next_view);
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
                            if(ll_view instanceof LinearLayout) {
                                getSimpleChildView((LinearLayout) ll_view);
                            }
                        }
                    }
                } catch (Exception e) {}
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
        Intent intent = new Intent(EditFormActivity.this,QuesSectionListActivity.class);
        intent.putExtra(AppKeys.SURVEY_TYPE,surveyType);
        intent.putExtra(AppKeys.CANDIDATE_SURVEY_DETAILS,candidateSurveyStatusDetails);
        startActivityOnTop(true,intent);
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
     * @date 16-3-2022
     * @param candidateDetails
     * @param id
     */
    private void updateCandidate(CandidateDetails candidateDetails, int id) {
        DatabaseUtil.on().getCandidateDetailsDao().update_survey_master_id(id,candidateDetails.getSurvey_master_id());
        DatabaseUtil.on().getCandidateDetailsDao().update_survey_section_id(id,candidateDetails.getSurvey_section_id());
        DatabaseUtil.on().getCandidateDetailsDao().update_survey_que_id(id,candidateDetails.getSurvey_que_id());
        DatabaseUtil.on().getCandidateDetailsDao().update_survey_que_option_id(id,candidateDetails.getSurvey_que_option_id());
        DatabaseUtil.on().getCandidateDetailsDao().update_survey_que_values(id,candidateDetails.getSurvey_que_values());
        DatabaseUtil.on().getCandidateDetailsDao().update_FormID(id,formId);
        DatabaseUtil.on().getCandidateDetailsDao().update_Current_Form_Status(id,candidateDetails.getCurrent_Form_Status());
        DatabaseUtil.on().getCandidateDetailsDao().update_age_value(id,candidateDetails.getAge_value());
        DatabaseUtil.on().getCandidateDetailsDao().update_Survey_StartDate(id,candidateDetails.getSurvey_StartDate());
        DatabaseUtil.on().getCandidateDetailsDao().update_Survey_EndDate(id,candidateDetails.getSurvey_EndDate());
        DatabaseUtil.on().getCandidateDetailsDao().update_created_by(id,candidateDetails.getCreated_by());
        DatabaseUtil.on().getCandidateDetailsDao().update_Latitude(id,candidateDetails.getLatitude());
        DatabaseUtil.on().getCandidateDetailsDao().update_Longitude(id,candidateDetails.getLongitude());
    }
}