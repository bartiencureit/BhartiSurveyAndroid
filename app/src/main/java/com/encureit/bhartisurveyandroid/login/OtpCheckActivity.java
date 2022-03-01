package com.encureit.bhartisurveyandroid.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.encureit.bhartisurveyandroid.Helpers.GlobalHelper;
import com.encureit.bhartisurveyandroid.R;
import com.encureit.bhartisurveyandroid.base.BaseActivity;
import com.encureit.bhartisurveyandroid.databinding.ActivityLoginBinding;
import com.encureit.bhartisurveyandroid.databinding.ActivityOtpCheckBinding;
import com.encureit.bhartisurveyandroid.lib.AppKeys;
import com.encureit.bhartisurveyandroid.models.OtpCheckResponseModel;
import com.encureit.bhartisurveyandroid.models.contracts.OtpContract;
import com.encureit.bhartisurveyandroid.presenter.OtpPresenter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class OtpCheckActivity extends BaseActivity implements OtpContract.ViewModel {
    public ActivityOtpCheckBinding mBinding;
    private String loginUserId;
    private OtpPresenter mPresenter;
    private GlobalHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityOtpCheckBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Intent intent = getIntent();
        if(intent.hasExtra(AppKeys.loginUserId)) {
            loginUserId = intent.getStringExtra(AppKeys.loginUserId);
        }
        mBinding.pinview.setValue("1111");
        helper = new GlobalHelper(this);
        mPresenter = new OtpPresenter(this,OtpCheckActivity.this);
        mPresenter.loginId = loginUserId;
        mBinding.setPresenter(mPresenter);
    }

    @Override
    public void getOtp(OtpCheckResponseModel otpCheckResponseModel) {
        //save date to shared preference
        helper.getSharedPreferencesHelper().setLoginDateTimeData(otpCheckResponseModel.getLoginDate());
        Snackbar.make(mBinding.getRoot(),""+otpCheckResponseModel.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void showOtpFailed(String error) {
        Snackbar.make(mBinding.getRoot(),""+error, BaseTransientBottomBar.LENGTH_LONG).show();
    }
}