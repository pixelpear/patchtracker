package me.bekrina.patchtracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.threeten.bp.OffsetDateTime;

import me.bekrina.patchtracker.data.Event;

public class AddEventDialog extends DialogFragment {
    private String calendarDate;

    public static AddEventDialog newInstance(String calendarDate) {
        AddEventDialog fragment = new AddEventDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EditEventActivity.CALENDAR_DATE_KEY, calendarDate);
        fragment.setArguments(bundle);

        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_add_event)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent("me.bekrina.patchtracker.EditEventActivity");
                        // TODO: decide what to do with serialising
                        if (getArguments() != null) {
                            calendarDate = getArguments().getString(EditEventActivity.CALENDAR_DATE_KEY);
                            if (calendarDate != null) {
                                intent.putExtra(EditEventActivity.CALENDAR_DATE_KEY, calendarDate);
                            }
                        }
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        return builder.create();
    }
}
