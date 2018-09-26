package me.bekrina.patchtracker.data;

import android.arch.persistence.room.TypeConverter;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class DateConverter {
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @TypeConverter
    public OffsetDateTime fromString(String value) {
        OffsetDateTime date = null;
        if (!value.equals("")){
            date = OffsetDateTime.parse(value, formatter);
        }
        return date;
    }

    @TypeConverter
    public String fromOffsetDateTime(OffsetDateTime date) {
        String formattedDate = "";
        if (date != null) {
            formattedDate = date.format(formatter);
        }
        return formattedDate;
    }
}
