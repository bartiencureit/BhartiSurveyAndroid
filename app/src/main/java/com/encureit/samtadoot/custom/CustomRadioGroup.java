package com.encureit.samtadoot.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

public class CustomRadioGroup extends RadioGroup {
    private Object subForm;
    private int linked_id;

    public CustomRadioGroup(Context context) {
        super(context);
    }

    public CustomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
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
