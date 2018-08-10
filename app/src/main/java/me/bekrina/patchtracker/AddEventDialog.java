package me.bekrina.patchtracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import me.bekrina.patchtracker.data.Event;

public class AddEventDialog extends DialogFragment {
    private static final String EVENT_KEY = "event";
    private Event event;

    public static AddEventDialog newInstance(Event event) {
        AddEventDialog fragment = new AddEventDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EVENT_KEY, event);
        fragment.setArguments(bundle);

        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        event = (Event) getArguments().getParcelable(EVENT_KEY);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_add_event)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent("me.bekrina.patchtracker.EditEventActivity");
                        // TODO: decide what to do with serialising
                        intent.putExtra(EVENT_KEY, event);
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
