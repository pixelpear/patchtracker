package me.bekrina.patchtracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.applikeysolutions.cosmocalendar.view.CalendarView;

public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendarView = findViewById(R.id.calendar);
    }
}
