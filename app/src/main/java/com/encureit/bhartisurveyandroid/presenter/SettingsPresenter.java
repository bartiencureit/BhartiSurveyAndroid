package com.encureit.bhartisurveyandroid.presenter;

import com.encureit.bhartisurveyandroid.Helpers.GlobalHelper;
import com.encureit.bhartisurveyandroid.R;
import com.encureit.bhartisurveyandroid.features.setting.SettingsActivity;
import com.encureit.bhartisurveyandroid.models.contracts.SettingsContract;

import androidx.databinding.ObservableField;

/**
 * Created by Swapna Thakur on 3/2/2022.
 */
public class SettingsPresenter implements SettingsContract.Presenter {
    private SettingsActivity mActivity;
    private SettingsContract.ViewModel mViewModel;

    public ObservableField<String> lastSyncDate;
    public ObservableField<String> lastSyncCandidateFormDate;
    public ObservableField<String> userIDRole;
    private String userRole;
    private String userId;
    private String loginUserId;

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

    }

    @Override
    public void syncAllForms() {

    }

    @Override
    public void logout() {

    }
}
