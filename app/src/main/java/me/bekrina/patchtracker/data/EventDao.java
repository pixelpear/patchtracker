package me.bekrina.patchtracker.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.threeten.bp.OffsetDateTime;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event ORDER BY plannedDate DESC")
    LiveData<List<Event>> getAllSortedDesc();

    @Query("SELECT * FROM event WHERE uid in (:eventIds)")
    LiveData<List<Event>> loadAllByIds(int[] eventIds);

    @Query("SELECT * FROM event WHERE plannedDate in (:plannedDate)")
    LiveData<List<Event>> loadAllByDate(OffsetDateTime plannedDate);

    @Query("SELECT * FROM event WHERE uid in (:eventId)")
    LiveData<Event> loadById(int eventId);

    @Query("SELECT * FROM event WHERE type IN (:eventType)")
    LiveData<List<Event>> loadAllByType(Event.EventType eventType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Event... events);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM event")
    void deleteAll();

    @Query("DELETE FROM event WHERE plannedDate > (:date)")
    void deleteAllFutureEvents(OffsetDateTime date);

    @Query("SELECT * FROM event WHERE plannedDate > (:date) ORDER BY plannedDate ASC")
    LiveData<List<Event>> getAllFutureEventsSortedAsc(OffsetDateTime date);

    @Update(onConflict = REPLACE)
    void updateAll(Event... events);
}
