package me.bekrina.patchtracker;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.Year;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.List;

import me.bekrina.patchtracker.data.Event;
import me.bekrina.patchtracker.data.EventViewModel;

import static android.support.v4.app.NotificationCompat.CATEGORY_REMINDER;


public class CalendarActivity extends AppCompatActivity {
    private OffsetDateTime visualisingMonth = OffsetDateTime.now().withDayOfMonth(1).withHour(0)
            .withMinute(0).withSecond(0).withNano(0);
    private List<Event> events;
    protected List<TextView> daysViews = new ArrayList<>();
    protected TextView monthNameTextView;
    Scheduling scheduling;

    NotificationManager mNotificationManager;
    public static int  NOTIFICATION_ID = 1;
    private static final String ACTION_NOTIFY =
            "com.example.android.standup.ACTION_NOTIFY";
    NotificationCompat.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Testing logic
        // If intent has this extras this run is a test
        Intent intent = getIntent();
        if (intent.hasExtra("testCurrentMonth") && intent.hasExtra("testCurrentYear")) {
            int month = intent.getIntExtra("testCurrentMonth", 0);
            int year = intent.getIntExtra("testCurrentYear", 0);
            visualisingMonth = OffsetDateTime.of(year, month,
                    1, 0, 0, 0, 0, ZoneOffset.UTC);
        }

