package com.encureit.samtadoot.models.contracts;

import com.encureit.samtadoot.models.SurveyType;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/2/2022.
 * Contract for dashboard
 */
public interface DashboardContract {

    interface ViewModel {
        void setupDashboardFields(List<SurveyType> list, String loginRole);
        void showResponseFailed(String error);
    }
    interface Presenter {
        void startDashboard(String loginRole);
        void getUserDeviceDetails(String loginUserId);
    }
}
