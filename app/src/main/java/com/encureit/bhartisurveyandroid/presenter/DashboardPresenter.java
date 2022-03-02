package com.encureit.bhartisurveyandroid.presenter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;

import com.encureit.bhartisurveyandroid.database.DatabaseUtil;
import com.encureit.bhartisurveyandroid.features.dashboard.DashboardActivity;
import com.encureit.bhartisurveyandroid.network.responsemodel.SurveyTypeResponseModel;
import com.encureit.bhartisurveyandroid.models.SurveyType;
import com.encureit.bhartisurveyandroid.models.UserDeviceDetails;
import com.encureit.bhartisurveyandroid.models.contracts.DashboardContract;
import com.encureit.bhartisurveyandroid.models.viewmodelobj.UserLoginObject;
import com.encureit.bhartisurveyandroid.network.retrofit.RetrofitClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import androidx.core.content.ContextCompat;
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

    /**
     * Checks internet connected or not
     * if internet is connected get data from server
     * else get data drom local database
     * @param loginRole
     */
    @Override
    public void startDashboard(String loginRole) {
        if (mActivity.isInternetConnected()) {
            syncData();
        } else {
            List<SurveyType> surveyTypes = DatabaseUtil.on().getAllSurveyType();
            mViewModel.setupDashboardFields(surveyTypes, loginRole);
        }
    }

    /**
     * to sync server data to local database
     */
    private void syncData() {
        getSurveyMaster();
    }

    @Override
    public void getUserDeviceDetails(String loginUserId) {
        String details = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            details = "\nUser Mobile System Information:"
                    + "\nDEVICE : " + Build.DEVICE + "\nBOARD : " + Build.BOARD + "\nBOOTLOADER : " + Build.BOOTLOADER + "\nCPU_ABI : " + Build.CPU_ABI
                    + "\nCPU_ABI2 : " + Build.CPU_ABI2 + "\nDISPLAY : " + Build.DISPLAY + "\nFINGERPRINT : " + Build.FINGERPRINT
                    + "\nHARDWARE : " + Build.HARDWARE + "\nHOST : " + Build.HOST + "\nID : " + Build.ID + "\nMANUFACTURER : " + Build.MANUFACTURER
                    + "\nBRAND : " + Build.BRAND + "\nMODEL : " + Build.MODEL + "\nPRODUCT : " + Build.PRODUCT + "\nSERIAL : " + Build.SERIAL
                    + "\nUSER : " + Build.USER + "\nTAGS : " + Build.TAGS + "\nTIME : " + Build.TIME + "\nTYPE : " + Build.TYPE
                    + "\nUNKNOWN : " + Build.UNKNOWN + "\nIMEI-2 : " + getDeviceIMEI() + "\nIMEI-3 : " + getUniqueIMEIId(mActivity)
                    + "\nDISPLAY_SIZE : " + getDeviceScreenResolution() + "\nMOBILE_NETWORK : " + isMobileNetworkAvailable(mActivity)
                    + "\nWIFI_ : " + isWifiNetworkAvailable(mActivity) + "\nBLUETOOTH_ : " + checkingBluetoothConnection()
                    /*+ "\nVERSION.SDK : " + android.net.NetworkInfo.DetailedState.values()*/ + "\nVERSION.SDK : " + Build.VERSION.SDK
                    + "\nVERSION.RELEASE : " + Build.VERSION.RELEASE + "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL
                    + "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT + "\nVERSION.BASE.OS : " + Build.VERSION.BASE_OS
                    + "\nVERSION.PREVIEW_SDK_INT : " + Build.VERSION.PREVIEW_SDK_INT + "\nVERSION.SECURITY_PATCH : " + Build.VERSION.SECURITY_PATCH
                    + "\nVERSION.CODENAME : " + Build.VERSION.CODENAME;
        }
        Log.e("act_dashboard", "Device_Details_: " + details);

        String device_manufacturer = Build.MANUFACTURER, device_brand = Build.BRAND, device_model = Build.MODEL, device_serial_no = Build.SERIAL;
        String device_imei_no = getDeviceIMEI(), device_display_size = getDeviceScreenResolution(), device_mobile_network = isMobileNetworkAvailable(mActivity);
        String device_wifi = isWifiNetworkAvailable(mActivity), device_bluetooth = checkingBluetoothConnection();
        String device_sdk_version_no = String.valueOf(Build.VERSION.SDK_INT), device_version_no = Build.VERSION.RELEASE, device_id = Build.ID;
        String device_os_version_no = Build.VERSION.RELEASE, device_host = Build.HOST, device_hardware = Build.HARDWARE, device_fingerprint = Build.FINGERPRINT;

        UserDeviceDetails userDeviceDetails = new UserDeviceDetails();
        userDeviceDetails.setUserRoleId(loginUserId);
        userDeviceDetails.setManufacturer(device_manufacturer);
        userDeviceDetails.setBrand(device_brand);
        userDeviceDetails.setModel(device_model);
        userDeviceDetails.setSerialNo(device_serial_no);
        userDeviceDetails.setImeiNo(device_imei_no);
        userDeviceDetails.setDisplaySize(device_display_size);
        userDeviceDetails.setMobileNetwork(device_mobile_network);
        userDeviceDetails.setWifi(device_wifi);
        userDeviceDetails.setBluetooth(device_bluetooth);
        userDeviceDetails.setSdkVersionNo(device_sdk_version_no);
        userDeviceDetails.setReleaseVersion(device_version_no);
        userDeviceDetails.setID(device_id);
        userDeviceDetails.setOsVersionNo(device_os_version_no);
        userDeviceDetails.setHost(device_host);
        userDeviceDetails.setHardware(device_hardware);
        userDeviceDetails.setFingerprint(device_fingerprint);
        DatabaseUtil.on().insertUserDeviceDetails(userDeviceDetails);
    }

    public String getDeviceScreenResolution() {
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x; //device width
        int height = size.y; //device height
        return width + " x " + height; //example "480 * 800"
    }

    public String isMobileNetworkAvailable(Context context) {
        /*Checks if the device has 3G or other mobile data connection.*/
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return "Connected";
        } else {
            return "Disconnected";
        }
    }

    public String isWifiNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return "Connected";
        } else {
            return "Disconnected";
        }
    }

    private String checkingBluetoothConnection() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return "Not supported"; //device not support Bluetooth
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                return "Disabled"; // Bluetooth is disabled
            } else {
                return "Enabled"; //Bluetooth adapter is anabled and ready to pair with other devices
            }
        }
    }


    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        String getSimSerialNumber = null, getSimNumber = null;
        TelephonyManager tm = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm && ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            deviceUniqueIdentifier = tm.getDeviceId();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(mActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        /*String abc = deviceUniqueIdentifier +"\tgetSimSerialNumber: "+getSimSerialNumber+"\tgetSimNumber: "+getSimNumber;*/
        return deviceUniqueIdentifier;
    }

    public static String getUniqueIMEIId(Context context) {
        String imei = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei = telephonyManager.getImei();
                } else {
                    imei = telephonyManager.getDeviceId();
                }
                if (imei != null && !imei.isEmpty()) {
                    return imei;
                } else {
                    imei = android.os.Build.SERIAL;
                    return imei;
                }
            }
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            return errors.toString();
        }
        return imei;
    }

    @Override
    public void getSurveyMaster() {
        mActivity.startProgressDialog();

        RetrofitClient.getApiService().getSurveyTypes().enqueue(new Callback<SurveyTypeResponseModel>() {
            @Override
            public void onResponse(Call<SurveyTypeResponseModel> call, Response<SurveyTypeResponseModel> response) {
                if (response.code() == 200 || response.code() == 201) {
                    if (response.body() != null && response.body().getSurvey_type() != null) {
                        List<SurveyType> surveyTypeList = response.body().getSurvey_type();
                        if(DatabaseUtil.on().hasSurveyType()) {
                            DatabaseUtil.on().deleteAllSurvey();
                        }
                        DatabaseUtil.on().insertAllSurveyTypes(surveyTypeList);
                        mViewModel.setupDashboardFields(surveyTypeList,"");
                    } else {
                        mActivity.dismissProgressDialog();
                        mViewModel.showResponseFailed("Null Response from server");
                    }
                } else {
                    mActivity.dismissProgressDialog();
                    mViewModel.showResponseFailed("Invalid Response from server");
                }
            }

            @Override
            public void onFailure(Call<SurveyTypeResponseModel> call, Throwable t) {
                mActivity.dismissProgressDialog();
                mViewModel.showResponseFailed(t.getMessage());
            }
        });
    }
}
