package com.encureit.samtadoot.models;

import com.encureit.samtadoot.database.TableNames;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Swapna Thakur on 3/10/2022.
 */
@Entity(tableName = TableNames.TABLE_OTHER_VALUES)
public class OtherValues {
    @PrimaryKey(autoGenerate = true)
    public int other_value_id;

    String id;
    String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
