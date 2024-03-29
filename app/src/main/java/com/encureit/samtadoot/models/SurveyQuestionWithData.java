package com.encureit.samtadoot.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Swapna Thakur on 3/8/2022.
 */
public class SurveyQuestionWithData implements Parcelable {
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
    String min_length;
    String max_length;
    String CreatedBy;
    String CreatedDate;
    String UpdatedBy;
    String UpdatedDate;
    String is_section;
    String IsActive;
    QuestionType questionType;
    List<SurveyQuestionWithData> childQuestions;
    List<SurveyQuestionWithData> linkedQuestions;
    List<HashMap<Integer,List<SurveyQuestionWithData>>> linkedQuestionInEdit;
    List<QuestionOption> questionOptions;
    String Value = "";
    int linked_question_id = 0;

    public SurveyQuestionWithData() {}


    protected SurveyQuestionWithData(Parcel in) {
        SurveyQuestion_ID = in.readString();
        SurveySection_ID = in.readString();
        QuestionTypeID = in.readString();
        Autopopulate = in.readString();
        LabelHeader = in.readString();
        Required = in.readString();
        QuestionSequence = in.readString();
        ValidationType = in.readString();
        IsValidation = in.readString();
        IsLinkedQuestionId = in.readString();
        ParentQuestionId = in.readString();
        OptionDependent = in.readString();
        Questions = in.readString();
        min_length = in.readString();
        max_length = in.readString();
        CreatedBy = in.readString();
        CreatedDate = in.readString();
        UpdatedBy = in.readString();
        UpdatedDate = in.readString();
        is_section = in.readString();
        IsActive = in.readString();
        childQuestions = in.createTypedArrayList(SurveyQuestionWithData.CREATOR);
        linkedQuestions = in.createTypedArrayList(SurveyQuestionWithData.CREATOR);
        Value = in.readString();
        linked_question_id = in.readInt();
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

    public String getMin_length() {
        return min_length;
    }

    public void setMin_length(String min_length) {
        this.min_length = min_length;
    }

    public String getMax_length() {
        return max_length;
    }

    public void setMax_length(String max_length) {
        this.max_length = max_length;
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

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<SurveyQuestionWithData> getChildQuestions() {
        return childQuestions;
    }

    public void setChildQuestions(List<SurveyQuestionWithData> childQuestions) {
        this.childQuestions = childQuestions;
    }

    public List<SurveyQuestionWithData> getLinkedQuestions() {
        return linkedQuestions;
    }

    public void setLinkedQuestions(List<SurveyQuestionWithData> linkedQuestions) {
        this.linkedQuestions = linkedQuestions;
    }

    public List<HashMap<Integer, List<SurveyQuestionWithData>>> getLinkedQuestionInEdit() {
        return linkedQuestionInEdit;
    }

    public void setLinkedQuestionInEdit(List<HashMap<Integer, List<SurveyQuestionWithData>>> linkedQuestionInEdit) {
        this.linkedQuestionInEdit = linkedQuestionInEdit;
    }

    public List<QuestionOption> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(List<QuestionOption> questionOptions) {
        this.questionOptions = questionOptions;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public int getLinked_question_id() {
        return linked_question_id;
    }

    public void setLinked_question_id(int linked_question_id) {
        this.linked_question_id = linked_question_id;
    }

    public static Creator<SurveyQuestionWithData> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SurveyQuestion_ID);
        dest.writeString(SurveySection_ID);
        dest.writeString(QuestionTypeID);
        dest.writeString(Autopopulate);
        dest.writeString(LabelHeader);
        dest.writeString(Required);
        dest.writeString(QuestionSequence);
        dest.writeString(ValidationType);
        dest.writeString(IsValidation);
        dest.writeString(IsLinkedQuestionId);
        dest.writeString(ParentQuestionId);
        dest.writeString(OptionDependent);
        dest.writeString(Questions);
        dest.writeString(min_length);
        dest.writeString(max_length);
        dest.writeString(CreatedBy);
        dest.writeString(CreatedDate);
        dest.writeString(UpdatedBy);
        dest.writeString(UpdatedDate);
        dest.writeString(is_section);
        dest.writeString(IsActive);
        dest.writeTypedList(childQuestions);
        dest.writeTypedList(linkedQuestions);
        dest.writeString(Value);
        dest.writeInt(linked_question_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SurveyQuestionWithData> CREATOR = new Creator<SurveyQuestionWithData>() {
        @Override
        public SurveyQuestionWithData createFromParcel(Parcel in) {
            return new SurveyQuestionWithData(in);
        }

        @Override
        public SurveyQuestionWithData[] newArray(int size) {
            return new SurveyQuestionWithData[size];
        }
    };

    public int getInputValidation() {
        switch (ValidationType) {
            case "text":
                return InputType.TYPE_CLASS_TEXT;
            case "numeric":
                return InputType.TYPE_CLASS_NUMBER;
            case "floats":
                return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }

        return InputType.TYPE_CLASS_TEXT;
    }

    public Spanned getRequiredLabel() {
        String astrict = "<font color='#EE0000'>*</font>";
        if (getRequired().equalsIgnoreCase("true")) {
            return Html.fromHtml(getQuestions()+" " + astrict);
        } else {
            return Html.fromHtml(getQuestions());
        }
    }
}
