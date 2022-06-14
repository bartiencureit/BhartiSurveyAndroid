package com.encureit.samtadoot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.databinding.SingleSurveySectionItemBinding;
import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;
import com.encureit.samtadoot.models.SurveySection;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SurveySectionListAdapter extends RecyclerView.Adapter<SurveySectionListAdapter.SurveySectionHolder> {
    private Context context;
    private List<SurveySection> stateList;
    private OnItemClickListener mListener;
    private boolean isEditMode;

    public SurveySectionListAdapter(Context context,boolean isEditMode, List<SurveySection> stateList, OnItemClickListener mListener) {
        this.context = context;
        this.stateList = stateList;
        this.mListener = mListener;
        this.isEditMode = isEditMode;
    }

    @NonNull
    @Override
    public SurveySectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SurveySectionHolder(SingleSurveySectionItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull SurveySectionHolder holder, int position) {
        SurveySection listItem = stateList.get(position);
        holder.binding.setSurveySection(listItem);
        if (isEditMode) {
            CandidateSurveyStatusDetails candidateSurveyStatusDetails = DatabaseUtil.on().getCandidateSurveyStatusDetailsDao().getCandidateDetails(listItem.getSurveySection_ID());
            if (candidateSurveyStatusDetails != null) {
                holder.binding.setImgVisibility(View.VISIBLE);
            } else {
                holder.binding.setImgVisibility(View.GONE);
            }
        } else {
            holder.binding.setImgVisibility(View.GONE);
        }
        holder.binding.tvQueSectionName.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onItemClicked(listItem,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stateList.size();
    }

    public class SurveySectionHolder extends RecyclerView.ViewHolder {
        private SingleSurveySectionItemBinding binding;

        public SurveySectionHolder(SingleSurveySectionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void  onItemClicked(SurveySection listModel,int position);
    }
}
