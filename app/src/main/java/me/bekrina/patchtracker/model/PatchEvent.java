package me.bekrina.patchtracker.model;

import java.util.Date;

public class PatchEvent implements Event {
    public PatchEvent(Date date, EventType type) {
        this.date = date;
        this.type = type;
    }
    public enum EventType {
        PUTON, CHANGE, PUTOFF
    }
    private EventType type;
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}
