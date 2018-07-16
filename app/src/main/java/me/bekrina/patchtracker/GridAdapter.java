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
import android.widget.TextView;

import org.threeten.bp.Month;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.Year;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.bekrina.patchtracker.data.Event;

public class GridAdapter extends ArrayAdapter {
    private static final String TAG = GridAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<OffsetDateTime> visibleDates;
    private Calendar currentCalendar;
    private List<Event> events;
    private Context context;
    public GridAdapter(Context context, List<OffsetDateTime> visibleDates, Calendar currentCalendar, List<Event> events) {
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
        OffsetDateTime dateDT = visibleDates.get(position);

        /*Date date = visibleDates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        int dayOfMonth = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int dateMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int dateYear = dateCalendar.get(Calendar.YEAR);*/

        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
        }

        OffsetDateTime currentDT = OffsetDateTime.now();
        /*int currentMonth = currentDateTime.getMonthValue();;
        int currentYear = currentDateTime.getYear();*/
        if (dateDT.getMonthValue() == currentDT.getMonthValue() && dateDT.getYear() == dateDT.getYear()) {
            view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight));
        } else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        //TODO: is it the same as in CustomCalendarView?
        // Add day to calendar
        TextView cell = (TextView)view.findViewById(R.id.calendar_date_id);
        cell.setText(String.valueOf(dateDT.getDayOfMonth()));
        if (events != null) {
            //Add events to the calendar
            Calendar eventCalendar = Calendar.getInstance();
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                OffsetDateTime eventDateDT = event.getDate();
                //eventCalendar.setTime(event.getDate());
                if (dateDT.getDayOfMonth() == eventDateDT.getDayOfMonth()
                        && dateDT.getMonthValue() == eventDateDT.getMonthValue()) {
                    TrackerApplication application = (TrackerApplication) context.getApplicationContext();
                    if (application.getType() == TrackerApplication.ContraceptionType.PATCH) {
                        Drawable eventImage;
                        if (event.isMarked()) {
                            switch (event.getType()) {
                                case PATCH_ON:
                                    eventImage = context.getDrawable(R.drawable.patch_on);
                                    cell.setBackground(eventImage);
                                    break;
                                case PATCH_CHANGE:
                                    eventImage = context.getDrawable(R.drawable.patch_change);
                                    cell.setBackground(eventImage);
                                    break;
                                case PATCH_OFF:
                                    eventImage = context.getDrawable(R.drawable.patch_off);
                                    cell.setBackground(eventImage);
                                    break;
                            }
                        } else {
                            switch (event.getType()) {
                                case PATCH_ON:
                                    eventImage = context.getDrawable(R.drawable.patch_on_accent);
                                    cell.setBackground(eventImage);
                                    break;
                                case PATCH_CHANGE:
                                    eventImage = context.getDrawable(R.drawable.patch_change_accent);
                                    cell.setBackground(eventImage);
                                    break;
                                case PATCH_OFF:
                                    eventImage = context.getDrawable(R.drawable.patch_off_accent);
                                    cell.setBackground(eventImage);
                                    break;
                            }
                        }
                    }
                }
            }
            /*if (eventCalendar.get(Calendar.DAY_OF_MONTH) && dateMonth == eventCalendar.get(Calendar.MONTH) + 1
                    && dateYear == eventCalendar.get(Calendar.YEAR)) {
                // Note: set viewport proportions equal (height/width)
                TrackerApplication application = (TrackerApplication)context.getApplicationContext();
                if (application.getType() == TrackerApplication.ContraceptionType.PATCH) {
                    Drawable eventImage;
                    if (event.isMarked()) {
                        switch (event.getType()) {
                            case PATCH_ON:
                                eventImage = context.getDrawable(R.drawable.patch_on);
                                cell.setBackground(eventImage);
                                break;
                            case PATCH_CHANGE:
                                eventImage = context.getDrawable(R.drawable.patch_change);
                                cell.setBackground(eventImage);
                                break;
                            case PATCH_OFF:
                                eventImage = context.getDrawable(R.drawable.patch_off);
                                cell.setBackground(eventImage);
                                break;
                        }
                    } else {
                        switch (event.getType()) {
                            case PATCH_ON:
                                eventImage = context.getDrawable(R.drawable.patch_on_accent);
                                cell.setBackground(eventImage);
                                break;
                            case PATCH_CHANGE:
                                eventImage = context.getDrawable(R.drawable.patch_change_accent);
                                cell.setBackground(eventImage);
                                break;
                            case PATCH_OFF:
                                eventImage = context.getDrawable(R.drawable.patch_off_accent);
                                cell.setBackground(eventImage);
                                break;
                        }
                    }*/
                //}
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
