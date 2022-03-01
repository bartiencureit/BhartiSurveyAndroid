package com.encureit.bhartisurveyandroid.presenter;

import android.util.Log;

import com.encureit.bhartisurveyandroid.Helpers.GlobalHelper;
import com.encureit.bhartisurveyandroid.MainActivity;
import com.encureit.bhartisurveyandroid.login.LoginActivity;
import com.encureit.bhartisurveyandroid.models.contracts.SplashContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import static com.encureit.bhartisurveyandroid.utils.CommonUtils.getCurrentDateOnly;

/**
 * Created by Swapna Thakur on 3/1/2022.
 * Presenter for splash to check last login date
 */
public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.ViewModel mViewModel;

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
//                    mViewModel.startAnotherActivity(DashboardActivity.class, true);
                    Log.i("SplashActivity::", "direct start");
                } else {
                    mViewModel.startAnotherActivity(LoginActivity.class, true);
                    Log.i("SplashActivity::", "login expired start");
                }
            } catch (Exception e) {
                Log.e("act_SplashActivity::", "Exception_: " + e);
                e.printStackTrace();
            }
        } else {
            // todo direct login screen
            mViewModel.startAnotherActivity(LoginActivity.class, true);
            Log.i("SplashActivity::", "Freshly start");
        }
    }
}
