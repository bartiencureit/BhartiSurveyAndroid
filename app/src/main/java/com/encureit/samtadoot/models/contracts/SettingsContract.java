package com.encureit.samtadoot.models.contracts;

import com.encureit.samtadoot.Helpers.GlobalHelper;

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
