package com.encureit.bhartisurveyandroid.models;

/**
 * Created by Swapna Thakur on 3/1/2022.
 */
public class OtpCheckResponseModel {
   boolean status;
   String errorCode;
   String loginDate;
   String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
