package com.encureit.bhartisurveyandroid.network;

import android.os.Environment;

public class Contants {
    public static final String BASE_URL = "https://machinetest.encureit.com";
    public static final String GET_LIST = "country.php";

    public static String PREF_IS_LOGIN = "PREF_IS_LOGIN";
    public static String PREF_AUTH_TOKEN = "PREF_AUTH_TOKEN";
    public static String FOLDER_SAVE = "android/data/data/com.encureit.samtadoot/";
    public static String DEFAULT_FILENAME = "encureit_aas_";
    public static String TABLE_FORM = "dynamicfield";
    public static String FOLDER_PATH = Environment.getExternalStorageDirectory().getPath() + "/" + FOLDER_SAVE;

    public static String APP_LOGIN_USER_ID = "com.encureit.samtadoot.SYNC_LOGIN_USER_ID";
    public static String APP_USER_ID = "com.encureit.samtadoot.SYNC_USER_ID";
    public static String APP_LOGIN_USER_ROLE = "com.encureit.samtadoot.SYNC_LOGIN_USER_ROLE";
    public static String SYNC_LOGIN_DATE_TIME_DATA = "com.encureit.samtadoot.SYNC_LOGIN_DATE_TIME_DATA";
    public static String SYNC_DATE_TIME_ALL_DATA = "com.encureit.samtadoot.all_data_sync_date_time";
    public static String SYNC_DATE_TIME_CANDIDATE_DATA = "com.encureit.samtadoot.candidate_data_sync_date_time";

    public static String SURVEY_MASTER_ID = "com.encureit.samtadoot.surveyMasterId";
    public static String SURVEY_SECTION_ID = "com.encureit.samtadoot.surveySection_ID";
    public static String SECTION_NO_NAME = "com.encureit.samtadoot.sectionNoName";

    public static String FORM_TYPE = "com.encureit.samtadoot.formType";
    public static String UNIQUE_FORM_ID = "com.encureit.samtadoot.uniqueFormID";
    public static String IS_FORM_NEW = "com.encureit.samtadoot.isFormNew";
    public static String IS_FORM_COMPLETED = "com.encureit.samtadoot.isFormCompleted";
}
