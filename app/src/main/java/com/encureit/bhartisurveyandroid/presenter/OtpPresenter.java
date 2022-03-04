package com.encureit.bhartisurveyandroid.presenter;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;

import com.encureit.bhartisurveyandroid.R;
import com.encureit.bhartisurveyandroid.base.BaseActivity;
import com.encureit.bhartisurveyandroid.lib.ScreenHelper;
import com.encureit.bhartisurveyandroid.login.OtpCheckActivity;
import com.encureit.bhartisurveyandroid.network.responsemodel.OtpCheckResponseModel;
import com.encureit.bhartisurveyandroid.models.contracts.OtpContract;
import com.encureit.bhartisurveyandroid.network.responsemodel.QuestionOptionResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.QuestionTypeResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.QuestionValidationResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.SurveyQuestionResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.SurveySectionResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.SurveyTypeResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.UserAssignedDetailsResponseModel;
import com.encureit.bhartisurveyandroid.network.retrofit.RetrofitClient;
import com.encureit.bhartisurveyandroid.network.retrofit.RetrofitClientLogin;

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
    public View rootView;

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
                mActivity.startProgressDialog(rootView);
                //mActivity.disableForm();

                RetrofitClientLogin.getApiService().getOtpCheckResponse(loginId,mActivity.mBinding.pinview.getValue()).enqueue(new Callback<OtpCheckResponseModel>() {
                    @Override
                    public void onResponse(Call<OtpCheckResponseModel> call, Response<OtpCheckResponseModel> response) {
                        if (response.code() == 200 || response.code() == 201) {
                            if (response.body().isStatus()) {
                                mViewModel.getOtp(response.body());
                            } else {
                                mViewModel.showOtpFailed(response.body().getMessage());
                            }
                        } else {
                            mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                        }
                    }

                    @Override
                    public void onFailure(Call<OtpCheckResponseModel> call, Throwable t) {
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

    @Override
    public void getSurveySectionFields() {
        RetrofitClient.getApiService().getSurveySection().enqueue(new Callback<SurveySectionResponseModel>() {
            @Override
            public void onResponse(Call<SurveySectionResponseModel> call, Response<SurveySectionResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if(response.body().isStatus()) {
                        mViewModel.getSurveySectionFieldsResponse(response.body());
                    } else {
                        mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(Call<SurveySectionResponseModel> call, Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getSurveyQuestionField() {
        RetrofitClient.getApiService().getSurveyQuestion().enqueue(new Callback<SurveyQuestionResponseModel>() {
            @Override
            public void onResponse(Call<SurveyQuestionResponseModel> call, Response<SurveyQuestionResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getSurveyQuestionFieldResponse(response.body());
                    } else {
                        mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(Call<SurveyQuestionResponseModel> call, Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getQuestionOptionField() {
        RetrofitClient.getApiService().getQuestionOptions().enqueue(new Callback<QuestionOptionResponseModel>() {
            @Override
            public void onResponse(Call<QuestionOptionResponseModel> call, Response<QuestionOptionResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getQuestionOptionFieldResponse(response.body());
                    } else {
                        mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(Call<QuestionOptionResponseModel> call, Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getQuestionTypesField() {
        RetrofitClient.getApiService().getQuestionTypes().enqueue(new Callback<QuestionTypeResponseModel>() {
            @Override
            public void onResponse(Call<QuestionTypeResponseModel> call, Response<QuestionTypeResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getQuestionTypesFieldResponse(response.body());
                    } else {
                        mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(Call<QuestionTypeResponseModel> call, Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getQuestionValidationFields() {
        RetrofitClient.getApiService().getQuestionValidations().enqueue(new Callback<QuestionValidationResponseModel>() {
            @Override
            public void onResponse(Call<QuestionValidationResponseModel> call, Response<QuestionValidationResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getQuestionValidationFieldsResponse(response.body());
                    } else {
                        mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(Call<QuestionValidationResponseModel> call, Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getUserAssignedDetails(String user_id) {
        RetrofitClient.getApiService().getUserAssignedDetails(user_id).enqueue(new Callback<UserAssignedDetailsResponseModel>() {
            @Override
            public void onResponse(Call<UserAssignedDetailsResponseModel> call, Response<UserAssignedDetailsResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getUserAssignedDetails(response.body());
                    } else {
                        mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(Call<UserAssignedDetailsResponseModel> call, Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getSurveyMaster() {
        RetrofitClient.getApiService().getSurveyTypes().enqueue(new Callback<SurveyTypeResponseModel>() {
            @Override
            public void onResponse(Call<SurveyTypeResponseModel> call, Response<SurveyTypeResponseModel> response) {
                //mActivity.enableForm();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getSurveyMasterResponse(response.body());
                    } else {
                        mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(Call<SurveyTypeResponseModel> call, Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }
}
