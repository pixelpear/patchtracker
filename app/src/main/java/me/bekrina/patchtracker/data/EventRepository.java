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

    public void insert(Event event) {
        new insertAsyncTask(eventDao).execute(event);
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

    public void delete(Event event) {
        new deleteAsynkTask(eventDao).execute(event);
    }

    private static class deleteAsynkTask extends AsyncTask<Event, Void, Void> {

        private EventDao asyncTaskDao;

        deleteAsynkTask(EventDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }

    public void update(Event event) {
        new updateAsynkTask(eventDao).execute(event);
    }
    private static class updateAsynkTask extends AsyncTask<Event, Void, Void> {

        private EventDao eventDao;

        updateAsynkTask(EventDao dao) {
            eventDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            eventDao.updateAll(params[0]);
            return null;
        }
    }

}
