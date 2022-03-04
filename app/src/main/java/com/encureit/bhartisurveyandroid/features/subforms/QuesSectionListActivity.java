package com.encureit.bhartisurveyandroid.features.subforms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.encureit.bhartisurveyandroid.R;
import com.encureit.bhartisurveyandroid.adapters.SurveySectionListAdapter;
import com.encureit.bhartisurveyandroid.base.BaseActivity;
import com.encureit.bhartisurveyandroid.databinding.ActivityQuesSectionListBinding;
import com.encureit.bhartisurveyandroid.models.SurveySection;
import com.encureit.bhartisurveyandroid.models.contracts.SurveySectionContract;
import com.encureit.bhartisurveyandroid.presenter.SurveySectionPresenter;

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

            }
        });
        mBinding.rvQueSections.setAdapter(mAdapter);

    }

    @Override
    public void showFormSaveFailed(String error) {

    }
}