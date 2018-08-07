package me.bekrina.patchtracker.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.threeten.bp.OffsetDateTime;

@Entity
public class Event implements Parcelable {
    public Event(OffsetDateTime date, EventType type) {
        this.date = date;
        this.type = type;
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

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
        this.date = OffsetDateTime.parse(in.readString());
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
        dest.writeString(date.toString());
    }

    public enum EventType {
        PATCH_ON, PATCH_CHANGE, PATCH_OFF
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

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
