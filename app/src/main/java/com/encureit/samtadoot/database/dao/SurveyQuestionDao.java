package com.encureit.samtadoot.database.dao;


import com.encureit.samtadoot.database.BaseDao;
import com.encureit.samtadoot.database.TableNames;
import com.encureit.samtadoot.models.SurveyQuestion;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao

public interface SurveyQuestionDao extends BaseDao<SurveyQuestion> {

    @Query("SELECT * FROM " + TableNames.TABLE_SURVEY_QUESTIONS)
    List<SurveyQuestion> getAllFlowableCodes();

    @Query("SELECT COUNT(id) FROM " + TableNames.TABLE_SURVEY_QUESTIONS)
    int getRowCount();

    @Query("DELETE FROM "  + TableNames.TABLE_SURVEY_QUESTIONS)
    void nukeTable();

    @Query("SELECT * FROM " + TableNames.TABLE_SURVEY_QUESTIONS+" WHERE SurveySection_ID =:section_id")
    List<SurveyQuestion> getAllQuestionsBySection(String section_id);

    @Query("SELECT * FROM " + TableNames.TABLE_SURVEY_QUESTIONS+" WHERE ParentQuestionId =:Ques_id")
    List<SurveyQuestion> getAllChildQuestion(String Ques_id);

    @Query("SELECT * FROM " + TableNames.TABLE_SURVEY_QUESTIONS+" WHERE Questions =:Questions")
    SurveyQuestion getQuestionFromText(String Questions);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET SurveyQuestion_ID =:SurveyQuestion_ID WHERE id =:id")
     void update_SurveyQuestion_ID(int id, String SurveyQuestion_ID);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET SurveySection_ID =:SurveySection_ID WHERE id =:id")
     void update_SurveySection_ID(int id, String SurveySection_ID);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET QuestionTypeID =:QuestionTypeID WHERE id =:id")
     void update_QuestionTypeID(int id, String QuestionTypeID);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET Autopopulate =:Autopopulate WHERE id =:id")
     void update_Autopopulate(int id, String Autopopulate);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET LabelHeader =:LabelHeader WHERE id =:id")
     void update_LabelHeader(int id, String LabelHeader);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET Required =:Required WHERE id =:id")
     void update_Required(int id, String Required);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET QuestionSequence =:QuestionSequence WHERE id =:id")
     void update_QuestionSequence(int id, String QuestionSequence);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET ValidationType =:ValidationType WHERE id =:id")
     void update_ValidationType(int id, String ValidationType);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET IsValidation =:IsValidation WHERE id =:id")
     void update_IsValidation(int id, String IsValidation);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET IsLinkedQuestionId =:IsLinkedQuestionId WHERE id =:id")
     void update_IsLinkedQuestionId(int id, String IsLinkedQuestionId);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET ParentQuestionId =:ParentQuestionId WHERE id =:id")
     void update_ParentQuestionId(int id, String ParentQuestionId);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET OptionDependent =:OptionDependent WHERE id =:id")
     void update_OptionDependent(int id, String OptionDependent);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET Questions =:Questions WHERE id =:id")
     void update_Questions(int id, String Questions);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET CreatedBy =:CreatedBy WHERE id =:id")
     void update_CreatedBy(int id, String CreatedBy);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET CreatedDate =:CreatedDate WHERE id =:id")
     void update_CreatedDate(int id, String CreatedDate);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET UpdatedBy =:UpdatedBy WHERE id =:id")
     void update_UpdatedBy(int id, String UpdatedBy);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET UpdatedDate =:UpdatedDate WHERE id =:id")
     void update_UpdatedDate(int id, String UpdatedDate);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET is_section =:is_section WHERE id =:id")
     void update_is_section(int id, String is_section);

     @Query("UPDATE " + TableNames.TABLE_SURVEY_QUESTIONS+" SET IsActive =:IsActive WHERE id =:id")
    void update_IsActive(int id, String IsActive);

}
