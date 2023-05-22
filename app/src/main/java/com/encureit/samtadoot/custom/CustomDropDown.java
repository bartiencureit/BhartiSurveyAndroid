package com.encureit.samtadoot.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;

public class CustomDropDown extends AppCompatSpinner {
    private Object subForm;
    private int linked_id;

    public CustomDropDown(Context context) {
        super(context);
    }

    public CustomDropDown(Context context, int mode) {
        super(context, mode);
    }

    public CustomDropDown(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDropDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomDropDown(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
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
