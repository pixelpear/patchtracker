package me.bekrina.patchtracker;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import org.threeten.bp.Month;
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;

import me.bekrina.patchtracker.data.Event;
import me.bekrina.patchtracker.data.EventViewModel;

public class Scheduling {
    FragmentActivity parentActivity;
    public Scheduling(FragmentActivity activity) {
        this.parentActivity = activity;
    }
    public void rescheduleCalendarStartingFrom(Event event) {
        EventViewModel eventViewModel =  ViewModelProviders.of(parentActivity).get(EventViewModel.class);
        ArrayList<Event> rescheduledEvents = new ArrayList<>();

        OffsetDateTime now = OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        Event currentEvent = event;

        eventViewModel.deleteAllFutureEvents(currentEvent.getPlannedDate());

        while (currentEvent.getPlannedDate().compareTo(now) < 0) {
            //TODO: think if we need to communicate it explicitly with user
            currentEvent.setMarked(true);
            //TODO: if we need here to update or there will be only new events?
            rescheduledEvents.add(currentEvent);
            currentEvent = findNextEvent(currentEvent);
        }
        //TODO: set Notification for currentEvent at time or immediate if time has passed (Ask to mark?)

        List<Event> plannedEvents = new ArrayList<>();
        // todo: use find next event until next event is in visible month, then calculate events for visible month
    }

    public static Event findNextEvent(Event event) {
        OffsetDateTime date = event.getPlannedDate().plusDays(7);
        switch(event.getType()) {
            case PATCH_1:
                return new Event(date, Event.EventType.PATCH_2);
            case PATCH_2:
                return new Event(date, Event.EventType.PATCH_3);
            case PATCH_3:
                return new Event(date, Event.EventType.NO_PATCH);
        }
        return new Event(date, Event.EventType.PATCH_1);
    }
}
