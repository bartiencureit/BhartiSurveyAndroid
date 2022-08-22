package com.encureit.samtadoot.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.encureit.samtadoot.models.SurveyQuestionWithData;

public class CustomRadioButton extends AppCompatRadioButton {
    private Object subForm;
    private int linked_id;

    public CustomRadioButton(Context context) {
        super(context);
    }

    public CustomRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
}
