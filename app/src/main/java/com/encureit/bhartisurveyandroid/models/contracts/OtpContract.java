package com.encureit.bhartisurveyandroid.models.contracts;

import com.encureit.bhartisurveyandroid.models.OtpCheckResponseModel;
import com.encureit.bhartisurveyandroid.models.viewmodelobj.UserLoginObject;

/**
 * Created by Swapna Thakur on 3/1/2022.
 * Contract for login
 */
public interface OtpContract {

    interface ViewModel {
        void getOtp(OtpCheckResponseModel otpCheckResponseModel);
        void showOtpFailed(String error);
    }
    interface Presenter {
        void sendOtp();
    }
}
