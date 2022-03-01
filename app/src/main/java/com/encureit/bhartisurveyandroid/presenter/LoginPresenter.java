package com.encureit.bhartisurveyandroid.presenter;

import android.content.DialogInterface;
import android.text.TextUtils;

import com.encureit.bhartisurveyandroid.base.BaseActivity;
import com.encureit.bhartisurveyandroid.lib.ScreenHelper;
import com.encureit.bhartisurveyandroid.login.LoginActivity;
import com.encureit.bhartisurveyandroid.models.LoginResponseModel;
import com.encureit.bhartisurveyandroid.models.contracts.LoginContract;
import com.encureit.bhartisurveyandroid.models.viewmodelobj.UserLoginObject;
import com.encureit.bhartisurveyandroid.network.retrofit.RetrofitClient;
import com.encureit.bhartisurveyandroid.network.retrofit.RetrofitClientLogin;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.databinding.ObservableField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Swapna Thakur on 3/1/2022.
 */
public class LoginPresenter implements LoginContract.Presenter {
    private LoginActivity mActivity;
    private LoginContract.ViewModel mViewModel;
    public ObservableField<String> userId;

    public LoginPresenter(LoginActivity mActivity, LoginContract.ViewModel mViewModel) {
        this.mActivity = mActivity;
        this.mViewModel = mViewModel;
        initFields();
    }

    private void initFields() {
        userId = new ObservableField<>();
    }

    private boolean validate() {
        if (TextUtils.isEmpty(userId.get())) {
            mViewModel.showLoginFailed("Empty User Id");
            return false;
        }

        return true;
    }

    /**
     * @date 1-3-2022
     * Checks internet connection if not connected to internet show not connected dialog.
     * if internet connected then it fetches login response from server using retrofit
     */
    @Override
    public void doLogin() {
        if (validate()) {
            ScreenHelper.hideKeyboard(mActivity);
            if(mActivity.isInternetConnected()) {
                mActivity.startProgressDialog();

                RetrofitClientLogin.getApiService().getLoginResponse(userId.get()).enqueue(new Callback<LoginResponseModel>() {
                    @Override
                    public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                        if (response.code() == 200 || response.code() == 201) {
                            if (response.body().isStatus()) {
                                UserLoginObject obj = new UserLoginObject();
                                obj.setResponse(response.body());
                                obj.setUserId(userId.get());
                                mActivity.dismissProgressDialog();
                                mViewModel.login(obj);
                            } else {
                                mActivity.dismissProgressDialog();
                                mViewModel.showLoginFailed(response.body().getMessage());
                            }
                        } else {
                            mActivity.dismissProgressDialog();
                            mViewModel.showLoginFailed("Invalid Response from server");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                        mActivity.dismissProgressDialog();
                        mViewModel.showLoginFailed(t.getMessage());
                    }
                });


            } else {
                mActivity.showNotInternetConnected(new BaseActivity.OnInternetConnectedListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }
}
