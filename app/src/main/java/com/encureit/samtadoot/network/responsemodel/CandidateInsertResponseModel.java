package com.encureit.samtadoot.network.responsemodel;

import com.encureit.samtadoot.models.OtherValues;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/11/2022.
 */
public class CandidateInsertResponseModel {
   boolean status;
   String errorCode;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
