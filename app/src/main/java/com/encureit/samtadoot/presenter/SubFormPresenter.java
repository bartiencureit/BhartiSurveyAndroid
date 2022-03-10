package com.encureit.samtadoot.presenter;

import android.content.Intent;

import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.features.subforms.SubFormActivity;
import com.encureit.samtadoot.lib.AppKeys;
import com.encureit.samtadoot.models.SurveyQuestionWithData;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.contracts.SubFormContract;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/7/2022.
 */
public class SubFormPresenter implements SubFormContract.Presenter {
    private SubFormActivity mActivity;
    private SubFormContract.ViewModel mViewModel;

    public SubFormPresenter(SubFormActivity mActivity, SubFormContract.ViewModel mViewModel) {
        this.mActivity = mActivity;
        this.mViewModel = mViewModel;
    }

    @Override
    public void startSubForm(SurveySection surveySection) {

        List<SurveyQuestionWithData> subFormList = DatabaseUtil.on().getAllQuestions(surveySection.getSurveySection_ID());
        mViewModel.setupSubForms(subFormList,surveySection);
    }

    @Override
    public void saveForm() {

    }
}
