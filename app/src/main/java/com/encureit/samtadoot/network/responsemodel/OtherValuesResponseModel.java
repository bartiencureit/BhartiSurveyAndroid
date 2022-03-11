package com.encureit.samtadoot.network.responsemodel;

import com.encureit.samtadoot.models.OtherValues;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/1/2022.
 */
public class OtherValuesResponseModel {
   boolean status;
   String errorCode;
   List<OtherValues> other_values;

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

    public List<OtherValues> getOther_values() {
        return other_values;
    }

    public void setOther_values(List<OtherValues> other_values) {
        this.other_values = other_values;
    }
}
