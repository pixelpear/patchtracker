package me.bekrina.patchtracker.model;

import java.util.Date;

public interface Event {
    Date getDate();
    void setDate(Date date);
    PatchEvent.EventType getType();
    void setType(PatchEvent.EventType type);
}
