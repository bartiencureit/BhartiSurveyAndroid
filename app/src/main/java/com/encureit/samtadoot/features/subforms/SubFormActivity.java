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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.encureit.samtadoot.custom.CustomCheckBox;
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
import com.encureit.samtadoot.custom.CustomDropDown;
import com.encureit.samtadoot.custom.CustomEditText;
import com.encureit.samtadoot.custom.CustomRadioButton;
import com.encureit.samtadoot.custom.CustomRadioGroup;
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
    private int linked_question_index = 1;
    private int child_linked_question_index = 1;
    private final List<HashMap<String, String>> multiEditTextValues = new ArrayList<>();
    private List<CandidateDetails> candidateDetails = new ArrayList<>();
    private boolean isValid = true;

    private LinearLayout mainLinear;
    private SurveySection section;
    private SurveyType surveyType;
    private String formId;
    private LocationManager locationManager;
    private double latitude, longitude;
    private String start_date;
    private String last_updated_date;
    private String end_date;
    private GlobalHelper helper;

    private File fileImage = null;
    private Uri uri;
    private ActivityResultLauncher<Uri> openCamera;
    private boolean hasFoto = false;
    private SurveyQuestionWithData imageSubForm = null;

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
            emptyAllLists();
            getAllChildViewIterative(mainLinear);
            if (candidateDetails.isEmpty()) {
                isValid = false;
            }
            if (multiEditTextValues.size() > 0) {
                return true;
            }
            return isValid;
        }

    }

    /**
     * @date 10-3-2022
     * Save data to sqlite
     */
    private void saveData() {
        end_date = getCurrentDate();
        last_updated_date = getCurrentDate();
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
            saveAllCandidates();
            saveMultiInputBoxDataToDb();
            ScreenHelper.showGreenSnackBar(mBinding.getRoot(), getResources().getString(R.string.data_sync_finished_successfully));
            startActivityOnTop(DashboardActivity.class, false);
        }
    }

    private void saveAllCandidates() {
       DatabaseUtil.on().insertAllCandidateDetails(candidateDetails);
    }

    private void saveFotoToDb() {
        CandidateDetails candidateDetails = new CandidateDetails();
        candidateDetails.setSurvey_master_id(surveyType.getForm_unique_id());
        candidateDetails.setSurvey_section_id(section.getSurveySection_ID());
        candidateDetails.setSurvey_que_id(imageSubForm.getSurveyQuestion_ID());
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
        last_updated_date = getCurrentDate();
        CandidateSurveyStatusDetails candidateSurveyStatusDetails = new CandidateSurveyStatusDetails();
        candidateSurveyStatusDetails.setFormID(formId);
        candidateSurveyStatusDetails.setSurvey_section_id(section.getSurveySection_ID());
        if (DatabaseUtil.on().isLastSurveySection(section.getSurveySection_ID(), formId)) {
            candidateSurveyStatusDetails.setSurvey_status(getResources().getString(R.string.completed));
        } else {
            candidateSurveyStatusDetails.setSurvey_status(getResources().getString(R.string.pending));
        }
        candidateSurveyStatusDetails.setStart_date(start_date);
        candidateSurveyStatusDetails.setLast_updated_date(last_updated_date);
        candidateSurveyStatusDetails.setStart_date(end_date);
        candidateSurveyStatusDetails.setForm_unique_id(surveyType.getForm_unique_id());
        DatabaseUtil.on().getCandidateSurveyStatusDetailsDao().insert(candidateSurveyStatusDetails);
    }

    /**
     * @date 10-3-2022
     * save edittext data to sqlite db
     */
    private void getInputCandidate(CustomEditText editText) {
        CandidateDetails candidateDetail = new CandidateDetails();
        if (editText.getSubForm() instanceof SurveyQuestionWithData) {
            SurveyQuestionWithData subForm = (SurveyQuestionWithData) editText.getSubForm();
            int linked_id = editText.getLinked_id();

            if (subForm.getRequired() != null && subForm.getRequired().equalsIgnoreCase("true")) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    isValid = false;
                }
            }

            candidateDetail.setSurvey_master_id(surveyType.getForm_unique_id());
            candidateDetail.setSurvey_section_id(section.getSurveySection_ID());
            candidateDetail.setSurvey_que_id(subForm.getSurveyQuestion_ID());
            candidateDetail.setSurvey_que_option_id("0");
            candidateDetail.setSurvey_que_values(editText.getText().toString());
            candidateDetail.setFormID(formId);
            candidateDetail.setCurrent_Form_Status("GY");
            candidateDetail.setAge_value("0");
            candidateDetail.setSurvey_StartDate(start_date);
            candidateDetail.setSurvey_EndDate(end_date);
            candidateDetail.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
            candidateDetail.setLatitude(Double.toString(latitude));
            candidateDetail.setLongitude(Double.toString(longitude));
            if (linked_id > 0) {
                candidateDetail.setIndex_if_linked_question(linked_id);
            }
        } else if (editText.getSubForm() instanceof QuestionOption) {
            QuestionOption questionOption = (QuestionOption) editText.getSubForm();
            int linked_id = editText.getLinked_id();

            SurveyQuestion subForm = DatabaseUtil.on().getSurveyQuestionDao().getQuestionById(questionOption.getSurveyQuestion_ID());

            if (subForm.getRequired() != null && subForm.getRequired().equalsIgnoreCase("true")) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    isValid = false;
                }
            }

            candidateDetail.setSurvey_master_id(surveyType.getForm_unique_id());
            candidateDetail.setSurvey_section_id(section.getSurveySection_ID());
            candidateDetail.setSurvey_que_id(questionOption.getSurveyQuestion_ID());
            candidateDetail.setSurvey_que_option_id(questionOption.getQNAOption_ID());
            candidateDetail.setSurvey_que_values(editText.getText().toString());
            candidateDetail.setFormID(formId);
            candidateDetail.setCurrent_Form_Status("GY");
            candidateDetail.setAge_value("0");
            candidateDetail.setSurvey_StartDate(start_date);
            candidateDetail.setSurvey_EndDate(end_date);
            candidateDetail.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
            candidateDetail.setLatitude(Double.toString(latitude));
            candidateDetail.setLongitude(Double.toString(longitude));
            if (linked_id > 0) {
                candidateDetail.setIndex_if_linked_question(linked_id);
            }
        }
        candidateDetails.add(candidateDetail);
    }

    /**
     * @date 10-3-2022
     * save drop down data to sqlite db
     */
    private void getDropDownCandidate(CustomDropDown spinner) {
        SurveyQuestionWithData subForm = (SurveyQuestionWithData) spinner.getSubForm();
        int linked_id = spinner.getLinked_id();
        QuestionOption questionOption = (QuestionOption) spinner.getSelectedItem();

        CandidateDetails candidateDetail = new CandidateDetails();
        candidateDetail.setSurvey_master_id(surveyType.getForm_unique_id());
        candidateDetail.setSurvey_section_id(section.getSurveySection_ID());
        candidateDetail.setSurvey_que_id(subForm.getSurveyQuestion_ID());
        candidateDetail.setSurvey_que_option_id(questionOption.getQNAOption_ID());
        candidateDetail.setSurvey_que_values(questionOption.getQNA_Values());
        candidateDetail.setFormID(formId);
        candidateDetail.setCurrent_Form_Status("GY");
        candidateDetail.setAge_value("0");
        candidateDetail.setSurvey_StartDate(start_date);
        candidateDetail.setSurvey_EndDate(end_date);
        candidateDetail.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
        candidateDetail.setLatitude(Double.toString(latitude));
        candidateDetail.setLongitude(Double.toString(longitude));
        if (linked_id > 0) {
            candidateDetail.setIndex_if_linked_question(linked_id);
        }
        candidateDetails.add(candidateDetail);
    }

    /**
     * @date 10-3-2022
     * save radio button data to sqlite db
     */
    private void getRadioButtonCandidate(CustomRadioButton radioButton) {
        QuestionOption questionOption = (QuestionOption) radioButton.getSubForm();
        int linked_id = radioButton.getLinked_id();
        SurveyQuestion question = DatabaseUtil.on().getSurveyQuestionDao().getQuestionById(questionOption.getSurveyQuestion_ID());
        if (question.getRequired() != null && question.getRequired().equalsIgnoreCase("true")) {
            if (!radioButton.isChecked()) {
                isValid = false;
            }
        }

        CandidateDetails candidateDetail = new CandidateDetails();
        candidateDetail.setSurvey_master_id(surveyType.getForm_unique_id());
        candidateDetail.setSurvey_section_id(section.getSurveySection_ID());
        candidateDetail.setSurvey_que_id(questionOption.getSurveyQuestion_ID());
        candidateDetail.setSurvey_que_option_id(questionOption.getQNAOption_ID());
        candidateDetail.setSurvey_que_values(radioButton.getText().toString());
        candidateDetail.setFormID(formId);
        candidateDetail.setCurrent_Form_Status("GY");
        candidateDetail.setAge_value("0");
        candidateDetail.setSurvey_StartDate(start_date);
        candidateDetail.setSurvey_EndDate(end_date);
        candidateDetail.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
        candidateDetail.setLatitude(Double.toString(latitude));
        candidateDetail.setLongitude(Double.toString(longitude));
        if (linked_id > 0) {
            candidateDetail.setIndex_if_linked_question(linked_id);
        }

        candidateDetails.add(candidateDetail);
    }

    /**
     * @date 10-3-2022
     * save check boxes data to sqlite db
     */
    private void getCheckBoxCandidate(CustomCheckBox checkBox) {
        QuestionOption questionOption = (QuestionOption) checkBox.getSubForm();
        int linked_id = checkBox.getLinked_id();

        CandidateDetails candidateDetail = new CandidateDetails();
        candidateDetail.setSurvey_master_id(surveyType.getForm_unique_id());
        candidateDetail.setSurvey_section_id(section.getSurveySection_ID());
        candidateDetail.setSurvey_que_id(questionOption.getSurveyQuestion_ID());
        candidateDetail.setSurvey_que_option_id(questionOption.getQNAOption_ID());
        candidateDetail.setSurvey_que_values(checkBox.getText().toString());
        candidateDetail.setFormID(formId);
        candidateDetail.setCurrent_Form_Status("GY");
        candidateDetail.setAge_value("0");
        candidateDetail.setSurvey_StartDate(start_date);
        candidateDetail.setSurvey_EndDate(end_date);
        candidateDetail.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
        candidateDetail.setLatitude(Double.toString(latitude));
        candidateDetail.setLongitude(Double.toString(longitude));
        if (linked_id > 0) {
            candidateDetail.setIndex_if_linked_question(linked_id);
        }

        candidateDetails.add(candidateDetail);
    }

    @Override
    public void setupSubForms(List<SurveyQuestionWithData> list, SurveySection surveySection) {
        this.list = list;
        //set up form tittle
        mBinding.toolbar.tvToolbarTitle.setText(surveySection.getSectionDescription());
        addChildViews(surveySection);
    }

    private void addChildViews(SurveySection surveySection) {
        //emptyAllLists();
        //Add Survey Section as a header
        /*HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
        headerTextView.setText(surveySection.getSectionDescription());
        mBinding.llFormList.addView(headerTextView);*/

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
                linked_question_index = 1;
                addLinkedQuestion(subForm,finalI);
                mBindingChild[i].btnAddAnother.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linked_question_index++;
                        addLinkedQuestion(subForm,finalI);
                    }
                });
                mBinding.llFormList.addView(mBindingChild[i].getRoot());
            }
        }
        mainLinear = mBinding.llFormList;
    }

    private void addPhotoView(LinearLayout llFormList, SurveyQuestionWithData subForm) {
        imageSubForm = subForm;
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
                child_linked_question_index = 1;
                addChildLinkedQuestion(subForm, linearLayout, (LinearLayout) parent);
                btnAddAnother.setVisibility(View.VISIBLE);
                btnAddAnother.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        subForm.setValue("");
                        child_linked_question_index++;
                        addChildLinkedQuestion(subForm, linearLayout, (LinearLayout) parent);
                    }
                });
            }
        } else {
            btnAddAnother.setVisibility(View.GONE);
        }
    }

    private void addLinkedQuestion(SurveyQuestionWithData subForm,int index) {

        //inflating layout single_add_another_parent_item
        SingleAddAnotherParentItemBinding binding = SingleAddAnotherParentItemBinding.inflate(getLayoutInflater());
        for (int j = 0; j < subForm.getLinkedQuestions().size(); j++) {
            SurveyQuestionWithData linkedQuestion = subForm.getLinkedQuestions().get(j);
            linkedQuestion.setLinked_question_id(linked_question_index);
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
            linkedQuestion.setLinked_question_id(child_linked_question_index);
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

        binding.multiSelectSpinnerWithSearch.setTag(subForm);

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
        if (subForm.getQuestions().contains("फोटो")) {
            hasFoto = true;
            addPhotoView(mBinding.llFormList,subForm);
        }
    }

    private void populateLabelText(SurveyQuestionWithData subForm) {
        if (!TextUtils.isEmpty(subForm.getLabelHeader().trim())) {
            //add multiple edittext
            populateMultiInputBox(subForm,mBinding.llFormList);
        } else {
            HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
            headerTextView.setText(subForm.getQuestions());
            mBinding.llFormList.addView(headerTextView);

            /*if (subForm.getQuestions().contains("फोटो")) {
                hasFoto = true;
                addPhotoView(mBinding.llFormList);
            }*/
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
            questionOption.setLinked_question_id(0);
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
        QuestionOption questionOption = new QuestionOption();
        questionOption.setQNA_Values("अनु क्र.");
        binding.setOption(questionOption);

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
        //binding.rgCheckboxQuestionOptions.setTag(subForm);
        binding.rgCheckboxQuestionOptions.setSubForm(subForm);
        binding.rgCheckboxQuestionOptions.setLinked_id(0);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            CustomCheckBox checkBox = new CustomCheckBox(SubFormActivity.this);
            checkBox.setText(questionOption.getQNA_Values());
            //checkBox.setTag(questionOption);
            checkBox.setSubForm(questionOption);
            checkBox.setLinked_id(0);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
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
                                if (DatabaseUtil.on().isPresentInOtherValues(questionOption)) {
                                    binding.llChildQuestion.removeAllViews();
                                    binding.llChildQuestion.setVisibility(View.GONE);
                                    binding.edtDropDownItar.setVisibility(View.VISIBLE);
                                    //binding.edtDropDownItar.setTag(questionOption);
                                    binding.edtDropDownItar.setSubForm(questionOption);
                                    binding.edtDropDownItar.setLinked_id(subForm.getLinked_question_id());
                                } else {
                                    binding.llChildQuestion.removeAllViews();
                                    binding.llChildQuestion.setVisibility(View.GONE);
                                    binding.edtDropDownItar.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(SubFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        binding.llChildQuestion.setVisibility(View.GONE);
                        binding.llChildQuestion.removeAllViews();
                        binding.edtDropDownItar.setVisibility(View.GONE);
                    }
                }
            });
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

        ArrayAdapter<QuestionOption> mAdapter = new ArrayAdapter<>(SubFormActivity.this,android.R.layout.simple_spinner_item,subForm.getQuestionOptions());
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //DropDownArrayAdapter adapter = new DropDownArrayAdapter(SubFormActivity.this, R.layout.single_drop_down_item, subForm.getQuestionOptions());
        binding.sprQuestionOption.setAdapter(mAdapter);
        //binding.sprQuestionOption.setTag(subForm);
        binding.sprQuestionOption.setSubForm(subForm);
        binding.sprQuestionOption.setLinked_id(0);
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
                            //binding.edtDropDownItar.setTag(questionOption);
                            binding.edtDropDownItar.setSubForm(questionOption);
                            binding.edtDropDownItar.setLinked_id(0);
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
        //binding.rgQuestionOptions.setTag(subForm);
        binding.rgQuestionOptions.setSubForm(subForm);
        binding.rgQuestionOptions.setLinked_id(0);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            CustomRadioButton radioButton = new CustomRadioButton(SubFormActivity.this);
            //radioButton.setTag(questionOption);
            radioButton.setSubForm(questionOption);
            radioButton.setLinked_id(0);
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
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
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
        binding.edtHeader.setText("");
        binding.edtHeader.setSubForm(subForm);
        binding.edtHeader.setLinked_id(0);
        binding.edtHeader.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int max_length = Integer.parseInt(subForm.getMax_length());
                int min_length = Integer.parseInt(subForm.getMin_length());

                if (min_length != 0 && max_length != 0) {
                    if (binding.edtHeader.getText().toString().length() < min_length || binding.edtHeader.getText().toString().length() > max_length ) {
                        binding.edtHeader.setError("कृपया कमीत कमी "+min_length+" आणि जास्तीत जास्त "+max_length+" अक्षरं टाका");
                        //binding.edtHeader.requestFocus();
                    }
                }
            }
        });
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

        binding.multiSelectSpinnerWithSearch.setTag(subForm);

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
        //binding.rgCheckboxQuestionOptions.setTag(subForm);
        binding.rgCheckboxQuestionOptions.setSubForm(subForm);
        binding.rgCheckboxQuestionOptions.setLinked_id(subForm.getLinked_question_id());

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            questionOption.setLinked_question_id(subForm.getLinked_question_id());
            CustomCheckBox checkBox = new CustomCheckBox(SubFormActivity.this);
            checkBox.setText(questionOption.getQNA_Values());
            //checkBox.setTag(questionOption);
            checkBox.setSubForm(questionOption);
            checkBox.setLinked_id(subForm.getLinked_question_id());
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
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
                                        binding.llChildQuestion.setVisibility(View.VISIBLE);
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
                                if (DatabaseUtil.on().isPresentInOtherValues(questionOption)) {
                                    binding.llChildQuestion.removeAllViews();
                                    binding.llChildQuestion.setVisibility(View.GONE);
                                    binding.edtDropDownItar.setVisibility(View.VISIBLE);
                                    //binding.edtDropDownItar.setTag(questionOption);
                                    binding.edtDropDownItar.setSubForm(questionOption);
                                    binding.edtDropDownItar.setLinked_id(subForm.getLinked_question_id());
                                } else {
                                    binding.llChildQuestion.removeAllViews();
                                    binding.llChildQuestion.setVisibility(View.GONE);
                                    binding.edtDropDownItar.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(SubFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        binding.llChildQuestion.setVisibility(View.GONE);
                        binding.llChildQuestion.removeAllViews();
                        binding.edtDropDownItar.setVisibility(View.GONE);
                    }
                }
            });
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

        ArrayAdapter<QuestionOption> mAdapter = new ArrayAdapter<>(SubFormActivity.this,android.R.layout.simple_spinner_item,subForm.getQuestionOptions());
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //DropDownArrayAdapter adapter = new DropDownArrayAdapter(SubFormActivity.this, R.layout.single_drop_down_item, subForm.getQuestionOptions());
        binding.sprQuestionOption.setAdapter(mAdapter);
        //binding.sprQuestionOption.setTag(subForm);
        binding.sprQuestionOption.setSubForm(subForm);
        binding.sprQuestionOption.setLinked_id(subForm.getLinked_question_id());
        binding.edtDropDownItar.setVisibility(View.GONE);
        binding.sprQuestionOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    QuestionOption questionOption = subForm.getQuestionOptions().get(i);
                    questionOption.setLinked_question_id(subForm.getLinked_question_id());
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
                                binding.llChildQuestion.setVisibility(View.VISIBLE);
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
                            //binding.edtDropDownItar.setTag(questionOption);
                            binding.edtDropDownItar.setSubForm(questionOption);
                            binding.edtDropDownItar.setLinked_id(subForm.getLinked_question_id());
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
        //binding.rgQuestionOptions.setTag(subForm);
        binding.rgQuestionOptions.setSubForm(subForm);
        binding.rgQuestionOptions.setLinked_id(subForm.getLinked_question_id());
        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            questionOption.setLinked_question_id(subForm.getLinked_question_id());
            CustomRadioButton radioButton = new CustomRadioButton(SubFormActivity.this);
            //radioButton.setTag(questionOption);
            radioButton.setSubForm(questionOption);
            radioButton.setLinked_id(subForm.getLinked_question_id());
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
            binding.edtHeader.setText("");
            binding.edtHeader.setSubForm(subForm);
            binding.edtHeader.setLinked_id(subForm.getLinked_question_id());
            binding.edtHeader.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    int max_length = Integer.parseInt(subForm.getMax_length());
                    int min_length = Integer.parseInt(subForm.getMin_length());

                    if (min_length != 0 && max_length != 0) {
                        if (binding.edtHeader.getText().toString().length() < min_length || binding.edtHeader.getText().toString().length() > max_length ) {
                            binding.edtHeader.setError("कृपया कमीत कमी "+min_length+" आणि जास्तीत जास्त "+max_length+" अक्षरं टाका");
                            //binding.edtHeader.requestFocus();
                        }
                    }
                }
            });
            rootView.addView(binding.getRoot());
        }
    }

    private void getAllChildViewIterative(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();

        for (int j = 0; j < count; j++) {
            View view = viewGroup.getChildAt(j);
            if (!(view instanceof RadioGroup) && view instanceof LinearLayout) {
                getAllChildViewIterative((LinearLayout) view);
            } else if (view instanceof CustomEditText) {
                CustomEditText editText = (CustomEditText) view;
                if (editText.getSubForm() != null) {
                    getInputCandidate(editText);
                }
            } else if (view instanceof CustomRadioGroup) {
                CustomRadioGroup radioGroup = (CustomRadioGroup) view;
                SurveyQuestionWithData subForm = (SurveyQuestionWithData) radioGroup.getSubForm();
                if (radioGroup.getChildAt(0) instanceof CustomRadioButton) {
                    int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    if (selectedRadioButtonId != -1) {
                        CustomRadioButton selectedRadioButton = radioGroup.findViewById(selectedRadioButtonId);
                        if (selectedRadioButton.getSubForm() != null) {
                            getRadioButtonCandidate(selectedRadioButton);
                        }
                    } else {
                        if (subForm.getRequired() != null && subForm.getRequired().equalsIgnoreCase("true")) {
                            isValid = false;
                        }
                    }
                } else if (radioGroup.getChildAt(0) instanceof CustomCheckBox) {
                    int childCount = radioGroup.getChildCount();
                    int checked_checkboxes = 0;

                    for (int k = 0; k < childCount; k++) {
                        View cbView = radioGroup.getChildAt(k);
                        if (cbView instanceof CustomCheckBox) {
                            CustomCheckBox checkBox = (CustomCheckBox) cbView;
                            if (checkBox.isChecked()) {
                                checked_checkboxes++;
                                getCheckBoxCandidate(checkBox);
                            }
                        }
                    }

                    if (checked_checkboxes <= 0) {
                        if (subForm.getRequired() != null && subForm.getRequired().equalsIgnoreCase("true")) {
                            isValid = false;
                        }
                    }
                }
            } else if (view instanceof CustomDropDown) {
                CustomDropDown spinner = (CustomDropDown) view;
                getDropDownCandidate(spinner);
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
            }
        }
    }

    private void emptyAllLists() {
        isValid = true;
        candidateDetails.clear();
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