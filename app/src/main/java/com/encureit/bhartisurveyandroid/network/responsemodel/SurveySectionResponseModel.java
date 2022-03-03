package com.encureit.bhartisurveyandroid.network.responsemodel;

import com.encureit.bhartisurveyandroid.models.SurveySection;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/3/2022.
 */
public class SurveySectionResponseModel {
    boolean status;
    String errorCode;
    List<SurveySection> survey_section;

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

    public List<SurveySection> getSurvey_section() {
        return survey_section;
    }

    public void setSurvey_section(List<SurveySection> survey_section) {
        this.survey_section = survey_section;
    }
}
