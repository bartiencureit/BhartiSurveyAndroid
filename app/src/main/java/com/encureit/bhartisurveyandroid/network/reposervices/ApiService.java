package com.encureit.bhartisurveyandroid.network.reposervices;

import com.ensureit.testapp.network.Contants;
import com.ensureit.testapp.network.responsemodel.ListModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST(Contants.GET_LIST)
    Call<List<ListModel>> getList(
            @Field("country") String country
    );
}

