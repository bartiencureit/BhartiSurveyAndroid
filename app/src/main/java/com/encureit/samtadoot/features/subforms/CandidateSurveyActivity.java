package com.encureit.samtadoot.features.subforms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.encureit.samtadoot.R;
import com.encureit.samtadoot.adapters.CandidateSurveyDetailsListAdapter;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.databinding.ActivityCandidateSurveyBinding;
import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;
import com.encureit.samtadoot.models.contracts.CandidateSurveyDetailsContract;
import com.encureit.samtadoot.presenter.CandidateSurveyDetailsPresenter;

import java.util.List;

public class CandidateSurveyActivity extends BaseActivity implements CandidateSurveyDetailsContract.ViewModel {
    private ActivityCandidateSurveyBinding mBinding;
    private CandidateSurveyDetailsPresenter mPresenter;
    private CandidateSurveyDetailsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCandidateSurveyBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPresenter = new CandidateSurveyDetailsPresenter(CandidateSurveyActivity.this,this);
        mBinding.setCandidatePresenter(mPresenter);
        mPresenter.getCandidateData();
    }

    @Override
    public void setUpCandidateDetails(List<CandidateSurveyStatusDetails> list) {
        mAdapter = new CandidateSurveyDetailsListAdapter(CandidateSurveyActivity.this, list, new CandidateSurveyDetailsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(CandidateSurveyStatusDetails listModel, int position) {

            }
        });
        mBinding.rvCandidateDetails.setLayoutManager(new LinearLayoutManager(CandidateSurveyActivity.this));
        mBinding.rvCandidateDetails.setAdapter(mAdapter);
    }

    @Override
    public void startNewFormActivity() {
        startActivityOnTop(QuesSectionListActivity.class,false);
    }
}