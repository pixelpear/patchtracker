package me.bekrina.patchtracker;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.ArrayList;
import java.util.List;

import me.bekrina.patchtracker.data.Event;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CalendarActivityUnitTest {
    @Rule
    public ActivityTestRule<CalendarActivity> calendarActivityRule =
            new ActivityTestRule(CalendarActivity.class);
    @Test
    public void findVisibleDatesNormalFebruary() {
        OffsetDateTime february2018 = OffsetDateTime.of(2018, 2, 1, 0,
                0, 0, 0, ZoneOffset.UTC);

        OffsetDateTime firstVisibleDate = OffsetDateTime.of(2018, 1, 29, 0,
                0, 0, 0, ZoneOffset.UTC);
        List<OffsetDateTime> visibleDates = makeDatesList(35, firstVisibleDate);

        List<OffsetDateTime> visibleDatesResult = calendarActivityRule.getActivity().findVisibleDates(february2018);

        assertEquals("Failure - Lists are not equal", visibleDates, visibleDatesResult);
    }

    @Test
    public void findVisibleDatesLeapFebruary() {
        OffsetDateTime february2016 = OffsetDateTime.of(2016, 2, 1, 0,
                0, 0, 0, ZoneOffset.UTC);

        OffsetDateTime firstVisibleDate = OffsetDateTime.of(2016, 2, 1, 0,
                0, 0, 0, ZoneOffset.UTC);
        List<OffsetDateTime> visibleDates = makeDatesList(35, firstVisibleDate);

        List<OffsetDateTime> visibleDatesResult = calendarActivityRule.getActivity().findVisibleDates(february2016);

        assertEquals("Failure - Lists are not equal", visibleDates, visibleDatesResult);
    }

    @Test
    public void findCalendarViews() {
        CalendarActivity calendarActivity = calendarActivityRule.getActivity();
        calendarActivity.findCalendarViews();

        assertEquals(42, calendarActivity.daysViews.size());
        assertEquals(R.id.textView_month_name, calendarActivity.monthNameTextView.getId());
    }

    @Test
    public void updateCalendar() {
        // Test events
        Event patchOn = new Event(OffsetDateTime.of(2018, 6, 7, 0,
                0, 0, 0, ZoneOffset.UTC), Event.EventType.PATCH_ON);
        patchOn.setMarked(true);
        Event patchChange = new Event(OffsetDateTime.of(2018, 6, 14, 0,
                0, 0, 0, ZoneOffset.UTC), Event.EventType.PATCH_CHANGE);
        patchChange.setMarked(true);
        Event patchChange2 = new Event(OffsetDateTime.of(2018, 6, 21, 0,
                0, 0, 0, ZoneOffset.UTC), Event.EventType.PATCH_CHANGE);
        Event patchOff = new Event(OffsetDateTime.of(2018, 6, 28, 0,
                0, 0, 0, ZoneOffset.UTC), Event.EventType.PATCH_OFF);
        List<Event> events = new ArrayList<>();
        events.add(patchOn);
        events.add(patchChange);
        events.add(patchChange2);
        events.add(patchOff);

        OffsetDateTime june2018 = OffsetDateTime.of(2018, 6, 1, 0,
                0, 0, 0, ZoneOffset.UTC);

        OffsetDateTime firstVisibleDate = OffsetDateTime.of(2018, 5, 28,
                0, 0, 0, 0, ZoneOffset.UTC);
        List<OffsetDateTime> visibleDates = makeDatesList(35, firstVisibleDate);

        Intent intentToLaunchActivity = new Intent();
        intentToLaunchActivity.putExtra("testCurrentMonth", june2018.getMonthValue());
        intentToLaunchActivity.putExtra("testCurrentYear", june2018.getYear());

        calendarActivityRule.finishActivity();
        calendarActivityRule.launchActivity(intentToLaunchActivity);
        CalendarActivity calendarActivity = calendarActivityRule.getActivity();


        for (int i = 0; i < visibleDates.size(); i++) {
            int dayNumber = Integer.parseInt(calendarActivity.daysViews.get(i).getText().toString());
            assertEquals("Failure - day in " + visibleDates.get(i).getYear() +
                    "/" + visibleDates.get(i).getMonthValue() +
                    "/" + visibleDates.get(i).getDayOfMonth() +
                    " is not equal to " +
                    calendarActivity.daysViews.get(i).getText(),
                    visibleDates.get(i).getDayOfMonth(), dayNumber);
        }
        for (int i = visibleDates.size(); i < calendarActivity.daysViews.size(); i++) {
            assertEquals("", calendarActivity.daysViews.get(i).getText());
        }
    }

    private List<OffsetDateTime> makeDatesList(int numberOfDates, OffsetDateTime firstDate) {
        OffsetDateTime startDate = firstDate;
        List<OffsetDateTime> dates = new ArrayList<>();
        for (int i = 0; i < numberOfDates; i++) {
            dates.add(startDate);
            startDate = startDate.plusDays(1);
        }
        return dates;
    }
}
