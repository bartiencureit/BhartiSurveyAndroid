package com.encureit.samtadoot.features.subforms;

import static com.encureit.samtadoot.utils.CommonUtils.getCurrentDate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.encureit.samtadoot.Helpers.Helper;
import com.encureit.samtadoot.adapters.ImageAdapter;
import com.encureit.samtadoot.custom.CustomCheckBox;

import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.encureit.samtadoot.custom.CustomCheckBoxLinearLayout;
import com.encureit.samtadoot.custom.CustomRadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.encureit.samtadoot.custom.CustomEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.encureit.samtadoot.Helpers.GPSTracker;
import com.encureit.samtadoot.Helpers.GlobalHelper;
import com.encureit.samtadoot.Helpers.PermissionsUtil;
import com.encureit.samtadoot.R;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.custom.CustomDropDown;
import com.encureit.samtadoot.custom.CustomRadioGroup;
import com.encureit.samtadoot.custom.HeaderTextView;
import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.databinding.ActivityEditFormBinding;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditFormActivity extends BaseActivity implements EditFormContract.ViewModel {
    private final List<CandidateDetails> multiEditTextValues = new ArrayList<>();
    private List<CandidateDetails> candidateDetails = new ArrayList<>();
    private ActivityEditFormBinding mBinding;
    private EditFormPresenter mPresenter;
    private SingleAddAnotherItemBinding[] mBindingChild;
    private List<SurveyQuestionWithData> list;
    private int i = 0;
    private int linked_question_index = 1;
    private int child_linked_question_index = 1;
    private boolean isValid = true;

    private LinearLayout mainLinear;
    private SurveySection section;
    private SurveyType surveyType;
    private CandidateSurveyStatusDetails candidateSurveyStatusDetails;
    private String formId;
    private LocationManager locationManager;
    private double latitude, longitude;
    private String start_date;
    private String last_updated_date;
    private String end_date;
    private GlobalHelper helper;
   // private List<Bitmap> photoList = new ArrayList<>();
    private ImageAdapter mImageAdapter;
    private SingleFotoViewBinding binding;
    int first_column_count = -1;
    int second_column_count = -1;
    int third_column_count = -1;
    int fourth_column_count = -1;
    int c = 0;

    private File fileImage = null;
    ActivityResultLauncher<Uri> openCamera = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        if (fileImage != null) {
                            editPhotoFromDB();
                            if(binding != null) {
                                setPhotoAdapter();
                            }
                        }
                    } else {
                        fileImage = null;
                    }
                }
            });

    private void setPhotoAdapter() {
        try {
            List<CandidateDetails> details = new ArrayList<>();
            details.add(new CandidateDetails());
            details.addAll(DatabaseUtil.on().getCandidateDetailsDao().getAllDetailsBySectionIdFormId(section.getSurveySection_ID(), formId));

            mImageAdapter = new ImageAdapter(EditFormActivity.this, details, new ImageAdapter.OnImageDeleteListener() {
                @Override
                public void onImageDelete(int position) {
                    //Toast.makeText(mActivity, "position : "+position, Toast.LENGTH_SHORT).show();
                    CandidateDetails detail = details.get(position);

                    AlertDialog dialog =  new AlertDialog.Builder(EditFormActivity.this).create();
                    dialog.setMessage("Do you want to delete this image?");
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseUtil.on().getCandidateDetailsDao().delete(detail);
                            dialog.dismiss();
                            setPhotoAdapter();
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
            binding.imgViewFoto.setAdapter(mImageAdapter);
            binding.imgViewFoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        if (i == 0) {
                            //capture image
                            binding.imgCapture.callOnClick();
                        } else {
                            //view image
                            CandidateDetails detail = details.get(i);
                            Intent intent = new Intent(EditFormActivity.this, ViewImageActivity.class);
                            intent.putExtra(AppKeys.IMAGE, detail);
                            EditFormActivity.this.startActivity(intent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "setPhotoAdapter: "+e.getMessage() );
        }
    }

    private SurveyQuestionWithData imageSubForm = null;
    private Uri uri;
    private boolean hasFoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityEditFormBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        try {
            Intent intent = mActivity.getIntent();
            if (intent.hasExtra(AppKeys.SURVEY_SECTION)) {
                section = intent.getParcelableExtra(AppKeys.SURVEY_SECTION);
                surveyType = intent.getParcelableExtra(AppKeys.SURVEY_TYPE);
                candidateSurveyStatusDetails = intent.getParcelableExtra(AppKeys.CANDIDATE_SURVEY_DETAILS);
                initData();
            }
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onCreate: "+e.getMessage() );
        }
    }

    private void initData() {
        try {
            if (candidateSurveyStatusDetails != null && candidateSurveyStatusDetails.getFormID() != null) {
                formId = candidateSurveyStatusDetails.getFormID();
            }
            helper = new GlobalHelper(EditFormActivity.this);
            mPresenter = new EditFormPresenter(EditFormActivity.this, this);
            startCircularProgressDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadViews();
                }
            }, 1000);
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "initData: "+e.getMessage() );
        }
    }

    /**
     * @date 14-3-2022
     * Load Views according to database
     */
    private void loadViews() {
        try {
            mBinding.setPresenter(mPresenter);
            mPresenter.startSubForm(section, formId);
            start_date = candidateSurveyStatusDetails.getStart_date();
            mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        AlertDialog dialog = new AlertDialog.Builder(EditFormActivity.this).create();
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
                    } catch (Exception e) {
                        Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "loadViews: "+e.getMessage() );
        }
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
        try {
            mBindingChild = new SingleAddAnotherItemBinding[list.size()];

            //add child views and linked views to linear layout
            for (i = 0; i < list.size(); i++) {
                SurveyQuestionWithData subForm = list.get(i);
                populateQuestion(subForm);
                for (int j = 0; j < subForm.getChildQuestions().size(); j++) {
                    populateQuestion(subForm.getChildQuestions().get(j));
                }
                int finalI = i;
                if (subForm.getLinkedQuestionInEdit() != null && subForm.getLinkedQuestionInEdit().size() > 0) {
                    mBindingChild[i] = SingleAddAnotherItemBinding.inflate(getLayoutInflater());
                    for (int j = 0; j < subForm.getLinkedQuestionInEdit().size(); j++) {
                        HashMap<Integer, List<SurveyQuestionWithData>> map = subForm.getLinkedQuestionInEdit().get(j);
                        for (Map.Entry<Integer, List<SurveyQuestionWithData>> entry : map.entrySet()) {
                            int index = entry.getKey();
                            linked_question_index = index+1;
                            List<SurveyQuestionWithData> linkedQuestions = entry.getValue();
                            addLinkedQuestion(linkedQuestions, index, finalI,linked_question_index);
                            mBindingChild[i].btnAddAnother.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    linked_question_index++;
                                    addLinkedQuestion(linkedQuestions, index, finalI,linked_question_index);
                                }
                            });
                        }
                    }
                    mBinding.llFormList.addView(mBindingChild[i].getRoot());
                } else if (subForm.getLinkedQuestions().size() > 0) {
                    mBindingChild[i] = SingleAddAnotherItemBinding.inflate(getLayoutInflater());
                    linked_question_index = 1;
                    addLinkedQuestion(subForm.getLinkedQuestions(), 0, finalI,linked_question_index);
                    mBindingChild[i].btnAddAnother.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linked_question_index++;
                            addLinkedQuestion(subForm.getLinkedQuestions(), 0, finalI,linked_question_index);
                        }
                    });
                    mBinding.llFormList.addView(mBindingChild[i].getRoot());
                }
            }

            mainLinear = mBinding.llFormList;
            dismissCircularProgressDialog();
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "addChildViews: "+e.getMessage() );
        }
    }

    private void addPhotoView(LinearLayout llFormList, SurveyQuestionWithData subForm) {
        imageSubForm = subForm;
        requestPermissions();
        binding = SingleFotoViewBinding.inflate(getLayoutInflater());
        setPhotoAdapter();
        binding.imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fileImage = createImageFile();
                    uri = FileProvider.getUriForFile(EditFormActivity.this, "com.encureit.samtadoot.fileprovider", fileImage);
                    openCamera.launch(uri);
                } catch (Exception e) {
                    Toast.makeText(EditFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        llFormList.addView(binding.getRoot());
    }

    private File createImageFile() throws IOException {
        File image = File.createTempFile("image", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        this.fileImage = new File(image.getAbsolutePath());
        return image;
    }

    private void addSingleChildViews(SurveyQuestionWithData subForm, LinearLayout linearLayout, View parent) {
        try {
            populateChildQuestionInput(subForm, linearLayout);
            if (subForm.getChildQuestions() != null) {
                for (int j = 0; j < subForm.getChildQuestions().size(); j++) {
                    populateChildQuestionInput(subForm.getChildQuestions().get(j), linearLayout);
                }
            }
            Button btnAddAnother = parent.findViewById(R.id.btn_add_another);
            if (subForm.getLinkedQuestionInEdit() != null && subForm.getLinkedQuestionInEdit().size() > 0) {
                for (int j = 0; j < subForm.getLinkedQuestionInEdit().size(); j++) {
                    HashMap<Integer, List<SurveyQuestionWithData>> map = subForm.getLinkedQuestionInEdit().get(j);
                    for (Map.Entry<Integer, List<SurveyQuestionWithData>> entry : map.entrySet()) {
                        int index = entry.getKey();
                        child_linked_question_index = index+1;
                        List<SurveyQuestionWithData> linkedQuestions = entry.getValue();
                        addChildLinkedQuestion(linkedQuestions, linearLayout, (LinearLayout) parent,child_linked_question_index);
                        btnAddAnother.setVisibility(View.VISIBLE);
                        btnAddAnother.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                child_linked_question_index++;
                                addChildLinkedQuestion(linkedQuestions, linearLayout, (LinearLayout) parent,child_linked_question_index);
                            }
                        });
                    }
                }
            } else if (subForm.getLinkedQuestions() != null && subForm.getLinkedQuestions().size() > 0) {
                if (btnAddAnother != null) {
                    child_linked_question_index = 1;
                    addChildLinkedQuestion(subForm.getLinkedQuestions(), linearLayout, (LinearLayout) parent,child_linked_question_index);
                    btnAddAnother.setVisibility(View.VISIBLE);
                    btnAddAnother.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            child_linked_question_index++;
                            subForm.setValue("");
                            addChildLinkedQuestion(subForm.getLinkedQuestions(), linearLayout, (LinearLayout) parent,child_linked_question_index);
                        }
                    });
                }
            } else {
                btnAddAnother.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "addSingleChildViews: "+e.getMessage() );
        }
    }

    private void addChildLinkedQuestion(List<SurveyQuestionWithData> linkedQuestionList, LinearLayout rootView, LinearLayout parent,int linked_id) {
        //inflating layout single_add_another_parent_item
        if (linkedQuestionList.size() > 0) {
            try {
                SingleAddAnotherParentItemBinding binding = SingleAddAnotherParentItemBinding.inflate(getLayoutInflater());
                for (int j = 0; j < linkedQuestionList.size(); j++) {
                    SurveyQuestionWithData linkedQuestion = linkedQuestionList.get(j);
                    linkedQuestion.setLinked_question_id(linked_id);
                    populateChildQuestionInput(linkedQuestion, binding.llAddAnotherChild);
                }

                binding.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog dialog = new AlertDialog.Builder(EditFormActivity.this).create();
                        dialog.setTitle(getResources().getString(R.string.do_you_want_to_delete));
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (linked_id > 1) {
                                    rootView.removeView(binding.getRoot());
                                    deleteLinkedQuestion(linkedQuestionList, linked_id, formId);
                                } else {
                                    Toast.makeText(EditFormActivity.this, "Cannot delete this", Toast.LENGTH_SHORT).show();
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

                rootView.addView(binding.getRoot());
            } catch (Exception e) {
                //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "addChildLinkedQuestion: "+e.getMessage() );
            }
        }
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
            if (section.getSurveySection_ID().equalsIgnoreCase("4")) {
                if (first_column_count == -1 && second_column_count == -1 && third_column_count == -1 && fourth_column_count == -1) {
                    return false;
                } else if (first_column_count == 0 && second_column_count == 0 && third_column_count == 0 && fourth_column_count == 0) {
                    return false;
                } else {
                    if (candidateDetails.isEmpty()) {
                        if (multiEditTextValues.size() > 0) {
                            return true;
                        } else {
                            isValid = false;
                        }
                    }

                }
            } else {
                if (candidateDetails.isEmpty()) {
                    if (multiEditTextValues.size() > 0) {
                        return true;
                    } else {
                        isValid = false;
                    }
                }

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
        //getAllChildView();
        uploadData();
    }

    /**
     * @date 10-3-2022
     * Save form to sqlite
     */
    private void uploadData() {
       // DatabaseUtil.on().getCandidateDetailsDao().deleteCandidateBySectionId(section.getSurveySection_ID());
        //save data to candidate details survey status
        editCandidateSurveyStatusDetails();
        if (!hasFoto) {
            editAllCandidates();
            //editMultiInputBoxDataToDb();
        }
        ScreenHelper.showGreenSnackBar(mBinding.getRoot(), getResources().getString(R.string.data_sync_finished_successfully));
        onBackPressed();
    }

    private void editAllCandidates() {
        if(candidateDetails.size() > 0 || multiEditTextValues.size() > 0) {
            DatabaseUtil.on().getCandidateDetailsDao().deleteCandidateBySectionIdFormId(section.getSurveySection_ID(),formId);
        }

        for (int j = 0; j < candidateDetails.size(); j++) {
            CandidateDetails candidateDetail = candidateDetails.get(j);
            DatabaseUtil.on().getCandidateDetailsDao().insert(candidateDetail);
        }

        for (int j = 0; j < multiEditTextValues.size(); j++) {
            CandidateDetails details = multiEditTextValues.get(j);
            DatabaseUtil.on().getCandidateDetailsDao().insert(details);
        }
    }

    private void editPhotoFromDB() {
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
        /*int id = DatabaseUtil.on().isCandidateDetailsPresent(candidateDetails);
        if (id != -1) {
            updateCandidate(candidateDetails, id);
        } else {*/
            DatabaseUtil.on().getCandidateDetailsDao().insert(candidateDetails);
        //}
    }

    private void editMultiInputBoxDataToDb() {
        for (int j = 0; j < multiEditTextValues.size(); j++) {
            CandidateDetails details = multiEditTextValues.get(j);
            int id = DatabaseUtil.on().isCandidateDetailsPresent(details);
            if (id != -1) {
                updateCandidate(details, id);
            } else {
                DatabaseUtil.on().getCandidateDetailsDao().insert(details);
            }
        }
    }

    /**
     * @date 10-3-2022
     * Save CandidateSurveyStatusDetails to room db
     */
    private void editCandidateSurveyStatusDetails() {
        last_updated_date = getCurrentDate();
        //CandidateSurveyStatusDetails candidateSurveyStatusDetails = new CandidateSurveyStatusDetails();
        candidateSurveyStatusDetails.setFormID(formId);
        candidateSurveyStatusDetails.setSurvey_section_id(section.getSurveySection_ID());
        if (DatabaseUtil.on().isLastSurveySection(section.getSurveySection_ID(), formId,surveyType.getForm_unique_id())) {
            candidateSurveyStatusDetails.setSurvey_status(getResources().getString(R.string.completed));
            end_date = last_updated_date;
        } else {
            end_date = null;
            candidateSurveyStatusDetails.setSurvey_status(getResources().getString(R.string.pending));
        }
        candidateSurveyStatusDetails.setLast_updated_date(last_updated_date);
        candidateSurveyStatusDetails.setEnd_date(end_date);
        candidateSurveyStatusDetails.setForm_unique_id(surveyType.getForm_unique_id());
        //DatabaseUtil.on().getCandidateSurveyStatusDetailsDao().update(candidateSurveyStatusDetails);
        DatabaseUtil.on().updateCandidateSurveyStatusDetail(candidateSurveyStatusDetails);
    }

    /**
     * @date 10-3-2022
     * save edittext data to sqlite db
     */
    private void getInputCandidate(CustomEditText editText) {
       // CandidateDetails candidateDetail = new CandidateDetails();
        if (editText.getSubForm() instanceof SurveyQuestionWithData) {
            SurveyQuestionWithData subForm = (SurveyQuestionWithData) editText.getSubForm();
            int linked_id = editText.getLinked_id();

            if (subForm.getRequired() != null && subForm.getRequired().equalsIgnoreCase("true")) {
                int max_length = Integer.parseInt(subForm.getMax_length());
                int min_length = Integer.parseInt(subForm.getMin_length());

                if (TextUtils.isEmpty(editText.getText().toString())) {
                    isValid = false;
                } else if (min_length != 0 && max_length != 0) {
                    if (editText.getText().toString().length() < min_length || editText.getText().toString().length() > max_length) {
                        editText.setError("कृपया कमीत कमी " + min_length + " आणि जास्तीत जास्त " + max_length + " अक्षरं टाका");
                        isValid = false;
                        //editText.requestFocus();
                    } else {
                        saveCandidate(subForm.getParentQuestionId(),subForm.getSurveyQuestion_ID(),"0",editText.getText().toString(),linked_id);
                    }
                } else {
                    saveCandidate(subForm.getParentQuestionId(),subForm.getSurveyQuestion_ID(),"0",editText.getText().toString(),linked_id);
                }
            } else  {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    if (subForm.getQuestions().contains("ईमेल")) {
                        if (Helper.emailValidator(editText.getText().toString())) {
                            saveCandidate(subForm.getParentQuestionId(), subForm.getSurveyQuestion_ID(), "0", editText.getText().toString(), linked_id);
                        } else {
                            isValid = false;
                            editText.setError("कृपया बरोबर ईमेल आय डी टाका");
                        }
                    } else {
                        saveCandidate(subForm.getParentQuestionId(), subForm.getSurveyQuestion_ID(), "0", editText.getText().toString(), linked_id);
                    }
                } else {
                    isValid = false;
                }
            }

        } else if (editText.getSubForm() instanceof QuestionOption) {
            QuestionOption questionOption = (QuestionOption) editText.getSubForm();
            int linked_id = editText.getLinked_id();

            SurveyQuestion subForm = DatabaseUtil.on().getSurveyQuestionDao().getQuestionById(questionOption.getSurveyQuestion_ID());

            if (subForm.getRequired() != null && subForm.getRequired().equalsIgnoreCase("true")) {
                int max_length = Integer.parseInt(subForm.getMax_length());
                int min_length = Integer.parseInt(subForm.getMin_length());

                if (TextUtils.isEmpty(editText.getText().toString())) {
                    isValid = false;
                } else if (min_length != 0 && max_length != 0) {
                    if (editText.getText().toString().length() < min_length || editText.getText().toString().length() > max_length) {
                        editText.setError("कृपया कमीत कमी " + min_length + " आणि जास्तीत जास्त " + max_length + " अक्षरं टाका");
                        isValid = false;
                        //editText.requestFocus();
                    } else {
                        saveCandidate(subForm.getParentQuestionId(),questionOption.getSurveyQuestion_ID(), questionOption.getQNAOption_ID(), editText.getText().toString(), linked_id);
                    }
                } else {
                    saveCandidate(subForm.getParentQuestionId(),questionOption.getSurveyQuestion_ID(), questionOption.getQNAOption_ID(), editText.getText().toString(), linked_id);
                }
            } else {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    saveCandidate(subForm.getParentQuestionId(), questionOption.getSurveyQuestion_ID(), questionOption.getQNAOption_ID(), editText.getText().toString(), linked_id);
                } else {
                    isValid = false;
                }
            }
        }
    }

    private void saveCandidate(String parent_ques_id,String ques_id,String option_id,String value,int linked_id) {
        CandidateDetails candidateDetail = new CandidateDetails();
        candidateDetail.setSurvey_master_id(surveyType.getForm_unique_id());
        candidateDetail.setSurvey_section_id(section.getSurveySection_ID());
        candidateDetail.setSurvey_que_id(ques_id);
        candidateDetail.setSurvey_que_option_id(option_id);
        candidateDetail.setSurvey_que_values(value);
        candidateDetail.setFormID(formId);
        candidateDetail.setCurrent_Form_Status("GY");
        candidateDetail.setAge_value("0");
        candidateDetail.setSurvey_StartDate(start_date);
        candidateDetail.setSurvey_EndDate(end_date);
        candidateDetail.setParent_ques_id(parent_ques_id);
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
        candidateDetail.setParent_ques_id(subForm.getParentQuestionId());
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
        //DatabaseUtil.on().deleteAllRadioButtonOptions(questionOption,formId,linked_id);

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
        candidateDetail.setParent_ques_id(question.getParentQuestionId());
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
     *
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
    }*/

    /**
     * @date 11-11-2022
     * save check boxes data to sqlite db
     */
    private void getCheckBoxCandidate(CustomRadioGroup radioGroup,String value,String ids) {
        SurveyQuestionWithData subForm = (SurveyQuestionWithData) radioGroup.getSubForm();
        int linked_id = radioGroup.getLinked_id();

        CandidateDetails candidateDetail = new CandidateDetails();
        candidateDetail.setSurvey_master_id(surveyType.getForm_unique_id());
        candidateDetail.setSurvey_section_id(section.getSurveySection_ID());
        candidateDetail.setSurvey_que_id(subForm.getSurveyQuestion_ID());
        candidateDetail.setSurvey_que_option_id(ids);
        candidateDetail.setSurvey_que_values(value);
        candidateDetail.setFormID(formId);
        candidateDetail.setCurrent_Form_Status("GY");
        candidateDetail.setAge_value("0");
        candidateDetail.setSurvey_StartDate(start_date);
        candidateDetail.setSurvey_EndDate(end_date);
        candidateDetail.setParent_ques_id(subForm.getParentQuestionId());
        candidateDetail.setCreated_by(helper.getSharedPreferencesHelper().getLoginUserId());
        candidateDetail.setLatitude(Double.toString(latitude));
        candidateDetail.setLongitude(Double.toString(longitude));
        if (linked_id > 0) {
            candidateDetail.setIndex_if_linked_question(linked_id);
        }
        candidateDetails.add(candidateDetail);
    }

    private void addLinkedQuestion(List<SurveyQuestionWithData> linkedQuestionList, int index, int finalI,int linked_id) {
        //inflating layout single_add_another_parent_item
        if (linkedQuestionList.size() > 0) {
            SingleAddAnotherParentItemBinding binding = SingleAddAnotherParentItemBinding.inflate(getLayoutInflater());
            for (int j = 0; j < linkedQuestionList.size(); j++) {
                SurveyQuestionWithData linkedQuestion = linkedQuestionList.get(j);
                linkedQuestion.setLinked_question_id(linked_id);
                populateChildQuestionInput(linkedQuestion, binding.llAddAnotherChild);
            }

            binding.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog dialog = new AlertDialog.Builder(EditFormActivity.this).create();
                    dialog.setTitle(getResources().getString(R.string.do_you_want_to_delete));
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (linked_id != 1) {
                                mBindingChild[finalI].llAddAnotherSingleView.removeView(binding.llAddAnother);
                                deleteLinkedQuestion(linkedQuestionList, linked_id, formId);
                            } else {
                                Toast.makeText(EditFormActivity.this, "Cannot delete this", Toast.LENGTH_SHORT).show();
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
            mBindingChild[finalI].llAddAnotherSingleView.addView(binding.getRoot());
        }
    }

    private void deleteLinkedQuestion(List<SurveyQuestionWithData> linkedQuestionList, int index, String FormId) {
        if (linkedQuestionList.size() > 0) {
            int tot_candidates = DatabaseUtil.on().getParentQuestionCandidateCount(linkedQuestionList.get(0).getParentQuestionId(),formId);
            for (int j = 0; j < tot_candidates; j++) {
                int curr_index = j + 1;
                if(curr_index > index) {
                    for (int k = 0; k < linkedQuestionList.size(); k++) {
                        List<CandidateDetails> candidateDetails = DatabaseUtil.on().getCandidateById(linkedQuestionList.get(k).getSurveyQuestion_ID(), curr_index, FormId);
                        if (candidateDetails != null) {
                            List<CandidateDetails> details = DatabaseUtil.on().getCandidateListOfChildQuestion(linkedQuestionList.get(k),curr_index,formId);
                            if (details.size() > 0) {
                                for (int l = 0; l < details.size(); l++) {
                                    DatabaseUtil.on().getCandidateDetailsDao().update_Linked_Index(details.get(l).getSurvey_que_id(),
                                            formId, curr_index, j);
                                }
                            }

                            for (int i = 0; i < candidateDetails.size(); i++) {
                                DatabaseUtil.on().getCandidateDetailsDao().update_Linked_Index(candidateDetails.get(i).getSurvey_que_id(),
                                        formId, curr_index, j);
                            }
                        }
                    }
                } else if (curr_index == index) {
                    for (int k = 0; k < linkedQuestionList.size(); k++) {
                        List<CandidateDetails> candidateDetails = DatabaseUtil.on().getCandidateById(linkedQuestionList.get(k).getSurveyQuestion_ID(), index, FormId);
                        if (candidateDetails != null) {
                            List<CandidateDetails> details = DatabaseUtil.on().getCandidateListOfChildQuestion(linkedQuestionList.get(k),curr_index,formId);
                            if (details.size() > 0) {
                                for (int i = 0; i < details.size(); i++) {
                                    DatabaseUtil.on().deleteCandidate(details.get(i));
                                }
                            }
                            for (int i = 0; i < candidateDetails.size(); i++) {
                                DatabaseUtil.on().deleteCandidate(candidateDetails.get(i));
                            }
                        }
                    }
                }
            }
           // EditFormActivity.this.recreate();
        }

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
        try {
            HeaderTextView headerTextView = new HeaderTextView(EditFormActivity.this);
            if (subForm.getRequired() != null && subForm.getRequired().equalsIgnoreCase("true")) {
                headerTextView.setText(subForm.getQuestions() + " *");
            } else {
                headerTextView.setText(subForm.getQuestions());
            }
            mBinding.llFormList.addView(headerTextView);

            if (subForm.getQuestions().contains("फोटो")) {
                hasFoto = true;
                addPhotoView(mBinding.llFormList, subForm);
            }
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "populateHeaderText: "+e.getMessage() );
        }
    }

    private void populateLabelText(SurveyQuestionWithData subForm) {
        try {
            if (!TextUtils.isEmpty(subForm.getLabelHeader().trim())) {
                //add multiple edittext
                subForm.setLinked_question_id(0);
                if (subForm.getSurveySection_ID().equalsIgnoreCase("4")) {
                    populateMultiInputBoxFourthSection(subForm, mBinding.llFormList);
                } else {
                    populateMultiInputBox(subForm, mBinding.llFormList);
                }
            } else {
                HeaderTextView headerTextView = new HeaderTextView(EditFormActivity.this);
                headerTextView.setText(subForm.getQuestions());
                mBinding.llFormList.addView(headerTextView);
            }
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "populateLabelText: "+e.getMessage() );
        }
    }

    private void populateMultiInputBoxFourthSection(SurveyQuestionWithData subForm, LinearLayout linearLayout) {
        HeaderTextView headerTextView = new HeaderTextView(EditFormActivity.this);
        headerTextView.setText(subForm.getQuestions());
        linearLayout.addView(headerTextView);

        SingleMultipleInputBoxParentLayoutBinding binding = SingleMultipleInputBoxParentLayoutBinding.inflate(getLayoutInflater());
        binding.llFourthSection.setVisibility(View.VISIBLE);
        binding.horizontalScrollOther.setVisibility(View.GONE);

        int width = getScreenWidth(3);
        int height = getScreenHeight(5);

        addLabelHeaderFourthSection(subForm, binding.llMultiInputParentOne);
        for (int j = 0; j < subForm.getQuestionOptions().size(); j++) {
            HeaderTextView firstColumn = new HeaderTextView(EditFormActivity.this);
            firstColumn.setText(subForm.getQuestionOptions().get(j).getQNA_Values());
            firstColumn.setBackground(getResources().getDrawable(R.drawable.balck_border_rectangle));
            //int width = CommonUtils.dip2pix(EditFormActivity.this, getResources().getDimensionPixelSize(R.dimen.multi_input_width));
            //int height = CommonUtils.dip2pix(EditFormActivity.this, getResources().getDimensionPixelSize(R.dimen.multi_input_height_one));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            firstColumn.setLayoutParams(params);
            firstColumn.setPadding(10,10,10,10);
            binding.llFirstColumn.addView(firstColumn);
            addInputOptionFourthSection(subForm.getInputValidation(), subForm.getQuestionOptions().get(j), binding.llMultiInputParentOne,subForm.getLinked_question_id());
        }
        Button btn_calculate = new Button(EditFormActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,10,0,10);
        btn_calculate.setLayoutParams(params);
        btn_calculate.setText(getResources().getString(R.string.calculate));
        btn_calculate.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn_calculate.setTextColor(Color.WHITE);
        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ll_count = binding.llMultiInputParentOne.getChildCount();
                String option = "";
                first_column_count = 0;
                second_column_count = 0;
                third_column_count = 0;
                fourth_column_count = 0;
                boolean isTotal = false;

                for (int k = 0; k < ll_count; k++) {
                    View child_view = binding.llMultiInputParentOne.getChildAt(k);

                    if (child_view instanceof LinearLayout) {
                        for (int j = 0; j < ((LinearLayout) child_view).getChildCount(); j++) {
                            View sub_child_view = ((LinearLayout) child_view).getChildAt(j);

                            if (sub_child_view instanceof HeaderTextView) {
                                HeaderTextView headerTextView = (HeaderTextView) sub_child_view;
                                option = headerTextView.getText().toString().replace("*", "").trim();
                                Log.e("Text", option);
                                if (option.contains("4.16")) {
                                    isTotal = true;
                                } else {
                                    isTotal = false;
                                }
                            }
                            if (sub_child_view instanceof CustomEditText) {
                                CustomEditText editText = (CustomEditText) sub_child_view;
                                if (isTotal) {
                                    if (editText.getIndex() == 0) {
                                        editText.setText(""+first_column_count);
                                    } else if (editText.getIndex() == 1) {
                                        editText.setText(""+second_column_count);
                                    } else if (editText.getIndex() == 2) {
                                        editText.setText(""+third_column_count);
                                    } else if (editText.getIndex() == 3) {
                                        editText.setText(""+fourth_column_count);
                                    }
                                } else {
                                    int value;
                                    if (editText.getText().toString().isEmpty()) {
                                        editText.setText("0");
                                        value = 0;
                                    } else {
                                        value = Integer.parseInt(editText.getText().toString());
                                    }
                                    if (editText.getIndex() == 0) {
                                        first_column_count = first_column_count + value;
                                    } else if (editText.getIndex() == 1) {
                                        second_column_count = second_column_count + value;
                                    } else if (editText.getIndex() == 2) {
                                        third_column_count = third_column_count + value;
                                    } else if (editText.getIndex() == 3) {
                                        fourth_column_count = fourth_column_count + value;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

       // binding.llMultiInputParent.addView(btn_calculate);
        linearLayout.addView(binding.getRoot());
        linearLayout.addView(btn_calculate);
    }

    private void addInputOptionFourthSection(int validation, QuestionOption questionOption, LinearLayout llMultiInputParent,int linked_question_index) {
        boolean isTotal = false;
        SingleMultipleInputBoxLayoutBinding binding = SingleMultipleInputBoxLayoutBinding.inflate(getLayoutInflater());
        binding.setOption(questionOption);
        binding.tvHeader.setVisibility(View.GONE);
        int tot_input_boxes = Integer.parseInt(questionOption.getDisplayTypeCount());
        if (questionOption.getQNA_Values().contains("4.16")) {
            isTotal = true;
        }
        int width = getScreenWidth(3);
        int height = getScreenHeight(5);

        CandidateDetails candidateDetails = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionId(questionOption.getQNAOption_ID(), formId,linked_question_index);
        if (candidateDetails != null && candidateDetails.getSurvey_que_values() != null) {
            String value = candidateDetails.getSurvey_que_values();
            List<String> values = Arrays.asList(value.split(","));
            for (int j = 0; j < tot_input_boxes; j++) {
                CustomEditText textView = new CustomEditText(EditFormActivity.this);
                if (isTotal) {
                    textView.setEnabled(false);
                }
                int ten_dp = CommonUtils.dip2pix(EditFormActivity.this, 10);
                textView.setPadding(ten_dp, ten_dp, ten_dp, ten_dp);
                textView.setInputType(validation);
                textView.setText(values.get(j));
                textView.setIndex(j);
                textView.setSubForm(questionOption.getQNAOption_ID());
                textView.setLinked_id(linked_question_index);
                textView.setBackground(getResources().getDrawable(R.drawable.balck_border_rectangle));
               // int width = CommonUtils.dip2pix(EditFormActivity.this, getResources().getDimensionPixelSize(R.dimen.multi_input_width));
                //int height = CommonUtils.dip2pix(EditFormActivity.this, getResources().getDimensionPixelSize(R.dimen.multi_input_height_one));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                textView.setLayoutParams(params);
                binding.llInputBox.addView(textView);
            }
        } else {
            for (int j = 0; j < tot_input_boxes; j++) {
                CustomEditText textView = new CustomEditText(EditFormActivity.this);
                if (isTotal) {
                    textView.setEnabled(false);
                }
                int ten_dp = CommonUtils.dip2pix(EditFormActivity.this, 10);
                textView.setPadding(ten_dp, ten_dp, ten_dp, ten_dp);
                textView.setInputType(validation);
                textView.setSubForm(questionOption.getQNAOption_ID());
                textView.setIndex(j);
                textView.setLinked_id(linked_question_index);
                textView.setBackground(getResources().getDrawable(R.drawable.balck_border_rectangle));
                //int width = CommonUtils.dip2pix(EditFormActivity.this, getResources().getDimensionPixelSize(R.dimen.multi_input_width));
                //int height = CommonUtils.dip2pix(EditFormActivity.this, getResources().getDimensionPixelSize(R.dimen.multi_input_height_one));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                textView.setLayoutParams(params);
                binding.llInputBox.addView(textView);
            }
        }
        llMultiInputParent.addView(binding.getRoot());
    }

    private void populateMultiInputBox(SurveyQuestionWithData subForm, LinearLayout linearLayout) {
        HeaderTextView headerTextView = new HeaderTextView(EditFormActivity.this);
        headerTextView.setText(subForm.getQuestions());
        linearLayout.addView(headerTextView);

        SingleMultipleInputBoxParentLayoutBinding binding = SingleMultipleInputBoxParentLayoutBinding.inflate(getLayoutInflater());

        binding.llFourthSection.setVisibility(View.GONE);
        binding.horizontalScrollOther.setVisibility(View.VISIBLE);

        addLabelHeader(subForm, binding.llMultiInputParent);
        for (int j = 0; j < subForm.getQuestionOptions().size(); j++) {
            addInputOption(subForm.getInputValidation(), subForm.getQuestionOptions().get(j), binding.llMultiInputParent,subForm.getLinked_question_id());
        }
        linearLayout.addView(binding.getRoot());
    }

    private void addInputOption(int validation, QuestionOption questionOption, LinearLayout llMultiInputParent,int linked_question_index) {
        SingleMultipleInputBoxLayoutBinding binding = SingleMultipleInputBoxLayoutBinding.inflate(getLayoutInflater());
        binding.setOption(questionOption);
        int tot_input_boxes = Integer.parseInt(questionOption.getDisplayTypeCount());
        int width = getScreenWidth(4);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
        binding.tvHeader.setLayoutParams(params);

        CandidateDetails candidateDetails = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionId(questionOption.getQNAOption_ID(), formId,linked_question_index);
        if (candidateDetails != null && candidateDetails.getSurvey_que_values() != null) {
            String value = candidateDetails.getSurvey_que_values();
            List<String> values = Arrays.asList(value.split(","));
            for (int j = 0; j < tot_input_boxes; j++) {
                CustomEditText textView = new CustomEditText(EditFormActivity.this);
                int ten_dp = CommonUtils.dip2pix(EditFormActivity.this, 8);
                textView.setPadding(ten_dp, ten_dp, ten_dp, ten_dp);
                textView.setInputType(validation);
                textView.setText(values.get(j));
                textView.setSubForm(questionOption.getQNAOption_ID());
                textView.setLinked_id(linked_question_index);
                textView.setBackground(getResources().getDrawable(R.drawable.balck_border_rectangle));
                //int width = CommonUtils.dip2pix(EditFormActivity.this, getResources().getDimensionPixelSize(R.dimen.multi_input_width));
                params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
                textView.setLayoutParams(params);
                binding.llInputBox.addView(textView);
            }
        } else {
            for (int j = 0; j < tot_input_boxes; j++) {
                CustomEditText textView = new CustomEditText(EditFormActivity.this);
                int ten_dp = CommonUtils.dip2pix(EditFormActivity.this, 8);
                textView.setPadding(ten_dp, ten_dp, ten_dp, ten_dp);
                textView.setInputType(validation);
                textView.setSubForm(questionOption.getQNAOption_ID());
                textView.setLinked_id(linked_question_index);
                textView.setBackground(getResources().getDrawable(R.drawable.balck_border_rectangle));
                //int width = CommonUtils.dip2pix(EditFormActivity.this, getResources().getDimensionPixelSize(R.dimen.multi_input_width));
                params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
                textView.setLayoutParams(params);
                binding.llInputBox.addView(textView);
            }
        }
        llMultiInputParent.addView(binding.getRoot());
    }

    /**
     * Label Header for fourth section
     * @param subForm
     * @param llMultiInputParent
     */
    private void addLabelHeaderFourthSection(SurveyQuestionWithData subForm, LinearLayout llMultiInputParent) {
        String label_header = subForm.getLabelHeader().replaceAll("\".*\"", "");
        List<String> labels = Arrays.asList(label_header.split(","));
        SingleMultipleInputBoxLayoutBinding binding = SingleMultipleInputBoxLayoutBinding.inflate(getLayoutInflater());
        QuestionOption questionOption = new QuestionOption();
        questionOption.setQNA_Values("अनु क्र.");
        binding.tvHeader.setVisibility(View.GONE);
        binding.setOption(questionOption);

        int width = getScreenWidth(3);

        for (int j = 0; j < labels.size(); j++) {
            //add Header To Layout
            HeaderTextView textView = new HeaderTextView(EditFormActivity.this);
            int ten_dp = CommonUtils.dip2pix(EditFormActivity.this, 10);
            textView.setPadding(ten_dp, ten_dp, ten_dp, ten_dp);
            textView.setBackground(getResources().getDrawable(R.drawable.balck_border_rectangle));
            //int width = CommonUtils.dip2pix(EditFormActivity.this, getResources().getDimensionPixelSize(R.dimen.multi_input_width));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(params);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            textView.setMinLines(2);
            textView.setMaxLines(100);
            textView.setText(labels.get(j));
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
        //binding.tvHeader.setVisibility(View.GONE);
        binding.setOption(questionOption);

        int width = getScreenWidth(4);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);

        binding.tvHeader.setLayoutParams(params);

        for (int j = 0; j < labels.size(); j++) {
            //add Header To Layout
            HeaderTextView textView = new HeaderTextView(EditFormActivity.this);
            int ten_dp = CommonUtils.dip2pix(EditFormActivity.this, 10);
            textView.setPadding(ten_dp, ten_dp, ten_dp, ten_dp);
            textView.setBackground(getResources().getDrawable(R.drawable.balck_border_rectangle));
            //int width = CommonUtils.dip2pix(EditFormActivity.this, getResources().getDimensionPixelSize(R.dimen.multi_input_width));
            params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(params);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            textView.setMinLines(2);
            textView.setMaxLines(100);
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
        try {
            SingleCheckBoxesLayoutBinding binding = SingleCheckBoxesLayoutBinding.inflate(getLayoutInflater());
            binding.setSubForm(subForm);
            binding.rgCheckboxQuestionOptions.setSubForm(subForm);
            binding.rgCheckboxQuestionOptions.setLinked_id(0);
            CandidateDetails candetails = null;
            String qna_options = null;
            List<CandidateDetails> detailList = DatabaseUtil.on().getCandidateDetailsDao().getAllDetailsBySurveyQueId(subForm.getSurveyQuestion_ID(),formId);
            if (detailList.size() > 0) {
                candetails = detailList.get(0);
                qna_options = candetails.getSurvey_que_option_id();
            }

            for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
                QuestionOption questionOption = subForm.getQuestionOptions().get(i);
                CustomCheckBox checkBox = new CustomCheckBox(EditFormActivity.this);
                checkBox.setText(questionOption.getQNA_Values());
                checkBox.setSubForm(questionOption);
                checkBox.setLinked_id(subForm.getLinked_question_id());
                if (qna_options != null && qna_options.contains(questionOption.getQNAOption_ID())) {
                    checkBox.setChecked(true);
                    if (DatabaseUtil.on().isPresentInOtherValues(questionOption)) {
                        binding.llChildQuestion.setVisibility(View.VISIBLE);
                        binding.edtDropDownItar.setVisibility(View.VISIBLE);
                        binding.edtDropDownItar.setSubForm(questionOption);
                        binding.edtDropDownItar.setLinked_id(0);
                        List<CandidateDetails> details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionIdList(questionOption.getQNAOption_ID(), formId,subForm.getLinked_question_id());
                        if (details != null && details.size() > 0) {
                            if (details.size() > 1) {
                                binding.edtDropDownItar.setText("" + details.get(1).getSurvey_que_values());
                            } else {
                                binding.edtDropDownItar.setText("" + details.get(0).getSurvey_que_values());
                            }
                        }
                    } else if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("null") && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                        String childQuesId = questionOption.getChildQuestionId();
                        if (childQuesId != null && childQuesId.contains(",")) {
                            List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                            for (int k = 0; k < childQuesIds.size(); k++) {
                                SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesIds.get(k),formId);
                                if (childQues != null) {
                                    childQues.setLinked_question_id(subForm.getLinked_question_id());
                                    singleBinding.llRoot.setQuestionOption(questionOption);
                                    addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                    binding.llChildQuestion.addView(singleBinding.getRoot());
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesId,formId);
                            SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                            if (childQues != null) {
                                childQues.setLinked_question_id(subForm.getLinked_question_id());
                                singleBinding.llRoot.setQuestionOption(questionOption);
                                addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                binding.llChildQuestion.addView(singleBinding.getRoot());
                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    //checkBox.callOnClick();
                    //checkBoxOnClick(checkBox, questionOption, binding.edtDropDownItar, binding.llChildQuestion, binding.getRoot(),subForm.getLinked_question_id());
                }
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //binding.llChildQuestion.removeAllViews();
                        binding.llChildQuestion.setVisibility(View.VISIBLE);
                        for (int j = 0; j < binding.rgCheckboxQuestionOptions.getChildCount(); j++) {
                            CustomCheckBox cb = (CustomCheckBox) binding.rgCheckboxQuestionOptions.getChildAt(j);
                            if (cb.getText().toString().equalsIgnoreCase(checkBox.getText().toString())) {
                                cb = checkBox;
                            }
                            if (cb.isChecked()) {
                                QuestionOption option = (QuestionOption) cb.getSubForm();
                                //Add check box child
                                if (DatabaseUtil.on().isPresentInOtherValues(option)) {
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                    binding.edtDropDownItar.setVisibility(View.VISIBLE);
                                    binding.edtDropDownItar.setSubForm(option);
                                    binding.edtDropDownItar.setLinked_id(0);
                                    List<CandidateDetails> details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionIdList(questionOption.getQNAOption_ID(), formId,subForm.getLinked_question_id());
                                    if (details != null && details.size() > 0) {
                                        if (details.size() > 1) {
                                            binding.edtDropDownItar.setText("" + details.get(1).getSurvey_que_values());
                                        } else {
                                            binding.edtDropDownItar.setText("" + details.get(0).getSurvey_que_values());
                                        }
                                    }
                                } else if (option.getChildQuestionId() != null && !option.getChildQuestionId().equalsIgnoreCase("null") && !option.getChildQuestionId().equalsIgnoreCase("")) {
                                    String childQuesId = option.getChildQuestionId();
                                    if (childQuesId != null && childQuesId.contains(",")) {
                                        List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                                        for (int k = 0; k < childQuesIds.size(); k++) {
                                            SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                            SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesIds.get(k),formId);
                                            if (childQues != null) {
                                                childQues.setLinked_question_id(subForm.getLinked_question_id());
                                                singleBinding.llRoot.setQuestionOption(option);
                                                addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                                binding.llChildQuestion.addView(singleBinding.getRoot());
                                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    } else {
                                        SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesId,formId);
                                        SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                        if (childQues != null) {
                                            childQues.setLinked_question_id(subForm.getLinked_question_id());
                                            singleBinding.llRoot.setQuestionOption(option);
                                            addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                            binding.llChildQuestion.addView(singleBinding.getRoot());
                                            binding.llChildQuestion.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            } else {
                                //remove check box child
                                //removeChild(cb,binding.llChildQuestion);
                                QuestionOption option = (QuestionOption) cb.getSubForm();
                                //Add check box child
                                if (option.getChildQuestionId() != null && !option.getChildQuestionId().equalsIgnoreCase("null") && !option.getChildQuestionId().equalsIgnoreCase("")) {
                                    for (int k = 0; k < binding.llChildQuestion.getChildCount(); k++) {
                                        View child_view = binding.llChildQuestion.getChildAt(k);
                                        if (child_view instanceof CustomCheckBoxLinearLayout) {
                                            QuestionOption child_option = ((CustomCheckBoxLinearLayout) child_view).getQuestionOption();
                                            if (child_option.getQNAOption_ID().equalsIgnoreCase(option.getQNAOption_ID())) {
                                                binding.llChildQuestion.removeView(child_view);
                                            }
                                        }
                                    }
                                } else if (DatabaseUtil.on().isPresentInOtherValues(option)) {
                                    binding.edtDropDownItar.setVisibility(View.GONE);
                                    binding.edtDropDownItar.setText("");
                                }
                            }
                        }
                    }
                });
                binding.rgCheckboxQuestionOptions.addView(checkBox, i);
            }
            mBinding.llFormList.addView(binding.getRoot());
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "populateCheckBox: "+e.getMessage() );
        }
    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert DropDown in layout
     */
    private void populateDropDown(SurveyQuestionWithData subForm) {
        try {
            SingleDropDownListLayoutBinding binding = SingleDropDownListLayoutBinding.inflate(getLayoutInflater());
            binding.setSubForm(subForm);

            ArrayAdapter<QuestionOption> mAdapter = new ArrayAdapter<>(EditFormActivity.this, android.R.layout.simple_spinner_item, subForm.getQuestionOptions());
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //DropDownArrayAdapter adapter = new DropDownArrayAdapter(EditFormActivity.this, R.layout.single_drop_down_item, subForm.getQuestionOptions());
            binding.sprQuestionOption.setAdapter(mAdapter);
            binding.sprQuestionOption.setSubForm(subForm);
            binding.sprQuestionOption.setLinked_id(0);
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
                                    SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                    SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesIds.get(j),formId);
                                    if (childQues != null) {
    //                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                                        childQues.setLinked_question_id(subForm.getLinked_question_id());
                                        addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                    }
                                    binding.llChildQuestion.addView(singleBinding.getRoot());
                                }
                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                                binding.edtDropDownItar.setVisibility(View.GONE);
                            } else {
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesId,formId);
                                if (childQues != null) {
    //                                populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    childQues.setLinked_question_id(subForm.getLinked_question_id());
                                    addSingleChildViews(childQues, binding.llChildQuestion, binding.getRoot());
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                    binding.edtDropDownItar.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            binding.llChildQuestion.removeAllViews();
                            binding.llChildQuestion.setVisibility(View.GONE);
                            if (DatabaseUtil.on().isPresentInOtherValues(questionOption)) {
                                binding.edtDropDownItar.setVisibility(View.VISIBLE);
                                binding.edtDropDownItar.setSubForm(questionOption);
                                binding.edtDropDownItar.setLinked_id(0);
                                List<CandidateDetails> details = DatabaseUtil.on().getCandidateDetailsDao()
                                        .getCandidateDetailsByQuestionOptionIdList(questionOption.getQNAOption_ID(), formId,0);
                                if (details != null && details.size() > 0) {
                                    if (details.size() > 1) {
                                        binding.edtDropDownItar.setText("" + details.get(1).getSurvey_que_values());
                                    } else {
                                        binding.edtDropDownItar.setText("" + details.get(0).getSurvey_que_values());
                                    }
                                }

                            } else {
                                binding.edtDropDownItar.setVisibility(View.GONE);
                            }
                        }

                    } catch (Exception e) {
                        Toast.makeText(EditFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            mBinding.llFormList.addView(binding.getRoot());
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "populateDropDown: "+e.getMessage() );
        }
    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert radio button in layout
     */
    private void populateRadioButton(SurveyQuestionWithData subForm) {
        try {
            SingleRadioButtonsLayoutBinding binding = SingleRadioButtonsLayoutBinding.inflate(getLayoutInflater());
            binding.setSubForm(subForm);
            binding.rgQuestionOptions.setSubForm(subForm);
            binding.rgQuestionOptions.setLinked_id(0);

            for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
                QuestionOption questionOption = subForm.getQuestionOptions().get(i);
                CustomRadioButton radioButton = new CustomRadioButton(EditFormActivity.this);
                radioButton.setSubForm(questionOption);
                radioButton.setLinked_id(0);
                radioButton.setText(questionOption.getQNA_Values());
                binding.rgQuestionOptions.addView(radioButton);
                List<CandidateDetails> details = DatabaseUtil.on().getCandidateDetailsDao()
                        .getCandidateDetailsByQuestionOptionIdList(questionOption.getQNAOption_ID(), formId,0);
                if (details != null && details.size() > 0 && questionOption.getQNA_Values().equalsIgnoreCase(details.get(0).getSurvey_que_values())) {
                    radioButton.setChecked(true);
                    //radioButtonOnClick(questionOption, binding.llChildQuestion, binding.getRoot(),0);
                    try {
                        if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("null") && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                            //Toast.makeText(SubFormActivity.this, "Id : "+questionOption.getChildQuestionId(), Toast.LENGTH_SHORT).show();
                            binding.llChildQuestion.removeAllViews();
                            binding.btnAddAnother.setVisibility(View.GONE);
                            String childQuesId = questionOption.getChildQuestionId();
                            if (childQuesId.contains(",")) {
                                //binding.btnAddAnother.setVisibility(View.GONE);
                                List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                                for (int j = 0; j < childQuesIds.size(); j++) {
                                    String currentChildQuesId = null;
                                    try {
                                        currentChildQuesId = childQuesIds.get(j);
                                    } catch (Exception e) {
                                        Log.e("TAG", "radioButtonOnClick: "+e.getMessage());
                                    }
                                    SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                    SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(currentChildQuesId,formId);
                                    if (childQues != null) {
//                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                                        childQues.setLinked_question_id(linked_question_index);
                                        addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                    }
                                    binding.llChildQuestion.addView(singleBinding.getRoot());
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                }
                            } else {
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesId,formId);
                                if (childQues != null) {
//                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    childQues.setLinked_question_id(linked_question_index);
                                    addSingleChildViews(childQues, binding.llChildQuestion, binding.getRoot());
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.llChildQuestion.removeAllViews();
                            binding.btnAddAnother.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        //Toast.makeText(EditFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "radioButtonOnClick: "+e.getMessage() );
                    }
                }
                radioButton.setSubForm(questionOption);
                radioButton.setLinked_id(0);
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // radioButtonOnClick(questionOption, binding.llChildQuestion, binding.getRoot(),0);
                        try {
                            if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("null") && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                                //Toast.makeText(SubFormActivity.this, "Id : "+questionOption.getChildQuestionId(), Toast.LENGTH_SHORT).show();
                                binding.llChildQuestion.removeAllViews();
                                binding.btnAddAnother.setVisibility(View.GONE);
                                String childQuesId = questionOption.getChildQuestionId();
                                if (childQuesId.contains(",")) {
                                    //binding.btnAddAnother.setVisibility(View.GONE);
                                    List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                                    for (int j = 0; j < childQuesIds.size(); j++) {
                                        String currentChildQuesId = null;
                                        try {
                                            currentChildQuesId = childQuesIds.get(j);
                                        } catch (Exception e) {
                                            Log.e("TAG", "radioButtonOnClick: "+e.getMessage());
                                        }
                                        SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                        SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(currentChildQuesId,formId);
                                        if (childQues != null) {
//                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                                            childQues.setLinked_question_id(linked_question_index);
                                            addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                        }
                                        binding.llChildQuestion.addView(singleBinding.getRoot());
                                        binding.llChildQuestion.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesId,formId);
                                    if (childQues != null) {
//                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                                        childQues.setLinked_question_id(linked_question_index);
                                        addSingleChildViews(childQues, binding.llChildQuestion, binding.getRoot());
                                        binding.llChildQuestion.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                binding.llChildQuestion.setVisibility(View.GONE);
                                binding.llChildQuestion.removeAllViews();
                                binding.btnAddAnother.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            //Toast.makeText(EditFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "radioButtonOnClick: "+e.getMessage() );
                        }
                    }
                });
            }
            mBinding.llFormList.addView(binding.getRoot());
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "populateRadioButton: "+e.getMessage() );
        }
    }

    /*private void radioButtonOnClick(QuestionOption questionOption, LinearLayout llChildQuestion, View root,int linked_question_index) {
        try {
            if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("null") && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                //Toast.makeText(SubFormActivity.this, "Id : "+questionOption.getChildQuestionId(), Toast.LENGTH_SHORT).show();
                llChildQuestion.removeAllViews();
                String childQuesId = questionOption.getChildQuestionId();
                if (childQuesId.contains(",")) {
                    //binding.btnAddAnother.setVisibility(View.GONE);
                    List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                    for (int j = 0; j < childQuesIds.size(); j++) {
                        String currentChildQuesId = null;
                        try {
                            currentChildQuesId = childQuesIds.get(j);
                        } catch (Exception e) {
                            Log.e("TAG", "radioButtonOnClick: "+e.getMessage());
                        }
                        SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                        SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(currentChildQuesId,formId);
                        if (childQues != null) {
//                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                            childQues.setLinked_question_id(linked_question_index);
                            addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                        }
                        llChildQuestion.addView(singleBinding.getRoot());
                        llChildQuestion.setVisibility(View.VISIBLE);
                    }
                } else {
                    SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesId,formId);
                    if (childQues != null) {
//                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                        childQues.setLinked_question_id(linked_question_index);
                        addSingleChildViews(childQues, llChildQuestion, root);
                        llChildQuestion.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                llChildQuestion.setVisibility(View.GONE);
                llChildQuestion.removeAllViews();
            }
        } catch (Exception e) {
            //Toast.makeText(EditFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "radioButtonOnClick: "+e.getMessage() );
        }
    }
*/
    /**
     * @param subForm
     * @date 7-3-2022
     * Insert CustomEditText with Label in layout
     */
    private void populateInputText(SurveyQuestionWithData subForm) {
        try {
            SingleInputBoxLayoutBinding binding = SingleInputBoxLayoutBinding.inflate(getLayoutInflater());
            String quesId = subForm.getSurveyQuestion_ID();
            CandidateDetails details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionIdFormId(quesId, formId,0);
            if (details != null) {
                subForm.setValue(details.getSurvey_que_values());
            }
            ScreenHelper.setEditTextValidation(subForm, binding.edtHeader);
            binding.edtHeader.setSubForm(subForm);
            binding.edtHeader.setLinked_id(0);
            binding.edtHeader.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    try {
                        int max_length = Integer.parseInt(subForm.getMax_length());
                        int min_length = Integer.parseInt(subForm.getMin_length());

                        if (min_length != 0 && max_length != 0) {
                            if (binding.edtHeader.getText().toString().length() < min_length || binding.edtHeader.getText().toString().length() > max_length) {
                                binding.edtHeader.setError("कृपया कमीत कमी " + min_length + " आणि जास्तीत जास्त " + max_length + " अक्षरं टाका");
                                //binding.edtHeader.requestFocus();
                            }
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            binding.setSubForm(subForm);
            mBinding.llFormList.addView(binding.getRoot());
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "populateInputText: "+e.getMessage() );
        }
    }

    /**
     * @param subForm
     * @date 7-3-2022
     * populate linked question
     */
    private void populateChildQuestionInput(SurveyQuestionWithData subForm, LinearLayout rootView) {
        switch (subForm.getQuestionType().getQuestionTypes()) {
            case AppKeys.INPUT_TEXT:
                populateChildInputText(subForm, rootView, null);
                break;
            case AppKeys.RADIO_BUTTON:
                populateChildRadioButton(subForm, rootView, null);
                break;
            case AppKeys.CHECKBOX:
                populateChildCheckBox(subForm, rootView, null);
                break;
            case AppKeys.DROPDOWNLIST:
                populateChildDropDown(subForm, rootView, null);
                break;
            case AppKeys.LABEL_TEXT:
                populateChildLabelText(subForm, rootView, null);
                break;
            case AppKeys.HEADER_TEXT:
                populateChildHeaderText(subForm, rootView, null);
                break;
            case AppKeys.DROPDOWNMULTISELECT:
                populateChildDropDownMultiSelect(subForm, rootView, null);
                break;
        }

    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert Multi Select Dropdown
     */
    private void populateChildDropDownMultiSelect(SurveyQuestionWithData subForm, LinearLayout rootView, CandidateDetails candidateDetails) {
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

    private void populateChildHeaderText(SurveyQuestionWithData subForm, LinearLayout rootView, CandidateDetails candidateDetails) {
        try {
            HeaderTextView headerTextView = new HeaderTextView(EditFormActivity.this);
            headerTextView.setText(subForm.getQuestions());
            rootView.addView(headerTextView);
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "populateChildHeaderText: "+e.getMessage() );
        }
    }

    private void populateChildLabelText(SurveyQuestionWithData subForm, LinearLayout rootView, CandidateDetails candidateDetails) {
        try {
            if (!TextUtils.isEmpty(subForm.getLabelHeader().trim())) {
                //add multiple edittext
                if(subForm.getLinked_question_id() == -1) {
                    subForm.setLinked_question_id(0);
                }
                populateMultiInputBox(subForm, rootView);
            } else {
                HeaderTextView headerTextView = new HeaderTextView(EditFormActivity.this);
                headerTextView.setText(subForm.getQuestions());
                rootView.addView(headerTextView);
            }
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "populateChildLabelText: "+e.getMessage() );
        }
    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert check box in layout
     */
    private void populateChildCheckBox(SurveyQuestionWithData subForm, LinearLayout rootView, CandidateDetails candidateDetails) {
        try {
            SingleCheckBoxesLayoutBinding binding = SingleCheckBoxesLayoutBinding.inflate(getLayoutInflater());
            binding.setSubForm(subForm);
            binding.rgCheckboxQuestionOptions.setSubForm(subForm);
            binding.rgCheckboxQuestionOptions.setLinked_id(subForm.getLinked_question_id());

            CandidateDetails candetails = null;
            String qna_options = null;
            List<CandidateDetails> detailList = DatabaseUtil.on().getCandidateDetailsDao().getAllDetailsBySurveyQueId(subForm.getSurveyQuestion_ID(),formId);
            if (detailList.size() > 0) {
                candetails = detailList.get(0);
                qna_options = candetails.getSurvey_que_option_id();
            }

            for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
                QuestionOption questionOption = subForm.getQuestionOptions().get(i);
                CustomCheckBox checkBox = new CustomCheckBox(EditFormActivity.this);
                checkBox.setText(questionOption.getQNA_Values());
                checkBox.setSubForm(questionOption);
                checkBox.setLinked_id(subForm.getLinked_question_id());
                if (qna_options != null && qna_options.contains(questionOption.getQNAOption_ID())) {
                    checkBox.setChecked(true);
                    if (DatabaseUtil.on().isPresentInOtherValues(questionOption)) {
                        binding.llChildQuestion.setVisibility(View.VISIBLE);
                        binding.edtDropDownItar.setVisibility(View.VISIBLE);
                        binding.edtDropDownItar.setSubForm(questionOption);
                        binding.edtDropDownItar.setLinked_id(subForm.getLinked_question_id());
                        List<CandidateDetails> details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionIdList(questionOption.getQNAOption_ID(), formId,subForm.getLinked_question_id());
                        if (details != null && details.size() > 0) {
                            if (details.size() > 1) {
                                binding.edtDropDownItar.setText("" + details.get(1).getSurvey_que_values());
                            } else {
                                binding.edtDropDownItar.setText("" + details.get(0).getSurvey_que_values());
                            }
                        }
                    } else if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("null") && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                        String childQuesId = questionOption.getChildQuestionId();
                        if (childQuesId != null && childQuesId.contains(",")) {
                            List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                            for (int k = 0; k < childQuesIds.size(); k++) {
                                SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesIds.get(k),formId);
                                if (childQues != null) {
                                    childQues.setLinked_question_id(subForm.getLinked_question_id());
                                    singleBinding.llRoot.setQuestionOption(questionOption);
                                    addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                    binding.llChildQuestion.addView(singleBinding.getRoot());
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesId,formId);
                            SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                            if (childQues != null) {
                                childQues.setLinked_question_id(subForm.getLinked_question_id());
                                singleBinding.llRoot.setQuestionOption(questionOption);
                                addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                binding.llChildQuestion.addView(singleBinding.getRoot());
                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    //checkBoxOnClick(checkBox, questionOption, binding.edtDropDownItar, binding.llChildQuestion, binding.getRoot(),subForm.getLinked_question_id());
                }
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //binding.llChildQuestion.removeAllViews();
                        binding.llChildQuestion.setVisibility(View.VISIBLE);
                        for (int j = 0; j < binding.rgCheckboxQuestionOptions.getChildCount(); j++) {
                            CustomCheckBox cb = (CustomCheckBox) binding.rgCheckboxQuestionOptions.getChildAt(j);
                            if (cb.getText().toString().equalsIgnoreCase(checkBox.getText().toString())) {
                                cb = checkBox;
                            }
                            if (cb.isChecked()) {
                                QuestionOption option = (QuestionOption) cb.getSubForm();
                                //Add check box child
                                if (DatabaseUtil.on().isPresentInOtherValues(option)) {
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                    binding.edtDropDownItar.setVisibility(View.VISIBLE);
                                    binding.edtDropDownItar.setSubForm(option);
                                    binding.edtDropDownItar.setLinked_id(checkBox.getLinked_id());
                                    List<CandidateDetails> details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionIdList(questionOption.getQNAOption_ID(), formId,subForm.getLinked_question_id());
                                    if (details != null && details.size() > 0) {
                                        if (details.size() > 1) {
                                            binding.edtDropDownItar.setText("" + details.get(1).getSurvey_que_values());
                                        } else {
                                            binding.edtDropDownItar.setText("" + details.get(0).getSurvey_que_values());
                                        }
                                    }
                                } else if (option.getChildQuestionId() != null && !option.getChildQuestionId().equalsIgnoreCase("null") && !option.getChildQuestionId().equalsIgnoreCase("")) {
                                    String childQuesId = questionOption.getChildQuestionId();
                                    if (childQuesId != null && childQuesId.contains(",")) {
                                        List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                                        for (int k = 0; k < childQuesIds.size(); k++) {
                                            SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                            SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesIds.get(k),formId);
                                            if (childQues != null) {
                                                childQues.setLinked_question_id(subForm.getLinked_question_id());
                                                singleBinding.llRoot.setQuestionOption(option);
                                                addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                                binding.llChildQuestion.addView(singleBinding.getRoot());
                                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    } else {
                                        SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesId,formId);
                                        SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                        if (childQues != null) {
                                            childQues.setLinked_question_id(subForm.getLinked_question_id());
                                            singleBinding.llRoot.setQuestionOption(option);
                                            addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                            binding.llChildQuestion.addView(singleBinding.getRoot());
                                            binding.llChildQuestion.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            } else {
                                //remove check box child
                                //removeChild(cb,binding.llChildQuestion);
                                QuestionOption option = (QuestionOption) cb.getSubForm();
                                //Add check box child
                                if (option.getChildQuestionId() != null && !option.getChildQuestionId().equalsIgnoreCase("null") && !option.getChildQuestionId().equalsIgnoreCase("")) {
                                    for (int k = 0; k < binding.llChildQuestion.getChildCount(); k++) {
                                        View child_view = binding.llChildQuestion.getChildAt(k);
                                        if (child_view instanceof CustomCheckBoxLinearLayout) {
                                            QuestionOption child_option = ((CustomCheckBoxLinearLayout) child_view).getQuestionOption();
                                            if (child_option.getQNAOption_ID().equalsIgnoreCase(option.getQNAOption_ID())) {
                                                binding.llChildQuestion.removeView(child_view);
                                            }
                                        }
                                    }
                                } else if (DatabaseUtil.on().isPresentInOtherValues(option)) {
                                    binding.edtDropDownItar.setVisibility(View.GONE);
                                    binding.edtDropDownItar.setText("");
                                }
                            }
                        }
                    }
                });
                binding.rgCheckboxQuestionOptions.addView(checkBox, i);
            }
            rootView.addView(binding.getRoot());
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "populateChildCheckBox: "+e.getMessage() );
        }
    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert DropDown in layout
     */
    private void populateChildDropDown(SurveyQuestionWithData subForm, LinearLayout rootView, CandidateDetails candidateDetails) {
        try {
            SingleDropDownListLayoutBinding binding = SingleDropDownListLayoutBinding.inflate(getLayoutInflater());
            binding.setSubForm(subForm);

            ArrayAdapter<QuestionOption> mAdapter = new ArrayAdapter<>(EditFormActivity.this, android.R.layout.simple_spinner_item, subForm.getQuestionOptions());
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //DropDownArrayAdapter adapter = new DropDownArrayAdapter(EditFormActivity.this, R.layout.single_drop_down_item, subForm.getQuestionOptions());
            binding.sprQuestionOption.setAdapter(mAdapter);
            binding.sprQuestionOption.setSubForm(subForm);
            binding.sprQuestionOption.setLinked_id(subForm.getLinked_question_id());
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
                                    SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                    SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesIds.get(j),formId);
                                    if (childQues != null) {
    //                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                                        childQues.setLinked_question_id(subForm.getLinked_question_id());
                                        addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                    }
                                    binding.llChildQuestion.addView(singleBinding.getRoot());
                                }
                                binding.llChildQuestion.setVisibility(View.VISIBLE);
                                binding.edtDropDownItar.setVisibility(View.GONE);
                            } else {
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesId,formId);
                                if (childQues != null) {
    //                                populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    childQues.setLinked_question_id(subForm.getLinked_question_id());
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
                                binding.edtDropDownItar.setSubForm(questionOption);
                                binding.edtDropDownItar.setLinked_id(subForm.getLinked_question_id());
                                List<CandidateDetails> details = DatabaseUtil.on().getCandidateDetailsDao()
                                        .getCandidateDetailsByQuestionOptionIdList(questionOption.getQNAOption_ID(), formId,subForm.getLinked_question_id());
                                if (details != null && details.size() > 0) {
                                    if (details.size() > 1) {
                                        binding.edtDropDownItar.setText("" + details.get(1).getSurvey_que_values());
                                    } else {
                                        binding.edtDropDownItar.setText("" + details.get(0).getSurvey_que_values());
                                    }
                                }
                            } else {
                                binding.llChildQuestion.removeAllViews();
                                binding.llChildQuestion.setVisibility(View.GONE);
                                binding.edtDropDownItar.setVisibility(View.GONE);
                            }
                        }

                    } catch (Exception e) {
                        Toast.makeText(EditFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            rootView.addView(binding.getRoot());
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "populateChildDropDown: "+e.getMessage() );
        }
    }

    private int getDropDownSelectedPosition(SurveyQuestionWithData subForm) {
        int position = 0;

        if (subForm != null && subForm.getQuestionOptions() != null && subForm.getQuestionOptions().size() > 0) {
            for (int j = 0; j < subForm.getQuestionOptions().size(); j++) {
                QuestionOption questionOption = subForm.getQuestionOptions().get(j);
                CandidateDetails candidateDetails = DatabaseUtil.on().getCandidateDetailsDao()
                        .getCandidateDetailsByQuestionOptionId(questionOption.getQNAOption_ID(), formId,subForm.getLinked_question_id());
                if (candidateDetails != null) {
                    position = j;
                }
            }
        }

        return position;
    }

    /**
     * @param subForm
     * @date 8-3-2022
     * Insert radio button in layout
     */
    private void populateChildRadioButton(SurveyQuestionWithData subForm, LinearLayout rootView, CandidateDetails candidateDetails) {
        try {
            SingleRadioButtonsLayoutBinding binding = SingleRadioButtonsLayoutBinding.inflate(getLayoutInflater());
            binding.setSubForm(subForm);
            binding.rgQuestionOptions.setSubForm(subForm);
            binding.rgQuestionOptions.setLinked_id(subForm.getLinked_question_id());

            for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
                QuestionOption questionOption = subForm.getQuestionOptions().get(i);
                CustomRadioButton radioButton = new CustomRadioButton(EditFormActivity.this);
                radioButton.setSubForm(questionOption);
                radioButton.setLinked_id(subForm.getLinked_question_id());
                radioButton.setText(questionOption.getQNA_Values());
                binding.rgQuestionOptions.addView(radioButton, i);
                List<CandidateDetails> details = DatabaseUtil.on().getCandidateDetailsDao().getCandidateDetailsByQuestionOptionIdList(questionOption.getQNAOption_ID(), formId,subForm.getLinked_question_id());
                if (details != null && details.size() > 0 && questionOption.getQNA_Values().equalsIgnoreCase(details.get(0).getSurvey_que_values())) {
                    radioButton.setChecked(true);
                    //radioButtonOnClick(questionOption, binding.llChildQuestion, binding.getRoot(),subForm.getLinked_question_id());
                    try {
                        if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("null") && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                            //Toast.makeText(SubFormActivity.this, "Id : "+questionOption.getChildQuestionId(), Toast.LENGTH_SHORT).show();
                            binding.llChildQuestion.removeAllViews();
                            binding.btnAddAnother.setVisibility(View.GONE);
                            String childQuesId = questionOption.getChildQuestionId();
                            if (childQuesId.contains(",")) {
                                //binding.btnAddAnother.setVisibility(View.GONE);
                                List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                                for (int j = 0; j < childQuesIds.size(); j++) {
                                    String currentChildQuesId = null;
                                    try {
                                        currentChildQuesId = childQuesIds.get(j);
                                    } catch (Exception e) {
                                        Log.e("TAG", "radioButtonOnClick: "+e.getMessage());
                                    }
                                    SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                    SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(currentChildQuesId,formId);
                                    if (childQues != null) {
//                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                                        childQues.setLinked_question_id(subForm.getLinked_question_id());
                                        addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                    }
                                    binding.llChildQuestion.addView(singleBinding.getRoot());
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                }
                            } else {
                                SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesId,formId);
                                if (childQues != null) {
//                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                                    childQues.setLinked_question_id(subForm.getLinked_question_id());
                                    addSingleChildViews(childQues, binding.llChildQuestion, binding.getRoot());
                                    binding.llChildQuestion.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            binding.llChildQuestion.setVisibility(View.GONE);
                            binding.llChildQuestion.removeAllViews();
                            binding.btnAddAnother.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        //Toast.makeText(EditFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "radioButtonOnClick: "+e.getMessage() );
                    }
                }
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //radioButtonOnClick(questionOption, binding.llChildQuestion, binding.getRoot(),subForm.getLinked_question_id());
                        try {
                            if (questionOption.getChildQuestionId() != null && !questionOption.getChildQuestionId().equalsIgnoreCase("null") && !questionOption.getChildQuestionId().equalsIgnoreCase("")) {
                                //Toast.makeText(SubFormActivity.this, "Id : "+questionOption.getChildQuestionId(), Toast.LENGTH_SHORT).show();
                                binding.llChildQuestion.removeAllViews();
                                binding.btnAddAnother.setVisibility(View.GONE);
                                String childQuesId = questionOption.getChildQuestionId();
                                if (childQuesId.contains(",")) {
                                    //binding.btnAddAnother.setVisibility(View.GONE);
                                    List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                                    for (int j = 0; j < childQuesIds.size(); j++) {
                                        String currentChildQuesId = null;
                                        try {
                                            currentChildQuesId = childQuesIds.get(j);
                                        } catch (Exception e) {
                                            Log.e("TAG", "radioButtonOnClick: "+e.getMessage());
                                        }
                                        SingleLinkedChildDataBinding singleBinding = SingleLinkedChildDataBinding.inflate(getLayoutInflater());
                                        SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(currentChildQuesId,formId);
                                        if (childQues != null) {
//                                        populateChildQuestionInput(childQues,binding.llChildQuestion);
                                            childQues.setLinked_question_id(subForm.getLinked_question_id());
                                            addSingleChildViews(childQues, singleBinding.llChildQuestion, singleBinding.getRoot());
                                        }
                                        binding.llChildQuestion.addView(singleBinding.getRoot());
                                        binding.llChildQuestion.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesId,formId);
                                    if (childQues != null) {
//                                    populateChildQuestionInput(childQues,binding.llChildQuestion);
                                        childQues.setLinked_question_id(subForm.getLinked_question_id());
                                        addSingleChildViews(childQues, binding.llChildQuestion, binding.getRoot());
                                        binding.llChildQuestion.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                binding.llChildQuestion.setVisibility(View.GONE);
                                binding.llChildQuestion.removeAllViews();
                                binding.btnAddAnother.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            //Toast.makeText(EditFormActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "radioButtonOnClick: "+e.getMessage() );
                        }
                    }
                });
            }
            rootView.addView(binding.getRoot());
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "populateChildRadioButton: "+e.getMessage() );
        }
    }

    /**
     * @param subForm
     * @param rootView
     * @date 7-3-2022
     * Insert CustomEditText with Label in layout
     */
    private void populateChildInputText(SurveyQuestionWithData subForm, LinearLayout rootView, CandidateDetails candidateDetails) {
        try {
            SingleInputBoxLayoutBinding binding = SingleInputBoxLayoutBinding.inflate(getLayoutInflater());
            String question = subForm.getQuestions();
            Log.e("TAG", "populateChildInputText: "+question);
            if (candidateDetails != null) {
                subForm.setValue(candidateDetails.getSurvey_que_values());
            } else {
                String quesId = subForm.getSurveyQuestion_ID();
                if (subForm.getLinked_question_id() != 0) {
                    CandidateDetails details = DatabaseUtil.on().getCandidateDetailsDao()
                            .getCandidateDetailsByQuestionIdFormId(quesId, formId,subForm.getLinked_question_id());
                    if (details != null) {
                        subForm.setValue(details.getSurvey_que_values());
                    } else {
                        subForm.setValue("");
                    }
                } else {
                    CandidateDetails details = DatabaseUtil.on().getCandidateDetailsDao()
                            .getCandidateDetailsByQuestionIdFormId(quesId, formId,0);
                    if (details != null) {
                        subForm.setValue(details.getSurvey_que_values());
                    } else {
                        subForm.setValue("");
                    }
                }
            }
            binding.edtHeader.setSubForm(subForm);
            binding.edtHeader.setLinked_id(subForm.getLinked_question_id());
            ScreenHelper.setEditTextValidation(subForm, binding.edtHeader);
            binding.edtHeader.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    int max_length = Integer.parseInt(subForm.getMax_length());
                    int min_length = Integer.parseInt(subForm.getMin_length());

                    if (min_length != 0 && max_length != 0) {
                        if (binding.edtHeader.getText().toString().length() < min_length || binding.edtHeader.getText().toString().length() > max_length) {
                            binding.edtHeader.setError("कृपया कमीत कमी " + min_length + " आणि जास्तीत जास्त " + max_length + " अक्षरं टाका");
                            //binding.edtHeader.requestFocus();
                        }
                    }
                }
            });
            binding.setSubForm(subForm);
            rootView.addView(binding.getRoot());
        } catch (Exception e) {
            //Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "populateChildInputText: "+e.getMessage() );
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
                if (editText.getSubForm() != null && editText.getVisibility() == View.VISIBLE) {
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
                    String str_cb_value = "";
                    String str_cb_option_ids = "";

                    for (int k = 0; k < childCount; k++) {
                        View cbView = radioGroup.getChildAt(k);
                        if (cbView instanceof CustomCheckBox) {
                            CustomCheckBox checkBox = (CustomCheckBox) cbView;
                            if (checkBox.isChecked()) {
                                checked_checkboxes++;
                                QuestionOption questionOption = (QuestionOption) checkBox.getSubForm();
                                //getCheckBoxCandidate(checkBox);
                                if (k == 0 || str_cb_value.isEmpty()) {
                                    str_cb_value = checkBox.getText().toString();
                                    str_cb_option_ids = questionOption.getQNAOption_ID();
                                } else {
                                    str_cb_value = str_cb_value + "," + checkBox.getText().toString();
                                    str_cb_option_ids = str_cb_option_ids + "," + questionOption.getQNAOption_ID();
                                }
                            }
                        }
                    }

                    getCheckBoxCandidate(radioGroup,str_cb_value,str_cb_option_ids);

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
            } else if (view instanceof RelativeLayout) {
                RelativeLayout relativeLayout = (RelativeLayout) view;
                getAllChildViewIterative(relativeLayout);

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
        int linked_id = 0;
        StringBuffer stringBuffer = new StringBuffer();

        for (int k = 0; k < ll_count; k++) {
            View child_view = linearLayout.getChildAt(k);
            //Check option label
            if (child_view instanceof HeaderTextView) {
                HeaderTextView headerTextView = (HeaderTextView) child_view;
                option = headerTextView.getText().toString().replace("*", "").trim();
                Log.e("Text", option);
            }
            if (child_view instanceof CustomEditText) {
                CustomEditText editText = (CustomEditText) child_view;
                optionId = (String) editText.getSubForm();
                linked_id = editText.getLinked_id();
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

        CandidateDetails candidateDetails = getMultiCandidateDetails(optionId,stringBuffer.toString(),linked_id);
        multiEditTextValues.add(candidateDetails);
    }

    private CandidateDetails getMultiCandidateDetails(String str_question,String value,int linked_id) {
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
            if (linked_id > 0) {
                candidateDetails.setIndex_if_linked_question(linked_id);
            }

            return candidateDetails;
        }

        return null;
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
        Intent intent = new Intent(EditFormActivity.this, QuesSectionListActivity.class);
        intent.putExtra(AppKeys.SURVEY_TYPE, surveyType);
        intent.putExtra(AppKeys.CANDIDATE_SURVEY_DETAILS, candidateSurveyStatusDetails);
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
     * @param candidateDetails
     * @param id
     * @date 16-3-2022
     */
    private void updateCandidate(CandidateDetails candidateDetails, int id) {
        DatabaseUtil.on().getCandidateDetailsDao().update_survey_master_id(id, candidateDetails.getSurvey_master_id());
        DatabaseUtil.on().getCandidateDetailsDao().update_survey_section_id(id, candidateDetails.getSurvey_section_id());
        DatabaseUtil.on().getCandidateDetailsDao().update_survey_que_id(id, candidateDetails.getSurvey_que_id());
        DatabaseUtil.on().getCandidateDetailsDao().update_survey_que_option_id(id, candidateDetails.getSurvey_que_option_id());
        DatabaseUtil.on().getCandidateDetailsDao().update_survey_que_values(id, candidateDetails.getSurvey_que_values());
        DatabaseUtil.on().getCandidateDetailsDao().update_FormID(id, formId);
        DatabaseUtil.on().getCandidateDetailsDao().update_Current_Form_Status(id, candidateDetails.getCurrent_Form_Status());
        DatabaseUtil.on().getCandidateDetailsDao().update_age_value(id, candidateDetails.getAge_value());
        DatabaseUtil.on().getCandidateDetailsDao().update_Survey_StartDate(id, candidateDetails.getSurvey_StartDate());
        DatabaseUtil.on().getCandidateDetailsDao().update_Survey_EndDate(id, candidateDetails.getSurvey_EndDate());
        DatabaseUtil.on().getCandidateDetailsDao().update_created_by(id, candidateDetails.getCreated_by());
        DatabaseUtil.on().getCandidateDetailsDao().update_Latitude(id, candidateDetails.getLatitude());
        DatabaseUtil.on().getCandidateDetailsDao().update_Longitude(id, candidateDetails.getLongitude());
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

    private int getScreenWidth(int divider) {
        int screen_width = Helper.getScreenWidth(EditFormActivity.this);
        int width = screen_width / divider;

        if (width == 0) {
            width = screen_width / 2;
        }

        return width;
    }

    private int getScreenHeight(int divider) {
        int screen_width = Helper.getScreenWidth(EditFormActivity.this);
        int height = screen_width / divider;

        if (height == 0) {
            height = screen_width / 4;
        }

        return height;
    }

}