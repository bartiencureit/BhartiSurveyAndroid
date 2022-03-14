package com.encureit.samtadoot.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.encureit.samtadoot.Helpers.GlobalHelper;
import com.encureit.samtadoot.R;
import com.encureit.samtadoot.lib.NetworkHelper;
import com.encureit.samtadoot.network.Contants;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private OnInternetConnectedListener mListener;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    public static BaseApplication application;
    public boolean doubleBackToExitPressedOnce = false;
    private GlobalHelper globalHelper;
    protected AppCompatActivity mActivity;
    private ProgressDialog progressDialog;
    private Snackbar snackbar;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        application = (BaseApplication) BaseApplication.getAppsContext();
        Contants.applicationContext = getApplicationContext();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //
        super.onCreate(savedInstanceState);
        mActivity = this;
    }



    public void startProgressDialog(View view) {
        this.view = view;
        view.setEnabled(false);
        snackbar = Snackbar.make(view,getResources().getString(R.string.loading), BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    public void startCircularProgressDialog() {
        String progressMessage = getResources().getString(R.string.loading);
        progressDialog = new ProgressDialog(this, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        progressDialog.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        progressDialog.getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(progressMessage);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        TextView tv = (TextView) progressDialog.findViewById(R.id.progress_tv);
        tv.setText(progressMessage);
    }

    public void dismissCircularProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void dismissProgressDialog() {
        if(snackbar != null) {
            snackbar.dismiss();
        }
    }

    public boolean isInternetConnected() {
        return NetworkHelper.hasNetworkAccess(BaseActivity.this);
    }

    public void showNotInternetConnected(OnInternetConnectedListener mListener) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(this).inflate(R.layout.activity_base,null);
        dialog.setView(view);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (mListener != null) {
                    mListener.onClick(dialog, which);
                }
            }
        });
        dialog.show();
    }

    public interface OnInternetConnectedListener {
        void onClick(DialogInterface dialog, int which);
    }

    public static BaseApplication getBaseApplication() {
        return application;
    }

    public void startActivityOnTop(Intent intent, boolean finishCurrent) {
        if (finishCurrent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
        if (finishCurrent) {
            finish();
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void startActivityOnTop(Class<?> cls, boolean finishCurrent) {
        Intent intent = new Intent(mActivity, cls);
        if (finishCurrent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
        if (finishCurrent) {
            finish();
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void startActivityOnTop(boolean finishCurrent, Intent intent) {
        if (finishCurrent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
        if (finishCurrent) {
            finish();
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public GlobalHelper getGlobalHelper() {
        if (this.globalHelper == null) {
            this.globalHelper = new GlobalHelper(this);
        }
        return globalHelper;
    }

    public void setNoActionBar() {
        this.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
    }

    public void setFullScreen() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}