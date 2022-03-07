package com.encureit.samtadoot.utils;

import android.util.Log;

import androidx.viewbinding.BuildConfig;


/**
 * Created by AbhishekR on
 */
public class Logger {

    private static String TAG = "TRIT";

    private static boolean isLogEnabled = BuildConfig.DEBUG;

    private static Level logLevel = Level.VERBOSE;

    public static void debug(String msg) {
        if (isLogEnabled && logLevel.ordinal() >= 3 && msg != null) {
            Log.d(TAG, msg);
            //logToFile(TAG, msg + "\r\n");
        }
    }

    public static void debug(String msg, boolean writeToFile) {
        if (isLogEnabled && logLevel.ordinal() >= 3 && msg != null) {
            Log.d(TAG, msg);
        }
    }

    public static void debug(Throwable t) {
        if (isLogEnabled && logLevel.ordinal() >= 3) {
            Log.d(TAG, "Exception: ", t);
        }
    }

    public static void debug(String msg, Throwable t) {
        if (isLogEnabled && logLevel.ordinal() >= 3 && msg != null) {
            Log.d(TAG, msg, t);
        }
    }

    public static void error(String msg) {
        if (isLogEnabled && logLevel.ordinal() >= 0 && msg != null) {
            Log.e(TAG, msg);
        }
    }

    public static Level getLogLevel() {
        return logLevel;
    }

    public static void setLogLevel(Level logLevel) {
        Logger.logLevel = logLevel;
    }

    public static void info(String msg) {
        if (isLogEnabled && logLevel.ordinal() >= 2 && msg != null) {
            Log.i(TAG, msg);
        }
    }

    public static boolean isLogEnabled() {
        return isLogEnabled;
    }

    public static void setLogEnabled(boolean isLogEnabled) {
        Logger.isLogEnabled = isLogEnabled;
    }

    public static void log(String msg) {
        if (isLogEnabled && logLevel.ordinal() >= 3 && msg != null) {
            Log.d(TAG, msg);
        }
    }

    public static void setLogTag(String logTag) {
        TAG = logTag;
    }

    public static void warn(String msg) {
        if (isLogEnabled && logLevel.ordinal() >= 1 && msg != null) {
            Log.w(TAG, msg);
        }
    }

    public enum Level {
        ERROR, WARN, INFO, DEBUG, VERBOSE
    }
}
