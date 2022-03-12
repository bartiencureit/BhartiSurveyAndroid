package com.encureit.samtadoot.features.subforms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.encureit.samtadoot.R;
import com.encureit.samtadoot.adapters.CandidateSurveyDetailsListAdapter;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.databinding.ActivityCandidateSurveyBinding;
import com.encureit.samtadoot.features.dashboard.DashboardActivity;
import com.encureit.samtadoot.lib.AppKeys;
import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;
import com.encureit.samtadoot.models.SurveyType;
import com.encureit.samtadoot.models.contracts.CandidateSurveyDetailsContract;
import com.encureit.samtadoot.presenter.CandidateSurveyDetailsPresenter;

import java.util.List;

public class CandidateSurveyActivity extends BaseActivity implements CandidateSurveyDetailsContract.ViewModel {
    private ActivityCandidateSurveyBinding mBinding;
    private CandidateSurveyDetailsPresenter mPresenter;
    private CandidateSurveyDetailsListAdapter mAdapter;
    private SurveyType listModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCandidateSurveyBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPresenter = new CandidateSurveyDetailsPresenter(CandidateSurveyActivity.this,this);
        mBinding.setCandidatePresenter(mPresenter);
        mPresenter.getCandidateData();
        Intent intent = getIntent();
        if (intent.hasExtra(AppKeys.SURVEY_TYPE)) {
            listModel = intent.getParcelableExtra(AppKeys.SURVEY_TYPE);
        }

    }

    @Override
    public void setUpCandidateDetails(List<CandidateSurveyStatusDetails> list) {
        mAdapter = new CandidateSurveyDetailsListAdapter(CandidateSurveyActivity.this, list, new CandidateSurveyDetailsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(CandidateSurveyStatusDetails candidateSurveyStatusDetails, int position) {
                Intent intent = new Intent(CandidateSurveyActivity.this, QuesSectionListActivity.class);
                intent.putExtra(AppKeys.SURVEY_TYPE, listModel);
                intent.putExtra(AppKeys.CANDIDATE_SURVEY_DETAILS, candidateSurveyStatusDetails);
                startActivityOnTop(false, intent);
            }
        });
        mBinding.rvCandidateDetails.setLayoutManager(new LinearLayoutManager(CandidateSurveyActivity.this));
        mBinding.rvCandidateDetails.setAdapter(mAdapter);
    }

    @Override
    public void startNewFormActivity() {
        Intent intent = new Intent(CandidateSurveyActivity.this, DashboardActivity.class);
        intent.putExtra(AppKeys.SURVEY_TYPE, listModel);
        startActivityOnTop(false, intent);
    }
}