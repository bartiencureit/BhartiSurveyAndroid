package com.encureit.samtadoot.presenter;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.encureit.samtadoot.Helpers.GlobalHelper;
import com.encureit.samtadoot.Helpers.Helper;
import com.encureit.samtadoot.R;
import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.features.setting.SettingsActivity;
import com.encureit.samtadoot.models.CandidateDetails;
import com.encureit.samtadoot.models.contracts.SettingsContract;
import com.encureit.samtadoot.network.responsemodel.CandidateInsertResponseModel;
import com.encureit.samtadoot.network.responsemodel.OtherValuesResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionOptionResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionValidationResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyQuestionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveySectionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.UserAssignedDetailsResponseModel;
import com.encureit.samtadoot.network.retrofit.RetrofitClient;
import com.encureit.samtadoot.network.retrofit.RetrofitClientInsert;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

/**
 * Created by Swapna Thakur on 3/2/2022.
 */
public class SettingsPresenter implements SettingsContract.Presenter {
    private final SettingsActivity mActivity;
    private final SettingsContract.ViewModel mViewModel;

    public ObservableField<String> lastSyncDate;
    public ObservableField<String> lastSyncCandidateFormDate;
    public ObservableField<String> userIDRole;
    private String userRole;
    private String userId;
    private String loginUserId;
    private int totuploadedForms = 0;
    private int uploadedForms = 0;
    private int uploadedFormsError = 0;

    public SettingsPresenter(SettingsActivity mActivity, SettingsContract.ViewModel mViewModel) {
        this.mActivity = mActivity;
        this.mViewModel = mViewModel;
        initFields();
    }

    private void initFields() {
        lastSyncDate = new ObservableField<>();
        lastSyncCandidateFormDate = new ObservableField<>();
        userIDRole = new ObservableField<>();
    }

    @Override
    public void setUpData(GlobalHelper helper) {
        String getDateAll = mActivity.getString(R.string.last_sync) + helper.getSharedPreferencesHelper().getLastSyncAllDataTime();
        lastSyncDate.set(getDateAll);
        String getDateCandidate = mActivity.getString(R.string.last_sync) + helper.getSharedPreferencesHelper().getLastSyncCandidateDataTime();
        lastSyncCandidateFormDate.set(getDateCandidate);
        ////
        userRole = helper.getSharedPreferencesHelper().getLoginUserRole();
        userId = helper.getSharedPreferencesHelper().getLoginUserId();
        loginUserId = helper.getSharedPreferencesHelper().getUserId();
        String divider = mActivity.getResources().getString(R.string.divider_v_line);
        userIDRole.set("User Role: " + userRole + divider + "User ID: " + userId);
    }

    @Override
    public void syncAll() {
        mViewModel.syncFinished();
    }

