package com.encureit.samtadoot.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Created by Swapna Thakur on 3/7/2022.
 */
@SuppressLint("AppCompatCustomView")
public class HeaderTextView extends TextView {

    public HeaderTextView(Context context) {
        super(context);
        init();
    }

    public HeaderTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HeaderTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        setTypeface(getTypeface(), Typeface.BOLD);
        setTextSize(15);
        setTextColor(Color.BLACK);
    }
}
