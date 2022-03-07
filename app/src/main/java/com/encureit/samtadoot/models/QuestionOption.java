package com.encureit.samtadoot.models;

import com.encureit.samtadoot.database.TableNames;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/3/2022.
 */
@Entity(tableName = TableNames.TABLE_QUESTIONS_OPTION)
public class QuestionOption {
    @PrimaryKey(autoGenerate = true)
    public int id;

    String QNAOption_ID;
    String SurveyQuestion_ID;
    String QNA_Values;
    String DisplayTypeCount;
    String Suffix;
    String childQuestionId;
    String CreatedBy;
    String CreatedDate;
    String UpdatedBy;
    String UpdatedDate;
    String IsActive;

    public String getQNAOption_ID() {
        return QNAOption_ID;
    }

    public void setQNAOption_ID(String QNAOption_ID) {
        this.QNAOption_ID = QNAOption_ID;
    }

    public String getSurveyQuestion_ID() {
        return SurveyQuestion_ID;
    }

    public void setSurveyQuestion_ID(String surveyQuestion_ID) {
        SurveyQuestion_ID = surveyQuestion_ID;
    }

    public String getQNA_Values() {
        return QNA_Values;
    }

    public void setQNA_Values(String QNA_Values) {
        this.QNA_Values = QNA_Values;
    }

    public String getDisplayTypeCount() {
        return DisplayTypeCount;
    }

    public void setDisplayTypeCount(String displayTypeCount) {
        DisplayTypeCount = displayTypeCount;
    }

    public String getSuffix() {
        return Suffix;
    }

    public void setSuffix(String suffix) {
        Suffix = suffix;
    }

    public String getChildQuestionId() {
        return childQuestionId;
    }

    public void setChildQuestionId(String childQuestionId) {
        this.childQuestionId = childQuestionId;
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
