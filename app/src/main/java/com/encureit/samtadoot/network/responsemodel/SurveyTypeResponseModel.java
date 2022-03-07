package com.encureit.samtadoot.network.responsemodel;

import com.encureit.samtadoot.models.SurveyType;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/2/2022.
 */
public class SurveyTypeResponseModel {
    boolean status;
    String errorCode;
    List<SurveyType> survey_type;

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

    public List<SurveyType> getSurvey_type() {
        return survey_type;
    }

    public void setSurvey_type(List<SurveyType> survey_type) {
        this.survey_type = survey_type;
    }
}
