package com.encureit.bhartisurveyandroid.models;

import com.encureit.bhartisurveyandroid.database.TableNames;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/3/2022.
 */
@Entity(tableName = TableNames.TABLE_SURVEY_SECTIONS)
public class SurveySection {
    @PrimaryKey(autoGenerate = true)
    public int id;

   String SurveySection_ID;
   String SurveyMaster_ID;
   String SectionNumber;
   String SectionName;
   String SectionDescription;
   String CreatedBy;
   String CreatedDate;
   String ModifiedBy;
   String ModifiedDate;
   String IsActive;

    public String getSurveySection_ID() {
        return SurveySection_ID;
    }

    public void setSurveySection_ID(String surveySection_ID) {
        SurveySection_ID = surveySection_ID;
    }

    public String getSurveyMaster_ID() {
        return SurveyMaster_ID;
    }

    public void setSurveyMaster_ID(String surveyMaster_ID) {
        SurveyMaster_ID = surveyMaster_ID;
    }

    public String getSectionNumber() {
        return SectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        SectionNumber = sectionNumber;
    }

    public String getSectionName() {
        return SectionName;
    }

    public void setSectionName(String sectionName) {
        SectionName = sectionName;
    }

    public String getSectionDescription() {
        return SectionDescription;
    }

    public void setSectionDescription(String sectionDescription) {
        SectionDescription = sectionDescription;
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

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        ModifiedDate = modifiedDate;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }
}
