package com.encureit.samtadoot.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.encureit.samtadoot.database.TableNames;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/10/2022.
 */
@Entity(tableName = TableNames.TABLE_CANDIDATE_DETAILS)
public class CandidateDetails implements Parcelable {
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
    boolean hasImage;

    public CandidateDetails() {}

    protected CandidateDetails(Parcel in) {
        id = in.readInt();
        survey_master_id = in.readString();
        survey_section_id = in.readString();
        survey_que_id = in.readString();
        survey_que_option_id = in.readString();
        survey_que_values = in.readString();
        FormID = in.readString();
        Current_Form_Status = in.readString();
        age_value = in.readString();
        Survey_StartDate = in.readString();
        Survey_EndDate = in.readString();
        created_by = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        index_if_linked_question = in.readInt();
        hasImage = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(survey_master_id);
        dest.writeString(survey_section_id);
        dest.writeString(survey_que_id);
        dest.writeString(survey_que_option_id);
        dest.writeString(survey_que_values);
        dest.writeString(FormID);
        dest.writeString(Current_Form_Status);
        dest.writeString(age_value);
        dest.writeString(Survey_StartDate);
        dest.writeString(Survey_EndDate);
        dest.writeString(created_by);
        dest.writeString(Latitude);
        dest.writeString(Longitude);
        dest.writeInt(index_if_linked_question);
        dest.writeByte((byte) (hasImage ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CandidateDetails> CREATOR = new Creator<CandidateDetails>() {
        @Override
        public CandidateDetails createFromParcel(Parcel in) {
            return new CandidateDetails(in);
        }

        @Override
        public CandidateDetails[] newArray(int size) {
            return new CandidateDetails[size];
        }
    };

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

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
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
                ", hasImage=" + hasImage +
                '}';
    }
}


