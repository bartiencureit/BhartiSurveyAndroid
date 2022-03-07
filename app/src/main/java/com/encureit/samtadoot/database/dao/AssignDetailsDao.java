package com.encureit.samtadoot.database.dao;


import com.encureit.samtadoot.database.BaseDao;
import com.encureit.samtadoot.database.TableNames;
import com.encureit.samtadoot.models.AssignDetails;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao

public interface AssignDetailsDao extends BaseDao<AssignDetails> {

    @Query("SELECT * FROM " + TableNames.TABLE_USER_ASSIGNED_DATA)
    List<AssignDetails> getAllFlowableCodes();

    @Query("SELECT COUNT(id) FROM " + TableNames.TABLE_USER_ASSIGNED_DATA)
    int getRowCount();

    @Query("DELETE FROM "  + TableNames.TABLE_USER_ASSIGNED_DATA)
    void nukeTable();

    @Query("UPDATE " + TableNames.TABLE_USER_ASSIGNED_DATA+" SET id =:id WHERE assign_id =:assign_id")
    void update_id(int assign_id,String id);

    @Query("UPDATE " + TableNames.TABLE_USER_ASSIGNED_DATA+" SET unique_code =:unique_code WHERE assign_id =:assign_id")
    void update_unique_code(int assign_id,String unique_code);

    @Query("UPDATE " + TableNames.TABLE_USER_ASSIGNED_DATA+" SET name =:name WHERE assign_id =:assign_id")
    void update_name(int assign_id,String name);

    @Query("UPDATE " + TableNames.TABLE_USER_ASSIGNED_DATA+" SET status =:status WHERE assign_id =:assign_id")
    void update_status(int assign_id,String status);

    @Query("UPDATE " + TableNames.TABLE_USER_ASSIGNED_DATA+" SET taluka_id =:taluka_id WHERE assign_id =:assign_id")
    void update_taluka_id(int assign_id,String taluka_id);

}
