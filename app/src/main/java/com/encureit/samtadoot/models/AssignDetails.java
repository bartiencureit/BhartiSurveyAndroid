package com.encureit.samtadoot.models;

import com.encureit.samtadoot.database.TableNames;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/3/2022.
 */
@Entity(tableName = TableNames.TABLE_USER_ASSIGNED_DATA)
public class AssignDetails {
    @PrimaryKey(autoGenerate = true)
    public int assign_id;

    String id;
    String unique_code;
    String name;
    String status;
    String taluka_id;

    public int getAssign_id() {
        return assign_id;
    }

    public void setAssign_id(int assign_id) {
        this.assign_id = assign_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnique_code() {
        return unique_code;
    }

    public void setUnique_code(String unique_code) {
        this.unique_code = unique_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaluka_id() {
        return taluka_id;
    }

    public void setTaluka_id(String taluka_id) {
        this.taluka_id = taluka_id;
    }
}
