package com.encureit.samtadoot.database.dao;


import com.encureit.samtadoot.database.BaseDao;
import com.encureit.samtadoot.database.TableNames;
import com.encureit.samtadoot.models.SurveyType;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao

public interface SurveyTypeDao extends BaseDao<SurveyType> {

    @Query("SELECT * FROM " + TableNames.TABLE_SURVEY_MASTER)
    List<SurveyType> getAllFlowableCodes();

    @Query("SELECT COUNT(id) FROM " + TableNames.TABLE_SURVEY_MASTER)
    int getRowCount();

    @Query("DELETE FROM "  + TableNames.TABLE_SURVEY_MASTER)
    void nukeTable();

    @Query("UPDATE " + TableNames.TABLE_SURVEY_MASTER+" SET Form_unique_id = :Form_unique_id WHERE id =:id")
    void update_Form_unique_id(int id, String Form_unique_id);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_MASTER+" SET Form_no = :Form_no WHERE id =:id")
    void update_Form_no(int id, String Form_no);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_MASTER+" SET form_type = :form_type WHERE id =:id")
    void update_form_type(int id, String form_type);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_MASTER+" SET form_description = :form_description WHERE id =:id")
    void update_form_description(int id, String form_description);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_MASTER+" SET isActive = :isActive WHERE id =:id")
    void update_isActive(int id, String isActive);

}
