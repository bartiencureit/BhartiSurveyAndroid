package com.encureit.bhartisurveyandroid.models;

import com.encureit.bhartisurveyandroid.database.TableNames;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/3/2022.
 */
@Entity(tableName = TableNames.TABLE_QUESTIONS_VALIDATION)
public class QuestionValidation {
    @PrimaryKey(autoGenerate = true)
    public int id;

    String QuestionValidation_ID;
    String Validation_ID;
    String SurveyQuestion_ID;
    String SurveySection_ID;
    String CreatedBy;
    String CreatedDate;
    String UpdatedBy;
    String UpdatedDate;
    String IsActive;

    public String getQuestionValidation_ID() {
        return QuestionValidation_ID;
    }

    public void setQuestionValidation_ID(String questionValidation_ID) {
        QuestionValidation_ID = questionValidation_ID;
    }

    public String getValidation_ID() {
        return Validation_ID;
    }

    public void setValidation_ID(String validation_ID) {
        Validation_ID = validation_ID;
    }

    public String getSurveyQuestion_ID() {
        return SurveyQuestion_ID;
    }

    public void setSurveyQuestion_ID(String surveyQuestion_ID) {
        SurveyQuestion_ID = surveyQuestion_ID;
    }

    public String getSurveySection_ID() {
        return SurveySection_ID;
    }

    public void setSurveySection_ID(String surveySection_ID) {
        SurveySection_ID = surveySection_ID;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }
}
