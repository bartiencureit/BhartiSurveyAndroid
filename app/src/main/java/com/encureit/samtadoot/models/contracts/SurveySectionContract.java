package com.encureit.samtadoot.models.contracts;

import com.encureit.samtadoot.models.SurveySection;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/2/2022.
 * Contract for dashboard
 */
public interface SurveySectionContract {

    interface ViewModel {
        void setupFields(List<SurveySection> list);
    }
    interface Presenter {
        void startSurveySection(String section_id);
    }
}
