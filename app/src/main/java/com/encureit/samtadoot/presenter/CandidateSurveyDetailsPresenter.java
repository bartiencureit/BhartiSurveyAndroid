package com.encureit.samtadoot.presenter;

import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.features.subforms.CandidateSurveyActivity;
import com.encureit.samtadoot.models.CandidateDetails;
import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;
import com.encureit.samtadoot.models.contracts.CandidateSurveyDetailsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swapna Thakur on 3/11/2022.
 */
public class CandidateSurveyDetailsPresenter implements CandidateSurveyDetailsContract.Presenter {
    private final CandidateSurveyActivity mActivity;
    private final CandidateSurveyDetailsContract.ViewModel mViewModel;

    public CandidateSurveyDetailsPresenter(CandidateSurveyActivity mActivity, CandidateSurveyDetailsContract.ViewModel mViewModel) {
        this.mActivity = mActivity;
        this.mViewModel = mViewModel;
    }

    @Override
    public void getCandidateData(String FormId) {
        List<CandidateSurveyStatusDetails> candidateSurveyStatusDetailsfinal = new ArrayList<>();
        List<CandidateSurveyStatusDetails> candidateSurveyStatusDetails = DatabaseUtil.on().getCandidateSurveyStatusDetailsDao().getAllCandidatesInSection(FormId);
        //List<CandidateSurveyStatusDetails> candidateSurveyStatusDetails = DatabaseUtil.on().getCandidateSurveyStatusDetailsDao().getAllFlowableCodes();
        for (int i = 0; i < candidateSurveyStatusDetails.size(); i++) {
            List<CandidateDetails> candidateDetails = DatabaseUtil.on().getCandidateDetailsDao().getAllDetailsByForm(candidateSurveyStatusDetails.get(i).getFormID());
            if (candidateDetails.size() > 0) {
                candidateSurveyStatusDetailsfinal.add(candidateSurveyStatusDetails.get(i));
            }
        }
        mViewModel.setUpCandidateDetails(candidateSurveyStatusDetailsfinal);
    }

    @Override
    public void createNewForm() {
        mViewModel.startNewFormActivity();
    }
}
