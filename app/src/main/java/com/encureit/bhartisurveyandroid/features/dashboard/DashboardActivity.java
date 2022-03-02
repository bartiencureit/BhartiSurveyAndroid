package com.encureit.bhartisurveyandroid.features.dashboard;

import android.os.Bundle;

import com.encureit.bhartisurveyandroid.Helpers.GlobalHelper;
import com.encureit.bhartisurveyandroid.base.BaseActivity;
import com.encureit.bhartisurveyandroid.databinding.ActivityDashboardBinding;
import com.encureit.bhartisurveyandroid.models.contracts.DashboardContract;
import com.encureit.bhartisurveyandroid.presenter.DashboardPresenter;

public class DashboardActivity extends BaseActivity implements DashboardContract.ViewModel {
    private ActivityDashboardBinding mBinding;
    private DashboardPresenter mPresenter;
    private GlobalHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPresenter = new DashboardPresenter(DashboardActivity.this,this);
        helper = new GlobalHelper(DashboardActivity.this);
        mPresenter.startDashboard(helper.getSharedPreferencesHelper().getLoginUserRole());
    }

    @Override
    public void setupDashboardFields(String loginRole) {

    }

    @Override
    public void showResponseFailed(String error) {

    }
}