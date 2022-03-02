package com.encureit.bhartisurveyandroid.models;

import com.encureit.bhartisurveyandroid.database.TableNames;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/2/2022.
 */
@Entity(tableName = TableNames.TABLE_USER_DEVICE_DETAILS)
public class UserDeviceDetails {
    @PrimaryKey(autoGenerate = true)
    public int id;

    private String device_info_id;
    private String userRoleId;
    private String dManufacturer;
    private String dBrand;
    private String dModel;
    private String dSerialNo;
    private String dImeiNo;
    private String dDisplaySize;
    private String dMobileNetwork;
    private String dWifi;
    private String dBluetooth;
    private String dSdkVersionNo;
    private String dReleaseVersion;
    private String dOsVersionNo;
    private String dID;
    private String dHost;
    private String dHardware;
    private String dFingerprint;

    public UserDeviceDetails() {
    }

    public String getManufacturer() {
        return dManufacturer;
    }

    public void setManufacturer(String dManufacturer) {
        this.dManufacturer = dManufacturer;
    }

    public String getBrand() {
        return dBrand;
    }

    public void setBrand(String dBrand) {
        this.dBrand = dBrand;
    }

    public String getModel() {
        return dModel;
    }

    public void setModel(String dModel) {
        this.dModel = dModel;
    }

    public String getSerialNo() {
        return dSerialNo;
    }

    public void setSerialNo(String dSerialNo) {
        this.dSerialNo = dSerialNo;
    }

    public String getImeiNo() {
        return dImeiNo;
    }

    public void setImeiNo(String dImeiNo) {
        this.dImeiNo = dImeiNo;
    }

    public String getDisplaySize() {
        return dDisplaySize;
    }

    public void setDisplaySize(String dDisplaySize) {
        this.dDisplaySize = dDisplaySize;
    }

    public String getMobileNetwork() {
        return dMobileNetwork;
    }

    public void setMobileNetwork(String dMobileNetwork) {
        this.dMobileNetwork = dMobileNetwork;
    }

    public String getWifi() {
        return dWifi;
    }

    public void setWifi(String dWifi) {
        this.dWifi = dWifi;
    }

    public String getBluetooth() {
        return dBluetooth;
    }

    public void setBluetooth(String dBluetooth) {
        this.dBluetooth = dBluetooth;
    }

    public String getSdkVersionNo() {
        return dSdkVersionNo;
    }

    public void setSdkVersionNo(String dSdkVersionNo) {
        this.dSdkVersionNo = dSdkVersionNo;
    }

    public String getReleaseVersion() {
        return dReleaseVersion;
    }

    public void setReleaseVersion(String dReleaseVersion) {
        this.dReleaseVersion = dReleaseVersion;
    }

    public String getOsVersionNo() {
        return dOsVersionNo;
    }

    public void setOsVersionNo(String dVersionNo) {
        this.dOsVersionNo = dVersionNo;
    }

    public String getID() {
        return dID;
    }

    public void setID(String dID) {
        this.dID = dID;
    }

    public String getHost() {
        return dHost;
    }

    public void setHost(String dHost) {
        this.dHost = dHost;
    }

    public String getHardware() {
        return dHardware;
    }

    public void setHardware(String dHardware) {
        this.dHardware = dHardware;
    }

    public String getFingerprint() {
        return dFingerprint;
    }

    public void setFingerprint(String dFingerprint) {
        this.dFingerprint = dFingerprint;
    }

    public String getDevice_info_id() {
        return device_info_id;
    }

    public void setDevice_info_id(String device_info_id) {
        this.device_info_id = device_info_id;
    }

    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }
}
