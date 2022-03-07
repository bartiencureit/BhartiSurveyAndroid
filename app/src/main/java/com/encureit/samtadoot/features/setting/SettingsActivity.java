package com.encureit.samtadoot.features.setting;

import android.os.Bundle;
import android.view.View;

import com.encureit.samtadoot.Helpers.GlobalHelper;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.databinding.ActivitySettingsBinding;
import com.encureit.samtadoot.models.contracts.SettingsContract;
import com.encureit.samtadoot.presenter.SettingsPresenter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class SettingsActivity extends BaseActivity implements SettingsContract.ViewModel {
    private ActivitySettingsBinding mBinding;
    private SettingsPresenter mPresenter;
    private GlobalHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        helper = new GlobalHelper(this);
        mPresenter = new SettingsPresenter(SettingsActivity.this, this);
        mBinding.setPresenter(mPresenter);
        mPresenter.setUpData(helper);
        mBinding.toolbar.backArrow.setVisibility(View.VISIBLE);
        mBinding.toolbar.backArrow.setOnClickListener(view -> finish());
    }

    @Override
    public void syncFinished() {
        Snackbar.make(mBinding.getRoot(),"Sync Finished", BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void syncFormsFinished() {
        Snackbar.make(mBinding.getRoot(),"Sync Forms Finished", BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void logoutFinished() {
        Snackbar.make(mBinding.getRoot(),"Successfully logged out", BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void showResponseFailed(String error) {
        Snackbar.make(mBinding.getRoot(),""+error, BaseTransientBottomBar.LENGTH_LONG).show();
    }
}