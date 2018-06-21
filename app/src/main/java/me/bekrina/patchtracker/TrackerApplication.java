package me.bekrina.patchtracker;

import android.app.Application;

public class TrackerApplication extends Application {
    private ContraceptionType type;

    public void setType(ContraceptionType type) {
        this.type = type;
    }

    public ContraceptionType getType() {
        return type;
    }

    enum ContraceptionType {
        PATCH,
        PILLS
    }
}
