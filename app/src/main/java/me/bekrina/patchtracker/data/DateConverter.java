package me.bekrina.patchtracker.data;

import android.arch.persistence.room.TypeConverter;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class DateConverter {
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @TypeConverter
    public OffsetDateTime fromString(String value) {
            return OffsetDateTime.parse(value, formatter);
    }

    @TypeConverter
    public String fromOffsetDateTime(OffsetDateTime date) {
        return date.format(formatter);
    }
}
