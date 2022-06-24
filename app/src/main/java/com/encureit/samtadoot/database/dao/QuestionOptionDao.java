package com.encureit.samtadoot.database.dao;


import com.encureit.samtadoot.database.BaseDao;
import com.encureit.samtadoot.database.TableNames;
import com.encureit.samtadoot.models.QuestionOption;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao

public interface QuestionOptionDao extends BaseDao<QuestionOption> {

    @Query("SELECT * FROM " + TableNames.TABLE_QUESTIONS_OPTION)
    List<QuestionOption> getAllFlowableCodes();

    @Query("SELECT COUNT(id) FROM " + TableNames.TABLE_QUESTIONS_OPTION)
    int getRowCount();

    @Query("DELETE FROM "  + TableNames.TABLE_QUESTIONS_OPTION)
    void nukeTable();

    @Query("SELECT * FROM " + TableNames.TABLE_QUESTIONS_OPTION+" WHERE SurveyQuestion_ID =:SurveyQuestion_ID")
    List<QuestionOption> getAllQuestionOption(String SurveyQuestion_ID);

    @Query("SELECT * FROM " + TableNames.TABLE_QUESTIONS_OPTION+" WHERE QNA_Values =:QNA_Values")
    QuestionOption getQuestionOptionByText(String QNA_Values);

    @Query("SELECT * FROM " + TableNames.TABLE_QUESTIONS_OPTION+" WHERE QNAOption_ID =:QNAOption_ID")
    QuestionOption getQuestionOptionByID(String QNAOption_ID);

    @Query("SELECT * FROM " + TableNames.TABLE_QUESTIONS_OPTION+" WHERE SurveyQuestion_ID =:SurveyQuestion_ID AND childQuestionId LIKE :childQuestionId")
    QuestionOption getChildQuesOption(String SurveyQuestion_ID,String childQuestionId);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_OPTION+" SET QNAOption_ID = :QNAOption_ID WHERE id=:id")
     void update_QNAOption_ID(int id, String QNAOption_ID);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_OPTION+" SET SurveyQuestion_ID = :SurveyQuestion_ID WHERE id=:id")
     void update_SurveyQuestion_ID(int id, String SurveyQuestion_ID);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_OPTION+" SET QNA_Values = :QNA_Values WHERE id=:id")
     void update_QNA_Values(int id, String QNA_Values);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_OPTION+" SET DisplayTypeCount = :DisplayTypeCount WHERE id=:id")
     void update_DisplayTypeCount(int id, String DisplayTypeCount);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_OPTION+" SET Suffix = :Suffix WHERE id=:id")
     void update_Suffix(int id, String Suffix);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_OPTION+" SET childQuestionId = :childQuestionId WHERE id=:id")
     void update_childQuestionId(int id, String childQuestionId);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_OPTION+" SET CreatedBy = :CreatedBy WHERE id=:id")
     void update_CreatedBy(int id, String CreatedBy);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_OPTION+" SET CreatedDate = :CreatedDate WHERE id=:id")
     void update_CreatedDate(int id, String CreatedDate);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_OPTION+" SET UpdatedBy = :UpdatedBy WHERE id=:id")
     void update_UpdatedBy(int id, String UpdatedBy);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_OPTION+" SET UpdatedDate = :UpdatedDate WHERE id=:id")
     void update_UpdatedDate(int id, String UpdatedDate);

     @Query("UPDATE " + TableNames.TABLE_QUESTIONS_OPTION+" SET IsActive = :IsActive WHERE id=:id")
    void update_IsActive(int id, String IsActive);

}
