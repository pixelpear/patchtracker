package me.bekrina.patchtracker.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class EventRepository {
    private EventDao eventDao;
    private LiveData<List<Event>> allEvents;

    EventRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        eventDao = database.eventDao();
        allEvents = eventDao.getAllSortedDesc();
    }

    LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public void insert(Event word) {
        new insertAsyncTask(eventDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Event, Void, Void> {

        private EventDao asyncTaskDao;

        insertAsyncTask(EventDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            asyncTaskDao.insertAll(params[0]);
            return null;
        }
    }
}
