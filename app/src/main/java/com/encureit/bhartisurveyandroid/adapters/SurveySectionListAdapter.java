package com.encureit.bhartisurveyandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.encureit.bhartisurveyandroid.databinding.SingleSurveySectionItemBinding;
import com.encureit.bhartisurveyandroid.models.SurveySection;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SurveySectionListAdapter extends RecyclerView.Adapter<SurveySectionListAdapter.SurveySectionHolder> {
    private Context context;
    private List<SurveySection> stateList;
    private OnItemClickListener mListener;

    public SurveySectionListAdapter(Context context, List<SurveySection> stateList, OnItemClickListener mListener) {
        this.context = context;
        this.stateList = stateList;
        this.mListener = mListener;
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
        holder.binding.tvQueSectionName.setOnClickListener(new View.OnClickListener() {
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
