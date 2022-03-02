package com.encureit.bhartisurveyandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.encureit.bhartisurveyandroid.databinding.SingleSurveyTypeItemBinding;
import com.encureit.bhartisurveyandroid.models.SurveyType;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SurveyTypeListAdapter extends RecyclerView.Adapter<SurveyTypeListAdapter.SurveyTypeHolder> {
    private Context context;
    private List<SurveyType> stateList;
    private OnItemClickListener mListener;

    public SurveyTypeListAdapter(Context context, List<SurveyType> stateList, OnItemClickListener mListener) {
        this.context = context;
        this.stateList = stateList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public SurveyTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SurveyTypeHolder(SingleSurveyTypeItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull SurveyTypeHolder holder, int position) {
        SurveyType listItem = stateList.get(position);
        holder.binding.setSurveyType(listItem);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClicked(listItem,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stateList.size();
    }

    public class SurveyTypeHolder extends RecyclerView.ViewHolder {
        private SingleSurveyTypeItemBinding binding;

        public SurveyTypeHolder(SingleSurveyTypeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void  onItemClicked(SurveyType listModel,int position);
    }
}
