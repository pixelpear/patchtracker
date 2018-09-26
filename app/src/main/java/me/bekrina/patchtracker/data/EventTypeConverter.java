package me.bekrina.patchtracker.data;

import android.arch.persistence.room.TypeConverter;

import me.bekrina.patchtracker.data.Event.EventType;

public class EventTypeConverter {
    @TypeConverter
    public Integer fromEventType(EventType type) {
        switch (type) {
            case PATCH_1:
                return 1;
            case PATCH_2:
                return 2;
            case PATCH_3:
                return 3;
        }
        // NO_PATCH
        return 4;
    }
    @TypeConverter
    public EventType fromInteger(Integer number) {
        switch (number) {
            case 1:
                return EventType.PATCH_1;
            case 2:
                return EventType.PATCH_2;
            case 3:
                return EventType.PATCH_3;
        }
        return EventType.NO_PATCH;
    }
}
