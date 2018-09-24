package me.bekrina.patchtracker.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import org.threeten.bp.OffsetDateTime;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    private EventRepository repository;
    private LiveData<List<Event>> allEvents;

    public EventViewModel (Application application) {
        super(application);
        repository = new EventRepository(application);
        allEvents = repository.getAllEvents();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public LiveData<List<Event>> loadAllByIds(int... ids) {
        return repository.loadAllByIds(ids);
    }

    public LiveData<List<Event>> loadAllByDate(OffsetDateTime plannedDate) {
        return repository.loadAllByDate(plannedDate);
    }

    public void insertAll(Event event) { repository.insertAll(event); }

    public void insertAll(List<Event> events) { repository.insertAll(events); }

    public void delete(Event event) {
        repository.delete(event);
    }

    public void deleteAllFutureEvents(OffsetDateTime date) {
        repository.deleteAllFutureEvents(date);
    }

    public LiveData<List<Event>> getFutureEvents(OffsetDateTime date){
        return repository.getFutureEvents(date);
    }
}
