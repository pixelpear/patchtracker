package me.bekrina.patchtracker.data;

import android.arch.persistence.room.TypeConverter;

import me.bekrina.patchtracker.data.Event.EventType;

public class EventTypeConverter {
    @TypeConverter
    public Integer fromEventType(EventType type) {
        switch (type) {
            case PATCH_ON:
                return 1;
            case PATCH_OFF:
                return 2;
        }
        return 3;
    }
    @TypeConverter
    public EventType fromInteger(Integer number) {
        switch (number) {
            case 1:
                return EventType.PATCH_ON;
            case 2:
                return EventType.PATCH_OFF;
        }
        return EventType.PATCH_CHANGE;
    }
}
