package me.bekrina.patchtracker.model;

import java.util.Date;

public class ConEvent {
    public ConEvent(Date date, EventType type) {
        this.date = date;
        this.type = type;
    }

    public enum EventType {
        PATCH_ON, PATCH_CHANGE, PATCH_OFF, PILL
    }
    private boolean marked;
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

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
}
