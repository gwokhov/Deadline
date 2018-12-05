package com.gwokhou.deadline.data;


import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "event_table")
public class Event {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entry_id")
    private String mId;

    @Nullable
    @ColumnInfo(name = "title")
    private String mTitle;

    @Nullable
    @ColumnInfo(name = "note")
    private String mNote;

    @ColumnInfo(name = "start_date")
    private long mStartDate;

    @ColumnInfo(name = "end_date")
    private long mEndDate;

    @ColumnInfo(name = "state")
    private int mState;

    @ColumnInfo(name = "durable_event")
    private boolean mDurableEvent;

    @ColumnInfo(name = "priority")
    private int mPriority;

    @ColumnInfo(name = "reminder")
    private int mReminder;

    @ColumnInfo(name = "category")
    private String mCategory;

    @ColumnInfo(name = "creation_date")
    private long mCreationDate;

    //Use this constructor to create a new active Event.
    @Ignore
    public Event(@Nullable String title, @Nullable String note, long startDate, long endDate, int state, boolean durableEvent, int priority, int reminder, String category) {
        this(title, note, startDate, endDate, state, durableEvent, priority, UUID.randomUUID().toString(), reminder, category, System.currentTimeMillis());
    }

    public Event(@Nullable String title, @Nullable String note, long startDate, long endDate, int state, boolean durableEvent, int priority, String id, int reminder, String category, long creationDate) {
        mId = id;
        mCreationDate = creationDate;
        mTitle = title;
        mNote = note;
        mStartDate = startDate;
        mEndDate = endDate;
        mState = state;
        mDurableEvent = durableEvent;
        mPriority = priority;
        mReminder = reminder;
        mCategory = category;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public long getCreationDate() {
        return mCreationDate;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getNote() {
        return mNote;
    }

    public long getStartDate() {
        return mStartDate;
    }

    public long getEndDate() {
        return mEndDate;
    }

    public int getState() {
        return mState;
    }

    public boolean isDurableEvent() {
        return mDurableEvent;
    }

    public int getPriority() {
        return mPriority;
    }

    public int getReminder() {
        return mReminder;
    }

    public String getCategory() {
        return mCategory;
    }
}
