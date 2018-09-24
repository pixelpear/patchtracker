package me.bekrina.patchtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.OffsetTime;

import java.util.ArrayList;
import java.util.List;

import me.bekrina.patchtracker.data.Event;
import me.bekrina.patchtracker.data.EventViewModel;

import static android.content.Context.ALARM_SERVICE;

public class Scheduling {
    FragmentActivity parentActivity;
    public static int  NOTIFICATION_ID = 1;
    private static final String ACTION_NOTIFY =
            "me.bekrina.patchtracker.ACTION_NOTIFY";
    private AlarmManager alarmManager;
    private EventViewModel eventViewModel;

    public Scheduling(FragmentActivity activity) {
        this.parentActivity = activity;
        alarmManager = (AlarmManager) parentActivity.getSystemService(ALARM_SERVICE);
        eventViewModel =  ViewModelProviders.of(parentActivity).get(EventViewModel.class);
    }

    private List<Event> getNewEvents(Event startingEvent, OffsetDateTime maximumDateToSchedule,
                                     boolean markPastEvents) {
        //TODO: set Notification for currentEvent at time or immediate if time has passed (Ask to mark?)
        List<Event> newEvents = new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        Event currentEvent = startingEvent;
        boolean notificationIsSet = false;
        while (currentEvent.getPlannedDate().compareTo(maximumDateToSchedule) <= 0) {
            newEvents.add(currentEvent);
            if (!currentEvent.isMarked() && currentEvent.getPlannedDate().compareTo(now) < 0) {
                //TODO: think if we need to communicate it explicitly with user
                currentEvent.setMarked(markPastEvents);
            }

            if (!notificationIsSet
                    && currentEvent.getPlannedDate().compareTo(now) >= 0
                    && !currentEvent.isMarked()
                    && (alarmManager.getNextAlarmClock() == null
                    || alarmManager.getNextAlarmClock().getShowIntent().getCreatorUid() != NOTIFICATION_ID)) {
                setNotification(currentEvent);
                notificationIsSet = true;
            }
            currentEvent = findNextEvent(currentEvent);
        }
        return newEvents;
    }

    public void reviewTypesOfNextEvents(Event editedEvent) {
        Event previousEvent = editedEvent;
        List<Event> nextEvents = eventViewModel.getFutureEvents(previousEvent.getPlannedDate()).getValue();
            for (int i = 0; i < nextEvents.size(); i++) {
                Event currentEvent = nextEvents.get(i);
                currentEvent.setType(findNextEvent(previousEvent).getType());
                previousEvent = currentEvent;
            }
        eventViewModel.insertAll(nextEvents);
    }

    public void rescheduleCalendarStartingFrom(Event startingEvent, OffsetDateTime maximumDateToSchedule,
                                               boolean markPastEvents) {
        eventViewModel.deleteAllFutureEvents(startingEvent.getPlannedDate());

        List<Event> newEvents = getNewEvents(startingEvent,
                maximumDateToSchedule, markPastEvents);

        // todo: use find next event until next event is in visible month, then calculate events for visible month
        eventViewModel.insertAll(newEvents);
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

    private void setNotification(Event event) {
        AlarmManager alarmManager = (AlarmManager) parentActivity.getSystemService(ALARM_SERVICE);

        Intent notifyIntent = new Intent(parentActivity, NotificationAlarmReceiver.class);
        notifyIntent.putExtra("eventType", event.getType().name());
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (parentActivity, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        OffsetTime timeOfNotification = OffsetTime.now().plusSeconds(20);
        long triggerTime = event.getPlannedDate().withHour(timeOfNotification.getHour())
                .withMinute(timeOfNotification.getMinute()).toEpochSecond() * 1000;

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, notifyPendingIntent);
    }
}
