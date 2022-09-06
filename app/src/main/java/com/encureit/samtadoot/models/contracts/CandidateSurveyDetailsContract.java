package com.encureit.samtadoot.models.contracts;

import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/11/2022.
 */
public interface CandidateSurveyDetailsContract {
    interface ViewModel {
        void setUpCandidateDetails(List<CandidateSurveyStatusDetails> list);
        void startNewFormActivity();
    }
    interface Presenter {
        void getCandidateData(String FormId);
        void createNewForm();
    }
}
