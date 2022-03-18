package com.encureit.samtadoot.base;

import android.os.Bundle;
import android.os.Handler;

import com.encureit.samtadoot.Helpers.GlobalHelper;
import com.encureit.samtadoot.databinding.ActivitySplashBinding;
import com.encureit.samtadoot.lib.ScreenHelper;
import com.encureit.samtadoot.models.contracts.SplashContract;
import com.encureit.samtadoot.presenter.SplashPresenter;

public class SplashActivity extends BaseActivity implements SplashContract.ViewModel {
    private ActivitySplashBinding mBinding;
    private SplashPresenter mPresenter;
    private Runnable mRunnable;
    private Handler mHandler;
    private final static long SPLASH_INTERVAL_IN_MILLIS = 500;
    private GlobalHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPresenter = new SplashPresenter(this);
        mPresenter.rootView = mBinding.getRoot();
        mBinding.setPresenter(mPresenter);
        initObject();
        init();
    }

    private void initObject() {
        helper = new GlobalHelper(this);
    }

    private void init() {
        mHandler = new Handler();
        mRunnable = () -> {
            mPresenter.checkLastLoginDate(helper);
        };
    }

    @Override
    public void startAnotherActivity(Class<?> cls, boolean finishCurrent) {
        startActivityOnTop(cls,finishCurrent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, SPLASH_INTERVAL_IN_MILLIS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onBackPressed() {
        ScreenHelper.exitAppWithoutDialog(SplashActivity.this);
    }
}