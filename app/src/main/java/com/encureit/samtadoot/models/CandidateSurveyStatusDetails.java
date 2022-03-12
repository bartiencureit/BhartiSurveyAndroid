package com.encureit.samtadoot.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.encureit.samtadoot.database.TableNames;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/10/2022.
 */
@Entity(tableName = TableNames.TABLE_CANDIDATE_SURVEY_DETAILS)
public class CandidateSurveyStatusDetails implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    String survey_section_id;
    String FormID;
    String survey_status;
    String start_date;
    String end_date;

    public CandidateSurveyStatusDetails() {}

    @Ignore
    public CandidateSurveyStatusDetails(Parcel in) {
        id = in.readInt();
        survey_section_id = in.readString();
        FormID = in.readString();
        survey_status = in.readString();
        start_date = in.readString();
        end_date = in.readString();
    }

    @Ignore
    public static final Creator<CandidateSurveyStatusDetails> CREATOR = new Creator<CandidateSurveyStatusDetails>() {
        @Override
        public CandidateSurveyStatusDetails createFromParcel(Parcel in) {
            return new CandidateSurveyStatusDetails(in);
        }

        @Override
        public CandidateSurveyStatusDetails[] newArray(int size) {
            return new CandidateSurveyStatusDetails[size];
        }
    };

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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(survey_section_id);
        parcel.writeString(FormID);
        parcel.writeString(survey_status);
        parcel.writeString(start_date);
        parcel.writeString(end_date);
    }
}
