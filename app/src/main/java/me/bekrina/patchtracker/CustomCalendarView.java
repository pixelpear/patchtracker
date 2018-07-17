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

import org.threeten.bp.OffsetDateTime;

import java.util.List;

import me.bekrina.patchtracker.data.Event;

public class CustomCalendarView extends LinearLayout {
    private static final String TAG = CustomCalendarView.class.getSimpleName();
    private Context context;
    private ImageView previousButton;
    private ImageView nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private OffsetDateTime mainDateTime;// = OffsetDateTime.now();
    private List<Event> events;
    private GridAdapter calendarAdapter;

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeUiLayout();
        calendarAdapter = new GridAdapter(context, events);
        calendarGridView.setAdapter(calendarAdapter);
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();
        Log.d(TAG, "I need to call this method");
    }

    public void setEvents (List<Event> events) {
        this.events = events;
        calendarAdapter.setEvents(events);
        calendarAdapter.notifyDataSetChanged();
    }

    private void initializeUiLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        previousButton = (ImageView)view.findViewById(R.id.previous_month);
        nextButton = (ImageView)view.findViewById(R.id.next_month);
        currentDate = (TextView)view.findViewById(R.id.current_month);
        currentDate.setText(OffsetDateTime.now().getMonth().toString());
        calendarGridView = (GridView)view.findViewById(R.id.calendar_grid);
    }

    private void setPreviousButtonClickEvent(){
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDateTime.minusMonths(1);
                calendarAdapter.drawNewMonth(mainDateTime.getMonth());
                calendarAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setNextButtonClickEvent(){
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDateTime.plusMonths(1);
                calendarAdapter.drawNewMonth(mainDateTime.getMonth());
                calendarAdapter.notifyDataSetChanged();
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
}
