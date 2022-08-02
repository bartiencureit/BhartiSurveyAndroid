package com.encureit.samtadoot.presenter;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.encureit.samtadoot.R;
import com.encureit.samtadoot.base.BaseActivity;
import com.encureit.samtadoot.lib.ScreenHelper;
import com.encureit.samtadoot.login.OtpCheckActivity;
import com.encureit.samtadoot.network.responsemodel.OtherValuesResponseModel;
import com.encureit.samtadoot.network.responsemodel.OtpCheckResponseModel;
import com.encureit.samtadoot.models.contracts.OtpContract;
import com.encureit.samtadoot.network.responsemodel.QuestionOptionResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionValidationResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyQuestionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveySectionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.UserAssignedDetailsResponseModel;
import com.encureit.samtadoot.network.retrofit.RetrofitClient;
import com.encureit.samtadoot.network.retrofit.RetrofitClientLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Swapna Thakur on 3/1/2022.
 */
public class OtpPresenter implements OtpContract.Presenter {
    private final OtpContract.ViewModel mViewModel;
    private final OtpCheckActivity mActivity;
    public String loginId;
    public View rootView;
    private static final String TAG = "OtpPresenter";

    public OtpPresenter(OtpContract.ViewModel mViewModel, OtpCheckActivity mActivity) {
        this.mViewModel = mViewModel;
        this.mActivity = mActivity;
    }

    private boolean validate() {
        if (TextUtils.isEmpty(mActivity.mBinding.pinview.getValue())) {
            mViewModel.showOtpFailed(mActivity.getResources().getString(R.string.empty_otp));
            return false;
        }

        return true;
    }


    @Override
    public void sendOtp() {
        try {
            if (validate()) {
                try {
                    ScreenHelper.hideKeyboard(mActivity);
                } catch (Exception e) {
                    Log.e(TAG, "sendOtp: "+e.getMessage());
                }
                if(mActivity.isInternetConnected()) {
                    try {
                        mActivity.startProgressDialog(rootView);
                        //mActivity.disableForm();

                        RetrofitClientLogin.getApiService().getOtpCheckResponse(loginId,mActivity.mBinding.pinview.getValue()).enqueue(new Callback<OtpCheckResponseModel>() {
                            @Override
                            public void onResponse(@NonNull Call<OtpCheckResponseModel> call, @NonNull Response<OtpCheckResponseModel> response) {
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
                            public void onFailure(@NonNull Call<OtpCheckResponseModel> call, @NonNull Throwable t) {
                                mViewModel.showOtpFailed(t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "sendOtp: "+e.getMessage());
                    }


                } else {
                    mActivity.showNotInternetConnected(new BaseActivity.OnInternetConnectedListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        } catch (Exception e) {
            Toast.makeText(mActivity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getSurveySectionFields() {
        mActivity.service.getSurveySection().enqueue(new Callback<SurveySectionResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SurveySectionResponseModel> call, @NonNull Response<SurveySectionResponseModel> response) {
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
            public void onFailure(@NonNull Call<SurveySectionResponseModel> call, @NonNull Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getSurveyQuestionField() {
        mActivity.service.getSurveyQuestion().enqueue(new Callback<SurveyQuestionResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SurveyQuestionResponseModel> call, @NonNull Response<SurveyQuestionResponseModel> response) {
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
            public void onFailure(@NonNull Call<SurveyQuestionResponseModel> call, @NonNull Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getQuestionOptionField() {
        mActivity.service.getQuestionOptions().enqueue(new Callback<QuestionOptionResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<QuestionOptionResponseModel> call, @NonNull Response<QuestionOptionResponseModel> response) {
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
            public void onFailure(@NonNull Call<QuestionOptionResponseModel> call, @NonNull Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getQuestionTypesField() {
        mActivity.service.getQuestionTypes().enqueue(new Callback<QuestionTypeResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<QuestionTypeResponseModel> call, @NonNull Response<QuestionTypeResponseModel> response) {
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
            public void onFailure(@NonNull Call<QuestionTypeResponseModel> call, @NonNull Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getQuestionValidationFields() {
        mActivity.service.getQuestionValidations().enqueue(new Callback<QuestionValidationResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<QuestionValidationResponseModel> call, @NonNull Response<QuestionValidationResponseModel> response) {
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
            public void onFailure(@NonNull Call<QuestionValidationResponseModel> call, @NonNull Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getUserAssignedDetails(String user_id) {
        mActivity.service.getUserAssignedDetails(user_id).enqueue(new Callback<UserAssignedDetailsResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<UserAssignedDetailsResponseModel> call, @NonNull Response<UserAssignedDetailsResponseModel> response) {
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
            public void onFailure(@NonNull Call<UserAssignedDetailsResponseModel> call, @NonNull Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getSurveyMaster() {
        mActivity.service.getSurveyTypes().enqueue(new Callback<SurveyTypeResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SurveyTypeResponseModel> call, @NonNull Response<SurveyTypeResponseModel> response) {
                //mActivity.enableForm();
                if (response.code() == 200 && response.body() != null) {
                    //if (response.body().isStatus()) {
                        mViewModel.getSurveyMasterResponse(response.body());
//                    } else {
//                        mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
//                    }
                } else {
                    mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SurveyTypeResponseModel> call, @NonNull Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }

    @Override
    public void getOtherValues() {
        mActivity.service.getOtherValues().enqueue(new Callback<OtherValuesResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<OtherValuesResponseModel> call, @NonNull Response<OtherValuesResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getOtherValuesResponse(response.body());
                    } else {
                        mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showOtpFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<OtherValuesResponseModel> call, @NonNull Throwable t) {
                mViewModel.showOtpFailed(""+t.getMessage());
            }
        });
    }
}
