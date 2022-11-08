package com.encureit.samtadoot.database;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.encureit.samtadoot.database.dao.AssignDetailsDao;
import com.encureit.samtadoot.database.dao.CandidateDetailsDao;
import com.encureit.samtadoot.database.dao.CandidateSurveyStatusDetailsDao;
import com.encureit.samtadoot.database.dao.OtherValuesDao;
import com.encureit.samtadoot.database.dao.QuestionOptionDao;
import com.encureit.samtadoot.database.dao.QuestionTypeDao;
import com.encureit.samtadoot.database.dao.QuestionValidationDao;
import com.encureit.samtadoot.database.dao.SurveyQuestionDao;
import com.encureit.samtadoot.database.dao.SurveySectionDao;
import com.encureit.samtadoot.database.dao.SurveyTypeDao;
import com.encureit.samtadoot.database.dao.UserDeviceDetailsDao;
import com.encureit.samtadoot.databinding.SingleLinkedChildDataBinding;
import com.encureit.samtadoot.models.AssignDetails;
import com.encureit.samtadoot.models.CandidateDetails;
import com.encureit.samtadoot.models.CandidateSurveyStatusDetails;
import com.encureit.samtadoot.models.OtherValues;
import com.encureit.samtadoot.models.QuestionOption;
import com.encureit.samtadoot.models.QuestionType;
import com.encureit.samtadoot.models.QuestionValidation;
import com.encureit.samtadoot.models.SurveyQuestion;
import com.encureit.samtadoot.models.SurveyQuestionWithData;
import com.encureit.samtadoot.models.SurveySection;
import com.encureit.samtadoot.models.SurveyType;
import com.encureit.samtadoot.models.UserDeviceDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    private CandidateDetailsDao mCandidateDetailsDao;
    private CandidateSurveyStatusDetailsDao mCandidateSurveyStatusDetailsDao;
    private OtherValuesDao mOtherValuesDao;

    private DatabaseUtil() {
        setSurveyTypeDao(UniqaDatabase.on().surveyTypeDao());
        setUserDeviceDetailsDao(UniqaDatabase.on().userDeviceDetailsDao());
        setQuestionOptionDao(UniqaDatabase.on().questionOptionDao());
        setQuestionTypeDao(UniqaDatabase.on().questionTypeDao());
        setQuestionValidationDao(UniqaDatabase.on().questionValidationDao());
        setSurveyQuestionDao(UniqaDatabase.on().surveyQuestionDao());
        setSurveySectionDao(UniqaDatabase.on().surveySectionDao());
        setAssignDetailsDao(UniqaDatabase.on().assignDetailsDao());
        setCandidateDetailsDao(UniqaDatabase.on().candidateDetailsDao());
        setCandidateSurveyStatusDetailsDao(UniqaDatabase.on().candidateSurveyStatusDetailsDao());
        setOtherValuesDao(UniqaDatabase.on().otherValuesDao());
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

    public CandidateDetailsDao getCandidateDetailsDao() {
        return mCandidateDetailsDao;
    }

    public void setCandidateDetailsDao(CandidateDetailsDao mCandidateDetailsDao) {
        this.mCandidateDetailsDao = mCandidateDetailsDao;
    }

    public CandidateSurveyStatusDetailsDao getCandidateSurveyStatusDetailsDao() {
        return mCandidateSurveyStatusDetailsDao;
    }

    public void setCandidateSurveyStatusDetailsDao(CandidateSurveyStatusDetailsDao mCandidateSurveyStatusDetailsDao) {
        this.mCandidateSurveyStatusDetailsDao = mCandidateSurveyStatusDetailsDao;
    }

    public OtherValuesDao getOtherValuesDao() {
        return mOtherValuesDao;
    }

    public void setOtherValuesDao(OtherValuesDao mOtherValuesDao) {
        this.mOtherValuesDao = mOtherValuesDao;
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
        getSurveyTypeDao().update_Form_unique_id(id, Form_unique_id);
    }

    public void update_Form_no(int id, String Form_no) {
        getSurveyTypeDao().update_Form_no(id, Form_no);
    }

    public void update_form_type(int id, String form_type) {
        getSurveyTypeDao().update_form_type(id, form_type);
    }

    public void update_form_description(int id, String form_description) {
        getSurveyTypeDao().update_form_description(id, form_description);
    }

    public void update_isActive(int id, String isActive) {
        getSurveyTypeDao().update_isActive(id, isActive);
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

    public long[] insertAllCandidateDetails(List<CandidateDetails> candidateDetails) {
        return getCandidateDetailsDao().insertBulk(candidateDetails);
    }

    public long[] insertAllCandidateSurveyStatusDetails(List<CandidateSurveyStatusDetails> candidateSurveyStatusDetails) {
        return getCandidateSurveyStatusDetailsDao().insertBulk(candidateSurveyStatusDetails);
    }

    public long[] insertAllOtherValues(List<OtherValues> otherValues) {
        return getOtherValuesDao().insertBulk(otherValues);
    }

    /**
     * @param sectionId
     * @return
     * @date 14-3-2022
     * Get All Questions In Case Of Edit Mode
     */
    public List<SurveyQuestionWithData> getAllQuestionsInEdit(String sectionId, String FormId) {
        List<SurveyQuestionWithData> surveyQuestionsWithData = new ArrayList<>();
        //get all questions by section id
        List<SurveyQuestion> questions = getSurveyQuestionDao().getAllQuestionsBySection(sectionId);

        //render through questions
        for (int i = 0; i < questions.size(); i++) {
            SurveyQuestion surveyQuestion = questions.get(i);
            //check if question is parent add to list otherwise not
            if (surveyQuestion.getParentQuestionId().equalsIgnoreCase("0")) {
                SurveyQuestionWithData surveyQuestionWithData = new SurveyQuestionWithData();
                surveyQuestionWithData.setSurveyQuestion_ID(surveyQuestion.getSurveyQuestion_ID());
                surveyQuestionWithData.setSurveySection_ID(surveyQuestion.getSurveySection_ID());
                surveyQuestionWithData.setQuestionTypeID(surveyQuestion.getQuestionTypeID());
                surveyQuestionWithData.setAutopopulate(surveyQuestion.getAutopopulate());
                surveyQuestionWithData.setLabelHeader(surveyQuestion.getLabelHeader());
                surveyQuestionWithData.setRequired(surveyQuestion.getRequired());
                surveyQuestionWithData.setQuestionSequence(surveyQuestion.getQuestionSequence());
                surveyQuestionWithData.setValidationType(surveyQuestion.getValidationType());
                surveyQuestionWithData.setIsValidation(surveyQuestion.getIsValidation());
                surveyQuestionWithData.setIsLinkedQuestionId(surveyQuestion.getIsLinkedQuestionId());
                surveyQuestionWithData.setParentQuestionId(surveyQuestion.getParentQuestionId());
                surveyQuestionWithData.setOptionDependent(surveyQuestion.getOptionDependent());
                surveyQuestionWithData.setQuestions(surveyQuestion.getQuestions());
                surveyQuestionWithData.setMin_length(surveyQuestion.getMin_length());
                surveyQuestionWithData.setMax_length(surveyQuestion.getMax_length());
                surveyQuestionWithData.setCreatedBy(surveyQuestion.getCreatedBy());
                surveyQuestionWithData.setCreatedDate(surveyQuestion.getCreatedDate());
                surveyQuestionWithData.setUpdatedBy(surveyQuestion.getUpdatedBy());
                surveyQuestionWithData.setUpdatedDate(surveyQuestion.getUpdatedDate());
                surveyQuestionWithData.setIs_section(surveyQuestion.getIs_section());
                surveyQuestionWithData.setIsActive(surveyQuestion.getIsActive());
                List<CandidateDetails> candidateDetailsList = getCandidateDetailsDao().getAllDetailsByQuestionIdFormId(surveyQuestion.getSurveyQuestion_ID(), FormId);
                if (candidateDetailsList.size() > 0) {
                    surveyQuestionWithData.setValue(candidateDetailsList.get(0).getSurvey_que_values());
                }

                //get question type of given question
                QuestionType questionType = getQuestionTypeOfQuestion(surveyQuestion);
                surveyQuestionWithData.setQuestionType(questionType);

                //get Question Options of given question
                List<QuestionOption> questionOptions = getQuestionOptionDao().getAllQuestionOption(surveyQuestion.getSurveyQuestion_ID());
                surveyQuestionWithData.setQuestionOptions(questionOptions);

                //get all child question and set to parent question
                List<SurveyQuestionWithData> childQuestions = getAllChildQuestionsInEdit(surveyQuestion.getSurveyQuestion_ID(), FormId);
                surveyQuestionWithData.setChildQuestions(childQuestions);

                //get all linked question and set to parent question
                List<HashMap<Integer, List<SurveyQuestionWithData>>> linkedQuestions = getAllLinkedQuestionsInEdit(surveyQuestion.getSurveyQuestion_ID(), FormId);
                if (linkedQuestions.size() > 0) {
                    surveyQuestionWithData.setLinkedQuestionInEdit(linkedQuestions);
                } else {
                    //get all linked question and set to parent question
                    List<SurveyQuestionWithData> simple_linkedQuestions = getAllLinkedQuestions(surveyQuestion.getSurveyQuestion_ID());
                    surveyQuestionWithData.setLinkedQuestions(simple_linkedQuestions);
                }

                surveyQuestionsWithData.add(surveyQuestionWithData);
            }

        }

        return surveyQuestionsWithData;
    }

    /**
     * get all child questions
     * if child question is linked = true then it should be added in linked question list
     * hence it's skipped here
     *
     * @param questionId
     * @return
     */
    public List<SurveyQuestionWithData> getAllChildQuestionsInEdit(String questionId, String FormId) {
        List<SurveyQuestionWithData> childQuestions = new ArrayList<>();
        List<SurveyQuestion> allChildQuestions = getSurveyQuestionDao().getAllChildQuestion(questionId);
        for (int i = 0; i < allChildQuestions.size(); i++) {
            SurveyQuestion childSurveyQuestion = allChildQuestions.get(i);
            QuestionOption childQesOption = getQuestionOptionDao().getChildQuesOption(questionId,'%'+childSurveyQuestion.getSurveyQuestion_ID()+'%');
            if (childQesOption == null) {
                if (!(childSurveyQuestion.getIsLinkedQuestionId() == null || childSurveyQuestion.getIsLinkedQuestionId().equalsIgnoreCase("true"))) {
                    SurveyQuestionWithData surveyQuestionWithData = new SurveyQuestionWithData();
                    surveyQuestionWithData.setSurveyQuestion_ID(childSurveyQuestion.getSurveyQuestion_ID());
                    surveyQuestionWithData.setSurveySection_ID(childSurveyQuestion.getSurveySection_ID());
                    surveyQuestionWithData.setQuestionTypeID(childSurveyQuestion.getQuestionTypeID());
                    surveyQuestionWithData.setAutopopulate(childSurveyQuestion.getAutopopulate());
                    surveyQuestionWithData.setLabelHeader(childSurveyQuestion.getLabelHeader());
                    surveyQuestionWithData.setRequired(childSurveyQuestion.getRequired());
                    surveyQuestionWithData.setQuestionSequence(childSurveyQuestion.getQuestionSequence());
                    surveyQuestionWithData.setValidationType(childSurveyQuestion.getValidationType());
                    surveyQuestionWithData.setIsValidation(childSurveyQuestion.getIsValidation());
                    surveyQuestionWithData.setIsLinkedQuestionId(childSurveyQuestion.getIsLinkedQuestionId());
                    surveyQuestionWithData.setParentQuestionId(childSurveyQuestion.getParentQuestionId());
                    surveyQuestionWithData.setOptionDependent(childSurveyQuestion.getOptionDependent());
                    surveyQuestionWithData.setQuestions(childSurveyQuestion.getQuestions());
                    surveyQuestionWithData.setMin_length(childSurveyQuestion.getMin_length());
                    surveyQuestionWithData.setMax_length(childSurveyQuestion.getMax_length());
                    surveyQuestionWithData.setCreatedBy(childSurveyQuestion.getCreatedBy());
                    surveyQuestionWithData.setCreatedDate(childSurveyQuestion.getCreatedDate());
                    surveyQuestionWithData.setUpdatedBy(childSurveyQuestion.getUpdatedBy());
                    surveyQuestionWithData.setUpdatedDate(childSurveyQuestion.getUpdatedDate());
                    surveyQuestionWithData.setIs_section(childSurveyQuestion.getIs_section());
                    surveyQuestionWithData.setIsActive(childSurveyQuestion.getIsActive());

                    List<CandidateDetails> candidateDetailsList = getCandidateDetailsDao().getAllDetailsByQuestionIdFormId(childSurveyQuestion.getSurveyQuestion_ID(), FormId);
                    if (candidateDetailsList.size() > 0) {
                        surveyQuestionWithData.setValue(candidateDetailsList.get(0).getSurvey_que_values());
                    }

                    //get question type of given child question
                    QuestionType questionType = getQuestionTypeOfQuestion(childSurveyQuestion);
                    surveyQuestionWithData.setQuestionType(questionType);

                    //get Question Options of given child question
                    List<QuestionOption> questionOptions = getQuestionOptionDao().getAllQuestionOption(childSurveyQuestion.getSurveyQuestion_ID());
                    surveyQuestionWithData.setQuestionOptions(questionOptions);

                    //get all linked question and set to parent question
                    List<HashMap<Integer, List<SurveyQuestionWithData>>> linkedQuestions = getAllLinkedQuestionsInEdit(childSurveyQuestion.getSurveyQuestion_ID(), FormId);
                    if (linkedQuestions.size() > 0) {
                        surveyQuestionWithData.setLinkedQuestionInEdit(linkedQuestions);
                    } else {
                        //get all linked question and set to parent question
                        List<SurveyQuestionWithData> simple_linkedQuestions = getAllLinkedQuestions(childSurveyQuestion.getSurveyQuestion_ID());
                        surveyQuestionWithData.setLinkedQuestions(simple_linkedQuestions);
                    }

                    childQuestions.add(surveyQuestionWithData);
                }
            }
        }
        return childQuestions;
    }

    /**
     * get all linked questions
     *
     * @param questionId
     * @return
     */
    public List<HashMap<Integer, List<SurveyQuestionWithData>>> getAllLinkedQuestionsInEdit(String questionId, String FormId) {
        List<HashMap<Integer, List<SurveyQuestionWithData>>> allLinkedQuestions = new ArrayList<>();
        int tot_candidates = getParentQuestionCandidateCount(questionId, FormId);
        List<SurveyQuestion> linkedQuestions = getSurveyQuestionDao().getAllLinkedQuestion(questionId, "true");
        for (int i = 0; i < tot_candidates; i++) {
            SurveyQuestion childSurveyQuestion = linkedQuestions.get(i);
            QuestionOption childQesOption = getQuestionOptionDao().getChildQuesOption(questionId,'%'+childSurveyQuestion.getSurveyQuestion_ID()+'%');
            if (childQesOption == null) {
                List<SurveyQuestionWithData> linkedQuestionListForSingleCandidate = new ArrayList<>();
                for (int j = 0; j < linkedQuestions.size(); j++) {
                    CandidateDetails candidateDetails = getCandidateById(linkedQuestions.get(j).getSurveyQuestion_ID(), i+1, FormId);
                    SurveyQuestionWithData surveyQuestionWithData = createDataWithCandidate(linkedQuestions.get(j), candidateDetails);
                    linkedQuestionListForSingleCandidate.add(surveyQuestionWithData);
                }
                HashMap<Integer, List<SurveyQuestionWithData>> map = new HashMap<>();
                map.put(i, linkedQuestionListForSingleCandidate);
                allLinkedQuestions.add(map);
            }
        }

        return allLinkedQuestions;
    }

    /**
     * @param Question_Id
     * @param index
     * @return
     * @date 14-03-2022
     * Get Candidate by question id
     */
    public CandidateDetails getCandidateById(String Question_Id, int index, String FormId) {
        List<CandidateDetails> candidateDetailsList = getCandidateDetailsDao().getAllDetailsByQuestionIdFormId(Question_Id, FormId);
        CandidateDetails candidateDetails = null;
        for (int i = 0; i < candidateDetailsList.size(); i++) {
            if (candidateDetailsList.get(i).getIndex_if_linked_question() == index) {
                candidateDetails = candidateDetailsList.get(i);
            }
        }
        return candidateDetails;
    }

    /**
     * @param Parent_Question_Id
     * @return
     * @date 14-03-2022
     * Get candidate count for parent question
     */
    private int getParentQuestionCandidateCount(String Parent_Question_Id, String FormId) {
        List<SurveyQuestion> linkedQuestion = getSurveyQuestionDao().getAllLinkedQuestion(Parent_Question_Id, "true");
        if (linkedQuestion.size() > 0) {
            return getCandidateDetailsDao().getAllDetailsByQuestionIdFormId(linkedQuestion.get(0).getSurveyQuestion_ID(), FormId).size();
        } else {
            return -1;
        }
    }

    /**
     * @param Question_Id
     * @return
     * @date 14-03-2022
     * Get candidate count for question
     */
    private int getQuestionCandidateCount(String Question_Id, String FormId) {
        return getCandidateDetailsDao().getAllDetailsByQuestionIdFormId(Question_Id, FormId).size();
    }

    /**
     * @param childSurveyQuestion
     * @param candidateDetails
     * @return
     * @date 14-03-2022
     * Create linked question in case of there are candidate details more than 1 for a question
     */
    private SurveyQuestionWithData createDataWithCandidate(SurveyQuestion childSurveyQuestion, CandidateDetails candidateDetails) {
        SurveyQuestionWithData surveyQuestionWithData = new SurveyQuestionWithData();
        surveyQuestionWithData.setSurveyQuestion_ID(childSurveyQuestion.getSurveyQuestion_ID());
        surveyQuestionWithData.setSurveySection_ID(childSurveyQuestion.getSurveySection_ID());
        surveyQuestionWithData.setQuestionTypeID(childSurveyQuestion.getQuestionTypeID());
        surveyQuestionWithData.setAutopopulate(childSurveyQuestion.getAutopopulate());
        surveyQuestionWithData.setLabelHeader(childSurveyQuestion.getLabelHeader());
        surveyQuestionWithData.setRequired(childSurveyQuestion.getRequired());
        surveyQuestionWithData.setQuestionSequence(childSurveyQuestion.getQuestionSequence());
        surveyQuestionWithData.setValidationType(childSurveyQuestion.getValidationType());
        surveyQuestionWithData.setIsValidation(childSurveyQuestion.getIsValidation());
        surveyQuestionWithData.setIsLinkedQuestionId(childSurveyQuestion.getIsLinkedQuestionId());
        surveyQuestionWithData.setParentQuestionId(childSurveyQuestion.getParentQuestionId());
        surveyQuestionWithData.setOptionDependent(childSurveyQuestion.getOptionDependent());
        surveyQuestionWithData.setQuestions(childSurveyQuestion.getQuestions());
        surveyQuestionWithData.setMin_length(childSurveyQuestion.getMin_length());
        surveyQuestionWithData.setMax_length(childSurveyQuestion.getMax_length());
        surveyQuestionWithData.setCreatedBy(childSurveyQuestion.getCreatedBy());
        surveyQuestionWithData.setCreatedDate(childSurveyQuestion.getCreatedDate());
        surveyQuestionWithData.setUpdatedBy(childSurveyQuestion.getUpdatedBy());
        surveyQuestionWithData.setUpdatedDate(childSurveyQuestion.getUpdatedDate());
        surveyQuestionWithData.setIs_section(childSurveyQuestion.getIs_section());
        surveyQuestionWithData.setIsActive(childSurveyQuestion.getIsActive());
        if (candidateDetails != null && candidateDetails.getSurvey_que_values() != null) {
            surveyQuestionWithData.setValue(candidateDetails.getSurvey_que_values());
        }
        assert candidateDetails != null;
        surveyQuestionWithData.setLinked_question_id(candidateDetails.getIndex_if_linked_question());
        //get question type of given child question
        QuestionType questionType = getQuestionTypeOfQuestion(childSurveyQuestion);
        surveyQuestionWithData.setQuestionType(questionType);

        //get Question Options of given child question
        List<QuestionOption> questionOptions = getQuestionOptionDao().getAllQuestionOption(childSurveyQuestion.getSurveyQuestion_ID());
        surveyQuestionWithData.setQuestionOptions(questionOptions);

        return surveyQuestionWithData;
    }

    public List<SurveyQuestionWithData> getAllQuestions(String sectionId) {
        List<SurveyQuestionWithData> surveyQuestionsWithData = new ArrayList<>();
        //get all questions by section id
        List<SurveyQuestion> questions = getSurveyQuestionDao().getAllQuestionsBySection(sectionId);

        //render through questions
        for (int i = 0; i < questions.size(); i++) {
            SurveyQuestion surveyQuestion = questions.get(i);
            //check if question is parent add to list otherwise not
            if (surveyQuestion.getParentQuestionId().equalsIgnoreCase("0")) {
                SurveyQuestionWithData surveyQuestionWithData = new SurveyQuestionWithData();
                surveyQuestionWithData.setSurveyQuestion_ID(surveyQuestion.getSurveyQuestion_ID());
                surveyQuestionWithData.setSurveySection_ID(surveyQuestion.getSurveySection_ID());
                surveyQuestionWithData.setQuestionTypeID(surveyQuestion.getQuestionTypeID());
                surveyQuestionWithData.setAutopopulate(surveyQuestion.getAutopopulate());
                surveyQuestionWithData.setLabelHeader(surveyQuestion.getLabelHeader());
                surveyQuestionWithData.setRequired(surveyQuestion.getRequired());
                surveyQuestionWithData.setQuestionSequence(surveyQuestion.getQuestionSequence());
                surveyQuestionWithData.setValidationType(surveyQuestion.getValidationType());
                surveyQuestionWithData.setIsValidation(surveyQuestion.getIsValidation());
                surveyQuestionWithData.setIsLinkedQuestionId(surveyQuestion.getIsLinkedQuestionId());
                surveyQuestionWithData.setParentQuestionId(surveyQuestion.getParentQuestionId());
                surveyQuestionWithData.setOptionDependent(surveyQuestion.getOptionDependent());
                surveyQuestionWithData.setQuestions(surveyQuestion.getQuestions());
                surveyQuestionWithData.setMin_length(surveyQuestion.getMin_length());
                surveyQuestionWithData.setMax_length(surveyQuestion.getMax_length());
                surveyQuestionWithData.setCreatedBy(surveyQuestion.getCreatedBy());
                surveyQuestionWithData.setCreatedDate(surveyQuestion.getCreatedDate());
                surveyQuestionWithData.setUpdatedBy(surveyQuestion.getUpdatedBy());
                surveyQuestionWithData.setUpdatedDate(surveyQuestion.getUpdatedDate());
                surveyQuestionWithData.setIs_section(surveyQuestion.getIs_section());
                surveyQuestionWithData.setIsActive(surveyQuestion.getIsActive());

                //get question type of given question
                QuestionType questionType = getQuestionTypeOfQuestion(surveyQuestion);
                surveyQuestionWithData.setQuestionType(questionType);

                //get Question Options of given question
                List<QuestionOption> questionOptions = getQuestionOptionDao().getAllQuestionOption(surveyQuestion.getSurveyQuestion_ID());
                surveyQuestionWithData.setQuestionOptions(questionOptions);

                //get all child question and set to parent question
                List<SurveyQuestionWithData> childQuestions = getAllChildQuestions(surveyQuestion.getSurveyQuestion_ID());
                surveyQuestionWithData.setChildQuestions(childQuestions);

                //get all linked question and set to parent question
                List<SurveyQuestionWithData> linkedQuestions = getAllLinkedQuestions(surveyQuestion.getSurveyQuestion_ID());
                surveyQuestionWithData.setLinkedQuestions(linkedQuestions);

                surveyQuestionsWithData.add(surveyQuestionWithData);
            }
        }
        return surveyQuestionsWithData;
    }

    /**
     * get all child questions
     * if child question is linked = true then it should be added in linked question list
     * hence it's skipped here
     *
     * @param questionId
     * @return
     */
    public List<SurveyQuestionWithData> getAllChildQuestions(String questionId) {
        List<SurveyQuestionWithData> childQuestions = new ArrayList<>();
        List<SurveyQuestion> allChildQuestions = getSurveyQuestionDao().getAllChildQuestion(questionId);

        for (int i = 0; i < allChildQuestions.size(); i++) {
            SurveyQuestion childSurveyQuestion = allChildQuestions.get(i);
            QuestionOption childQesOption = getQuestionOptionDao().getChildQuesOption(questionId,'%'+childSurveyQuestion.getSurveyQuestion_ID()+'%');
            if (childQesOption == null) {
//               // if (!(childSurveyQuestion.getIsLinkedQuestionId() == null || childSurveyQuestion.getIsLinkedQuestionId().equalsIgnoreCase("true"))) {
                if(childSurveyQuestion.getIsLinkedQuestionId() == null || childSurveyQuestion.getIsLinkedQuestionId().equalsIgnoreCase("false") || childSurveyQuestion.getIsLinkedQuestionId().trim().isEmpty()) {
                    SurveyQuestionWithData surveyQuestionWithData = new SurveyQuestionWithData();
                    surveyQuestionWithData.setSurveyQuestion_ID(childSurveyQuestion.getSurveyQuestion_ID());
                    surveyQuestionWithData.setSurveySection_ID(childSurveyQuestion.getSurveySection_ID());
                    surveyQuestionWithData.setQuestionTypeID(childSurveyQuestion.getQuestionTypeID());
                    surveyQuestionWithData.setAutopopulate(childSurveyQuestion.getAutopopulate());
                    surveyQuestionWithData.setLabelHeader(childSurveyQuestion.getLabelHeader());
                    surveyQuestionWithData.setRequired(childSurveyQuestion.getRequired());
                    surveyQuestionWithData.setQuestionSequence(childSurveyQuestion.getQuestionSequence());
                    surveyQuestionWithData.setValidationType(childSurveyQuestion.getValidationType());
                    surveyQuestionWithData.setIsValidation(childSurveyQuestion.getIsValidation());
                    surveyQuestionWithData.setIsLinkedQuestionId(childSurveyQuestion.getIsLinkedQuestionId());
                    surveyQuestionWithData.setParentQuestionId(childSurveyQuestion.getParentQuestionId());
                    surveyQuestionWithData.setOptionDependent(childSurveyQuestion.getOptionDependent());
                    surveyQuestionWithData.setQuestions(childSurveyQuestion.getQuestions());
                    surveyQuestionWithData.setMin_length(childSurveyQuestion.getMin_length());
                    surveyQuestionWithData.setMax_length(childSurveyQuestion.getMax_length());
                    surveyQuestionWithData.setCreatedBy(childSurveyQuestion.getCreatedBy());
                    surveyQuestionWithData.setCreatedDate(childSurveyQuestion.getCreatedDate());
                    surveyQuestionWithData.setUpdatedBy(childSurveyQuestion.getUpdatedBy());
                    surveyQuestionWithData.setUpdatedDate(childSurveyQuestion.getUpdatedDate());
                    surveyQuestionWithData.setIs_section(childSurveyQuestion.getIs_section());
                    surveyQuestionWithData.setIsActive(childSurveyQuestion.getIsActive());

                    //get question type of given child question
                    QuestionType questionType = getQuestionTypeOfQuestion(childSurveyQuestion);
                    surveyQuestionWithData.setQuestionType(questionType);

                    //get Question Options of given child question
                    List<QuestionOption> questionOptions = getQuestionOptionDao().getAllQuestionOption(childSurveyQuestion.getSurveyQuestion_ID());
                    surveyQuestionWithData.setQuestionOptions(questionOptions);

                    childQuestions.add(surveyQuestionWithData);
                }
            }
        }


        return childQuestions;
    }

    /**
     * get all linked questions
     * @param questionId
     * @return
     */
    public List<SurveyQuestionWithData> getAllLinkedQuestions(String questionId) {
        List<SurveyQuestionWithData> childQuestions = new ArrayList<>();
        List<SurveyQuestion> allChildQuestions = getSurveyQuestionDao().getAllChildQuestion(questionId);

        for (int i = 0; i < allChildQuestions.size(); i++) {
            SurveyQuestion childSurveyQuestion = allChildQuestions.get(i);
            QuestionOption childQesOption = getQuestionOptionDao().getChildQuesOption(questionId,'%'+childSurveyQuestion.getSurveyQuestion_ID()+'%');
            if (childQesOption == null) {
                if (childSurveyQuestion.getIsLinkedQuestionId() != null && childSurveyQuestion.getIsLinkedQuestionId().equalsIgnoreCase("true")) {
                    SurveyQuestionWithData surveyQuestionWithData = new SurveyQuestionWithData();
                    surveyQuestionWithData.setSurveyQuestion_ID(childSurveyQuestion.getSurveyQuestion_ID());
                    surveyQuestionWithData.setSurveySection_ID(childSurveyQuestion.getSurveySection_ID());
                    surveyQuestionWithData.setQuestionTypeID(childSurveyQuestion.getQuestionTypeID());
                    surveyQuestionWithData.setAutopopulate(childSurveyQuestion.getAutopopulate());
                    surveyQuestionWithData.setLabelHeader(childSurveyQuestion.getLabelHeader());
                    surveyQuestionWithData.setRequired(childSurveyQuestion.getRequired());
                    surveyQuestionWithData.setQuestionSequence(childSurveyQuestion.getQuestionSequence());
                    surveyQuestionWithData.setValidationType(childSurveyQuestion.getValidationType());
                    surveyQuestionWithData.setIsValidation(childSurveyQuestion.getIsValidation());
                    surveyQuestionWithData.setIsLinkedQuestionId(childSurveyQuestion.getIsLinkedQuestionId());
                    surveyQuestionWithData.setParentQuestionId(childSurveyQuestion.getParentQuestionId());
                    surveyQuestionWithData.setOptionDependent(childSurveyQuestion.getOptionDependent());
                    surveyQuestionWithData.setQuestions(childSurveyQuestion.getQuestions());
                    surveyQuestionWithData.setMin_length(childSurveyQuestion.getMin_length());
                    surveyQuestionWithData.setMax_length(childSurveyQuestion.getMax_length());
                    surveyQuestionWithData.setCreatedBy(childSurveyQuestion.getCreatedBy());
                    surveyQuestionWithData.setCreatedDate(childSurveyQuestion.getCreatedDate());
                    surveyQuestionWithData.setUpdatedBy(childSurveyQuestion.getUpdatedBy());
                    surveyQuestionWithData.setUpdatedDate(childSurveyQuestion.getUpdatedDate());
                    surveyQuestionWithData.setIs_section(childSurveyQuestion.getIs_section());
                    surveyQuestionWithData.setIsActive(childSurveyQuestion.getIsActive());

                    //get question type of given child question
                    QuestionType questionType = getQuestionTypeOfQuestion(childSurveyQuestion);
                    surveyQuestionWithData.setQuestionType(questionType);

                    //get Question Options of given child question
                    List<QuestionOption> questionOptions = getQuestionOptionDao().getAllQuestionOption(childSurveyQuestion.getSurveyQuestion_ID());
                    surveyQuestionWithData.setQuestionOptions(questionOptions);

                    childQuestions.add(surveyQuestionWithData);
                }
            }
        }
        return childQuestions;
    }

    /**
     * @param question
     * @return question_type
     * @date 7-3-2022
     * Get QuestionType of given Question
     */
    public QuestionType getQuestionTypeOfQuestion(SurveyQuestion question) {
        return getQuestionTypeDao().getQuestionTypeById(question.getQuestionTypeID());
    }

    /**
     * @param str_question
     * @return Question id
     * @date 10-3-2022
     * Get question id from question
     */
    public String getQuestionIdFromQuestion(String str_question) {
        SurveyQuestion question = getSurveyQuestionFromQuestion(str_question);
        if (question != null) {
            return question.getSurveyQuestion_ID();
        }
        return null;
    }

    /**
     * @param str_question
     * @return Question id
     * @date 10-3-2022
     * Get question id from question
     */
    public SurveyQuestion getSurveyQuestionFromQuestion(String str_question) {
        if (str_question.contains("*")) {
            str_question = str_question.replace("*", "");
            str_question = str_question.trim();
        }
        SurveyQuestion question = getSurveyQuestionDao().getQuestionFromText(str_question);
        if (question == null) {
            str_question = str_question.substring(0,10);
            question = getSurveyQuestionDao().getQuestionFromTextUsingLike('%'+str_question+'%');
        }
        return question;
    }

    /**
     * @param Survey_section_id
     * @return boolean
     * @date 10-3-2022
     * Get Last survey section id, if it is equals to Survey_section_id then
     * check if all sections are entered in for particular form or not
     * if yes
     * return true
     * else return false
     * else return false
     */
    public boolean isLastSurveySection(String Survey_section_id, String form_id,String Survey_MasterId) {
        List<SurveySection> surveySections = getSurveySectionDao().getAllSections(Survey_MasterId);
        if (surveySections.get(surveySections.size() - 1).getSurveySection_ID().equalsIgnoreCase(Survey_section_id)) {
            int tot_candidates = getCandidateDetailsDao().getAllSectionDetailsByForm(form_id).size();
            if(surveySections.size() == tot_candidates + 1) {
                return true;
            } else if(surveySections.size() == tot_candidates) {
                return true;
            } else if(surveySections.size() == tot_candidates - 1) {
                return true;
            } else {
                return false;
            }
            //return  ;
        } else {
            return false;
        }
    }

    /**
     * @param questionOption
     * @return
     * @date 10-3-2022
     * Check option is present in other values or not
     */
    public boolean isPresentInOtherValues(QuestionOption questionOption) {
        OtherValues values = getOtherValuesDao().checkValue(questionOption.getQNA_Values());
        return values != null;
    }

    /**
     * @param question
     * @param option
     * @return
     * @date 10-3-2022
     * Get option id from question and option string
     */
    public String getOptionId(String question, String option) {
        if (question.contains("*")) {
            question = question.replace("*", "");
            question = question.trim();
        }
        SurveyQuestion surveyQuestion = getSurveyQuestionFromQuestion(question);
        if (surveyQuestion != null) {
            List<QuestionOption> options = getQuestionOptionDao().getAllQuestionOption(surveyQuestion.getSurveyQuestion_ID());
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).getQNA_Values().equalsIgnoreCase(option)) {
                    return options.get(i).getQNAOption_ID();
                }
            }
        }
        return null;
    }

    public int deleteCandidate(CandidateDetails candidateDetails) {
        return getCandidateDetailsDao().delete(candidateDetails);
    }

    public List<CandidateDetails> getCandidateDetailsByForm(String formId) {
        return getCandidateDetailsDao().getAllDetailsByForm(formId);
    }

    /**
     * @param candidateDetails
     * @return
     * @date 16-3-2022
     * Get Candidate Id If Present
     */
    public int isCandidateDetailsPresent(CandidateDetails candidateDetails) {
        int isCandidatePresent = 0;
        try {
            isCandidatePresent = -1;
            if (candidateDetails != null) {
                List<CandidateDetails> candidateDetailsList = getCandidateDetailsDao().getAllDetailsByForm(candidateDetails.getFormID());
                if (candidateDetailsList != null) {
                    for (int i = 0; i < candidateDetailsList.size(); i++) {
                        if (
                                candidateDetailsList.get(i).getSurvey_master_id().equalsIgnoreCase(candidateDetails.getSurvey_master_id()) &&
                                        candidateDetailsList.get(i).getSurvey_section_id().equalsIgnoreCase(candidateDetails.getSurvey_section_id()) &&
                                        candidateDetailsList.get(i).getSurvey_que_id().equalsIgnoreCase(candidateDetails.getSurvey_que_id()) &&
                                        candidateDetailsList.get(i).getSurvey_que_option_id().equalsIgnoreCase(candidateDetails.getSurvey_que_option_id())
                                        && candidateDetailsList.get(i).getIndex_if_linked_question() == candidateDetails.getIndex_if_linked_question()
                        ) {
                            isCandidatePresent = candidateDetailsList.get(i).id;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "isCandidateDetailsPresent: "+e.getMessage());
        }
        return isCandidatePresent;
    }

    public QuestionOption getQuestionIdFromQuestionOptionText(String str_question) {
        return getQuestionOptionDao().getQuestionOptionByText(str_question);
    }

    /**
     * @param childQuestionId
     * @return
     * @date 10-06-2022
     * Get Child Question
     */
    public SurveyQuestionWithData getChildQuestionFromIdInEdit(String childQuestionId,String FormId) {
        SurveyQuestion surveyQuestion = getSurveyQuestionDao().getQuestionById(childQuestionId);

        //check if question is parent add to list otherwise not
        SurveyQuestionWithData surveyQuestionWithData = null;
        if (surveyQuestion != null && surveyQuestion.getSurveyQuestion_ID() != null) {
            surveyQuestionWithData = new SurveyQuestionWithData();
            surveyQuestionWithData.setSurveyQuestion_ID(surveyQuestion.getSurveyQuestion_ID());
            surveyQuestionWithData.setSurveySection_ID(surveyQuestion.getSurveySection_ID());
            surveyQuestionWithData.setQuestionTypeID(surveyQuestion.getQuestionTypeID());
            surveyQuestionWithData.setAutopopulate(surveyQuestion.getAutopopulate());
            surveyQuestionWithData.setLabelHeader(surveyQuestion.getLabelHeader());
            surveyQuestionWithData.setRequired(surveyQuestion.getRequired());
            surveyQuestionWithData.setQuestionSequence(surveyQuestion.getQuestionSequence());
            surveyQuestionWithData.setValidationType(surveyQuestion.getValidationType());
            surveyQuestionWithData.setIsValidation(surveyQuestion.getIsValidation());
            surveyQuestionWithData.setIsLinkedQuestionId(surveyQuestion.getIsLinkedQuestionId());
            surveyQuestionWithData.setParentQuestionId(surveyQuestion.getParentQuestionId());
            surveyQuestionWithData.setOptionDependent(surveyQuestion.getOptionDependent());
            surveyQuestionWithData.setQuestions(surveyQuestion.getQuestions());
            surveyQuestionWithData.setMin_length(surveyQuestion.getMin_length());
            surveyQuestionWithData.setMax_length(surveyQuestion.getMax_length());
            surveyQuestionWithData.setCreatedBy(surveyQuestion.getCreatedBy());
            surveyQuestionWithData.setCreatedDate(surveyQuestion.getCreatedDate());
            surveyQuestionWithData.setUpdatedBy(surveyQuestion.getUpdatedBy());
            surveyQuestionWithData.setUpdatedDate(surveyQuestion.getUpdatedDate());
            surveyQuestionWithData.setIs_section(surveyQuestion.getIs_section());
            surveyQuestionWithData.setIsActive(surveyQuestion.getIsActive());
            List<CandidateDetails> candidateDetailsList = getCandidateDetailsDao().getAllDetailsByQuestionIdFormId(surveyQuestion.getSurveyQuestion_ID(), FormId);
            if (candidateDetailsList.size() > 0) {
                surveyQuestionWithData.setValue(candidateDetailsList.get(0).getSurvey_que_values());
            }

            //get question type of given question
            QuestionType questionType = getQuestionTypeOfQuestion(surveyQuestion);
            surveyQuestionWithData.setQuestionType(questionType);

            //get Question Options of given question
            List<QuestionOption> questionOptions = getQuestionOptionDao().getAllQuestionOption(surveyQuestion.getSurveyQuestion_ID());
            surveyQuestionWithData.setQuestionOptions(questionOptions);

            //get all child question and set to parent question
            List<SurveyQuestionWithData> childQuestions = getAllChildQuestions(surveyQuestion.getSurveyQuestion_ID());
            surveyQuestionWithData.setChildQuestions(childQuestions);

            //get all linked question and set to parent question
            List<HashMap<Integer, List<SurveyQuestionWithData>>> linkedQuestions = getAllLinkedQuestionsInEdit(surveyQuestion.getSurveyQuestion_ID(), FormId);
            if (linkedQuestions.size() > 0) {
                surveyQuestionWithData.setLinkedQuestionInEdit(linkedQuestions);
            } else {
                //get all linked question and set to parent question
                List<SurveyQuestionWithData> simple_linkedQuestions = getAllLinkedQuestions(surveyQuestion.getSurveyQuestion_ID());
                surveyQuestionWithData.setLinkedQuestions(simple_linkedQuestions);
            }
        }


        return surveyQuestionWithData;
    }

    public SurveyQuestionWithData getChildQuestionFromId(String childQuestionId) {
        SurveyQuestion surveyQuestion = getSurveyQuestionDao().getQuestionById(childQuestionId);

        //check if question is parent add to list otherwise not
        SurveyQuestionWithData surveyQuestionWithData = null;
        if (surveyQuestion != null && surveyQuestion.getSurveyQuestion_ID() != null) {
            surveyQuestionWithData = new SurveyQuestionWithData();
            surveyQuestionWithData.setSurveyQuestion_ID(surveyQuestion.getSurveyQuestion_ID());
            surveyQuestionWithData.setSurveySection_ID(surveyQuestion.getSurveySection_ID());
            surveyQuestionWithData.setQuestionTypeID(surveyQuestion.getQuestionTypeID());
            surveyQuestionWithData.setAutopopulate(surveyQuestion.getAutopopulate());
            surveyQuestionWithData.setLabelHeader(surveyQuestion.getLabelHeader());
            surveyQuestionWithData.setRequired(surveyQuestion.getRequired());
            surveyQuestionWithData.setQuestionSequence(surveyQuestion.getQuestionSequence());
            surveyQuestionWithData.setValidationType(surveyQuestion.getValidationType());
            surveyQuestionWithData.setIsValidation(surveyQuestion.getIsValidation());
            surveyQuestionWithData.setIsLinkedQuestionId(surveyQuestion.getIsLinkedQuestionId());
            surveyQuestionWithData.setParentQuestionId(surveyQuestion.getParentQuestionId());
            surveyQuestionWithData.setOptionDependent(surveyQuestion.getOptionDependent());
            surveyQuestionWithData.setQuestions(surveyQuestion.getQuestions());
            surveyQuestionWithData.setMin_length(surveyQuestion.getMin_length());
            surveyQuestionWithData.setMax_length(surveyQuestion.getMax_length());
            surveyQuestionWithData.setCreatedBy(surveyQuestion.getCreatedBy());
            surveyQuestionWithData.setCreatedDate(surveyQuestion.getCreatedDate());
            surveyQuestionWithData.setUpdatedBy(surveyQuestion.getUpdatedBy());
            surveyQuestionWithData.setUpdatedDate(surveyQuestion.getUpdatedDate());
            surveyQuestionWithData.setIs_section(surveyQuestion.getIs_section());
            surveyQuestionWithData.setIsActive(surveyQuestion.getIsActive());

            //get question type of given question
            QuestionType questionType = getQuestionTypeOfQuestion(surveyQuestion);
            surveyQuestionWithData.setQuestionType(questionType);

            //get Question Options of given question
            List<QuestionOption> questionOptions = getQuestionOptionDao().getAllQuestionOption(surveyQuestion.getSurveyQuestion_ID());
            surveyQuestionWithData.setQuestionOptions(questionOptions);

            //get all child question and set to parent question
            List<SurveyQuestionWithData> childQuestions = getAllChildQuestions(surveyQuestion.getSurveyQuestion_ID());
            surveyQuestionWithData.setChildQuestions(childQuestions);

            //get all linked question and set to parent question
            List<SurveyQuestionWithData> linkedQuestions = getAllLinkedQuestions(surveyQuestion.getSurveyQuestion_ID());
            surveyQuestionWithData.setLinkedQuestions(linkedQuestions);
        }


        return surveyQuestionWithData;
    }

    public boolean isSectionFilled(SurveySection listItem, String FormId) {
        if (listItem != null && FormId != null) {
            List<CandidateDetails> candidateDetails = getCandidateDetailsDao().getAllDetailsBySectionIdFormId(listItem.getSurveySection_ID(), FormId);
            return candidateDetails != null && candidateDetails.size() > 0;
        } else {
            return false;
        }
    }

    public void deleteAllRadioButtonOptions(QuestionOption questionOption, String formId, int linked_id) {
        String ques_id = questionOption.getSurveyQuestion_ID();
        SurveyQuestionWithData subForm = getChildQuestionFromId(ques_id);

        if (subForm.getQuestionOptions() != null) {
            for (int i = 0; i < subForm.getQuestionOptions().size(); i++) {
                QuestionOption selectedOption = subForm.getQuestionOptions().get(i);

                if (selectedOption.getChildQuestionId() != null && !selectedOption.getChildQuestionId().equalsIgnoreCase("null") && !selectedOption.getChildQuestionId().equalsIgnoreCase("")) {
                    String childQuesId = selectedOption.getChildQuestionId();
                    if (childQuesId.contains(",")) {
                        //binding.btnAddAnother.setVisibility(View.GONE);
                        List<String> childQuesIds = Arrays.asList(childQuesId.split(","));
                        for (int j = 0; j < childQuesIds.size(); j++) {
                            getCandidateDetailsDao().deleteCandidateByQuestionId(childQuesIds.get(j));
                        }
                    } else {
                        SurveyQuestionWithData childQues = DatabaseUtil.on().getChildQuestionFromIdInEdit(childQuesId, formId);
                        if (childQues != null) {
                            getCandidateDetailsDao().deleteCandidateByQuestionId(childQues.getSurveyQuestion_ID());
                        }
                    }
                }
            }
        }

        getCandidateDetailsDao().deleteCandidateByQuestionId(ques_id);

    }

    public List<CandidateDetails> getAllFilledFormData() {
        List<CandidateDetails> candidateDetails = new ArrayList<>();
        for (int i = 0; i < getCandidateSurveyStatusDetailsDao().getAllFlowableCodes().size(); i++) {
            CandidateSurveyStatusDetails candidateSurveyStatusDetails = getCandidateSurveyStatusDetailsDao().getAllFlowableCodes().get(i);
            if(candidateSurveyStatusDetails.getEnd_date() != null) {
                candidateDetails.addAll(getCandidateDetailsDao().getAllDetailsByForm(candidateSurveyStatusDetails.getFormID()));
            }
        }

        return candidateDetails;
    }

    public void logout() {
        UniqaDatabase.on().clearAllTables();
    }
}
