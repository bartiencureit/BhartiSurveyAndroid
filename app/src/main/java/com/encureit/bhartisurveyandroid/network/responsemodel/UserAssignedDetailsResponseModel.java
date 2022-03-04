package com.encureit.bhartisurveyandroid.network.responsemodel;

import com.encureit.bhartisurveyandroid.models.AssignDetails;

import java.util.List;

/**
 * Created by Swapna Thakur on 3/3/2022.
 */
public class UserAssignedDetailsResponseModel {
    boolean status;
    List<AssignDetails> assign_details;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<AssignDetails> getAssign_details() {
        return assign_details;
    }

    public void setAssign_details(List<AssignDetails> assign_details) {
        this.assign_details = assign_details;
    }
}
