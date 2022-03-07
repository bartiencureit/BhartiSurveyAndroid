package com.encureit.samtadoot.login;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.encureit.samtadoot.Helpers.GlobalHelper;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.databinding.ActivityLoginBinding;
import com.encureit.samtadoot.lib.AppKeys;
import com.encureit.samtadoot.lib.ScreenHelper;
import com.encureit.samtadoot.models.contracts.LoginContract;
import com.encureit.samtadoot.models.viewmodelobj.UserLoginObject;
import com.encureit.samtadoot.presenter.LoginPresenter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class LoginActivity extends BaseActivity implements LoginContract.ViewModel {
    private ActivityLoginBinding mBinding;
    private LoginPresenter mPresenter;
    private static final int REQUEST = 112;
    private GlobalHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        helper = new GlobalHelper(this);
        mPresenter = new LoginPresenter(this,this);
        mPresenter.rootView = mBinding.getRoot();
        mPresenter.userId.set("1111");
        askPermission();
        mBinding.setPresenter(mPresenter);
    }

    @Override
    public void login(UserLoginObject userLoginObject) {
        try {
            //save user id and role in shared preference
            helper.getSharedPreferencesHelper().setLoginUserId(userLoginObject.getResponse().getUser_id());
            helper.getSharedPreferencesHelper().setLoginUserRole(userLoginObject.getResponse().getUser_role());

            //start otp check activity
            Intent intent = new Intent(this, OtpCheckActivity.class);
            intent.putExtra(AppKeys.loginUserId,userLoginObject.getResponse().getUser_id());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityOnTop(false,intent);
        } catch (Exception e) {
            ScreenHelper.showErrorSnackBar(mBinding.getRoot(),e.getMessage());
        }
    }

    @Override
    public void showLoginFailed(String error) {
        ScreenHelper.showErrorSnackBar(mBinding.getRoot(),error);
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d(TAG, "@@@ IN IF Build.VERSION.SDK_INT >= 23");
            String[] PERMISSIONS = {Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
            };

            if (!hasPermissions(this, PERMISSIONS)) {
                Log.d(TAG, "@@@ IN IF hasPermissions");
                ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST);
            } else {
                Log.d(TAG, "@@@ IN ELSE hasPermissions");
            }
        } else {
            Log.d(TAG, "@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "@@@ PERMISSIONS grant");
                } else {
                    Log.d(TAG, "@@@ PERMISSIONS Denied");
                }
            }
            break;
        }
    }
}