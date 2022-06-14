package com.encureit.samtadoot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.databinding.SingleSurveySectionItemBinding;
import com.encureit.samtadoot.models.SurveySection;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SurveySectionListAdapter extends RecyclerView.Adapter<SurveySectionListAdapter.SurveySectionHolder> {
    private Context context;
    private List<SurveySection> stateList;
    private OnItemClickListener mListener;
    private boolean isEditMode;
    private String formId;

    public SurveySectionListAdapter(Context context,String formId,boolean isEditMode, List<SurveySection> stateList, OnItemClickListener mListener) {
        this.context = context;
        this.stateList = stateList;
        this.mListener = mListener;
        this.isEditMode = isEditMode;
        this.formId = formId;
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
        if (isEditMode && formId != null) {
            if (DatabaseUtil.on().isSectionFilled(listItem,formId)) {
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
