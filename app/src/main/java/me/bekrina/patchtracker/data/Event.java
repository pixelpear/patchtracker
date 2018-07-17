package me.bekrina.patchtracker.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import org.threeten.bp.OffsetDateTime;

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    public Event(OffsetDateTime date, EventType type) {
        this.date = date;
        this.type = type;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public enum EventType {
        PATCH_ON, PATCH_CHANGE, PATCH_OFF
    }
    @ColumnInfo(name = "is_marked")
    private boolean marked;

    @ColumnInfo(name = "type")
    @TypeConverters(EventTypeConverter.class)
    @NonNull
    private EventType type;

    @ColumnInfo(name = "date")
    @TypeConverters(DateConverter.class)
    @NonNull
    private OffsetDateTime date;

    public void setDate(@NonNull OffsetDateTime date) {
        this.date = date;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public EventType getType() {
        return type;
    }

    public void setType(@NonNull EventType type) {
        this.type = type;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
}
