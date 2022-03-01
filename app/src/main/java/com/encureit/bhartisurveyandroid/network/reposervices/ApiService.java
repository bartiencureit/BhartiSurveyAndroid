package com.encureit.bhartisurveyandroid.network.reposervices;


import com.encureit.bhartisurveyandroid.models.LoginResponseModel;
import com.encureit.bhartisurveyandroid.network.Contants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
}

