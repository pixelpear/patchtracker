package me.bekrina.patchtracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import me.bekrina.patchtracker.data.Event;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationAlarmReceiver extends BroadcastReceiver {
    private static String NOTIFICATION_CHANNEL_ID = "patchtracker_events";
    private static String NOTIFICATION_CHANNEL_NAME = "PatchTracker_Events";
    private static String NOTIFICATION_CHANNEL_DESCRIPTION = "Channel for PatchTracker events notifications";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 500});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        String eventType = intent.getStringExtra("eventType");
        String notificationTitle;
        if (eventType != Event.EventType.NO_PATCH.name()) {
            notificationTitle = context.getString(R.string.notification_title_next_patch);
        } else {
            notificationTitle = context.getString(R.string.notification_title_no_patch);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        // Create an Intent for the activity you want to start
        Intent contentIntent = new Intent(context, CalendarActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(contentIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent contentPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.drawable.ic_smile)
                .setContentTitle(notificationTitle)
                .setContentText(context.getString(R.string.notification_text))
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_REMINDER);

        notificationManager.notify(Scheduling.NOTIFICATION_ID, builder.build());
    }
}
