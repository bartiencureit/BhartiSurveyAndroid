/*
 * Copyright (c) - 2020 & Created By AbhishekR - EncureIT :)
 */

package com.encureit.samtadoot.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.encureit.samtadoot.network.Contants;


public class SharedPreferencesHelper extends GlobalHelper {

    SharedPreferences preferences;

    public SharedPreferencesHelper(Context context) {
        super(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public void clear() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setPrefIsLogin(boolean isLogin) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Contants.PREF_IS_LOGIN, isLogin);
        editor.apply();
    }

    public boolean getPrefIsLogin() {
        return preferences.getBoolean(Contants.PREF_IS_LOGIN, false);
    }

    public void setPrefAuthToken(String auth_token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Contants.PREF_AUTH_TOKEN, auth_token);
        editor.apply();
    }

    public void setLoginDateTimeData(String dateTime) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Contants.SYNC_LOGIN_DATE_TIME_DATA, dateTime);
        editor.apply();
    }

    public void setLoginUserId(String loginUserId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Contants.APP_LOGIN_USER_ID, loginUserId);
        editor.apply();
    }

    public void setUserId(String loginUserId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Contants.APP_USER_ID, loginUserId);
        editor.apply();
    }

    public void setLoginUserRole(String loginUserRole) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Contants.APP_LOGIN_USER_ROLE, loginUserRole);
        editor.apply();
    }

    public void setLastSyncTimeAllData(String dateTime) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Contants.SYNC_DATE_TIME_ALL_DATA, dateTime);
        editor.apply();
    }

    public void setLastSyncTimeCandidateData(String dateTime) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Contants.SYNC_DATE_TIME_CANDIDATE_DATA, dateTime);
        editor.apply();
    }

    public String getLoginDateTimeData(){
        return preferences.getString(Contants.SYNC_LOGIN_DATE_TIME_DATA, "");
    }

    public String getLoginUserId(){
        return preferences.getString(Contants.APP_LOGIN_USER_ID, "");
    }

    public String getUserId(){
        return preferences.getString(Contants.APP_USER_ID, "");
    }

    public String getLoginUserRole(){
        return preferences.getString(Contants.APP_LOGIN_USER_ROLE, "");
    }

    public String getLastSyncAllDataTime(){
        return preferences.getString(Contants.SYNC_DATE_TIME_ALL_DATA, "");
    }

    public String getLastSyncCandidateDataTime(){
        return preferences.getString(Contants.SYNC_DATE_TIME_CANDIDATE_DATA, "");
    }

    public String getPrefAuthToken() {
        return preferences.getString(Contants.PREF_AUTH_TOKEN, "");
    }


}
