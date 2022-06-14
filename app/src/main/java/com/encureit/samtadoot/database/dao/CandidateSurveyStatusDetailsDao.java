package com.encureit.samtadoot.database.dao;


import com.encureit.samtadoot.database.BaseDao;
import com.encureit.samtadoot.database.TableNames;
import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao

public interface CandidateSurveyStatusDetailsDao extends BaseDao<CandidateSurveyStatusDetails> {

    @Query("SELECT * FROM " + TableNames.TABLE_CANDIDATE_SURVEY_DETAILS)
    List<CandidateSurveyStatusDetails> getAllFlowableCodes();

    @Query("SELECT COUNT(id) FROM " + TableNames.TABLE_CANDIDATE_SURVEY_DETAILS)
    int getRowCount();

    @Query("DELETE FROM "  + TableNames.TABLE_CANDIDATE_SURVEY_DETAILS)
    void nukeTable();

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_SURVEY_DETAILS+" SET survey_section_id =:survey_section_id WHERE id =:id")
    void update_survey_section_id(int id, String survey_section_id);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_SURVEY_DETAILS+" SET FormID =:FormID WHERE id =:id")
    void update_FormID(int id, String FormID);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_SURVEY_DETAILS+" SET survey_status =:survey_status WHERE id =:id")
    void update_survey_status(int id, String survey_status);

    @Query("SELECT * FROM " + TableNames.TABLE_CANDIDATE_SURVEY_DETAILS+" WHERE survey_section_id=:survey_section_id")
    CandidateSurveyStatusDetails getCandidateDetails(String survey_section_id);
}
