package com.encureit.bhartisurveyandroid.models.contracts;

import com.encureit.bhartisurveyandroid.models.viewmodelobj.UserLoginObject;

/**
 * Created by Swapna Thakur on 3/2/2022.
 * Contract for dashboard
 */
public interface DashboardContract {

    interface ViewModel {
        void setupDashboardFields(String loginRole);
        void showResponseFailed(String error);
    }
    interface Presenter {
        void startDashboard(String loginRole);
        void getSurveyMaster();
    }
}
