package com.encureit.bhartisurveyandroid.presenter;

import android.content.DialogInterface;
import android.text.TextUtils;

import com.encureit.bhartisurveyandroid.base.BaseActivity;
import com.encureit.bhartisurveyandroid.lib.ScreenHelper;
import com.encureit.bhartisurveyandroid.login.LoginActivity;
import com.encureit.bhartisurveyandroid.login.OtpCheckActivity;
import com.encureit.bhartisurveyandroid.models.OtpCheckResponseModel;
import com.encureit.bhartisurveyandroid.models.contracts.LoginContract;
import com.encureit.bhartisurveyandroid.models.contracts.OtpContract;
import com.encureit.bhartisurveyandroid.models.viewmodelobj.UserLoginObject;
import com.encureit.bhartisurveyandroid.network.retrofit.RetrofitClientLogin;

import androidx.databinding.ObservableField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Swapna Thakur on 3/1/2022.
 */
public class OtpPresenter implements OtpContract.Presenter {
    private OtpContract.ViewModel mViewModel;
    private OtpCheckActivity mActivity;
    public String loginId;

    public OtpPresenter(OtpContract.ViewModel mViewModel, OtpCheckActivity mActivity) {
        this.mViewModel = mViewModel;
        this.mActivity = mActivity;
    }

    private boolean validate() {
        if (TextUtils.isEmpty(mActivity.mBinding.pinview.getValue())) {
            mViewModel.showOtpFailed("Empty Otp");
            return false;
        }

        return true;
    }


    @Override
    public void sendOtp() {
        if (validate()) {
            ScreenHelper.hideKeyboard(mActivity);
            if(mActivity.isInternetConnected()) {
                mActivity.startProgressDialog();

                RetrofitClientLogin.getApiService().getOtpCheckResponse(loginId,mActivity.mBinding.pinview.getValue()).enqueue(new Callback<OtpCheckResponseModel>() {
                    @Override
                    public void onResponse(Call<OtpCheckResponseModel> call, Response<OtpCheckResponseModel> response) {
                        if (response.code() == 200 || response.code() == 201) {
                            if (response.body().isStatus()) {
                                mActivity.dismissProgressDialog();
                                mViewModel.getOtp(response.body());
                            } else {
                                mActivity.dismissProgressDialog();
                                mViewModel.showOtpFailed(response.body().getMessage());
                            }
                        } else {
                            mActivity.dismissProgressDialog();
                            mViewModel.showOtpFailed("Invalid Response from server");
                        }
                    }

                    @Override
                    public void onFailure(Call<OtpCheckResponseModel> call, Throwable t) {
                        mActivity.dismissProgressDialog();
                        mViewModel.showOtpFailed(t.getMessage());
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
