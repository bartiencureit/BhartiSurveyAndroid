package com.encureit.bhartisurveyandroid.models.contracts;

import com.encureit.bhartisurveyandroid.Helpers.GlobalHelper;
import com.encureit.bhartisurveyandroid.models.SurveyType;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/2/2022.
 * Contract for dashboard
 */
public interface SettingsContract {

    interface ViewModel {
        void syncFinished();
        void syncFormsFinished();
        void logoutFinished();
        void showResponseFailed(String error);
    }
    interface Presenter {
        void setUpData(GlobalHelper helper);
        void syncAll();
        void syncAllForms();
        void logout();
    }
}
