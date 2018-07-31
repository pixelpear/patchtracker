package me.bekrina.patchtracker;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.ArrayList;
import java.util.List;

import me.bekrina.patchtracker.data.DateConverter;
import me.bekrina.patchtracker.data.Event;
import me.bekrina.patchtracker.data.EventDao;
import me.bekrina.patchtracker.data.EventTypeConverter;

@Database(entities = {Event.class}, version = 2)
@TypeConverters({EventTypeConverter.class, DateConverter.class})
public abstract class TestDatabase extends RoomDatabase {
    private static me.bekrina.patchtracker.data.AppDatabase INSTANCE;
    public abstract EventDao eventDao();

    //TODO: move singleton to Dagger
    static me.bekrina.patchtracker.data.AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (me.bekrina.patchtracker.data.AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            me.bekrina.patchtracker.data.AppDatabase.class, "word_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            //TODO: implement Migration
                            // https://medium.com/google-developers/understanding-migrations-with-room-f01e04b07929
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new me.bekrina.patchtracker.TestDatabase.PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final EventDao dao;

        PopulateDbAsync(me.bekrina.patchtracker.data.AppDatabase db) {
            dao = db.eventDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            dao.deleteAll();

            Event patchOn = new Event(OffsetDateTime.of(2018, 7, 7, 0,
                    0, 0, 0, ZoneOffset.UTC), Event.EventType.PATCH_ON);
            patchOn.setMarked(true);
            Event patchChange = new Event(OffsetDateTime.of(2018, 7, 14, 0,
                    0, 0, 0, ZoneOffset.UTC), Event.EventType.PATCH_CHANGE);
            patchChange.setMarked(true);
            Event patchChange2 = new Event(OffsetDateTime.of(2018, 7, 21, 0,
                    0, 0, 0, ZoneOffset.UTC), Event.EventType.PATCH_CHANGE);
            Event patchOff = new Event(OffsetDateTime.of(2018, 7, 28, 0,
                    0, 0, 0, ZoneOffset.UTC), Event.EventType.PATCH_OFF);

            dao.insertAll(patchChange, patchChange2, patchOn, patchOff);

            return null;
        }
    }
}
