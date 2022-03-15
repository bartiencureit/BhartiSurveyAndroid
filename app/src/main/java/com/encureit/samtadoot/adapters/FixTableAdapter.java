package com.encureit.samtadoot.adapters;

import android.content.Context;
import android.widget.TextView;

import com.app.feng.fixtablelayout.inter.IDataAdapter;
import com.encureit.samtadoot.R;
import com.encureit.samtadoot.models.QuestionOption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swapna Thakur on 3/15/2022.
 */
public class FixTableAdapter implements IDataAdapter {
    private Context mContext;
    private List<String> titles;
    private List<QuestionOption> questionOptions;

    public FixTableAdapter(Context mContext, List<String> titles, List<QuestionOption> questionOptions) {
        this.mContext = mContext;
        this.titles = new ArrayList<>();
        this.titles.add("");
        this.titles.addAll(titles);
        this.questionOptions = questionOptions;
    }

    @Override
    public String getTitleAt(int i) {
        return titles.get(i);
    }

    @Override
    public int getTitleCount() {
        return titles.size();
    }

    @Override
    public int getItemCount() {
        return questionOptions.size();
    }

    @Override
    public void convertData(int i, List<TextView> list) {
        QuestionOption option = questionOptions.get(i);
        list.get(0).setText(option.getQNA_Values());
        for (int j = 1; j < list.size(); j++) {
            list.get(j).setBackground(mContext.getResources().getDrawable(R.drawable.edittext_rounded_corners));
            list.get(j).setCursorVisible(true);
            list.get(j).setFocusableInTouchMode(true);
            list.get(j).requestFocus();
            list.get(j).setEnabled(true);
        }
    }

    @Override
    public void convertLeftData(int i, TextView textView) {
        textView.setText(questionOptions.get(i).getQNA_Values());
    }
}
