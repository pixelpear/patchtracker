package me.bekrina.patchtracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;

import me.bekrina.patchtracker.data.Event;
import me.bekrina.patchtracker.data.EventViewModel;


public class CalendarActivity extends AppCompatActivity {
    private OffsetDateTime currentMonthDateTime = OffsetDateTime.now();
    private List<Event> events;
    private List<TextView> views = new ArrayList<>();
    //private ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        //layout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_calendar, null);
        //final CustomCalendarView calendarView = findViewById(R.id.calendar);
        EventViewModel eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable final List<Event> events) {
                // Update the cached copy of the events in the view.
                //calendarView.setEvents(events);
                updateCalendar(currentMonthDateTime, events);
            }
        });
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


    private void updateCalendar(OffsetDateTime currentMonthDateTime, List<Event> events) {
        this.events = events;

        if (views.size() == 0) {
            views.add((TextView) findViewById(R.id.textView_1));
            views.add((TextView) findViewById(R.id.textView_2));
            views.add((TextView) findViewById(R.id.textView_3));
            views.add((TextView) findViewById(R.id.textView_4));
            views.add((TextView) findViewById(R.id.textView_5));
            views.add((TextView) findViewById(R.id.textView_6));
            views.add((TextView) findViewById(R.id.textView_7));
            views.add((TextView) findViewById(R.id.textView_8));
            views.add((TextView) findViewById(R.id.textView_9));
            views.add((TextView) findViewById(R.id.textView_10));
            views.add((TextView) findViewById(R.id.textView_11));
            views.add((TextView) findViewById(R.id.textView_12));
            views.add((TextView) findViewById(R.id.textView_13));
            views.add((TextView) findViewById(R.id.textView_14));
            views.add((TextView) findViewById(R.id.textView_15));
            views.add((TextView) findViewById(R.id.textView_16));
            views.add((TextView) findViewById(R.id.textView_17));
            views.add((TextView) findViewById(R.id.textView_18));
            views.add((TextView) findViewById(R.id.textView_19));
            views.add((TextView) findViewById(R.id.textView_20));
            views.add((TextView) findViewById(R.id.textView_21));
            views.add((TextView) findViewById(R.id.textView_22));
            views.add((TextView) findViewById(R.id.textView_23));
            views.add((TextView) findViewById(R.id.textView_24));
            views.add((TextView) findViewById(R.id.textView_25));
            views.add((TextView) findViewById(R.id.textView_26));
            views.add((TextView) findViewById(R.id.textView_27));
            views.add((TextView) findViewById(R.id.textView_28));
            views.add((TextView) findViewById(R.id.textView_29));
            views.add((TextView) findViewById(R.id.textView_30));
            views.add((TextView) findViewById(R.id.textView_31));
            views.add((TextView) findViewById(R.id.textView_32));
            views.add((TextView) findViewById(R.id.textView_33));
            views.add((TextView) findViewById(R.id.textView_34));
            views.add((TextView) findViewById(R.id.textView_35));
            views.add((TextView) findViewById(R.id.textView_36));
            views.add((TextView) findViewById(R.id.textView_37));
            views.add((TextView) findViewById(R.id.textView_38));
            views.add((TextView) findViewById(R.id.textView_39));
            views.add((TextView) findViewById(R.id.textView_40));
            views.add((TextView) findViewById(R.id.textView_41));
            views.add((TextView) findViewById(R.id.textView_42));
        }

        List<OffsetDateTime> visibleDates = calculateVisibleDates(currentMonthDateTime);
        //for (int i = 0; i < visibleDates.size(); i++) {
        TextView textView;
        for (int i = 0; i < views.size(); i++) {
            textView = views.get(i);
            textView.setText(String.valueOf(i + 1));
            textView.setBackground(this.getDrawable(R.drawable.patch_on));
        }

    }
}
