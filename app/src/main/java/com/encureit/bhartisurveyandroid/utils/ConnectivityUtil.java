/*
 * Copyright (c) - 2020 & Created By AbhishekR - EncureIT :)
 */

package com.encureit.bhartisurveyandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.encureit.samtadoot.Base.App;

public class ConnectivityUtil {


    /**
     * Get the network info
     *
     * @return {@link NetworkInfo}
     */
    public static NetworkInfo getNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @return true when the device is connected to any data network false otherwise
     */
    public static boolean isConnected() {
        NetworkInfo info = getNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity and shows the message on SnackBar
     *
     * @return true when the device is connected to any data network false otherwise
     */
    public static boolean isConnected(Activity activity) {
        NetworkInfo info = getNetworkInfo();
        if ((info != null && info.isConnected())) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Check if there is fast connectivity
     *
     * @return true when the device is connected to a fast data network false otherwise
     */
    public static boolean isConnectedFast() {
        NetworkInfo info = ConnectivityUtil.getNetworkInfo();
        return (info != null && info.isConnected() && ConnectivityUtil
                .isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check if there is any connectivity to a mobile network
     *
     * @return true when the device is connected to a mobile data network false otherwise
     */
    public static boolean isConnectedMobile() {
        NetworkInfo info = ConnectivityUtil.getNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is any connectivity to a Wifi network
     *
     * @return true when the device is connected to a wifi network false otherwise
     */
    public static boolean isConnectedWifi() {
        NetworkInfo info = ConnectivityUtil.getNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if the connection is fast
     */
    private static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 * Here the Minimum API Level is 22 so no need
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }
}
