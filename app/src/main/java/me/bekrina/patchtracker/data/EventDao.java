package me.bekrina.patchtracker.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event ORDER BY date DESC")
    LiveData<List<Event>> getAll();

    @Query("SELECT * FROM event WHERE uid in (:eventIds)")
    LiveData<List<Event>> loadAllByIds(int[] eventIds);

    @Query("SELECT * FROM event WHERE type IN (:eventType)")
    LiveData<List<Event>> loadAllByType(Event.EventType eventType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Event... events);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM event")
    void deleteAll();
}
