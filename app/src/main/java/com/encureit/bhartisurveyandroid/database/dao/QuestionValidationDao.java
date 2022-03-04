package com.encureit.bhartisurveyandroid.database.dao;


import com.encureit.bhartisurveyandroid.database.BaseDao;
import com.encureit.bhartisurveyandroid.database.TableNames;
import com.encureit.bhartisurveyandroid.models.QuestionValidation;
import com.encureit.bhartisurveyandroid.models.UserDeviceDetails;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao

public interface QuestionValidationDao extends BaseDao<QuestionValidation> {

    @Query("SELECT * FROM " + TableNames.TABLE_QUESTIONS_VALIDATION)
    List<QuestionValidation> getAllFlowableCodes();

    @Query("SELECT COUNT(id) FROM " + TableNames.TABLE_QUESTIONS_VALIDATION)
    int getRowCount();

    @Query("DELETE FROM "  + TableNames.TABLE_QUESTIONS_VALIDATION)
    void nukeTable();

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_VALIDATION+" SET QuestionValidation_ID = :QuestionValidation_ID WHERE id =:id")
     void update_QuestionValidation_ID(int id, String QuestionValidation_ID);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_VALIDATION+" SET Validation_ID = :Validation_ID WHERE id =:id")
     void update_Validation_ID(int id, String Validation_ID);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_VALIDATION+" SET SurveyQuestion_ID = :SurveyQuestion_ID WHERE id =:id")
     void update_SurveyQuestion_ID(int id, String SurveyQuestion_ID);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_VALIDATION+" SET SurveySection_ID = :SurveySection_ID WHERE id =:id")
     void update_SurveySection_ID(int id, String SurveySection_ID);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_VALIDATION+" SET CreatedBy = :CreatedBy WHERE id =:id")
     void update_CreatedBy(int id, String CreatedBy);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_VALIDATION+" SET CreatedDate = :CreatedDate WHERE id =:id")
     void update_CreatedDate(int id, String CreatedDate);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_VALIDATION+" SET UpdatedBy = :UpdatedBy WHERE id =:id")
     void update_UpdatedBy(int id, String UpdatedBy);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_VALIDATION+" SET UpdatedDate = :UpdatedDate WHERE id =:id")
     void update_UpdatedDate(int id, String UpdatedDate);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_VALIDATION+" SET IsActive = :IsActive WHERE id =:id")
    void update_IsActive(int id, String IsActive);

}
