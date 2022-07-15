package com.encureit.samtadoot.models.contracts;

import com.encureit.samtadoot.models.SurveyQuestionWithData;
import com.encureit.samtadoot.models.SurveySection;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/2/2022.
 * Contract for dashboard
 */
public interface SubFormContract {

    interface ViewModel {
        void setupSubForms(List<SurveyQuestionWithData> list, SurveySection surveySection);
        void showResponseFailed(String error);
    }
    interface Presenter {
        void startSubForm(SurveySection surveySection);
    }
}
