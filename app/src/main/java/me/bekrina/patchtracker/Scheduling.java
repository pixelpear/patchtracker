package me.bekrina.patchtracker;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.OffsetTime;

import java.util.ArrayList;
import java.util.List;

import me.bekrina.patchtracker.data.Event;
import me.bekrina.patchtracker.data.EventViewModel;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class Scheduling {
    FragmentActivity parentActivity;
    public static int  NOTIFICATION_ID = 1;
    private static final String ACTION_NOTIFY =
            "me.bekrina.patchtracker.ACTION_NOTIFY";
    private AlarmManager alarmManager;

    public Scheduling(FragmentActivity activity) {
        this.parentActivity = activity;
        alarmManager = (AlarmManager) parentActivity.getSystemService(ALARM_SERVICE);
    }

    public void rescheduleCalendarStartingFrom(Event event, OffsetDateTime maximumDateToSchedule,
                                               boolean markPastEvents) {
        EventViewModel eventViewModel =  ViewModelProviders.of(parentActivity).get(EventViewModel.class);
        List<Event> rescheduledEvents = new ArrayList<>();

        OffsetDateTime now = OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        eventViewModel.deleteAllFutureEvents(event.getPlannedDate());

        //TODO: set Notification for currentEvent at time or immediate if time has passed (Ask to mark?)

        Event currentEvent = event;
        boolean notificationIsSet = false;
        while (currentEvent.getPlannedDate().compareTo(maximumDateToSchedule) <= 0) {
            rescheduledEvents.add(currentEvent);
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

        // todo: use find next event until next event is in visible month, then calculate events for visible month
        eventViewModel.insertAll(rescheduledEvents);
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
