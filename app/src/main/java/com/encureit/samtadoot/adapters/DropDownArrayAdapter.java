package com.encureit.samtadoot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.encureit.samtadoot.databinding.SingleDropDownItemBinding;
import com.encureit.samtadoot.models.QuestionOption;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Swapna Thakur on 3/8/2022.
 */
public class DropDownArrayAdapter extends ArrayAdapter<QuestionOption> {
    private Context context;
    private List<QuestionOption> questionOptions;
    private int resource;
    private SingleDropDownItemBinding mBinding;

    public DropDownArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public DropDownArrayAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public DropDownArrayAdapter(@NonNull Context context, int resource, @NonNull QuestionOption[] objects) {
        super(context, resource, objects);
    }

    public DropDownArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull QuestionOption[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public DropDownArrayAdapter(@NonNull Context context, int resource, @NonNull List<QuestionOption> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource  = resource;
        this.questionOptions = objects;
    }

    public DropDownArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<QuestionOption> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public int getCount() {
        return questionOptions.size();
    }

    @Nullable
    @Override
    public QuestionOption getItem(int position) {
        return questionOptions.get(position);
    }

    @Override
    public int getPosition(@Nullable QuestionOption item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            mBinding = SingleDropDownItemBinding.inflate(LayoutInflater.from(context));
            convertView = mBinding.getRoot();
        }
        mBinding.setQuestionOption(getItem(position));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            mBinding = SingleDropDownItemBinding.inflate(LayoutInflater.from(context));
            convertView = mBinding.getRoot();
        }
        mBinding.setQuestionOption(getItem(position));
        return convertView;
    }
}
