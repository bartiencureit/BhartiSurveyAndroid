package com.encureit.bhartisurveyandroid.database;

import android.content.Context;

import com.encureit.bhartisurveyandroid.models.SurveyType;

import java.util.List;


public class DatabaseUtil {
    /**
     * Fields
     */
    private static DatabaseUtil sInstance;
    private UniqaCustomDao mCodeDao;

    private DatabaseUtil() {
        setCodeDao(UniqaDatabase.on().codeDao());
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

    private UniqaCustomDao getCodeDao() {
        return mCodeDao;
    }

    private void setCodeDao(UniqaCustomDao codeDao) {
        mCodeDao = codeDao;
    }

    public long[] insertSurveyType(SurveyType surveyType) {
        return getCodeDao().insert(surveyType);
    }

    public List<SurveyType> getAllSurveyType() {
        return getCodeDao().getAllFlowableCodes();
    }

    public int deleteEntity(SurveyType code) {
        return getCodeDao().delete(code);
    }

    public int getItemCount() {
        return getCodeDao().getRowCount();
    }

    public void deleteAll() {
        getCodeDao().nukeTable();
    }

    public void update_Form_unique_id(int id, String Form_unique_id) {
        getCodeDao().update_Form_unique_id(id,Form_unique_id);
    }
    public void update_Form_no(int id, String Form_no) {
        getCodeDao().update_Form_no(id,Form_no);
    }
    public void update_form_type(int id, String form_type) {
        getCodeDao().update_form_type(id,form_type);
    }
    public void update_form_description(int id, String form_description) {
        getCodeDao().update_form_description(id,form_description);
    }
    public void update_isActive(int id, String isActive) {
        getCodeDao().update_isActive(id,isActive);
    }

    public boolean hasSurveyType() {
        return getAllSurveyType().size() > 0;
    }

}
