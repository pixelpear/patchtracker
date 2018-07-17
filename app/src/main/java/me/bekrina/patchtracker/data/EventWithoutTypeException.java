package me.bekrina.patchtracker.data;

public class EventWithoutTypeException extends Exception {
    public EventWithoutTypeException (String message) {
        super(message);
    }
}
