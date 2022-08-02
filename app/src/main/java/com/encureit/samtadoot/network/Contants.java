package com.encureit.samtadoot.network;

import android.content.Context;
import android.os.Environment;

public class Contants {
    public static final String BASE_URL = "http://evaluation.rkenterprises.group/";
    public static final String OTHER_BASE_URL = BASE_URL+"api/";
    public static final String CANDIDATE_BASE_URL = BASE_URL+"getdata/";
    public static final String LOGIN_BASE_URL = BASE_URL+"login_api/";
    public static final String GET_LOGIN = "isLoginf";
    public static final String GET_VERIFY_OTP = "verifyOTP";
    public static final String GET_SURVEY_TYPE = "getForms";
    //public static final String GET_SURVEY_TYPE = "get_survey_masters";
    public static final String GET_SURVEY_SECTION = "get_survey_section";
    public static final String GET_SURVEY_QUESTION = "get_survey_question";
    public static final String GET_QUESTION_OPTION = "get_question_option";
    public static final String GET_QUESTION_TYPES = "get_question_type";
    public static final String GET_QUESTION_VALIDATION = "get_question_validation";
    public static final String GET_USER_ASSIGNED_DETAILS = "get_user_assigned_details";
    public static final String GET_OTHER_VALUES = "get_other_values";
    public static final String INSERT_CANDIDATE = "insertData";

    public static String PREF_IS_LOGIN = "PREF_IS_LOGIN";
    public static String PREF_AUTH_TOKEN = "PREF_AUTH_TOKEN";
    public static String FOLDER_SAVE = "android/data/data/com.encureit.samtadoot/";
    public static String DEFAULT_FILENAME = "encureit_aas_";
    public static String TABLE_FORM = "dynamicfield";
    public static String FOLDER_PATH = Environment.getExternalStorageDirectory().getPath() + "/" + FOLDER_SAVE;

    public static String APP_LOGIN_USER_ID = "com.encureit.samtadoot.SYNC_LOGIN_USER_ID";
    public static String APP_LOGIN_USER_KEY = "com.encureit.samtadoot.SYNC_LOGIN_USER_KEY";
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
    public static Context applicationContext = null;
}
