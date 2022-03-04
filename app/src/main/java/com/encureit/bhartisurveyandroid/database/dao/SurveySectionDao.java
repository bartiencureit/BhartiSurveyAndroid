package com.encureit.bhartisurveyandroid.database.dao;


import com.encureit.bhartisurveyandroid.database.BaseDao;
import com.encureit.bhartisurveyandroid.database.TableNames;
import com.encureit.bhartisurveyandroid.models.SurveySection;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao

public interface SurveySectionDao extends BaseDao<SurveySection> {

    @Query("SELECT * FROM " + TableNames.TABLE_SURVEY_SECTIONS)
    List<SurveySection> getAllFlowableCodes();

    @Query("SELECT COUNT(id) FROM " + TableNames.TABLE_SURVEY_SECTIONS)
    int getRowCount();

    @Query("DELETE FROM "  + TableNames.TABLE_SURVEY_SECTIONS)
    void nukeTable();

//    @Query("UPDATE " + TableNames.TABLE_SURVEY_SECTIONS+" SET device_info_id = :device_info_id WHERE id =:id")
//    void update_device_info_id(int id, String device_info_id);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_SECTIONS+" SET SurveySection_ID =:SurveySection_ID WHERE id =:id")
    void update_SurveySection_ID(int id, String SurveySection_ID);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_SECTIONS+" SET SurveyMaster_ID =:SurveyMaster_ID WHERE id =:id")
    void update_SurveyMaster_ID(int id, String SurveyMaster_ID);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_SECTIONS+" SET SectionNumber =:SectionNumber WHERE id =:id")
    void update_SectionNumber(int id, String SectionNumber);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_SECTIONS+" SET SectionName =:SectionName WHERE id =:id")
    void update_SectionName(int id, String SectionName);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_SECTIONS+" SET SectionDescription =:SectionDescription WHERE id =:id")
    void update_SectionDescription(int id, String SectionDescription);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_SECTIONS+" SET CreatedBy =:CreatedBy WHERE id =:id")
    void update_CreatedBy(int id, String CreatedBy);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_SECTIONS+" SET CreatedDate =:CreatedDate WHERE id =:id")
    void update_CreatedDate(int id, String CreatedDate);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_SECTIONS+" SET ModifiedBy =:ModifiedBy WHERE id =:id")
    void update_ModifiedBy(int id, String ModifiedBy);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_SECTIONS+" SET ModifiedDate =:ModifiedDate WHERE id =:id")
    void update_ModifiedDate(int id, String ModifiedDate);

    @Query("UPDATE " + TableNames.TABLE_SURVEY_SECTIONS+" SET IsActive =:IsActive WHERE id =:id")
    void update_IsActive(int id, String IsActive);

}
