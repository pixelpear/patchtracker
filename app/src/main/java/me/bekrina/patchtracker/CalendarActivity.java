package me.bekrina.patchtracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.List;

import me.bekrina.patchtracker.data.Event;
import me.bekrina.patchtracker.data.EventViewModel;


public class CalendarActivity extends AppCompatActivity {
    private OffsetDateTime currentMonthDateTime = OffsetDateTime.now();
    private List<Event> events;
    private List<TextView> daysOfMonthViews = new ArrayList<>();
    private TextView monthNameTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        EventViewModel eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);

        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable final List<Event> events) {
                updateCalendar(currentMonthDateTime, events);
            }
        });

        setNextButtonClickEvent();
        setPreviousButtonClickEvent();
    }

    private List<OffsetDateTime> calculateVisibleDates(OffsetDateTime currentMonthDateTime) {
        List<OffsetDateTime> visibleDates;
        visibleDates = new ArrayList<>();

        currentMonthDateTime = currentMonthDateTime.withDayOfMonth(1);
        int visibleDaysInPreviousMonth = currentMonthDateTime.withDayOfMonth(1).getDayOfWeek().getValue() - 1;
        currentMonthDateTime = currentMonthDateTime.minusDays(visibleDaysInPreviousMonth);

        OffsetDateTime lastDayOfMonth = OffsetDateTime.now();
        int length = lastDayOfMonth.getMonth().maxLength();
        lastDayOfMonth = lastDayOfMonth.withDayOfMonth(lastDayOfMonth.getMonth().maxLength());
        int visibleDaysInNextMonth = 7 - lastDayOfMonth.getDayOfWeek().getValue();

        int maxCalendarCell = visibleDaysInPreviousMonth + lastDayOfMonth.getDayOfMonth()
                + visibleDaysInNextMonth;

        while(visibleDates.size() < maxCalendarCell){
            visibleDates.add(currentMonthDateTime);
            currentMonthDateTime = currentMonthDateTime.plusDays(1);
        }
        return visibleDates;
    }

    private void setPreviousButtonClickEvent(){
        ImageView previousButton = (ImageView)findViewById(R.id.arrow_left);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMonthDateTime = currentMonthDateTime.minusMonths(1);
                updateCalendar(currentMonthDateTime, events);
            }
        });
    }

    private void setNextButtonClickEvent() {
        ImageView nextButton = (ImageView)findViewById(R.id.arrow_right);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMonthDateTime = currentMonthDateTime.plusMonths(1);
                updateCalendar(currentMonthDateTime, events);
            }
        });
    }

    private void findCalendarViews() {
        if (monthNameTextView == null) {
            monthNameTextView = findViewById(R.id.textView_month_name);
        }
        if (daysOfMonthViews.size() == 0) {
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_1));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_2));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_3));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_4));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_5));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_6));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_7));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_8));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_9));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_10));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_11));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_12));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_13));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_14));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_15));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_16));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_17));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_18));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_19));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_20));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_21));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_22));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_23));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_24));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_25));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_26));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_27));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_28));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_29));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_30));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_31));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_32));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_33));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_34));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_35));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_36));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_37));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_38));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_39));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_40));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_41));
            daysOfMonthViews.add((TextView) findViewById(R.id.textView_42));
        }
    }

    private void updateCalendar(OffsetDateTime currentMonthDateTime, List<Event> events) {
        this.events = events;

        findCalendarViews();
        List<OffsetDateTime> visibleDates = calculateVisibleDates(currentMonthDateTime);

        monthNameTextView.setText(currentMonthDateTime.getMonth().getDisplayName(
                TextStyle.FULL_STANDALONE, getResources().getConfiguration().locale));

        // Go through visible dates
        for(int i = 0; i < visibleDates.size(); i++) {
            TextView textView = daysOfMonthViews.get(i);
            OffsetDateTime date = visibleDates.get(i);

            // Mark day from non-current month with gray color
            if (date.getMonthValue() != currentMonthDateTime.getMonthValue()) {
                textView.setBackgroundColor(getResources().getColor(R.color.colorDisabledGray));
            } else {
                textView.setBackgroundColor(getResources().getColor(R.color.background));
            }

            // Set day of month
            textView.setText(String.valueOf(date.getDayOfMonth()));

            // Go through events to see if we need to show an event on this day
            for (int y = 0; y < events.size(); y++) {
                Event event = events.get(y);
                OffsetDateTime eventDate = event.getDate();
                // If event is for current date
                if (date.getDayOfMonth() == eventDate.getDayOfMonth()
                        && date.getMonthValue() == eventDate.getMonthValue()) {
                    Drawable eventImage;
                    // See its type and set an image
                    if (event.isMarked()) {
                        switch (event.getType()) {
                            case PATCH_ON:
                                eventImage = getDrawable(R.drawable.patch_on);
                                textView.setBackground(eventImage);
                                break;
                            case PATCH_CHANGE:
                                eventImage = getDrawable(R.drawable.patch_change);
                                textView.setBackground(eventImage);
                                break;
                            case PATCH_OFF:
                                eventImage = getDrawable(R.drawable.patch_off);
                                textView.setBackground(eventImage);
                                break;
                        }
                    } else {
                        switch (event.getType()) {
                            case PATCH_ON:
                                eventImage = getDrawable(R.drawable.patch_on_accent);
                                textView.setBackground(eventImage);
                                break;
                            case PATCH_CHANGE:
                                eventImage = getDrawable(R.drawable.patch_change_accent);
                                textView.setBackground(eventImage);
                                break;
                            case PATCH_OFF:
                                eventImage = getDrawable(R.drawable.patch_off_accent);
                                textView.setBackground(eventImage);
                                break;
                        }
                    }
                }
            }
        }
    }
}
