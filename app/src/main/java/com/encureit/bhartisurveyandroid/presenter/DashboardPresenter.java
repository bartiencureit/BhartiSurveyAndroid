package com.encureit.bhartisurveyandroid.presenter;

import android.content.DialogInterface;
import android.text.TextUtils;

import com.encureit.bhartisurveyandroid.base.BaseActivity;
import com.encureit.bhartisurveyandroid.features.dashboard.DashboardActivity;
import com.encureit.bhartisurveyandroid.lib.ScreenHelper;
import com.encureit.bhartisurveyandroid.login.LoginActivity;
import com.encureit.bhartisurveyandroid.models.LoginResponseModel;
import com.encureit.bhartisurveyandroid.models.contracts.DashboardContract;
import com.encureit.bhartisurveyandroid.models.contracts.LoginContract;
import com.encureit.bhartisurveyandroid.models.viewmodelobj.UserLoginObject;
import com.encureit.bhartisurveyandroid.network.retrofit.RetrofitClientLogin;

import androidx.databinding.ObservableField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Swapna Thakur on 3/2/2022.
 */
public class DashboardPresenter implements DashboardContract.Presenter {
    private DashboardActivity mActivity;
    private DashboardContract.ViewModel mViewModel;

    public DashboardPresenter(DashboardActivity mActivity, DashboardContract.ViewModel mViewModel) {
        this.mActivity = mActivity;
        this.mViewModel = mViewModel;
    }

    @Override
    public void startDashboard(String loginRole) {
        mViewModel.setupDashboardFields(loginRole);
    }

    @Override
    public void getSurveyMaster() {

    }
}