    @Override
    public void syncAllForms() {
        uploadedForms = 0;
        uploadedFormsError = 0;
        mActivity.startProgressDialog(mActivity.mBinding.getRoot());
        List<CandidateDetails> candidateDetails = DatabaseUtil.on().getCandidateDetailsDao().getAllFlowableCodes();
        for (int i = 0; i < candidateDetails.size(); i++) {
            CandidateDetails details = candidateDetails.get(i);

            RequestBody survey_master_id = Helper.prepareStringRequest(details.getSurvey_master_id());
            RequestBody survey_section_id = Helper.prepareStringRequest(details.getSurvey_section_id());
            RequestBody survey_que_id = Helper.prepareStringRequest(details.getSurvey_que_id());
            RequestBody survey_que_option_id = Helper.prepareStringRequest(details.getSurvey_que_option_id());
            RequestBody FormID = Helper.prepareStringRequest(details.getFormID());
            RequestBody Current_Form_Status = Helper.prepareStringRequest(details.getCurrent_Form_Status());
            RequestBody age_value = Helper.prepareStringRequest(details.getAge_value());
            RequestBody Survey_StartDate = Helper.prepareStringRequest(details.getSurvey_StartDate());
            RequestBody Survey_EndDate = Helper.prepareStringRequest(details.getSurvey_EndDate());
            RequestBody created_by = Helper.prepareStringRequest(details.getCreated_by());
            RequestBody Latitude = Helper.prepareStringRequest(details.getLatitude());
            RequestBody Longitude = Helper.prepareStringRequest(details.getLongitude());
            if (details.isHasImage()) {
                RequestBody survey_que_values = Helper.prepareStringRequest("");
                File file = new File(details.getSurvey_que_values());
                MultipartBody.Part image = Helper.prepareFilePart(mActivity, "image", file);

                RetrofitClientInsert.getApiService().insertCandidateData(survey_master_id,survey_section_id,survey_que_id,survey_que_option_id,
                        survey_que_values,FormID,Current_Form_Status,age_value,Survey_StartDate,Survey_EndDate,created_by,Latitude,Longitude,image)
                        .enqueue(new Callback<CandidateInsertResponseModel>() {
                            @Override
                            public void onResponse(@NonNull Call<CandidateInsertResponseModel> call, @NonNull Response<CandidateInsertResponseModel> response) {
                                if (response.code() == 200) {
                                    if (response.body().isStatus()) {
                                        DatabaseUtil.on().deleteCandidate(details);
                                        uploadedForms++;
                                        totuploadedForms++;
                                    } else {
                                        uploadedFormsError++;
                                        totuploadedForms++;
                                    }
                                }
                                if (totuploadedForms >= candidateDetails.size()) {
                                    checkData(candidateDetails.size());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<CandidateInsertResponseModel> call, @NonNull Throwable t) {
                                uploadedFormsError++;
                                totuploadedForms++;
                                if (totuploadedForms >= candidateDetails.size()) {
                                    checkData(candidateDetails.size());
                                }
                            }
                        });
            } else {
                RequestBody survey_que_values = Helper.prepareStringRequest(details.getSurvey_que_values());
                RetrofitClientInsert.getApiService().insertCandidateData(survey_master_id,survey_section_id,survey_que_id,survey_que_option_id,
                        survey_que_values,FormID,Current_Form_Status,age_value,Survey_StartDate,Survey_EndDate,created_by,Latitude,Longitude,null)
                        .enqueue(new Callback<CandidateInsertResponseModel>() {
                            @Override
                            public void onResponse(@NonNull Call<CandidateInsertResponseModel> call, @NonNull Response<CandidateInsertResponseModel> response) {
                                if (response.code() == 200) {
                                    if (response.body().isStatus()) {
                                        DatabaseUtil.on().deleteCandidate(details);
                                        uploadedForms++;
                                        totuploadedForms++;
                                    } else {
                                        uploadedFormsError++;
                                        totuploadedForms++;
                                    }
                                }
                                if (totuploadedForms >= candidateDetails.size()) {
                                    checkData(candidateDetails.size());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<CandidateInsertResponseModel> call, @NonNull Throwable t) {
                                uploadedFormsError++;
                                totuploadedForms++;
                                if (totuploadedForms >= candidateDetails.size()) {
                                    checkData(candidateDetails.size());
                                }
                            }
                        });
            }


        }
        if (candidateDetails.size() == 0) {
            mViewModel.showResponseNoData("No forms to sync");
        }

    }

    private void checkData(int totSize) {
        if (uploadedForms >= totSize) {
            mViewModel.syncFormsFinished();
        } else if(uploadedFormsError >= totSize) {
            mViewModel.showResponseFailed("Error in uploading form try again");
        } else {
            mViewModel.showResponseFailed(""+uploadedForms+" forms are uploaded");
        }
    }

    @Override
    public void logout() {
        AlertDialog dialog = new AlertDialog.Builder(mActivity).create();
        dialog.setTitle("Do you want to logout?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseUtil.on().getUserDeviceDetailsDao().nukeTable();
                mActivity.helper.getSharedPreferencesHelper().clear();
                mViewModel.logoutFinished();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void getSurveySectionFields() {
        mActivity.service.getSurveySection().enqueue(new Callback<SurveySectionResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SurveySectionResponseModel> call, @NonNull Response<SurveySectionResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if(response.body().isStatus()) {
                        mViewModel.getSurveySectionFieldsResponse(response.body());
                    } else {
                        mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SurveySectionResponseModel> call, @NonNull Throwable t) {
                mViewModel.showResponseFailed(""+t.getMessage());
            }
        });
    }

    public void getSurveyQuestionField() {
        mActivity.service.getSurveyQuestion().enqueue(new Callback<SurveyQuestionResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SurveyQuestionResponseModel> call, @NonNull Response<SurveyQuestionResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getSurveyQuestionFieldResponse(response.body());
                    } else {
                        mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SurveyQuestionResponseModel> call, @NonNull Throwable t) {
                mViewModel.showResponseFailed(""+t.getMessage());
            }
        });
    }

    public void getQuestionOptionField() {
        mActivity.service.getQuestionOptions().enqueue(new Callback<QuestionOptionResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<QuestionOptionResponseModel> call, @NonNull Response<QuestionOptionResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getQuestionOptionFieldResponse(response.body());
                    } else {
                        mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuestionOptionResponseModel> call, @NonNull Throwable t) {
                mViewModel.showResponseFailed(""+t.getMessage());
            }
        });
    }

    public void getQuestionTypesField() {
        mActivity.service.getQuestionTypes().enqueue(new Callback<QuestionTypeResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<QuestionTypeResponseModel> call, @NonNull Response<QuestionTypeResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getQuestionTypesFieldResponse(response.body());
                    } else {
                        mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuestionTypeResponseModel> call, @NonNull Throwable t) {
                mViewModel.showResponseFailed(""+t.getMessage());
            }
        });
    }

    public void getQuestionValidationFields() {
        mActivity.service.getQuestionValidations().enqueue(new Callback<QuestionValidationResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<QuestionValidationResponseModel> call, @NonNull Response<QuestionValidationResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getQuestionValidationFieldsResponse(response.body());
                    } else {
                        mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuestionValidationResponseModel> call, @NonNull Throwable t) {
                mViewModel.showResponseFailed(""+t.getMessage());
            }
        });
    }

    public void getUserAssignedDetails(String user_id) {
        mActivity.service.getUserAssignedDetails(user_id).enqueue(new Callback<UserAssignedDetailsResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<UserAssignedDetailsResponseModel> call, @NonNull Response<UserAssignedDetailsResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getUserAssignedDetails(response.body());
                    } else {
                        mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserAssignedDetailsResponseModel> call, @NonNull Throwable t) {
                mViewModel.showResponseFailed(""+t.getMessage());
            }
        });
    }

    public void getSurveyMaster() {
        mActivity.service.getSurveyTypes().enqueue(new Callback<SurveyTypeResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SurveyTypeResponseModel> call, @NonNull Response<SurveyTypeResponseModel> response) {
                //mActivity.enableForm();
                if (response.code() == 200 && response.body() != null) {
                    //if (response.body().isStatus()) {
                    mViewModel.getSurveyMasterResponse(response.body());
//                    } else {
//                        mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
//                    }
                } else {
                    mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SurveyTypeResponseModel> call, @NonNull Throwable t) {
                mViewModel.showResponseFailed(""+t.getMessage());
            }
        });
    }

    public void getOtherValues() {
        mActivity.service.getOtherValues().enqueue(new Callback<OtherValuesResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<OtherValuesResponseModel> call, @NonNull Response<OtherValuesResponseModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        mViewModel.getOtherValuesResponse(response.body());
                    } else {
                        mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                    }
                } else {
                    mViewModel.showResponseFailed(""+mActivity.getResources().getString(R.string.invalid_response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<OtherValuesResponseModel> call, @NonNull Throwable t) {
                mViewModel.showResponseFailed(""+t.getMessage());
            }
        });
    }
}
