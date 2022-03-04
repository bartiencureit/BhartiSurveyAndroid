package com.encureit.bhartisurveyandroid.features.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.encureit.bhartisurveyandroid.Helpers.GlobalHelper;
import com.encureit.bhartisurveyandroid.R;
import com.encureit.bhartisurveyandroid.adapters.SurveyTypeListAdapter;
import com.encureit.bhartisurveyandroid.base.BaseActivity;
import com.encureit.bhartisurveyandroid.databinding.ActivityDashboardBinding;
import com.encureit.bhartisurveyandroid.features.setting.SettingsActivity;
import com.encureit.bhartisurveyandroid.features.subforms.QuesSectionListActivity;
import com.encureit.bhartisurveyandroid.lib.AppKeys;
import com.encureit.bhartisurveyandroid.lib.ScreenHelper;
import com.encureit.bhartisurveyandroid.models.SurveyType;
import com.encureit.bhartisurveyandroid.models.contracts.DashboardContract;
import com.encureit.bhartisurveyandroid.presenter.DashboardPresenter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

public class DashboardActivity extends BaseActivity implements DashboardContract.ViewModel {
    private ActivityDashboardBinding mBinding;
    private DashboardPresenter mPresenter;
    private GlobalHelper helper;
    private LocationManager locationManager;
    private static final int REQUESTLocation = 113;
    boolean locationEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPresenter = new DashboardPresenter(DashboardActivity.this,this);
        helper = new GlobalHelper(DashboardActivity.this);
        mBinding.setPresenter(mPresenter);
        mPresenter.startDashboard(helper.getSharedPreferencesHelper().getLoginUserRole());
        mPresenter.getUserDeviceDetails(helper.getSharedPreferencesHelper().getLoginUserId());
    }


    @Override
    public void setupDashboardFields(List<SurveyType> list, String loginRole) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mBinding.gridView.setLayoutManager(gridLayoutManager);
        SurveyTypeListAdapter surveyAdapter = new SurveyTypeListAdapter(this, list, new SurveyTypeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(SurveyType listModel, int position) {
                Intent intent = new Intent(DashboardActivity.this, QuesSectionListActivity.class);
                intent.putExtra(AppKeys.SURVEY_TYPE,listModel);
                startActivityOnTop(false, intent);
                finish();
            }
        });
        mBinding.gridView.setAdapter(surveyAdapter);
    }

    @Override
    public void showResponseFailed(String error) {
        ScreenHelper.showErrorSnackBar(mBinding.getRoot(),error);
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUESTLocation);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUESTLocation: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (helper.getGpsTracker().canGetLocation()) {
                        Log.e("TAG", "if getLocation: " + "latitude: " + helper.getGpsTracker().getLatitude() + "\tlongitude: " + helper.getGpsTracker().getLongitude());
                        locationEnabled = true;
                        helper.getGpsTracker().stopUsingGPS();
                    } else {
                        Log.e("TAG", "else getLocation: " + helper.getGpsTracker().canGetLocation());
                        locationEnabled = false;
                        helper.getGpsTracker().showSettingsAlert();
                    }
                } else {
                    locationEnabled = false;
                    Snackbar.make(mBinding.getRoot(), "Please Enable GPS Location", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.menu_setting == item.getItemId()) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityOnTop(false, intent);
            finish();
        }
        return true;
    }
}