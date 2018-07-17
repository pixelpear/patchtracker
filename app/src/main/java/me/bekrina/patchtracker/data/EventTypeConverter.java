package me.bekrina.patchtracker.data;

import android.arch.persistence.room.TypeConverter;

import me.bekrina.patchtracker.data.Event.EventType;

public class EventTypeConverter {
    @TypeConverter
    public Integer fromEventType(EventType type) {
        switch (type) {
            case PATCH_ON:
                return 1;
            case PATCH_CHANGE:
                return 2;
            case PATCH_OFF:
                return 3;
        }
        return 2;
        /*throw new EventWithoutTypeException("Event type couldn't be mapped. Type field contains "
                + type.toString());*/
    }
    @TypeConverter
    public EventType fromInteger(Integer number) {
        switch (number) {
            case 1:
                return EventType.PATCH_ON;
            case 2:
                return EventType.PATCH_CHANGE;
            case 3:
                return EventType.PATCH_OFF;
        }
        return EventType.PATCH_CHANGE;
        /*throw new EventWithoutTypeException("Event type couldn't be mapped. Type field contains "
                + number);*/
    }
}
