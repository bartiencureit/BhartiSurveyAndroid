package com.encureit.samtadoot.network.reposervices;


import com.encureit.samtadoot.network.responsemodel.CandidateInsertResponseModel;
import com.encureit.samtadoot.network.responsemodel.LoginResponseModel;
import com.encureit.samtadoot.network.responsemodel.OtherValuesResponseModel;
import com.encureit.samtadoot.network.responsemodel.OtpCheckResponseModel;
import com.encureit.samtadoot.network.Contants;
import com.encureit.samtadoot.network.responsemodel.QuestionOptionResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.QuestionValidationResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyQuestionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveySectionResponseModel;
import com.encureit.samtadoot.network.responsemodel.SurveyTypeResponseModel;
import com.encureit.samtadoot.network.responsemodel.UserAssignedDetailsResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    /**
     * @date 1-3-2022
     * Gets Login Response from server
     * Response has status, key, user_id and user_role
     * @param user_serial_no
     * @return
     */
    @FormUrlEncoded
    @POST(Contants.GET_LOGIN)
    Call<LoginResponseModel> getLoginResponse(
            @Field("user_serial_no") String user_serial_no
    );

    /**
     * @date 1-3-2022
     * Checks Otp
     * Response has status, error_code, login_date and message
     * @param user_id
     * @param otp
     * @return
     */
    @FormUrlEncoded
    @POST(Contants.GET_VERIFY_OTP)
    Call<OtpCheckResponseModel> getOtpCheckResponse(
            @Field("user_id") String user_id,
            @Field("otp") String otp
    );

    /**
     * @date 2-3-2022
     * gets all survey types
     * response has list of survey type
     * @return
     */
    @GET(Contants.GET_SURVEY_TYPE)
    Call<SurveyTypeResponseModel> getSurveyTypes();

    /**
     * @date 3-3-2022
     * gets all survey section
     * response has list of survey section
     * @return
     */
    @GET(Contants.GET_SURVEY_SECTION)
    Call<SurveySectionResponseModel> getSurveySection();

    /**
     * @date 3-3-2022
     * gets all survey types
     * response has list of survey type
     * @return
     */
    @GET(Contants.GET_SURVEY_QUESTION)
    Call<SurveyQuestionResponseModel> getSurveyQuestion();

    /**
     * @date 3-3-2022
     * gets all question option
     * response has list of question option
     * @return
     */
    @GET(Contants.GET_QUESTION_OPTION)
    Call<QuestionOptionResponseModel> getQuestionOptions();

    /**
     * @date 3-3-2022
     * gets all question types
     * response has list of question types
     * @return
     */
    @GET(Contants.GET_QUESTION_TYPES)
    Call<QuestionTypeResponseModel> getQuestionTypes();

    /**
     * @date 3-3-2022
     * gets all question validation
     * response has list of question validation
     * @return
     */
    @GET(Contants.GET_QUESTION_VALIDATION)
    Call<QuestionValidationResponseModel> getQuestionValidations();

    /**
     * @date 3-3-2022
     * gets all user assiged data
     * response has list of user assiged data
     * @return
     */
    @FormUrlEncoded
    @POST(Contants.GET_USER_ASSIGNED_DETAILS)
    Call<UserAssignedDetailsResponseModel> getUserAssignedDetails(
            @Field("user_id") String user_id
    );

    /**
     * @date 10-3-2022
     * gets all question validation
     * response has list of question validation
     * @return
     */
    @GET(Contants.GET_OTHER_VALUES)
    Call<OtherValuesResponseModel> getOtherValues();

    /**
     * @date 11-3-2022
     * gets all question validation
     * response has list of question validation
     * @return
     */
    @Multipart
    @POST(Contants.INSERT_CANDIDATE)
    Call<CandidateInsertResponseModel> insertCandidateData(
            @Part("survey_master_id") RequestBody survey_master_id,
            @Part("survey_section_id") RequestBody survey_section_id,
            @Part("survey_que_id") RequestBody survey_que_id,
            @Part("survey_que_option_id") RequestBody survey_que_option_id,
            @Part("survey_que_values") RequestBody survey_que_values,
            @Part("FormID") RequestBody FormID,
            @Part("Current_Form_Status") RequestBody Current_Form_Status,
            @Part("age_value") RequestBody age_value,
            @Part("Survey_StartDate") RequestBody Survey_StartDate,
            @Part("Survey_EndDate") RequestBody Survey_EndDate,
            @Part("created_by") RequestBody created_by,
            @Part("Latitude") RequestBody Latitude,
            @Part("Longitude") RequestBody Longitude,
            @Part MultipartBody.Part image
            );

}

