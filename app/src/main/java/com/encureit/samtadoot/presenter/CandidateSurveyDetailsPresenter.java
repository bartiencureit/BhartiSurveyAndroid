package com.encureit.samtadoot.presenter;

import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.features.subforms.CandidateSurveyActivity;
import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;
import com.encureit.samtadoot.models.contracts.CandidateSurveyDetailsContract;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/11/2022.
 */
public class CandidateSurveyDetailsPresenter implements CandidateSurveyDetailsContract.Presenter {
    private CandidateSurveyActivity mActivity;
    private CandidateSurveyDetailsContract.ViewModel mViewModel;

    public CandidateSurveyDetailsPresenter(CandidateSurveyActivity mActivity, CandidateSurveyDetailsContract.ViewModel mViewModel) {
        this.mActivity = mActivity;
        this.mViewModel = mViewModel;
    }

    @Override
    public void getCandidateData() {
        List<CandidateSurveyStatusDetails> candidateSurveyStatusDetails = DatabaseUtil.on().getCandidateSurveyStatusDetailsDao().getAllFlowableCodes();
        mViewModel.setUpCandidateDetails(candidateSurveyStatusDetails);
    }

    @Override
    public void createNewForm() {
        mViewModel.startNewFormActivity();
    }
}
