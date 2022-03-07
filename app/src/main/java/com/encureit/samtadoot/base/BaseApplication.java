/*
 * Copyright (c) - 2020 & Created By AbhishekR - EncureIT :)
 */

package com.encureit.samtadoot.base;

import android.content.Context;
import android.os.StrictMode;

import com.encureit.samtadoot.Helpers.GlobalHelper;
import com.encureit.samtadoot.database.DatabaseUtil;

import androidx.multidex.MultiDexApplication;


public class BaseApplication extends MultiDexApplication {
    private static Context appsContext;
    private static BaseApplication instances;
    private static GlobalHelper globalHelper;

    public static BaseApplication getInstances() {
        return instances;
    }

    public static Context getAppsContext() {
        return appsContext;
    }

    private void setStrictPolicy() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void onCreate() {
        this.appsContext = this;
        this.instances = this;

        super.onCreate();
        DatabaseUtil.init(getApplicationContext());
        setStrictPolicy();
    }

    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }*/

    public GlobalHelper getGlobalHelper() {
        if (this.globalHelper == null) {
            this.globalHelper = new GlobalHelper(this);
        }
        return this.globalHelper;
    }
}
