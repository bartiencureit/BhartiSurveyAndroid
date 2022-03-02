package com.encureit.bhartisurveyandroid.database;

import android.content.Context;

import com.encureit.bhartisurveyandroid.database.dao.SurveyTypeDao;
import com.encureit.bhartisurveyandroid.database.dao.UserDeviceDetailsDao;
import com.encureit.bhartisurveyandroid.models.SurveyType;
import com.encureit.bhartisurveyandroid.models.UserDeviceDetails;

import java.util.List;


public class DatabaseUtil {
    /**
     * Fields
     */
    private static DatabaseUtil sInstance;
    private SurveyTypeDao mSurveyTypeDao;
    private UserDeviceDetailsDao mUserDeviceDetailsDao;

    private DatabaseUtil() {
        setSurveyTypeDao(UniqaDatabase.on().surveyTypeDao());
        setUserDeviceDetailsDao(UniqaDatabase.on().userDeviceDetailsDao());
    }

    /**
     * This method builds an instance
     */
    public static void init(Context context) {
        UniqaDatabase.init(context);

        if (sInstance == null) {
            sInstance = new DatabaseUtil();
        }
    }

    public static DatabaseUtil on() {
        if (sInstance == null) {
            sInstance = new DatabaseUtil();
        }

        return sInstance;
    }

    private SurveyTypeDao getSurveyTypeDao() {
        return mSurveyTypeDao;
    }

    private void setSurveyTypeDao(SurveyTypeDao surveyTypeDao) {
        mSurveyTypeDao = surveyTypeDao;
    }

    public UserDeviceDetailsDao getUserDeviceDetailsDao() {
        return mUserDeviceDetailsDao;
    }

    public void setUserDeviceDetailsDao(UserDeviceDetailsDao mUserDeviceDetailsDao) {
        this.mUserDeviceDetailsDao = mUserDeviceDetailsDao;
    }

    public long[] insertSurveyType(SurveyType surveyType) {
        return getSurveyTypeDao().insert(surveyType);
    }

    public long[] insertAllSurveyTypes(List<SurveyType> surveyTypes) {
        return getSurveyTypeDao().insertBulk(surveyTypes);
    }

    public List<SurveyType> getAllSurveyType() {
        return getSurveyTypeDao().getAllFlowableCodes();
    }

    public int deleteEntity(SurveyType code) {
        return getSurveyTypeDao().delete(code);
    }

    public int getItemCount() {
        return getSurveyTypeDao().getRowCount();
    }

    public void deleteAllSurvey() {
        getSurveyTypeDao().nukeTable();
    }

    public void update_Form_unique_id(int id, String Form_unique_id) {
        getSurveyTypeDao().update_Form_unique_id(id,Form_unique_id);
    }
    public void update_Form_no(int id, String Form_no) {
        getSurveyTypeDao().update_Form_no(id,Form_no);
    }
    public void update_form_type(int id, String form_type) {
        getSurveyTypeDao().update_form_type(id,form_type);
    }
    public void update_form_description(int id, String form_description) {
        getSurveyTypeDao().update_form_description(id,form_description);
    }
    public void update_isActive(int id, String isActive) {
        getSurveyTypeDao().update_isActive(id,isActive);
    }

    public boolean hasSurveyType() {
        return getAllSurveyType().size() > 0;
    }

    public long[] insertUserDeviceDetails(UserDeviceDetails userDeviceDetails) {
        return getUserDeviceDetailsDao().insert(userDeviceDetails);
    }

}
