package me.bekrina.patchtracker;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.temporal.ChronoField;

import me.bekrina.patchtracker.data.Event;
import me.bekrina.patchtracker.data.EventViewModel;

public class EditEventActivity  extends AppCompatActivity {
    RadioGroup typeRadioGroup;
    CheckBox markedCheckbox;
    TextView dateTextView;
    ZonedDateTime date;
    Event event;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent);

        typeRadioGroup = findViewById(R.id.event_type_radio_group);
        markedCheckbox = findViewById(R.id.marked);
        dateTextView = findViewById(R.id.date);

        Intent intent = getIntent();
        if (intent.hasExtra("event")) {
            event = intent.getParcelableExtra("event");

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
            dateTextView.setText(event.getDate().format(formatter));
        }


        ZonedDateTime now = ZonedDateTime.now();

        LocalDate localDate = LocalDate.parse(dateTextView.getText(), formatter);
        date = ZonedDateTime.of(localDate, LocalTime.MIDNIGHT, now.getZone());

        if (date.getDayOfYear() >= now.getDayOfYear() && date.getYear() >= now.getYear()) {
            markedCheckbox.setVisibility(View.VISIBLE);
        } else {
            markedCheckbox.setVisibility(View.INVISIBLE);
            markedCheckbox.setChecked(true);
        }
        setSaveButtonClickEvent();
    }

        private void setSaveButtonClickEvent () {
            Button previousButton = findViewById(R.id.save_button);
            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Event eventToSave = createEvent();
                    EventViewModel eventViewModel = ViewModelProviders.of(EditEventActivity.this)
                            .get(EventViewModel.class);
                    eventViewModel.insert(eventToSave);
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
                    event = new Event(dateOffset, Event.EventType.PATCH_1);
                    event.setMarked(markedCheckbox.isChecked());
                    return event;
                case R.id.patch_2_radio:
                    event = new Event(dateOffset, Event.EventType.PATCH_2);
                    event.setMarked(markedCheckbox.isChecked());
                    return event;
                case R.id.patch_3_radio:
                    event = new Event(dateOffset, Event.EventType.PATCH_3);
                    event.setMarked(markedCheckbox.isChecked());
                    return event;
            }
            event = new Event(dateOffset, Event.EventType.NO_PATCH);

            if (date.getDayOfYear() < now.getDayOfYear() && date.getYear() < now.getYear()) {
                event.setMarked(markedCheckbox.isChecked());
            }

            return event;
        }
    }
}
