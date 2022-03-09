package com.encureit.samtadoot.features.subforms;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.encureit.samtadoot.R;
import com.encureit.samtadoot.adapters.DropDownArrayAdapter;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.custom.HeaderTextView;
import com.encureit.samtadoot.databinding.ActivitySubFormBinding;
import com.encureit.samtadoot.databinding.SingleAddAnotherItemBinding;
import com.encureit.samtadoot.databinding.SingleAddAnotherParentItemBinding;
import com.encureit.samtadoot.databinding.SingleCheckBoxesLayoutBinding;
import com.encureit.samtadoot.databinding.SingleDropDownListLayoutBinding;
import com.encureit.samtadoot.databinding.SingleDropDownMultiSelectListLayoutBinding;
import com.encureit.samtadoot.databinding.SingleInputBoxLayoutBinding;
import com.encureit.samtadoot.databinding.SingleRadioButtonsLayoutBinding;
import com.encureit.samtadoot.lib.AppKeys;
import com.encureit.samtadoot.models.QuestionOption;
import com.encureit.samtadoot.models.SurveyQuestionWithData;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.contracts.SubFormContract;
import com.encureit.samtadoot.presenter.SubFormPresenter;

import java.util.ArrayList;
import java.util.List;

public class SubFormActivity extends BaseActivity implements SubFormContract.ViewModel {
    private ActivitySubFormBinding mBinding;
    private SubFormPresenter mPresenter;
    private SingleAddAnotherItemBinding mBindingChild;
    private List<SurveyQuestionWithData> list;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySubFormBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPresenter = new SubFormPresenter(SubFormActivity.this,this);
        mBinding.setPresenter(mPresenter);
        mPresenter.startSubForm();
    }

    @Override
    public void setupSubForms(List<SurveyQuestionWithData> list, SurveySection surveySection) {
        this.list = list;
        //set up form tittle
        mBinding.toolbar.tvToolbarTitle.setText(surveySection.getSectionDescription());
        addChildViews(surveySection);

    }

    private void addChildViews(SurveySection surveySection) {
        mBindingChild = SingleAddAnotherItemBinding.inflate(getLayoutInflater());
        //Add Survey Section as a header
        HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
        headerTextView.setText(surveySection.getSectionDescription());
        mBinding.llFormList.addView(headerTextView);

        //add child views and linked views to linear layout
        for ( i = 0; i < list.size(); i++) {
            SurveyQuestionWithData subForm = list.get(i);
            populateQuestion(subForm);
            for (int j = 0; j < subForm.getChildQuestions().size(); j++) {
                populateQuestion(subForm.getChildQuestions().get(j));
            }
            if(subForm.getLinkedQuestions().size() > 0) {
                addLinkedQuestion(subForm);
                mBindingChild.btnAddAnother.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addLinkedQuestion(subForm);
                    }
                });
            }
        }

        mBinding.llFormList.addView(mBindingChild.getRoot());
    }

    private void addLinkedQuestion(SurveyQuestionWithData subForm) {
        //inflating layout single_add_another_parent_item
        SingleAddAnotherParentItemBinding binding = SingleAddAnotherParentItemBinding.inflate(getLayoutInflater());
        for (int j = 0; j < subForm.getLinkedQuestions().size(); j++) {
            SurveyQuestionWithData linkedQuestion = subForm.getLinkedQuestions().get(j);
            populateChildQuestionInput(linkedQuestion,binding.llAddAnotherChild);
        }
        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBindingChild.llAddAnotherSingleView.removeView(binding.llAddAnother);
            }
        });
        mBindingChild.llAddAnotherSingleView.addView(binding.getRoot());
    }

    /*private void addLinkedQuestion(SurveyQuestionWithData subForm) {
        SingleAddAnotherParentItemBinding singleAddAnotherParentItemBinding = SingleAddAnotherParentItemBinding.inflate(getLayoutInflater());
        for (int j = 0; j< subForm.getLinkedQuestions().size();j++) {
            mBindingLinked = SingleLinkedQuestionLayoutBinding.inflate(getLayoutInflater());
            populateChildQuestionInput(subForm.getLinkedQuestions().get(j),j);
            singleAddAnotherParentItemBinding.llAddAnother.addView(mBindingLinked.getRoot());
        }

        singleAddAnotherParentItemBinding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBindingChild.llAddAnotherSingleView.removeView(singleAddAnotherParentItemBinding.llAddAnother);
            }
        });
        mBindingChild.llAddAnotherSingleView.addView(singleAddAnotherParentItemBinding.getRoot());
    }*/

    /**
     * @date 7-3-2022
     * populate question
     * @param subForm
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
     * @date 8-3-2022
     * Insert Multi Select Dropdown
     * @param subForm
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

    private void populateHeaderText(SurveyQuestionWithData subForm) {
        HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
        headerTextView.setText(subForm.getQuestions());
        mBinding.llFormList.addView(headerTextView);
    }

    private void populateLabelText(SurveyQuestionWithData subForm) {
        HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
        headerTextView.setText(subForm.getQuestions());
        mBinding.llFormList.addView(headerTextView);
    }

    /**
     * @date 8-3-2022
     * Insert check box in layout
     * @param subForm
     */
    private void populateCheckBox(SurveyQuestionWithData subForm) {
        SingleCheckBoxesLayoutBinding binding = SingleCheckBoxesLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            CheckBox checkBox = new CheckBox(SubFormActivity.this);
            checkBox.setText(questionOption.getQNA_Values());
            binding.rgCheckboxQuestionOptions.addView(checkBox,i);
        }
        mBinding.llFormList.addView(binding.getRoot());
    }

    /**
     * @date 8-3-2022
     * Insert DropDown in layout
     * @param subForm
     */
    private void populateDropDown(SurveyQuestionWithData subForm) {
        SingleDropDownListLayoutBinding binding = SingleDropDownListLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        DropDownArrayAdapter adapter = new DropDownArrayAdapter(SubFormActivity.this, R.layout.single_drop_down_item,subForm.getQuestionOptions());
        binding.sprQuestionOption.setAdapter(adapter);

        mBinding.llFormList.addView(binding.getRoot());
    }

    /**
     * @date 8-3-2022
     * Insert radio button in layout
     * @param subForm
     */
    private void populateRadioButton(SurveyQuestionWithData subForm) {
        SingleRadioButtonsLayoutBinding binding = SingleRadioButtonsLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            RadioButton radioButton = new RadioButton(SubFormActivity.this);
            radioButton.setText(questionOption.getQNA_Values());
            binding.rgQuestionOptions.addView(radioButton,i);
        }
        mBinding.llFormList.addView(binding.getRoot());
    }

    /**
     * @date 7-3-2022
     * Insert EditText with Label in layout
     * @param subForm
     */
    private void populateInputText(SurveyQuestionWithData subForm) {
        SingleInputBoxLayoutBinding binding = SingleInputBoxLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);
        binding.edtHeader.setInputType(subForm.getInputValidation());
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
                populateChildInputText(subForm,rootView);
                break;
            case AppKeys.RADIO_BUTTON:
                populateChildRadioButton(subForm,rootView);
                break;
            case AppKeys.CHECKBOX:
                populateChildCheckBox(subForm,rootView);
                break;
            case AppKeys.DROPDOWNLIST:
                populateChildDropDown(subForm,rootView);
                break;
            case AppKeys.LABEL_TEXT:
                populateChildLabelText(subForm,rootView);
                break;
            case AppKeys.HEADER_TEXT:
                populateChildHeaderText(subForm,rootView);
                break;
            case AppKeys.DROPDOWNMULTISELECT:
                populateChildDropDownMultiSelect(subForm,rootView);
                break;
        }

    }

    /**
     * @date 8-3-2022
     * Insert Multi Select Dropdown
     * @param subForm
     */
    private void populateChildDropDownMultiSelect(SurveyQuestionWithData subForm,LinearLayout rootView) {
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

    private void populateChildHeaderText(SurveyQuestionWithData subForm,LinearLayout rootView) {
        HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
        headerTextView.setText(subForm.getQuestions());
        rootView.addView(headerTextView);
    }

    private void populateChildLabelText(SurveyQuestionWithData subForm,LinearLayout rootView) {
        HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
        headerTextView.setText(subForm.getQuestions());
        rootView.addView(headerTextView);
    }

    /**
     * @date 8-3-2022
     * Insert check box in layout
     * @param subForm
     */
    private void populateChildCheckBox(SurveyQuestionWithData subForm,LinearLayout rootView) {
        SingleCheckBoxesLayoutBinding binding = SingleCheckBoxesLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            CheckBox checkBox = new CheckBox(SubFormActivity.this);
            checkBox.setText(questionOption.getQNA_Values());
            binding.rgCheckboxQuestionOptions.addView(checkBox,i);
        }
        rootView.addView(binding.getRoot());
    }

    /**
     * @date 8-3-2022
     * Insert DropDown in layout
     * @param subForm
     */
    private void populateChildDropDown(SurveyQuestionWithData subForm,LinearLayout rootView) {
        SingleDropDownListLayoutBinding binding = SingleDropDownListLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        DropDownArrayAdapter adapter = new DropDownArrayAdapter(SubFormActivity.this, R.layout.single_drop_down_item,subForm.getQuestionOptions());
        binding.sprQuestionOption.setAdapter(adapter);

        rootView.addView(binding.getRoot());
    }

    /**
     * @date 8-3-2022
     * Insert radio button in layout
     * @param subForm
     */
    private void populateChildRadioButton(SurveyQuestionWithData subForm,LinearLayout rootView) {
        SingleRadioButtonsLayoutBinding binding = SingleRadioButtonsLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);

        for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
            QuestionOption questionOption = subForm.getQuestionOptions().get(i);
            RadioButton radioButton = new RadioButton(SubFormActivity.this);
            radioButton.setText(questionOption.getQNA_Values());
            binding.rgQuestionOptions.addView(radioButton,i);
        }
        rootView.addView(binding.getRoot());
    }

    /**
     * @date 7-3-2022
     * Insert EditText with Label in layout
     * @param subForm
     * @param rootView
     */
    private void populateChildInputText(SurveyQuestionWithData subForm, LinearLayout rootView) {
        SingleInputBoxLayoutBinding binding = SingleInputBoxLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);
        binding.edtHeader.setInputType(subForm.getInputValidation());
        rootView.addView(binding.getRoot());
    }

    @Override
    public void showResponseFailed(String error) {

    }

    @Override
    public void onBackPressed() {
        startActivityOnTop(QuesSectionListActivity.class,false);
    }
}