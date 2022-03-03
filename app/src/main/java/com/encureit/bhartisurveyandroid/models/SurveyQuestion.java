package com.encureit.bhartisurveyandroid.models;

import com.encureit.bhartisurveyandroid.database.TableNames;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/3/2022.
 */
@Entity(tableName = TableNames.TABLE_SURVEY_QUESTIONS)
public class SurveyQuestion {
    @PrimaryKey(autoGenerate = true)
    public int id;

    String SurveyQuestion_ID;
    String SurveySection_ID;
    String QuestionTypeID;
    String Autopopulate;
    String LabelHeader;
    String Required;
    String QuestionSequence;
    String ValidationType;
    String IsValidation;
    String IsLinkedQuestionId;
    String ParentQuestionId;
    String OptionDependent;
    String Questions;
    String CreatedBy;
    String CreatedDate;
    String UpdatedBy;
    String UpdatedDate;
    String is_section;
    String IsActive;

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

    public String getQuestionTypeID() {
        return QuestionTypeID;
    }

    public void setQuestionTypeID(String questionTypeID) {
        QuestionTypeID = questionTypeID;
    }

    public String getAutopopulate() {
        return Autopopulate;
    }

    public void setAutopopulate(String autopopulate) {
        Autopopulate = autopopulate;
    }

    public String getLabelHeader() {
        return LabelHeader;
    }

    public void setLabelHeader(String labelHeader) {
        LabelHeader = labelHeader;
    }

    public String getRequired() {
        return Required;
    }

    public void setRequired(String required) {
        Required = required;
    }

    public String getQuestionSequence() {
        return QuestionSequence;
    }

    public void setQuestionSequence(String questionSequence) {
        QuestionSequence = questionSequence;
    }

    public String getValidationType() {
        return ValidationType;
    }

    public void setValidationType(String validationType) {
        ValidationType = validationType;
    }

    public String getIsValidation() {
        return IsValidation;
    }

    public void setIsValidation(String isValidation) {
        IsValidation = isValidation;
    }

    public String getIsLinkedQuestionId() {
        return IsLinkedQuestionId;
    }

    public void setIsLinkedQuestionId(String isLinkedQuestionId) {
        IsLinkedQuestionId = isLinkedQuestionId;
    }

    public String getParentQuestionId() {
        return ParentQuestionId;
    }

    public void setParentQuestionId(String parentQuestionId) {
        ParentQuestionId = parentQuestionId;
    }

    public String getOptionDependent() {
        return OptionDependent;
    }

    public void setOptionDependent(String optionDependent) {
        OptionDependent = optionDependent;
    }

    public String getQuestions() {
        return Questions;
    }

    public void setQuestions(String questions) {
        Questions = questions;
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

    public String getIs_section() {
        return is_section;
    }

    public void setIs_section(String is_section) {
        this.is_section = is_section;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }
}
