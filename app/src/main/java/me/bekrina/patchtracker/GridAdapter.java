package me.bekrina.patchtracker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.bekrina.patchtracker.model.Event;

public class GridAdapter extends ArrayAdapter {
    private static final String TAG = GridAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<Date> visibleDates;
    private Calendar currentCalendar;
    private List<Event> events;
    private Context context;
    public GridAdapter(Context context, List<Date> visibleDates, Calendar currentCalendar, List<Event> events) {
        super(context, R.layout.single_cell_layout);
        this.visibleDates = visibleDates;
        this.currentCalendar = currentCalendar;
        this.events = events;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Date date = visibleDates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        int dayOfMonth = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int dateMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int dateYear = dateCalendar.get(Calendar.YEAR);

        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
        }

        int currentMonth = this.currentCalendar.get(Calendar.MONTH) + 1;
        int currentYear = this.currentCalendar.get(Calendar.YEAR);
        if (dateMonth == currentMonth && dateYear == currentYear) {
            view.setBackgroundColor(Color.parseColor("#FF5733"));
        } else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        // Add day to calendar
        TextView cell = (TextView)view.findViewById(R.id.calendar_date_id);
        cell.setText(String.valueOf(dayOfMonth));

        //Add events to the calendar
        Calendar eventCalendar = Calendar.getInstance();
        for (int i = 0; i < events.size(); i++) {
            eventCalendar.setTime(events.get(i).getDate());
            if (dayOfMonth == eventCalendar.get(Calendar.DAY_OF_MONTH) && dateMonth == eventCalendar.get(Calendar.MONTH) + 1
                    && dateYear == eventCalendar.get(Calendar.YEAR)) {
                // Note: set viewport proportions equal (height/width)
                Drawable myDrawable = context.getDrawable(R.drawable.patch_normal);
                cell.setBackground(myDrawable);
            }
        }
        return view;
    }
    @Override
    public int getCount() {
        return visibleDates.size();
    }
    @Nullable
    @Override
    public Object getItem(int position) {
        return visibleDates.get(position);
    }
    @Override
    public int getPosition(Object item) {
        return visibleDates.indexOf(item);
    }
}
