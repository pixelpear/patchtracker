package me.bekrina.patchtracker.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.threeten.bp.OffsetDateTime;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    LiveData<List<Event>> loadAllByIds(int [] ids) { return eventDao.loadAllByIds(ids); }

    LiveData<List<Event>> loadAllByDate(OffsetDateTime plannedDate) {
        plannedDate = plannedDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
        return eventDao.loadAllByDate(plannedDate);
    }

    public void insertAll(Event... event) {
        new insertAsyncTask(eventDao).execute(event);
    }

    public void insertAll(List<Event> events) {
        new insertAsyncTask(eventDao).execute(events.toArray(new Event[events.size()]));
    }

    private static class insertAsyncTask extends AsyncTask<Event, Void, Void> {

        private EventDao asyncTaskDao;

        insertAsyncTask(EventDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            if (params.length > 0) {
                asyncTaskDao.insertAll(params);
            }
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

    public void deleteAllFutureEvents(OffsetDateTime date) {
        new deleteAllFutureEventsAsynkTask(eventDao).execute(date);
    }

    private static class deleteAllFutureEventsAsynkTask extends AsyncTask<OffsetDateTime, Void, Void> {

        private EventDao eventDao;

        deleteAllFutureEventsAsynkTask(EventDao dao) {
            eventDao = dao;
        }

        @Override
        protected Void doInBackground(final OffsetDateTime... params) {
            eventDao.deleteAllFutureEvents(params[0]);
            return null;
        }
    }

    public List<Event> getFutureEvents(OffsetDateTime date) {
        try {
            return new getFutureEventsAsynkTask(eventDao).execute(date).get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    private static class getFutureEventsAsynkTask extends AsyncTask<OffsetDateTime, List<Event>, List<Event>> {

        private EventDao eventDao;

        getFutureEventsAsynkTask(EventDao dao) {
            eventDao = dao;
        }

        @Override
        protected List<Event> doInBackground(final OffsetDateTime... params) {
            return eventDao.getAllFutureEventsSortedAsc(params[0]);
        }
    }

}
