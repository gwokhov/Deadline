package com.gwokhou.deadline.data;

import com.gwokhou.deadline.util.AppExecutors;

import androidx.annotation.NonNull;

public class EventLocalDataSource implements EventDataSource {

    private static volatile EventLocalDataSource INSTANCE;

    private EventsDao mEventsDao;

    private AppExecutors mAppExecutors;

    private EventLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull EventsDao eventsDao) {
        mAppExecutors = appExecutors;
        mEventsDao = eventsDao;
    }

    public static EventLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull EventsDao eventsDao) {
        if (INSTANCE == null) {
            synchronized (EventLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EventLocalDataSource(appExecutors, eventsDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getEvent(@NonNull final String taskId, @NonNull final GetEventCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Event event = mEventsDao.getEventById(taskId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (event != null) {
                            callback.onEventLoaded(event);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveEvent(@NonNull final Event event) {
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mEventsDao.insertEvent(event);

            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void updateEventState(@NonNull final Event event, final int state) {
        Runnable completeRunnable = new Runnable() {
            @Override
            public void run() {
                mEventsDao.updateState(event.getId(), state);
            }
        };

        mAppExecutors.diskIO().execute(completeRunnable);
    }

    @Override
    public void updateEventState(@NonNull final String taskId, final int state) {
        Runnable completeRunnable = new Runnable() {
            @Override
            public void run() {
                mEventsDao.updateState(taskId, state);
            }
        };

        mAppExecutors.diskIO().execute(completeRunnable);
    }

    @Override
    public void clearCompletedEvents() {
        Runnable clearTasksRunnable = new Runnable() {
            @Override
            public void run() {
                mEventsDao.deleteCompletedEvents();

            }
        };
        mAppExecutors.diskIO().execute(clearTasksRunnable);
    }

    @Override
    public void deleteAllEvents() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mEventsDao.deleteAllEvents();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteEvent(@NonNull final String taskId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mEventsDao.deleteEventById(taskId);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

}
