package com.encureit.samtadoot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.databinding.SingleCandidateSurveyDetailsItemBinding;
import com.encureit.samtadoot.models.CandidateDetails;
import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CandidateSurveyDetailsListAdapter extends RecyclerView.Adapter<CandidateSurveyDetailsListAdapter.CandidateSurveyDetailsHolder> {
    private Context context;
    private List<CandidateSurveyStatusDetails> candidateSurveyDetailsList;
    private OnItemClickListener mListener;

    public CandidateSurveyDetailsListAdapter(Context context, List<CandidateSurveyStatusDetails> candidateSurveyDetailsList, OnItemClickListener mListener) {
        this.context = context;
        this.candidateSurveyDetailsList = candidateSurveyDetailsList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public CandidateSurveyDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CandidateSurveyDetailsHolder(SingleCandidateSurveyDetailsItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull CandidateSurveyDetailsHolder holder, int position) {
        CandidateSurveyStatusDetails listItem = candidateSurveyDetailsList.get(position);
        List<CandidateDetails> candidateDetails = DatabaseUtil.on().getCandidateDetailsByForm(listItem.getFormID());
        if(candidateDetails.size() > 0) {
            CandidateDetails candidateDetail = candidateDetails.get(0);
            holder.binding.setCandidateSurveyDetails(listItem);
            holder.binding.setCandidateDetails(candidateDetail);
        }
        holder.binding.tvQueSectionName.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onItemClicked(listItem,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return candidateSurveyDetailsList.size();
    }

    public class CandidateSurveyDetailsHolder extends RecyclerView.ViewHolder {
        private SingleCandidateSurveyDetailsItemBinding binding;

        public CandidateSurveyDetailsHolder(SingleCandidateSurveyDetailsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void  onItemClicked(CandidateSurveyStatusDetails listModel,int position);
    }
}
