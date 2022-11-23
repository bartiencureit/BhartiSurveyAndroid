package com.encureit.samtadoot.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.encureit.samtadoot.models.SurveyQuestionWithData;

public class CustomCheckBox extends AppCompatCheckBox {
    private Object subForm;
    private int linked_id;
    private int id;

    public CustomCheckBox(@NonNull Context context) {
        super(context);
    }

    public CustomCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCheckBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Object getSubForm() {
        return subForm;
    }

    public void setSubForm(Object subForm) {
        this.subForm = subForm;
    }

    public int getLinked_id() {
        return linked_id;
    }

    public void setLinked_id(int linked_id) {
        this.linked_id = linked_id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
