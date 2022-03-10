package com.encureit.samtadoot.models;

import com.encureit.samtadoot.database.TableNames;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/10/2022.
 */
@Entity(tableName = TableNames.TABLE_CANDIDATE_SURVEY_DETAILS)
public class CandidateSurveyStatusDetails {
    @PrimaryKey(autoGenerate = true)
    public int id;

    String survey_section_id;
    String FormID;
    String survey_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurvey_section_id() {
        return survey_section_id;
    }

    public void setSurvey_section_id(String survey_section_id) {
        this.survey_section_id = survey_section_id;
    }

    public String getFormID() {
        return FormID;
    }

    public void setFormID(String formID) {
        FormID = formID;
    }

    public String getSurvey_status() {
        return survey_status;
    }

    public void setSurvey_status(String survey_status) {
        this.survey_status = survey_status;
    }
}
