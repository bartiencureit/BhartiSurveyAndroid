package com.encureit.samtadoot.features.subforms;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.encureit.samtadoot.adapters.SurveySectionListAdapter;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.databinding.ActivityQuesSectionListBinding;
import com.encureit.samtadoot.features.dashboard.DashboardActivity;
import com.encureit.samtadoot.lib.AppKeys;
import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.SurveyType;
import com.encureit.samtadoot.models.contracts.SurveySectionContract;
import com.encureit.samtadoot.presenter.SurveySectionPresenter;

import java.util.List;

public class QuesSectionListActivity extends BaseActivity implements SurveySectionContract.ViewModel {
    private ActivityQuesSectionListBinding mBinding;
    private SurveySectionPresenter mPresenter;
    private SurveySectionListAdapter mAdapter;
    private SurveyType surveyType;
    private boolean inEditMode = false;
    private CandidateSurveyStatusDetails candidateSurveyStatusDetails;
    private static final String TAG = "QuesSectionListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityQuesSectionListBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPresenter = new SurveySectionPresenter(QuesSectionListActivity.this,this);
        mPresenter.rootView = mBinding.getRoot();
        try {
            Intent intent = getIntent();
            if (intent.hasExtra(AppKeys.SURVEY_TYPE)) {
                surveyType = intent.getParcelableExtra(AppKeys.SURVEY_TYPE);
                mBinding.toolbar.tvToolbarTitle.setText(surveyType.getForm_description());
            }
            if(intent.hasExtra(AppKeys.CANDIDATE_SURVEY_DETAILS)) {
                inEditMode = true;
                candidateSurveyStatusDetails = intent.getParcelableExtra(AppKeys.CANDIDATE_SURVEY_DETAILS);
            }
            //mPresenter.startSurveySection();
            if (surveyType != null) {
                mPresenter.startSurveySection(surveyType.getForm_unique_id());
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate: "+e.getMessage());
        }
    }

    /**
     * Set adpater to recyclerview
     * @param list
     */
    @Override
    public void setupFields(List<SurveySection> list) {
        mBinding.rvQueSections.setLayoutManager(new LinearLayoutManager(QuesSectionListActivity.this));
        String FormId = null;
        if (candidateSurveyStatusDetails != null) {
            FormId = candidateSurveyStatusDetails.getFormID();
        }
        try {
            mAdapter = new SurveySectionListAdapter(this, FormId, inEditMode, list, (listModel, position) -> {
                Intent intent;
                if (inEditMode) {
                    intent = new Intent(QuesSectionListActivity.this, EditFormActivity.class);
                    intent.putExtra(AppKeys.SURVEY_TYPE,surveyType);
                    intent.putExtra(AppKeys.SURVEY_SECTION,listModel);
                    intent.putExtra(AppKeys.CANDIDATE_SURVEY_DETAILS,candidateSurveyStatusDetails);
                } else {
                    intent = new Intent(QuesSectionListActivity.this, SubFormActivity.class);
                    intent.putExtra(AppKeys.SURVEY_TYPE,surveyType);
                    intent.putExtra(AppKeys.SURVEY_SECTION,listModel);
                }
                startActivityOnTop(true,intent);
            });
            mBinding.rvQueSections.setAdapter(mAdapter);
        } catch (Exception e) {
            Log.e(TAG, "setupFields: "+e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        startActivityOnTop(DashboardActivity.class,false);
    }
}