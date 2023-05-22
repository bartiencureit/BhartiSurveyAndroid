package com.encureit.samtadoot.database.dao;

import com.encureit.samtadoot.database.BaseDao;
import com.encureit.samtadoot.database.TableNames;
import com.encureit.samtadoot.models.UserDeviceDetails;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao

public interface UserDeviceDetailsDao extends BaseDao<UserDeviceDetails> {

    @Query("SELECT * FROM " + TableNames.TABLE_USER_DEVICE_DETAILS)
    List<UserDeviceDetails> getAllFlowableCodes();

    @Query("SELECT COUNT(id) FROM " + TableNames.TABLE_USER_DEVICE_DETAILS)
    int getRowCount();

    @Query("DELETE FROM "  + TableNames.TABLE_USER_DEVICE_DETAILS)
    void nukeTable();

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET device_info_id = :device_info_id WHERE id =:id")
    void update_device_info_id(int id, String device_info_id);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET userRoleId = :userRoleId WHERE id =:id")
    void update_userRoleId(int id, String userRoleId);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dManufacturer = :dManufacturer WHERE id =:id")
    void update_dManufacturer(int id, String dManufacturer);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dBrand = :dBrand WHERE id =:id")
    void update_dBrand(int id, String dBrand);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dModel = :dModel WHERE id =:id")
    void update_dModel(int id, String dModel);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dSerialNo = :dSerialNo WHERE id =:id")
    void update_dSerialNo(int id, String dSerialNo);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dImeiNo = :dImeiNo WHERE id =:id")
    void update_dImeiNo(int id, String dImeiNo);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dDisplaySize = :dDisplaySize WHERE id =:id")
    void update_dDisplaySize(int id, String dDisplaySize);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dMobileNetwork = :dMobileNetwork WHERE id =:id")
    void update_dMobileNetwork(int id, String dMobileNetwork);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dWifi = :dWifi WHERE id =:id")
    void update_dWifi(int id, String dWifi);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dBluetooth = :dBluetooth WHERE id =:id")
    void update_dBluetooth(int id, String dBluetooth);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dSdkVersionNo = :dSdkVersionNo WHERE id =:id")
    void update_dSdkVersionNo(int id, String dSdkVersionNo);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dReleaseVersion = :dReleaseVersion WHERE id =:id")
    void update_dReleaseVersion(int id, String dReleaseVersion);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dOsVersionNo = :dOsVersionNo WHERE id =:id")
    void update_dOsVersionNo(int id, String dOsVersionNo);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dID = :dID WHERE id =:id")
    void update_dID(int id, String dID);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dHost = :dHost WHERE id =:id")
    void update_dHost(int id, String dHost);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dHardware = :dHardware WHERE id =:id")
    void update_dHardware(int id, String dHardware);

    @Query("UPDATE " + TableNames.TABLE_USER_DEVICE_DETAILS+" SET dFingerprint = :dFingerprint WHERE id =:id")
    void update_dFingerprint(int id, String dFingerprint);




}
