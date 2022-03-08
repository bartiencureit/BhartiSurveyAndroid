package com.encureit.samtadoot.features.subforms;

import android.os.Bundle;
import android.util.Log;

import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.custom.HeaderTextView;
import com.encureit.samtadoot.databinding.ActivitySubFormBinding;
import com.encureit.samtadoot.databinding.SingleInputBoxLayoutBinding;
import com.encureit.samtadoot.lib.AppKeys;
import com.encureit.samtadoot.models.SurveyQuestionWithData;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.contracts.SubFormContract;
import com.encureit.samtadoot.presenter.SubFormPresenter;

import java.util.List;

public class SubFormActivity extends BaseActivity implements SubFormContract.ViewModel {
    private ActivitySubFormBinding mBinding;
    private SubFormPresenter mPresenter;

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
        //Add Survey Section as a header
        HeaderTextView headerTextView = new HeaderTextView(SubFormActivity.this);
        headerTextView.setText(surveySection.getSectionDescription());
        mBinding.llFormList.addView(headerTextView);

        for (int i = 0; i < list.size(); i++) {
            SurveyQuestionWithData subForm = list.get(i);
            populateQuestion(subForm);
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
                populateInputText(subForm);
                break;
            case AppKeys.RADIO_BUTTON:
                populateRadioButton(subForm);
                break;
            case AppKeys.CHECKBOX:
                break;
            case AppKeys.DROPDOWNLIST:
                break;
            case AppKeys.LABEL_TEXT:
                break;
            case AppKeys.HEADER_TEXT:
                break;
            case AppKeys.DROPDOWNMULTISELECT:
                break;
        }
    }

    private void populateRadioButton(SurveyQuestionWithData subForm) {

    }

    /**
     * @date 7-3-2022
     * Insert EditText with Label in layout
     * @param subForm
     */
    private void populateInputText(SurveyQuestionWithData subForm) {
        SingleInputBoxLayoutBinding binding = SingleInputBoxLayoutBinding.inflate(getLayoutInflater());
        binding.setSubForm(subForm);
        mBinding.llFormList.addView(binding.getRoot());
    }

    @Override
    public void showResponseFailed(String error) {

    }
}