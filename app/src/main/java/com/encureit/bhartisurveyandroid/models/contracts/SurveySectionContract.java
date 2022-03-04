package com.encureit.bhartisurveyandroid.models.contracts;

import com.encureit.bhartisurveyandroid.models.SurveySection;
import com.encureit.bhartisurveyandroid.models.SurveyType;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/2/2022.
 * Contract for dashboard
 */
public interface SurveySectionContract {

    interface ViewModel {
        void setupFields(List<SurveySection> list);
        void showFormSaveFailed(String error);
    }
    interface Presenter {
        void startSurveySection();
        void saveForm();
    }
}