        EventViewModel eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);

        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable final List<Event> events) {
                drawCalendar(visualisingMonth, events);
            }
        });

        setNextButtonClickEvent();
        setPreviousButtonClickEvent();

        scheduling = new Scheduling(CalendarActivity.this);
        //notifyIn15();
    }

    private void notifyIn15() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent contentIntent = new Intent(this, CalendarActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        builder = new NotificationCompat.Builder(this, CATEGORY_REMINDER)
                .setSmallIcon(R.drawable.ic_smile)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        Intent notifyIntent = new Intent(ACTION_NOTIFY);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long triggerTime = SystemClock.elapsedRealtime()
                + AlarmManager.INTERVAL_FIFTEEN_MINUTES;

        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime, repeatInterval, notifyPendingIntent);
    }

    private void setPreviousButtonClickEvent(){
        ImageView previousButton = findViewById(R.id.arrow_left);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualisingMonth = visualisingMonth.minusMonths(1);
                drawCalendar(visualisingMonth, events);
            }
        });
    }

    private void setNextButtonClickEvent() {
        ImageView nextButton = findViewById(R.id.arrow_right);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualisingMonth = visualisingMonth.plusMonths(1);
                scheduling.rescheduleCalendarStartingFrom(events.get(0),
                        visualisingMonth.plusMonths(1).withDayOfMonth(6), false);
            }
        });
    }

    private void setCellClickEvent(final View cell, final OffsetDateTime date) {
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String calendarDate = date.format(EditEventActivity.dateFormatter);
                AddEventDialog addEventDialog = AddEventDialog.newInstance(calendarDate);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(addEventDialog, this.getClass().getName());
                fragmentTransaction.commit();
            }
        });
    }

    private void setCellClickEvent(final View cell, final Event event) {
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventActionsDialog eventActionsDialog = EventActionsDialog.newInstance(event);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(eventActionsDialog, this.getClass().getName());
                fragmentTransaction.commit();
            }
        });
    }

    protected List<OffsetDateTime> findVisibleDates(OffsetDateTime currentMonth) {
        OffsetDateTime currentMonthFirstDay = currentMonth.withDayOfMonth(1);
        int visibleDaysInPreviousMonth = currentMonthFirstDay.getDayOfWeek().getValue() - 1;

        int lengthOfCurrentMonth;
        Year currentYear = Year.of(currentMonth.getYear());
        if (!currentYear.isLeap() && currentMonth.getMonthValue() == 2) {
            lengthOfCurrentMonth = 28;
        } else {
            lengthOfCurrentMonth = currentMonth.getMonth().maxLength();
        }

        OffsetDateTime lastDayOfMonth = currentMonth.withDayOfMonth(lengthOfCurrentMonth);
        int visibleDaysInNextMonth = 7 - lastDayOfMonth.getDayOfWeek().getValue();

        int amountOfVisibleDates = visibleDaysInPreviousMonth + lengthOfCurrentMonth
                + visibleDaysInNextMonth;

        List<OffsetDateTime> visibleDates = new ArrayList<>();
        OffsetDateTime earliestVisibleDate = currentMonthFirstDay.minusDays(visibleDaysInPreviousMonth);
        while(visibleDates.size() < amountOfVisibleDates){
            visibleDates.add(earliestVisibleDate);
            earliestVisibleDate = earliestVisibleDate.plusDays(1);
        }
        return visibleDates;
    }

    protected void findCalendarViews() {
        if (monthNameTextView == null) {
            monthNameTextView = findViewById(R.id.textView_month_name);
        }
        if (daysViews.size() == 0) {
            daysViews.add((TextView) findViewById(R.id.textView_1));
            daysViews.add((TextView) findViewById(R.id.textView_2));
            daysViews.add((TextView) findViewById(R.id.textView_3));
            daysViews.add((TextView) findViewById(R.id.textView_4));
            daysViews.add((TextView) findViewById(R.id.textView_5));
            daysViews.add((TextView) findViewById(R.id.textView_6));
            daysViews.add((TextView) findViewById(R.id.textView_7));
            daysViews.add((TextView) findViewById(R.id.textView_8));
            daysViews.add((TextView) findViewById(R.id.textView_9));
            daysViews.add((TextView) findViewById(R.id.textView_10));
            daysViews.add((TextView) findViewById(R.id.textView_11));
            daysViews.add((TextView) findViewById(R.id.textView_12));
            daysViews.add((TextView) findViewById(R.id.textView_13));
            daysViews.add((TextView) findViewById(R.id.textView_14));
            daysViews.add((TextView) findViewById(R.id.textView_15));
            daysViews.add((TextView) findViewById(R.id.textView_16));
            daysViews.add((TextView) findViewById(R.id.textView_17));
            daysViews.add((TextView) findViewById(R.id.textView_18));
            daysViews.add((TextView) findViewById(R.id.textView_19));
            daysViews.add((TextView) findViewById(R.id.textView_20));
            daysViews.add((TextView) findViewById(R.id.textView_21));
            daysViews.add((TextView) findViewById(R.id.textView_22));
            daysViews.add((TextView) findViewById(R.id.textView_23));
            daysViews.add((TextView) findViewById(R.id.textView_24));
            daysViews.add((TextView) findViewById(R.id.textView_25));
            daysViews.add((TextView) findViewById(R.id.textView_26));
            daysViews.add((TextView) findViewById(R.id.textView_27));
            daysViews.add((TextView) findViewById(R.id.textView_28));
            daysViews.add((TextView) findViewById(R.id.textView_29));
            daysViews.add((TextView) findViewById(R.id.textView_30));
            daysViews.add((TextView) findViewById(R.id.textView_31));
            daysViews.add((TextView) findViewById(R.id.textView_32));
            daysViews.add((TextView) findViewById(R.id.textView_33));
            daysViews.add((TextView) findViewById(R.id.textView_34));
            daysViews.add((TextView) findViewById(R.id.textView_35));
            daysViews.add((TextView) findViewById(R.id.textView_36));
            daysViews.add((TextView) findViewById(R.id.textView_37));
            daysViews.add((TextView) findViewById(R.id.textView_38));
            daysViews.add((TextView) findViewById(R.id.textView_39));
            daysViews.add((TextView) findViewById(R.id.textView_40));
            daysViews.add((TextView) findViewById(R.id.textView_41));
            daysViews.add((TextView) findViewById(R.id.textView_42));
        }
    }

    protected void drawCalendar(OffsetDateTime currentMonth, List<Event> events) {
        this.events = events;

        findCalendarViews();
        List<OffsetDateTime> visibleDates = findVisibleDates(currentMonth);

        monthNameTextView.setText(currentMonth.getMonth().getDisplayName(
                TextStyle.FULL_STANDALONE, getResources().getConfiguration().locale));

        // Go through visible dates
        for(int i = 0; i < visibleDates.size(); i++) {
            TextView textView = daysViews.get(i);
            OffsetDateTime date = visibleDates.get(i);
            setCellClickEvent(textView, date);

            // Mark day from non-current month with gray color
            if (date.getMonthValue() != currentMonth.getMonthValue()) {
                textView.setBackgroundColor(getResources().getColor(R.color.colorDisabledGray));
            } else {
                textView.setBackgroundColor(getResources().getColor(R.color.background));
            }

            // Set day of month
            textView.setText(String.valueOf(date.getDayOfMonth()));

            // Go through events to see if we need to show an event on this day
            for (int y = 0; y < events.size(); y++) {
                Event event = events.get(y);
                OffsetDateTime eventDate = event.getPlannedDate();
                // If event is for current date
                if (date.getDayOfMonth() == eventDate.getDayOfMonth()
                        && date.getMonthValue() == eventDate.getMonthValue()
                        && date.getYear() == eventDate.getYear()) {
                    setCellClickEvent(textView, event);
                    Drawable eventImage;
                    // See its type and set an image
                    if (event.isMarked()) {
                        switch (event.getType()) {
                            case PATCH_1:
                                eventImage = getDrawable(R.drawable.patch_on);
                                textView.setBackground(eventImage);
                                break;
                            case PATCH_2:
                                eventImage = getDrawable(R.drawable.patch_change);
                                textView.setBackground(eventImage);
                                break;
                            case PATCH_3:
                                eventImage = getDrawable(R.drawable.patch_change);
                                textView.setBackground(eventImage);
                                break;
                            case NO_PATCH:
                                eventImage = getDrawable(R.drawable.patch_off);
                                textView.setBackground(eventImage);
                                break;
                        }
                    } else {
                        switch (event.getType()) {
                            case PATCH_1:
                                eventImage = getDrawable(R.drawable.patch_on_accent);
                                textView.setBackground(eventImage);
                                break;
                            case PATCH_2:
                                eventImage = getDrawable(R.drawable.patch_change_accent);
                                textView.setBackground(eventImage);
                                break;
                            case PATCH_3:
                                eventImage = getDrawable(R.drawable.patch_change_accent);
                                textView.setBackground(eventImage);
                                break;
                            case NO_PATCH:
                                eventImage = getDrawable(R.drawable.patch_off_accent);
                                textView.setBackground(eventImage);
                                break;
                        }
                    }
                }
            }
        }
        //  Wipe out data from unused views
        if (visibleDates.size() < daysViews.size()) {
            for (int i = visibleDates.size(); i < daysViews.size(); i++) {
                daysViews.get(i).setText("");
                daysViews.get(i).setBackgroundColor(getResources().getColor(R.color.background));
            }
        }
    }
}

