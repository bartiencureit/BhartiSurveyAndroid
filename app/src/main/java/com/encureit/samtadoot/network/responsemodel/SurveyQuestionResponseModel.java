package com.encureit.samtadoot.network.responsemodel;

import com.encureit.samtadoot.models.SurveyQuestion;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/3/2022.
 */
public class SurveyQuestionResponseModel {
    boolean status;
    String errorCode;
    List<SurveyQuestion> survey_question_list;

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

    public List<SurveyQuestion> getSurvey_question_list() {
        return survey_question_list;
    }

    public void setSurvey_question_list(List<SurveyQuestion> survey_question_list) {
        this.survey_question_list = survey_question_list;
    }
}
