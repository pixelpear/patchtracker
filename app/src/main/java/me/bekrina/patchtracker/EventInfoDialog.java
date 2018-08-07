package me.bekrina.patchtracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.threeten.bp.OffsetDateTime;

import me.bekrina.patchtracker.data.Event;

public class EventInfoDialog extends DialogFragment {
    private static final String EVENT_KEY = "event_key";
    private Event event;

    public static EventInfoDialog newInstance(Event event) {
        EventInfoDialog fragment = new EventInfoDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EVENT_KEY, event);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        event = (Event) getArguments().getParcelable(EVENT_KEY);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_eventinfo, null);
        TextView message = dialogView.findViewById(R.id.message);
        message.setText(event.getType().toString());
        OffsetDateTime eventDateTime = event.getDate();
        TextView time = dialogView.findViewById(R.id.time);
        String timeString = String.valueOf(eventDateTime.getHour()) + ":"
                + String.valueOf(eventDateTime.getMinute());
        time.setText(timeString);
        TextView date = dialogView.findViewById(R.id.date);
        String dateString = String.valueOf(eventDateTime.getDayOfMonth()) + "."
                + String.valueOf(eventDateTime.getMonthValue()) + "."
                + String.valueOf(eventDateTime.getYear());
        date.setText(dateString);
        builder.setView(dialogView);
        // Add action buttons
            builder.setPositiveButton(R.string.editevent, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent("me.bekrina.patchtracker.EditEventActivity");
                    intent.putExtra("event", event);
                    startActivity(intent);
                }
            })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EventInfoDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
