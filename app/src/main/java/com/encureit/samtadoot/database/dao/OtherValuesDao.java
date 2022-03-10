package com.encureit.samtadoot.database.dao;


import com.encureit.samtadoot.database.BaseDao;
import com.encureit.samtadoot.database.TableNames;
import com.encureit.samtadoot.models.OtherValues;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao

public interface OtherValuesDao extends BaseDao<OtherValues> {

    @Query("SELECT * FROM " + TableNames.TABLE_OTHER_VALUES)
    List<OtherValues> getAllFlowableCodes();

    @Query("SELECT COUNT(id) FROM " + TableNames.TABLE_OTHER_VALUES)
    int getRowCount();

    @Query("DELETE FROM "  + TableNames.TABLE_OTHER_VALUES)
    void nukeTable();

    @Query("UPDATE " + TableNames.TABLE_OTHER_VALUES+" SET id =:id WHERE other_value_id =:other_value_id")
    void update_id(int other_value_id, String id);

    @Query("UPDATE " + TableNames.TABLE_OTHER_VALUES+" SET value =:value WHERE other_value_id =:other_value_id")
    void update_value(int other_value_id, String value);

}
