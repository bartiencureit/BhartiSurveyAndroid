package com.encureit.bhartisurveyandroid.models.contracts;

import com.encureit.bhartisurveyandroid.Helpers.GlobalHelper;
import com.encureit.bhartisurveyandroid.models.viewmodelobj.UserLoginObject;

/**
 * Created by Swapna Thakur on 3/1/2022.
 * Contract for login
 */
public interface LoginContract {

    interface ViewModel {
        void login(UserLoginObject userLoginObject);
        void showLoginFailed(String error);
    }
    interface Presenter {
        void doLogin();
    }
}
