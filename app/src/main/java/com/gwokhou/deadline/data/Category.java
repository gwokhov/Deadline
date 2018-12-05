package com.gwokhou.deadline.data;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_table")
public class Category {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entry_id")
    private String mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @Ignore
    public Category(String name) {
        this(name, UUID.randomUUID().toString());
    }

    public Category(String name, String id) {
        mName = name;
        mId = id;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }
}
