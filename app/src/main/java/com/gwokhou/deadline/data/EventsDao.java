package com.gwokhou.deadline.data;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface EventsDao {

    @Query("SELECT * FROM event_table")
    LiveData<List<Event>> getAllEvents();

    @Query("SELECT * FROM event_table WHERE entry_id = :eventId")
    Event getEventById(String eventId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvent(Event event);

    @Query("UPDATE event_table SET state = :state WHERE entry_id = :eventId")
    void updateState(String eventId, int state);

    @Query("DELETE FROM event_table WHERE entry_id = :eventId")
    int deleteEventById(String eventId);

    @Query("DELETE FROM event_table")
    void deleteAllEvents();

    @Query("DELETE FROM event_table WHERE state = 4")
    int deleteCompletedEvents();

}
