package com.encureit.samtadoot.database.dao;


import com.encureit.samtadoot.database.BaseDao;
import com.encureit.samtadoot.database.TableNames;
import com.encureit.samtadoot.models.CandidateDetails;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao

public interface CandidateDetailsDao extends BaseDao<CandidateDetails> {

    @Query("SELECT * FROM " + TableNames.TABLE_CANDIDATE_DETAILS)
    List<CandidateDetails> getAllFlowableCodes();

    @Query("SELECT COUNT(id) FROM " + TableNames.TABLE_CANDIDATE_DETAILS)
    int getRowCount();

    @Query("DELETE FROM "  + TableNames.TABLE_CANDIDATE_DETAILS)
    void nukeTable();

    @Query("DELETE FROM "  + TableNames.TABLE_CANDIDATE_DETAILS+" WHERE survey_que_id=:question_id")
    void deleteCandidateByQuestionId(String question_id);

    @Query("DELETE FROM "  + TableNames.TABLE_CANDIDATE_DETAILS+" WHERE survey_section_id=:section_id")
    void deleteCandidateBySectionId(String section_id);

    @Query("SELECT * FROM " + TableNames.TABLE_CANDIDATE_DETAILS+" WHERE FormID =:FormID")
    List<CandidateDetails> getAllDetailsByForm(String FormID);

    @Query("SELECT * FROM " + TableNames.TABLE_CANDIDATE_DETAILS+" WHERE FormID =:FormID GROUP BY survey_section_id")
    List<CandidateDetails> getAllSectionDetailsByForm(String FormID);

    @Query("SELECT * FROM " + TableNames.TABLE_CANDIDATE_DETAILS+" WHERE survey_que_option_id =:survey_que_option_id AND FormID=:formId AND index_if_linked_question=:linked_id")
    CandidateDetails getCandidateDetailsByQuestionOptionId(String survey_que_option_id,String formId,int linked_id);

    @Query("SELECT * FROM " + TableNames.TABLE_CANDIDATE_DETAILS+" WHERE survey_que_id =:survey_que_id AND FormID =:FormId AND index_if_linked_question=:linked_id")
    CandidateDetails getCandidateDetailsByQuestionIdFormId(String survey_que_id,String FormId,int linked_id);

    @Query("SELECT * FROM " + TableNames.TABLE_CANDIDATE_DETAILS+" WHERE survey_que_id =:survey_que_id")
    List<CandidateDetails> getAllDetailsByQuestionId(String survey_que_id);

    @Query("SELECT * FROM " + TableNames.TABLE_CANDIDATE_DETAILS+" WHERE survey_que_id =:survey_que_id AND FormID=:FormId")
    List<CandidateDetails> getAllDetailsByQuestionIdFormId(String survey_que_id,String FormId);

    @Query("SELECT * FROM " + TableNames.TABLE_CANDIDATE_DETAILS+" WHERE survey_section_id =:survey_section_id AND FormID =:formId")
    List<CandidateDetails> getAllDetailsBySectionIdFormId(String survey_section_id,String formId);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET survey_master_id =:survey_master_id WHERE id =:id")
    void update_survey_master_id(int id,String survey_master_id);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET survey_section_id =:survey_section_id WHERE id =:id")
    void update_survey_section_id(int id,String survey_section_id);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET survey_que_id =:survey_que_id WHERE id =:id")
    void update_survey_que_id(int id,String survey_que_id);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET survey_que_option_id =:survey_que_option_id WHERE id =:id")
    void update_survey_que_option_id(int id,String survey_que_option_id);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET survey_que_values =:survey_que_values WHERE id =:id")
    void update_survey_que_values(int id,String survey_que_values);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET FormID =:FormID WHERE id =:id")
    void update_FormID(int id,String FormID);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET Current_Form_Status =:Current_Form_Status WHERE id =:id")
    void update_Current_Form_Status(int id,String Current_Form_Status);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET age_value =:age_value WHERE id =:id")
    void update_age_value(int id,String age_value);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET Survey_StartDate =:Survey_StartDate WHERE id =:id")
    void update_Survey_StartDate(int id,String Survey_StartDate);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET Survey_EndDate =:Survey_EndDate WHERE id =:id")
    void update_Survey_EndDate(int id,String Survey_EndDate);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET created_by =:created_by WHERE id =:id")
    void update_created_by(int id,String created_by);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET Latitude =:Latitude WHERE id =:id")
    void update_Latitude(int id,String Latitude);

    @Query("UPDATE " + TableNames.TABLE_CANDIDATE_DETAILS+" SET Longitude =:Longitude WHERE id =:id")
    void update_Longitude(int id,String Longitude);


}
