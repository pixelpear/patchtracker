package me.bekrina.patchtracker.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.common.io.Resources;

import org.threeten.bp.OffsetDateTime;

import me.bekrina.patchtracker.R;

@Entity
public class Event implements Parcelable {
    public Event(OffsetDateTime plannedDate, EventType type) {
        setPlannedDate(plannedDate);
        setType(type);
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "is_marked")
    private boolean marked;

    @ColumnInfo(name = "type")
    @TypeConverters(EventTypeConverter.class)
    @NonNull
    private EventType type;

    @ColumnInfo(name = "plannedDate")
    @TypeConverters(DateConverter.class)
    @NonNull
    private OffsetDateTime plannedDate;

    @ColumnInfo(name = "markedDate")
    @TypeConverters(DateConverter.class)
    private OffsetDateTime markedDate;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    private Event(Parcel in) {
        this.uid = in.readInt();
        this.marked = in.readByte() != 0;
        this.type = EventType.valueOf(in.readString());
        this.plannedDate = OffsetDateTime.parse(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.uid);
        dest.writeByte((byte) (marked ? 1 : 0));
        dest.writeString(type.toString());
        dest.writeString(plannedDate.toString());
    }

    public enum EventType {
        PATCH_1, PATCH_2, PATCH_3, NO_PATCH
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setPlannedDate(@NonNull OffsetDateTime date) {
        this.plannedDate = date.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public OffsetDateTime getPlannedDate() {
        return plannedDate;
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
        if (!this.marked && marked) {
            setMarkedDate(OffsetDateTime.now());
        }
        this.marked = marked;
    }

    public OffsetDateTime getMarkedDate() {
        return markedDate;
    }

    public void setMarkedDate(OffsetDateTime markedDate) {
        this.markedDate = markedDate;
    }
}
