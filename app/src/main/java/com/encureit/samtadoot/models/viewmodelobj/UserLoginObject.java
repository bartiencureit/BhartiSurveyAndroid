package com.encureit.samtadoot.models.viewmodelobj;

import com.encureit.samtadoot.network.responsemodel.LoginResponseModel;

/**
 * Created by Swapna Thakur on 3/1/2022.
 */
public class UserLoginObject {
    String userId;
    LoginResponseModel response;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LoginResponseModel getResponse() {
        return response;
    }

    public void setResponse(LoginResponseModel response) {
        this.response = response;
    }
}
