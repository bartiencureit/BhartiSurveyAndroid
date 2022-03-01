package com.encureit.bhartisurveyandroid.network.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Swapna Thakur on 2/15/2022.
 */
public class ListModel {
    @SerializedName("domains")
    @Expose
    List<String> domains;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("country")
    @Expose
    String country;

    @SerializedName("alpha_two_code")
    @Expose
    String alpha_two_code;

    @SerializedName("state-province")
    @Expose
    String state;

    @SerializedName("web_pages")
    @Expose
    List<String> web_pages;


    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAlpha_two_code() {
        return alpha_two_code;
    }

    public void setAlpha_two_code(String alpha_two_code) {
        this.alpha_two_code = alpha_two_code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getWeb_pages() {
        return web_pages;
    }

    public void setWeb_pages(List<String> web_pages) {
        this.web_pages = web_pages;
    }
}
