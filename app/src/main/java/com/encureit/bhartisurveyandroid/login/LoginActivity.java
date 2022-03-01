package com.encureit.bhartisurveyandroid.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.encureit.bhartisurveyandroid.R;
import com.encureit.bhartisurveyandroid.base.BaseActivity;
import com.encureit.bhartisurveyandroid.databinding.ActivityLoginBinding;
import com.encureit.bhartisurveyandroid.models.contracts.LoginContract;
import com.encureit.bhartisurveyandroid.models.viewmodelobj.UserLoginObject;
import com.encureit.bhartisurveyandroid.presenter.LoginPresenter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends BaseActivity implements LoginContract.ViewModel {
    private ActivityLoginBinding mBinding;
    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPresenter = new LoginPresenter(this,this);
        mBinding.setPresenter(mPresenter);
    }

    @Override
    public void login(UserLoginObject userLoginObject) {
        Snackbar.make(mBinding.getRoot(),"Successful : "+userLoginObject.getResponse().toString(), BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void showLoginFailed(String error) {
        Snackbar.make(mBinding.getRoot(),"Error : "+error, BaseTransientBottomBar.LENGTH_LONG).show();
    }
}