package com.encureit.samtadoot.presenter;

import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.features.subforms.SubFormActivity;
import com.encureit.samtadoot.models.SurveyQuestionWithData;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.contracts.SubFormContract;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/7/2022.
 */
public class SubFormPresenter implements SubFormContract.Presenter {
    private final SubFormActivity mActivity;
    private final SubFormContract.ViewModel mViewModel;

    public SubFormPresenter(SubFormActivity mActivity, SubFormContract.ViewModel mViewModel) {
        this.mActivity = mActivity;
        this.mViewModel = mViewModel;
    }

    @Override
    public void startSubForm(SurveySection surveySection) {

        List<SurveyQuestionWithData> subFormList = DatabaseUtil.on().getAllQuestions(surveySection.getSurveySection_ID());
        mViewModel.setupSubForms(subFormList,surveySection);
    }

}
