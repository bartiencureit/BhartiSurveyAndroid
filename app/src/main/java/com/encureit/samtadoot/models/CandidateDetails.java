package com.encureit.samtadoot.models;

import com.encureit.samtadoot.database.TableNames;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/10/2022.
 */
@Entity(tableName = TableNames.TABLE_CANDIDATE_DETAILS)
public class CandidateDetails {
    @PrimaryKey(autoGenerate = true)
    public int id;

    String survey_master_id;
    String survey_section_id;
    String survey_que_id;
    String survey_que_option_id;
    String survey_que_values;
    String FormID;
    String Current_Form_Status;
    String age_value;
    String Survey_StartDate;
    String Survey_EndDate;
    String created_by;
    String Latitude;
    String Longitude;
    int index_if_linked_question;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurvey_master_id() {
        return survey_master_id;
    }

    public void setSurvey_master_id(String survey_master_id) {
        this.survey_master_id = survey_master_id;
    }

    public String getSurvey_section_id() {
        return survey_section_id;
    }

    public void setSurvey_section_id(String survey_section_id) {
        this.survey_section_id = survey_section_id;
    }

    public String getSurvey_que_id() {
        return survey_que_id;
    }

    public void setSurvey_que_id(String survey_que_id) {
        this.survey_que_id = survey_que_id;
    }

    public String getSurvey_que_option_id() {
        return survey_que_option_id;
    }

    public void setSurvey_que_option_id(String survey_que_option_id) {
        this.survey_que_option_id = survey_que_option_id;
    }

    public String getSurvey_que_values() {
        return survey_que_values;
    }

    public void setSurvey_que_values(String survey_que_values) {
        this.survey_que_values = survey_que_values;
    }

    public String getFormID() {
        return FormID;
    }

    public void setFormID(String formID) {
        FormID = formID;
    }

    public String getCurrent_Form_Status() {
        return Current_Form_Status;
    }

    public void setCurrent_Form_Status(String current_Form_Status) {
        Current_Form_Status = current_Form_Status;
    }

    public String getAge_value() {
        return age_value;
    }

    public void setAge_value(String age_value) {
        this.age_value = age_value;
    }

    public String getSurvey_StartDate() {
        return Survey_StartDate;
    }

    public void setSurvey_StartDate(String survey_StartDate) {
        Survey_StartDate = survey_StartDate;
    }

    public String getSurvey_EndDate() {
        return Survey_EndDate;
    }

    public void setSurvey_EndDate(String survey_EndDate) {
        Survey_EndDate = survey_EndDate;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public int getIndex_if_linked_question() {
        return index_if_linked_question;
    }

    public void setIndex_if_linked_question(int index_if_linked_question) {
        this.index_if_linked_question = index_if_linked_question;
    }

    @Override
    public String toString() {
        return "CandidateDetails{" +
                "id=" + id +
                ", survey_master_id='" + survey_master_id + '\'' +
                ", survey_section_id='" + survey_section_id + '\'' +
                ", survey_que_id='" + survey_que_id + '\'' +
                ", survey_que_option_id='" + survey_que_option_id + '\'' +
                ", survey_que_values='" + survey_que_values + '\'' +
                ", FormID='" + FormID + '\'' +
                ", Current_Form_Status='" + Current_Form_Status + '\'' +
                ", age_value='" + age_value + '\'' +
                ", Survey_StartDate='" + Survey_StartDate + '\'' +
                ", Survey_EndDate='" + Survey_EndDate + '\'' +
                ", created_by='" + created_by + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", index_if_linked_question=" + index_if_linked_question +
                '}';
    }
}


