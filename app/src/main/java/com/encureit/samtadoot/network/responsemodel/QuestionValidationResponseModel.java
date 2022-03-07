package com.encureit.samtadoot.network.responsemodel;

import com.encureit.samtadoot.models.QuestionValidation;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/3/2022.
 */
public class QuestionValidationResponseModel {
    boolean status;
    String errorCode;
    List<QuestionValidation> ques_validation_list;

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

    public List<QuestionValidation> getQues_validation_list() {
        return ques_validation_list;
    }

    public void setQues_validation_list(List<QuestionValidation> ques_validation_list) {
        this.ques_validation_list = ques_validation_list;
    }
}
