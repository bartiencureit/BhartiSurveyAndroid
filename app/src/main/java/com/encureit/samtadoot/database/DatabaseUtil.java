package com.encureit.samtadoot.database;

import android.content.Context;

import com.encureit.samtadoot.database.dao.AssignDetailsDao;
import com.encureit.samtadoot.database.dao.QuestionOptionDao;
import com.encureit.samtadoot.database.dao.QuestionTypeDao;
import com.encureit.samtadoot.database.dao.QuestionValidationDao;
import com.encureit.samtadoot.database.dao.SurveyQuestionDao;
import com.encureit.samtadoot.database.dao.SurveySectionDao;
import com.encureit.samtadoot.database.dao.SurveyTypeDao;
import com.encureit.samtadoot.database.dao.UserDeviceDetailsDao;
import com.encureit.samtadoot.models.AssignDetails;
import com.encureit.samtadoot.models.QuestionOption;
import com.encureit.samtadoot.models.QuestionType;
import com.encureit.samtadoot.models.QuestionValidation;
import com.encureit.samtadoot.models.SurveyQuestion;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.SurveyType;
import com.encureit.samtadoot.models.UserDeviceDetails;

import java.util.List;


public class DatabaseUtil {
    /**
     * Fields
     */
    private static DatabaseUtil sInstance;
    private SurveyTypeDao mSurveyTypeDao;
    private UserDeviceDetailsDao mUserDeviceDetailsDao;
    private QuestionOptionDao mQuestionOptionDao;
    private QuestionTypeDao mQuestionTypeDao;
    private QuestionValidationDao mQuestionValidationDao;
    private SurveyQuestionDao mSurveyQuestionDao;
    private SurveySectionDao mSurveySectionDao;
    private AssignDetailsDao mAssignDetailsDao;

    private DatabaseUtil() {
        setSurveyTypeDao(UniqaDatabase.on().surveyTypeDao());
        setUserDeviceDetailsDao(UniqaDatabase.on().userDeviceDetailsDao());
        setQuestionOptionDao(UniqaDatabase.on().questionOptionDao());
        setQuestionTypeDao(UniqaDatabase.on().questionTypeDao());
        setQuestionValidationDao(UniqaDatabase.on().questionValidationDao());
        setSurveyQuestionDao(UniqaDatabase.on().surveyQuestionDao());
        setSurveySectionDao(UniqaDatabase.on().surveySectionDao());
        setAssignDetailsDao(UniqaDatabase.on().assignDetailsDao());
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

    public SurveyTypeDao getSurveyTypeDao() {
        return mSurveyTypeDao;
    }

    public void setSurveyTypeDao(SurveyTypeDao surveyTypeDao) {
        mSurveyTypeDao = surveyTypeDao;
    }

    public UserDeviceDetailsDao getUserDeviceDetailsDao() {
        return mUserDeviceDetailsDao;
    }

    public void setUserDeviceDetailsDao(UserDeviceDetailsDao mUserDeviceDetailsDao) {
        this.mUserDeviceDetailsDao = mUserDeviceDetailsDao;
    }

    public QuestionOptionDao getQuestionOptionDao() {
        return mQuestionOptionDao;
    }

    public void setQuestionOptionDao(QuestionOptionDao mQuestionOptionDao) {
        this.mQuestionOptionDao = mQuestionOptionDao;
    }

    public QuestionTypeDao getQuestionTypeDao() {
        return mQuestionTypeDao;
    }

    public void setQuestionTypeDao(QuestionTypeDao mQuestionTypeDao) {
        this.mQuestionTypeDao = mQuestionTypeDao;
    }

    public QuestionValidationDao getQuestionValidationDao() {
        return mQuestionValidationDao;
    }

    public void setQuestionValidationDao(QuestionValidationDao mQuestionValidationDao) {
        this.mQuestionValidationDao = mQuestionValidationDao;
    }

    public SurveyQuestionDao getSurveyQuestionDao() {
        return mSurveyQuestionDao;
    }

    public void setSurveyQuestionDao(SurveyQuestionDao mSurveyQuestionDao) {
        this.mSurveyQuestionDao = mSurveyQuestionDao;
    }

    public SurveySectionDao getSurveySectionDao() {
        return mSurveySectionDao;
    }

    public void setSurveySectionDao(SurveySectionDao mSurveySectionDao) {
        this.mSurveySectionDao = mSurveySectionDao;
    }

    public AssignDetailsDao getAssignDetailsDao() {
        return mAssignDetailsDao;
    }

    public void setAssignDetailsDao(AssignDetailsDao mAssignDetailsDao) {
        this.mAssignDetailsDao = mAssignDetailsDao;
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

    public long[] insertQuestionOption(QuestionOption questionOption) {
        return getQuestionOptionDao().insert(questionOption);
    }

    public long[] insertQuestionType(QuestionType questionType) {
        return getQuestionTypeDao().insert(questionType);
    }

    public long[] insertQuestionValidation(QuestionValidation questionValidation) {
        return getQuestionValidationDao().insert(questionValidation);
    }

    public long[] insertSurveyQuestion(SurveyQuestion surveyQuestion) {
        return getSurveyQuestionDao().insert(surveyQuestion);
    }

    public long[] insertSurveySection(SurveySection surveySection) {
        return getSurveySectionDao().insert(surveySection);
    }

    /**
     * insert list
     */
    public long[] insertAllQuestionOption(List<QuestionOption> questionOptions) {
        return getQuestionOptionDao().insertBulk(questionOptions);
    }

    public long[] insertAllQuestionType(List<QuestionType> questionTypes) {
        return getQuestionTypeDao().insertBulk(questionTypes);
    }

    public long[] insertAllQuestionValidation(List<QuestionValidation> questionValidations) {
        return getQuestionValidationDao().insertBulk(questionValidations);
    }

    public long[] insertAllSurveyQuestion(List<SurveyQuestion> surveyQuestions) {
        return getSurveyQuestionDao().insertBulk(surveyQuestions);
    }

    public long[] insertAllSurveySection(List<SurveySection> surveySections) {
        return getSurveySectionDao().insertBulk(surveySections);
    }

    public long[] insertAllUserAssignedDetails(List<AssignDetails> assignDetails) {
        return getAssignDetailsDao().insertBulk(assignDetails);
    }

}
