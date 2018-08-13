package me.bekrina.patchtracker;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import me.bekrina.patchtracker.data.Event;
import me.bekrina.patchtracker.data.EventViewModel;

import static me.bekrina.patchtracker.data.Event.EventType.NO_PATCH;
import static me.bekrina.patchtracker.data.Event.EventType.PATCH_1;
import static me.bekrina.patchtracker.data.Event.EventType.PATCH_2;
import static me.bekrina.patchtracker.data.Event.EventType.PATCH_3;

public class EditEventActivity  extends AppCompatActivity {
    RadioGroup typeRadioGroup;
    CheckBox markedCheckbox;
    TextView dateTextView;
    ZonedDateTime date;
    Event event;
    boolean createNeuEvent;
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static String EVENT_KEY = "event";
    public static String CALENDAR_DATE_KEY = "calendar_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent);

        typeRadioGroup = findViewById(R.id.event_type_radio_group);
        markedCheckbox = findViewById(R.id.marked);
        dateTextView = findViewById(R.id.date);

        Intent intent = getIntent();
        if (intent.hasExtra(EVENT_KEY)) {
            event = intent.getParcelableExtra(EVENT_KEY);

            switch (event.getType()) {
                case PATCH_1:
                    typeRadioGroup.check(R.id.patch_1_radio);
                    break;
                case PATCH_2:
                    typeRadioGroup.check(R.id.patch_2_radio);
                    break;
                case PATCH_3:
                    typeRadioGroup.check(R.id.patch_3_radio);
                    break;
                case NO_PATCH:
                    typeRadioGroup.check(R.id.no_patch_radio);
                    break;
            }
            markedCheckbox.setChecked(event.isMarked());
            dateTextView.setText(event.getPlannedDate().format(dateFormatter));
            createNeuEvent = false;
        } else {
            String calendarDate = intent.getStringExtra(CALENDAR_DATE_KEY);
            dateTextView.setText(calendarDate);
            createNeuEvent = true;
        }


        ZonedDateTime now = ZonedDateTime.now();

        LocalDate localDate = LocalDate.parse(dateTextView.getText(), dateFormatter);
        date = ZonedDateTime.of(localDate, LocalTime.MIDNIGHT, now.getZone());

        if (date.getDayOfYear() <= now.getDayOfYear() && date.getYear() <= now.getYear()) {
            markedCheckbox.setVisibility(View.VISIBLE);
        } else {
            markedCheckbox.setVisibility(View.INVISIBLE);
        }
        setSaveButtonClickEvent();
    }

    private void setSaveButtonClickEvent () {
        Button previousButton = findViewById(R.id.save_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventViewModel eventViewModel = ViewModelProviders.of(EditEventActivity.this)
                        .get(EventViewModel.class);
                Event eventToSave;
                if (createNeuEvent) {
                    eventToSave = createEvent();
                    eventViewModel.insertAll(eventToSave);
                } else {
                    eventToSave = editEvent(event);
                    eventViewModel.insertAll(eventToSave);
                }
                Scheduling scheduling = new Scheduling(EditEventActivity.this);
                scheduling.rescheduleCalendarStartingFrom(eventToSave);
                EditEventActivity.this.finish();
            }
        });
    }

    private Event createEvent() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime dateOffset = date.toOffsetDateTime()
                .withHour(now.getHour())
                .withMinute(now.getMinute());

        switch (typeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.patch_1_radio:
                event = new Event(dateOffset, PATCH_1);
                break;
            case R.id.patch_2_radio:
                event = new Event(dateOffset, PATCH_2);
                break;
            case R.id.patch_3_radio:
                event = new Event(dateOffset, PATCH_3);
                break;
            case R.id.no_patch_radio:
                event = new Event(dateOffset, Event.EventType.NO_PATCH);
        }

        if (date.getDayOfYear() <= now.getDayOfYear() && date.getYear() <= now.getYear()) {
            event.setMarked(markedCheckbox.isChecked());
        }
        return event;
    }

    private Event editEvent(Event event) {
        switch (typeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.patch_1_radio:
                event.setType(PATCH_1);
                break;
            case R.id.patch_2_radio:
                event.setType(PATCH_2);
                break;
            case R.id.patch_3_radio:
                event.setType(PATCH_3);
                break;
            case R.id.no_patch_radio:
                event.setType(NO_PATCH);
                break;
        }
        event.setMarked(markedCheckbox.isChecked());
        return event;
    }
}
