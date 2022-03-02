package com.encureit.bhartisurveyandroid.network.reposervices;


import com.encureit.bhartisurveyandroid.network.responsemodel.LoginResponseModel;
import com.encureit.bhartisurveyandroid.network.responsemodel.OtpCheckResponseModel;
import com.encureit.bhartisurveyandroid.network.Contants;
import com.encureit.bhartisurveyandroid.network.responsemodel.SurveyTypeResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
}

