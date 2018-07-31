package me.bekrina.patchtracker;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.OffsetDateTime;

import java.io.IOException;
import java.util.List;

import me.bekrina.patchtracker.data.Event;
import me.bekrina.patchtracker.data.EventDao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DaoTest {
    private EventDao mEventDao;
    private TestDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, TestDatabase.class).build();
        mEventDao = mDb.eventDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writeEventAndReadInList() throws Exception {
        Event event = new Event(OffsetDateTime.now(), Event.EventType.PATCH_ON);
        event.setMarked(true);
        mEventDao.insertAll(event);
        List<Event> byName = mEventDao.loadById(event.getUid()).getValue();
        assertThat(byName.get(0), equalTo(event));
    }
}
