package com.gwokhou.deadline.data;

import android.app.Application;

import com.gwokhou.deadline.dataType.StateType;
import com.gwokhou.deadline.util.AppExecutors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class DeadlineRepository implements EventDataSource, CategoryDataSource {

    private static DeadlineRepository INSTANCE = null;

    private final EventDataSource mTasksLocalDataSource;

    private final CategoryLocalDataSource mCategoryLocalDataSource;

    private LiveData<List<Event>> mAllEvents;

    private Map<String, Event> mCachedEvents;

    private Map<String, Event> mCachedDeletedEvents;

    private Map<String, Category> mCachedCategories;

    private DeadlineRepository(Application application) {
        DeadlineDatabase db = DeadlineDatabase.getDatabase(application);
        EventsDao eventsDao = db.eventDao();
        CategoriesDao categoriesDao = db.categoriesDao();
        mTasksLocalDataSource = EventLocalDataSource.getInstance(new AppExecutors(), eventsDao);
        mCategoryLocalDataSource = CategoryLocalDataSource.getInstance(new AppExecutors(), categoriesDao);
        mAllEvents = eventsDao.getAllEvents();
    }

    public static DeadlineRepository getInstance(Application application) {
        if (INSTANCE == null) {
            return INSTANCE = new DeadlineRepository(application);
        } else {
            return INSTANCE;
        }
    }

    public LiveData<List<Event>> getAllEvents() {
        return mAllEvents;
    }

    @Override
    public void saveEvent(@NonNull Event event) {
        mTasksLocalDataSource.saveEvent(event);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedEvents == null) {
            mCachedEvents = new LinkedHashMap<>();
        }

        mCachedEvents.put(event.getId(), event);
    }

    @Override
    public void updateEventState(@NonNull Event event, int state) {
        mTasksLocalDataSource.updateEventState(event, state);

        Event newStateEvent = new Event(event.getTitle(), event.getNote(), event.getStartDate(), event.getEndDate(), state, event.isDurableEvent(), event.getPriority(), event.getId(), event.getReminder(), event.getCategory(), event.getCreationDate());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedEvents == null) {
            mCachedEvents = new LinkedHashMap<>();
        }
        mCachedEvents.put(event.getId(), newStateEvent);
    }

    @Override
    public void updateEventState(@NonNull String eventId, int state) {
        mTasksLocalDataSource.updateEventState(eventId, state);

        if (mCachedEvents == null) {
            mCachedEvents = new LinkedHashMap<>();
        }
    }

    @Override
    public void clearCompletedEvents() {
        mTasksLocalDataSource.clearCompletedEvents();

        // Do in memory cache update to keep the app UI up to date
        if (mCachedEvents == null) {
            mCachedEvents = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Event>> it = mCachedEvents.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Event> entry = it.next();
            if (entry.getValue().getState() == StateType.COMPLETED) {
                it.remove();
            }
        }
    }

    @Override
    public void getEvent(@NonNull final String eventId, @NonNull final GetEventCallback callback) {
        Event cachedEvent = getTaskWithId(eventId);

        // Respond immediately with cache if available
        if (cachedEvent != null) {
            callback.onEventLoaded(cachedEvent);
            return;
        }

        mTasksLocalDataSource.getEvent(eventId, new GetEventCallback() {
            @Override
            public void onEventLoaded(Event event) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedEvents == null) {
                    mCachedEvents = new LinkedHashMap<>();
                }
                mCachedEvents.put(event.getId(), event);
                callback.onEventLoaded(event);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void addCacheDeletedEvent(String eventId, Event event) {
        if (mCachedDeletedEvents == null) {
            mCachedDeletedEvents = new LinkedHashMap<>();
        }

        mCachedDeletedEvents.put(eventId, event);

    }

    public int countCacheDeletedEvents() {
        if (mCachedDeletedEvents == null) {
            mCachedDeletedEvents = new LinkedHashMap<>();
        }
        return mCachedDeletedEvents.size();
    }

    public void undoCacheDeletedEvents() {
        if (mCachedDeletedEvents == null) {
            mCachedDeletedEvents = new LinkedHashMap<>();
        }
        for (Event event : mCachedDeletedEvents.values()) {
            saveEvent(event);
        }

        clearCacheDeletedEvents();
    }

    public void clearCacheDeletedEvents() {
        mCachedDeletedEvents.clear();
    }

    @Override
    public void deleteAllEvents() {
        mTasksLocalDataSource.deleteAllEvents();

        if (mCachedEvents == null) {
            mCachedEvents = new LinkedHashMap<>();
        }
        mCachedEvents.clear();
    }

    @Override
    public void deleteEvent(@NonNull String taskId) {
        addCacheDeletedEvent(taskId, getTaskWithId(taskId));
        mTasksLocalDataSource.deleteEvent(taskId);

        if (mCachedEvents != null) {
            mCachedEvents.remove(taskId);
        }
    }

    @Nullable
    private Event getTaskWithId(@NonNull String id) {
        if (mCachedEvents == null || mCachedEvents.isEmpty()) {
            return null;
        } else {
            return mCachedEvents.get(id);
        }
    }

    @Override
    public void getAllCategories(@NonNull final LoadCategoriesCallback callback) {
        if (mCachedCategories != null) {
            callback.onCategoriesLoaded(new ArrayList<>(mCachedCategories.values()));
            return;
        }

        mCategoryLocalDataSource.getAllCategories(new LoadCategoriesCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
                refreshCategoriesCache(categories);

                callback.onCategoriesLoaded(new ArrayList<>(mCachedCategories.values()));
            }

            @Override
            public void onDataNotAvailable() {
            }
        });
    }

    @Override
    public void getCategory(@NonNull String categoryId, @NonNull final GetCategoryCallback callback) {
        Category cachedCategory = getCategoryWithId(categoryId);

        if (cachedCategory != null) {
            callback.onCategoryLoaded(cachedCategory);
            return;
        }

        mCategoryLocalDataSource.getCategory(categoryId, new GetCategoryCallback() {
            @Override
            public void onCategoryLoaded(Category category) {
                if (mCachedCategories == null) {
                    callback.onCategoryLoaded(category);
                }
                mCachedCategories.put(category.getId(), category);

                callback.onCategoryLoaded(category);
            }

            @Override
            public void onDataNotAvailable() {
            }
        });
    }

    @Override
    public void saveCategory(@NonNull Category category) {
        mCategoryLocalDataSource.saveCategory(category);

        if (mCachedCategories == null) {
            mCachedCategories = new LinkedHashMap<>();
        }
        mCachedCategories.put(category.getId(), category);
    }

    @Override
    public void deleteAllCategories() {
        mCategoryLocalDataSource.deleteAllCategories();
    }

    @Override
    public void deleteCategory(@NonNull String categoryId) {
        mCategoryLocalDataSource.deleteCategory(categoryId);

        mCachedCategories.remove(categoryId);
    }

    private void refreshCategoriesCache(List<Category> categories) {
        if (mCachedCategories == null) {
            mCachedCategories = new LinkedHashMap<>();
        }
        mCachedCategories.clear();
        for (Category category : categories) {
            mCachedCategories.put(category.getId(), category);
        }
    }

    @Nullable
    private Category getCategoryWithId(@NonNull String id) {
        if (mCachedCategories == null || mCachedCategories.isEmpty()) {
            return null;
        } else {
            return mCachedCategories.get(id);
        }
    }
}
