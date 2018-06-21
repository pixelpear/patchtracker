package me.bekrina.patchtracker;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.bekrina.patchtracker.model.ConEvent;

public class CustomCalendarView extends LinearLayout {
    private Context context;
    private static final String TAG = CustomCalendarView.class.getSimpleName();
    private ImageView previousButton;
    private ImageView nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private Calendar mainCalendar = Calendar.getInstance(Locale.ENGLISH);
    private int maxCalendarCell = 42;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    //private DatabaseQuery mQuery;  // mock
    private GridAdapter mAdapter;

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeUiLayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();
        Log.d(TAG, "I need to call this method");
    }

    private void initializeUiLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        previousButton = (ImageView)view.findViewById(R.id.previous_month);
        nextButton = (ImageView)view.findViewById(R.id.next_month);
        currentDate = (TextView)view.findViewById(R.id.display_current_date);
        calendarGridView = (GridView)view.findViewById(R.id.calendar_grid);
    }

    private void setPreviousButtonClickEvent(){
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainCalendar.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();
            }
        });
    }

    private void setNextButtonClickEvent(){
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainCalendar.add(Calendar.MONTH, 1);
                setUpCalendarAdapter();
            }
        });
    }

    private void setGridCellClickEvents(){
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "Clicked " + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpCalendarAdapter(){
        List<Date> visibleDates = new ArrayList<Date>();

        Calendar calendar = (Calendar) mainCalendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // If first day of the month is not first in a week, we need to find out
        // how many days of previous month are visible on screen
        // Calculate amount of visible days in previous month
        int visibleDaysInPreviousMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        // Set calendar to first visible day
        calendar.add(Calendar.DAY_OF_MONTH, -visibleDaysInPreviousMonth);
        // Same is for the end of the calendar. If the last day of the month is not last day of week
        // we need to find out how many days from next month are visible
        Calendar lastDayOfMonth = (Calendar) mainCalendar.clone();
        lastDayOfMonth.set(Calendar.DAY_OF_MONTH, mainCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int visibleDaysInNextMonth = 7 - lastDayOfMonth.get(Calendar.DAY_OF_WEEK);
        // So we can say how many days will be shown on calendar
        maxCalendarCell = visibleDaysInPreviousMonth + lastDayOfMonth.get(Calendar.DAY_OF_MONTH) + visibleDaysInNextMonth;

        // Put all visible dates in array for GridAdapter to use them in views
        while(visibleDates.size() < maxCalendarCell){
            visibleDates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d(TAG, "Number of date " + visibleDates.size());

        String sDate = formatter.format(mainCalendar.getTime());
        currentDate.setText(sDate);

        // TODO: here should be DB query or smth alike
        List<ConEvent> events = new ArrayList<>();
        events.add(new ConEvent(new Date(1529583360000L), ConEvent.EventType.PATCH_ON));
        TrackerApplication application = (TrackerApplication)context.getApplicationContext();
        application.setType(TrackerApplication.ContraceptionType.PATCH);

        mAdapter = new GridAdapter(context, visibleDates, mainCalendar, events);
        calendarGridView.setAdapter(mAdapter);
    }
}
