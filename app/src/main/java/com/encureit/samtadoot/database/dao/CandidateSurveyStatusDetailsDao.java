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

    @Query("SELECT * FROM " + TableNames.TABLE_CANDIDATE_SURVEY_DETAILS+" WHERE form_unique_id=:FormId")
    List<CandidateSurveyStatusDetails> getAllCandidatesInSection(String FormId);

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

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_SURVEY_DETAILS+" SET survey_section_id =:survey_section_id AND FormID =:FormID AND survey_status =:survey_status AND start_date =:start_date AND last_updated_date =:last_updated_date AND end_date =:end_date AND form_unique_id =:form_unique_id WHERE id =:id")
    void update_all(int id, String survey_section_id,String FormID,String survey_status,String start_date,String last_updated_date,String end_date,String form_unique_id);
}
