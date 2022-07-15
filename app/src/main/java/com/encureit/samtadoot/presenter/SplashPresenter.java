package com.encureit.samtadoot.presenter;

import android.util.Log;
import android.view.View;

import com.encureit.samtadoot.Helpers.GlobalHelper;
import com.encureit.samtadoot.features.dashboard.DashboardActivity;
import com.encureit.samtadoot.lib.ScreenHelper;
import com.encureit.samtadoot.login.LoginActivity;
import com.encureit.samtadoot.models.contracts.SplashContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import static com.encureit.samtadoot.utils.CommonUtils.getCurrentDateOnly;

/**
 * Created by Swapna Thakur on 3/1/2022.
 * Presenter for splash to check last login date
 */
public class SplashPresenter implements SplashContract.Presenter {
    private final SplashContract.ViewModel mViewModel;
    public View rootView;
    private static final String TAG = "SplashPresenter";

    public SplashPresenter(SplashContract.ViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }

    /**
     * Checks last login date
     * @param helper
     */
    @Override
    public void checkLastLoginDate(GlobalHelper helper) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy/MM/dd");
        String loginDate = helper.getSharedPreferencesHelper().getLoginDateTimeData();
        //String loginDate = "2019/10/28";
        String currentDate = getCurrentDateOnly();
        if (!loginDate.isEmpty()) {
            try {
                Date currentD = myFormat.parse(currentDate);
                Date loginD = myFormat.parse(loginDate);
                long difference = currentD.getTime() - loginD.getTime();
                float daysBetween = (difference / (1000 * 60 * 60 * 24));
                /* You can also convert the milliseconds to days using this method
                 * float daysBetween = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS) */
                if (daysBetween < 15) {
                   mViewModel.startAnotherActivity(DashboardActivity.class, true);
                    Log.i("SplashActivity::", "direct start");
                } else {
                    try {
                        mViewModel.startAnotherActivity(LoginActivity.class, true);
                    } catch (Exception e) {
                        Log.e(TAG, "checkLastLoginDate: "+e.getMessage() );
                    }
                    Log.i("SplashActivity::", "login expired start");
                }
            } catch (Exception e) {
                ScreenHelper.showErrorSnackBar(rootView,e.getMessage());
            }
        } else {
            // todo direct login screen
            try {
                mViewModel.startAnotherActivity(LoginActivity.class, true);
            } catch (Exception e) {
                Log.e(TAG, "checkLastLoginDate: "+e.getMessage());
            }
            Log.i("SplashActivity::", "Freshly start");
        }
    }
}
