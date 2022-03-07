package com.encureit.samtadoot.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.encureit.samtadoot.database.TableNames;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/2/2022.
 */
@Entity(tableName = TableNames.TABLE_SURVEY_MASTER)
public class SurveyType implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    String Form_unique_id;
    String Form_no;
    String form_type;
    String form_description;
    String isActive;

    public SurveyType () {}

    @Ignore
    public SurveyType(Parcel in) {
        id = in.readInt();
        Form_unique_id = in.readString();
        Form_no = in.readString();
        form_type = in.readString();
        form_description = in.readString();
        isActive = in.readString();
    }

    @Ignore
    public static final Creator<SurveyType> CREATOR = new Creator<SurveyType>() {
        @Override
        public SurveyType createFromParcel(Parcel in) {
            return new SurveyType(in);
        }

        @Override
        public SurveyType[] newArray(int size) {
            return new SurveyType[size];
        }
    };

    public String getForm_unique_id() {
        return Form_unique_id;
    }

    public void setForm_unique_id(String form_unique_id) {
        Form_unique_id = form_unique_id;
    }

    public String getForm_no() {
        return Form_no;
    }

    public void setForm_no(String form_no) {
        Form_no = form_no;
    }

    public String getForm_type() {
        return form_type;
    }

    public void setForm_type(String form_type) {
        this.form_type = form_type;
    }

    public String getForm_description() {
        return form_description;
    }

    public void setForm_description(String form_description) {
        this.form_description = form_description;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
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
        parcel.writeString(Form_unique_id);
        parcel.writeString(Form_no);
        parcel.writeString(form_type);
        parcel.writeString(form_description);
        parcel.writeString(isActive);
    }
}
