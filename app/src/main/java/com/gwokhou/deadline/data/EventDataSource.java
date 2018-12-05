package com.gwokhou.deadline.data;

import androidx.annotation.NonNull;

public interface EventDataSource {

    interface GetEventCallback {

        void onEventLoaded(Event event);

        void onDataNotAvailable();
    }

    void getEvent(@NonNull String taskId, @NonNull GetEventCallback callback);

    void saveEvent(@NonNull Event event);

    void updateEventState(@NonNull Event event, int state);

    void updateEventState(@NonNull String taskId, int state);

    void clearCompletedEvents();

    void deleteAllEvents();

    void deleteEvent(@NonNull String taskId);
}
