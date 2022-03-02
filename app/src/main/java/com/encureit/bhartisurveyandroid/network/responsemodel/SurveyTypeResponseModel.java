package com.encureit.bhartisurveyandroid.network.responsemodel;

import com.encureit.bhartisurveyandroid.models.SurveyType;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/2/2022.
 */
public class SurveyTypeResponseModel {
    List<SurveyType> survey_type;

    public List<SurveyType> getSurvey_type() {
        return survey_type;
    }

    public void setSurvey_type(List<SurveyType> survey_type) {
        this.survey_type = survey_type;
    }
}
