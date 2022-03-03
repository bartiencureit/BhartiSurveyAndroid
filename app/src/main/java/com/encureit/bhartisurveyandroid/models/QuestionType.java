package com.encureit.bhartisurveyandroid.models;

import com.encureit.bhartisurveyandroid.database.TableNames;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/3/2022.
 */
@Entity(tableName = TableNames.TABLE_QUESTIONS_TYPES)
public class QuestionType {
    @PrimaryKey(autoGenerate = true)
    public int id;

    String QuestionTypeID;
    String QuestionTypes;
    String CreatedBy;
    String CreatedDate;
    String UpdatedBy;
    String UpdatedDate;
    String IsActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionTypeID() {
        return QuestionTypeID;
    }

    public void setQuestionTypeID(String questionTypeID) {
        QuestionTypeID = questionTypeID;
    }

    public String getQuestionTypes() {
        return QuestionTypes;
    }

    public void setQuestionTypes(String questionTypes) {
        QuestionTypes = questionTypes;
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
