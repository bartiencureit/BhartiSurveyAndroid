package com.encureit.bhartisurveyandroid.models.contracts;

import com.encureit.bhartisurveyandroid.network.responsemodel.OtpCheckResponseModel;

/**
 * Created by Swapna Thakur on 3/1/2022.
 * Contract for otp
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
