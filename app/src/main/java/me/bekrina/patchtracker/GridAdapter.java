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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.bekrina.patchtracker.data.Event;

public class GridAdapter extends ArrayAdapter {
    private static final String TAG = GridAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<OffsetDateTime> visibleDates;
    private OffsetDateTime mainDateTime;
    private List<Event> events;
    private Context context;

    public GridAdapter(Context context, List<Event> events) {
        super(context, R.layout.single_cell_layout);
        this.context = context;
        this.events = events;
        mainDateTime = OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        calculateVisibleDates();

        mInflater = LayoutInflater.from(context);
    }

    private void calculateVisibleDates() {
        visibleDates = new ArrayList<>();

        mainDateTime = mainDateTime.withDayOfMonth(1);
        int visibleDaysInPreviousMonth = mainDateTime.withDayOfMonth(1).getDayOfWeek().getValue() - 1;
        mainDateTime = mainDateTime.minusDays(visibleDaysInPreviousMonth);

        OffsetDateTime lastDayOfMonth = OffsetDateTime.now();
        int length = lastDayOfMonth.getMonth().maxLength();
        lastDayOfMonth = lastDayOfMonth.withDayOfMonth(lastDayOfMonth.getMonth().maxLength());
        int visibleDaysInNextMonth = 7 - lastDayOfMonth.getDayOfWeek().getValue();
        int maxCalendarCell = visibleDaysInPreviousMonth + lastDayOfMonth.getDayOfMonth()
                + visibleDaysInNextMonth;

        while(visibleDates.size() < maxCalendarCell){
            visibleDates.add(mainDateTime);
            mainDateTime = mainDateTime.plusDays(1);
        }
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OffsetDateTime dateDT = visibleDates.get(position);

        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
        }

        OffsetDateTime currentDT = OffsetDateTime.now();
        if (dateDT.getMonthValue() == currentDT.getMonthValue() && dateDT.getYear() == dateDT.getYear()) {
            view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight));
        } else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        // Add day to calendar
        TextView cell = (TextView)view.findViewById(R.id.calendar_date_id);
        cell.setText(String.valueOf(dateDT.getDayOfMonth()));
        if (events != null) {
            //Add events to the calendar
            //Calendar eventCalendar = Calendar.getInstance();
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                OffsetDateTime eventDateDT = event.getDate();
                if (dateDT.getDayOfMonth() == eventDateDT.getDayOfMonth()
                        && dateDT.getMonthValue() == eventDateDT.getMonthValue()) {
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
    public void drawNewMonth(Month monthToDraw) {
        mainDateTime = mainDateTime.withMonth(monthToDraw.getValue());
    }
}
