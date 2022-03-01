/*
 * Copyright (c) - 2020 & Created By AbhishekR - EncureIT :)
 */

package com.encureit.bhartisurveyandroid.Helpers;

import android.content.Context;


public class UserHelper extends GlobalHelper {

    public UserHelper(Context context) {
        super(context);
    }

    public boolean checkUserLogin() {
        return true;
    }

    public void userLogout() {
        /*this.getSharedPreferencesHelper().clear();
        showNotification("You have been log out", context);
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);*/
    }

}
