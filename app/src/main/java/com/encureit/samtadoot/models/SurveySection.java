package com.encureit.samtadoot.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.encureit.samtadoot.database.TableNames;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/3/2022.
 */
@Entity(tableName = TableNames.TABLE_SURVEY_SECTIONS)
public class SurveySection implements Parcelable {
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

   public SurveySection() {}

   @Ignore
    public SurveySection(Parcel in) {
        id = in.readInt();
        SurveySection_ID = in.readString();
        SurveyMaster_ID = in.readString();
        SectionNumber = in.readString();
        SectionName = in.readString();
        SectionDescription = in.readString();
        CreatedBy = in.readString();
        CreatedDate = in.readString();
        ModifiedBy = in.readString();
        ModifiedDate = in.readString();
        IsActive = in.readString();
    }

    @Ignore
    public static final Creator<SurveySection> CREATOR = new Creator<SurveySection>() {
        @Override
        public SurveySection createFromParcel(Parcel in) {
            return new SurveySection(in);
        }

        @Override
        public SurveySection[] newArray(int size) {
            return new SurveySection[size];
        }
    };

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

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(SurveySection_ID);
        parcel.writeString(SurveyMaster_ID);
        parcel.writeString(SectionNumber);
        parcel.writeString(SectionName);
        parcel.writeString(SectionDescription);
        parcel.writeString(CreatedBy);
        parcel.writeString(CreatedDate);
        parcel.writeString(ModifiedBy);
        parcel.writeString(ModifiedDate);
        parcel.writeString(IsActive);
    }
}
