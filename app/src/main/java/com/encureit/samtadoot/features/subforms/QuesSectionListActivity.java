package com.encureit.samtadoot.features.subforms;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.encureit.samtadoot.adapters.SurveySectionListAdapter;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.databinding.ActivityQuesSectionListBinding;
import com.encureit.samtadoot.lib.AppKeys;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.contracts.SurveySectionContract;
import com.encureit.samtadoot.presenter.SurveySectionPresenter;

import java.util.List;

public class QuesSectionListActivity extends BaseActivity implements SurveySectionContract.ViewModel {
    private ActivityQuesSectionListBinding mBinding;
    private SurveySectionPresenter mPresenter;
    private SurveySectionListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityQuesSectionListBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPresenter = new SurveySectionPresenter(QuesSectionListActivity.this,this);
        mPresenter.rootView = mBinding.getRoot();
        mPresenter.startSurveySection();
    }

    @Override
    public void setupFields(List<SurveySection> list) {
        mBinding.rvQueSections.setLayoutManager(new LinearLayoutManager(QuesSectionListActivity.this));
        mAdapter = new SurveySectionListAdapter(this, list, new SurveySectionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(SurveySection listModel, int position) {
                Intent intent = new Intent(QuesSectionListActivity.this,SubFormActivity.class);
                intent.putExtra(AppKeys.SURVEY_SECTION,listModel);
                startActivityOnTop(true,intent);
            }
        });
        mBinding.rvQueSections.setAdapter(mAdapter);

    }

    @Override
    public void showFormSaveFailed(String error) {

    }
}