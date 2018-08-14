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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;

import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;

import me.bekrina.patchtracker.data.Event;
import me.bekrina.patchtracker.data.EventViewModel;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class Scheduling {
    FragmentActivity parentActivity;
    NotificationManager notificationManager;
    public static int  NOTIFICATION_ID = 1;
    private static final String ACTION_NOTIFY =
            "com.example.android.standup.ACTION_NOTIFY";
    NotificationCompat.Builder builder;

    public Scheduling(FragmentActivity activity) {
        this.parentActivity = activity;
    }

    public void rescheduleCalendarStartingFrom(Event event, OffsetDateTime maximumDateToSchedule,
                                               boolean markPastEvents) {
        EventViewModel eventViewModel =  ViewModelProviders.of(parentActivity).get(EventViewModel.class);
        List<Event> rescheduledEvents = new ArrayList<>();

        OffsetDateTime now = OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        Event currentEvent = event;

        eventViewModel.deleteAllFutureEvents(currentEvent.getPlannedDate());

        while (currentEvent.getPlannedDate().compareTo(now) < 0) {
            //TODO: think if we need to communicate it explicitly with user
            currentEvent.setMarked(markPastEvents);
            //TODO: if we need here to update or there will be only new events?
            rescheduledEvents.add(currentEvent);
            currentEvent = findNextEvent(currentEvent);
        }
        //TODO: set Notification for currentEvent at time or immediate if time has passed (Ask to mark?)
        notifyIn1();
        while (currentEvent.getPlannedDate().compareTo(maximumDateToSchedule) <= 0) {
            rescheduledEvents.add(currentEvent);
            currentEvent = Scheduling.findNextEvent(currentEvent);
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

    private void notifyIn1() {
        notificationManager = (NotificationManager) parentActivity.getSystemService(NOTIFICATION_SERVICE);

        Intent contentIntent = new Intent(parentActivity, parentActivity.getClass());
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (parentActivity, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        builder = new NotificationCompat.Builder(parentActivity, NOTIFICATION_CHANNEL_ID);

        builder.setSmallIcon(R.drawable.ic_smile)
                .setContentTitle(parentActivity.getString(R.string.notification_title))
                .setContentText(parentActivity.getString(R.string.notification_text))
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_REMINDER);

        Intent notifyIntent = new Intent(ACTION_NOTIFY);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (parentActivity, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) parentActivity.getSystemService(ALARM_SERVICE);

        long triggerTime = SystemClock.elapsedRealtime()
                + 1;

        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

        notificationManager.notify(NOTIFICATION_ID, builder.build());

        //alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        //        triggerTime, repeatInterval, notifyPendingIntent);
    }
}
