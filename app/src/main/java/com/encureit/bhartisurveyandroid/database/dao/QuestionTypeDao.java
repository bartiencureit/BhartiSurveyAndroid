package com.encureit.bhartisurveyandroid.database.dao;


import com.encureit.bhartisurveyandroid.database.BaseDao;
import com.encureit.bhartisurveyandroid.database.TableNames;
import com.encureit.bhartisurveyandroid.models.QuestionType;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao

public interface QuestionTypeDao extends BaseDao<QuestionType> {

    @Query("SELECT * FROM " + TableNames.TABLE_QUESTIONS_TYPES)
    List<QuestionType> getAllFlowableCodes();

    @Query("SELECT COUNT(id) FROM " + TableNames.TABLE_QUESTIONS_TYPES)
    int getRowCount();

    @Query("DELETE FROM "  + TableNames.TABLE_QUESTIONS_TYPES)
    void nukeTable();

    @Query("UPDATE " + TableNames.TABLE_QUESTIONS_TYPES+" SET QuestionTypeID = :QuestionTypeID WHERE id =:id")
    void update_QuestionTypeID(int id, String QuestionTypeID);

    @Query("UPDATE " + TableNames.TABLE_QUESTIONS_TYPES+" SET QuestionTypes = :QuestionTypes WHERE id =:id")
    void update_QuestionTypes(int id, String QuestionTypes);

    @Query("UPDATE " + TableNames.TABLE_QUESTIONS_TYPES+" SET CreatedBy = :CreatedBy WHERE id =:id")
    void update_CreatedBy(int id, String CreatedBy);

    @Query("UPDATE " + TableNames.TABLE_QUESTIONS_TYPES+" SET CreatedDate = :CreatedDate WHERE id =:id")
    void update_CreatedDate(int id, String CreatedDate);

    @Query("UPDATE " + TableNames.TABLE_QUESTIONS_TYPES+" SET UpdatedBy = :UpdatedBy WHERE id =:id")
    void update_UpdatedBy(int id, String UpdatedBy);

    @Query("UPDATE " + TableNames.TABLE_QUESTIONS_TYPES+" SET UpdatedDate = :UpdatedDate WHERE id =:id")
    void update_UpdatedDate(int id, String UpdatedDate);

    @Query("UPDATE " + TableNames.TABLE_QUESTIONS_TYPES+" SET IsActive = :IsActive WHERE id =:id")
    void update_IsActive(int id, String IsActive);

}
