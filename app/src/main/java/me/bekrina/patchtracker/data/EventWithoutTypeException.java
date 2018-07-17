package me.bekrina.patchtracker.data;

public class EventWithoutTypeException extends EventException {
    public EventWithoutTypeException (String message) {
        super(message);
    }
}
