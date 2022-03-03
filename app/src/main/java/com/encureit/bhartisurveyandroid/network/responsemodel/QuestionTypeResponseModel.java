package com.encureit.bhartisurveyandroid.network.responsemodel;

import com.encureit.bhartisurveyandroid.models.QuestionType;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/3/2022.
 */
public class QuestionTypeResponseModel {
    boolean status;
    String errorCode;
    List<QuestionType> question_type;

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

    public List<QuestionType> getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(List<QuestionType> question_type) {
        this.question_type = question_type;
    }
}
