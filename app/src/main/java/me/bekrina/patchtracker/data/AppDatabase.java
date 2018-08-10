package me.bekrina.patchtracker.data;

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

@Database(entities = {Event.class}, version = 3)
@TypeConverters({EventTypeConverter.class, DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract EventDao eventDao();

    //TODO: move singleton to Dagger
    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "word_database")
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
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final EventDao dao;

        PopulateDbAsync(AppDatabase db) {
            dao = db.eventDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            dao.deleteAll();

            Event patchOn = new Event(OffsetDateTime.of(2018, 7, 7, 0,
                    0, 0, 0, ZoneOffset.UTC), Event.EventType.PATCH_1);
            patchOn.setMarked(true);
            Event patchChange = new Event(OffsetDateTime.of(2018, 7, 14, 0,
                    0, 0, 0, ZoneOffset.UTC), Event.EventType.PATCH_2);
            patchChange.setMarked(true);
            Event patchChange2 = new Event(OffsetDateTime.of(2018, 7, 21, 0,
                    0, 0, 0, ZoneOffset.UTC), Event.EventType.PATCH_3);
            Event patchOff = new Event(OffsetDateTime.of(2018, 7, 28, 0,
                    0, 0, 0, ZoneOffset.UTC), Event.EventType.NO_PATCH);

            dao.insertAll(patchChange, patchChange2, patchOn, patchOff);

            return null;
        }
    }
}
