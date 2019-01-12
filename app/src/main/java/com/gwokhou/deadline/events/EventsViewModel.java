package com.gwokhou.deadline.events;

import android.app.Application;

import com.gwokhou.deadline.AppPreferences;
import com.gwokhou.deadline.R;
import com.gwokhou.deadline.data.Category;
import com.gwokhou.deadline.data.CategoryDataSource;
import com.gwokhou.deadline.data.DrawerItem;
import com.gwokhou.deadline.data.Event;
import com.gwokhou.deadline.dataType.FilterType;
import com.gwokhou.deadline.dataType.QuickViewBehType;
import com.gwokhou.deadline.dataType.SortType;
import com.gwokhou.deadline.dataType.StateType;
import com.gwokhou.deadline.data.DeadlineRepository;
import com.gwokhou.deadline.util.DateTimeUtils;
import com.gwokhou.deadline.util.FilterUtils;
import com.gwokhou.deadline.util.ResourceValueUtils;
import com.gwokhou.deadline.util.SortUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;


public class EventsViewModel extends AndroidViewModel {

    private Application mApplication;

    private DeadlineRepository mRepository;

    public UpdateEventsDataListener mListener;

    private LiveData<List<Event>> mAllEvents;

    List<DrawerItem> filterItems = new ArrayList<>();

    List<Category> categoryItems = new ArrayList<>();

    public MutableLiveData<Integer> sortType = new MutableLiveData<>();
    public LiveData<String> sortDesc = Transformations.map(sortType, new Function<Integer, String>() {
        @Override
        public String apply(Integer type) {
            return getSortOrderDesc(type, isOrderAsc.getValue());
        }
    });

    public MutableLiveData<String> category = new MutableLiveData<>();
    public LiveData<String> categoryDesc = Transformations.map(category, new Function<String, String>() {
        @Override
        public String apply(String category) {
            return getCategoryDesc(category);
        }
    });

    public MutableLiveData<Boolean> isExpendCategories = new MutableLiveData<>();

    public MutableLiveData<Boolean> isShowQuickView = new MutableLiveData<>();

    public MutableLiveData<Boolean> displayGotIt = new MutableLiveData<>();

    public MutableLiveData<Boolean> isListEmpty = new MutableLiveData<>();

    public MutableLiveData<Boolean> isOrderAsc = new MutableLiveData<>();

    public MutableLiveData<String> quickViewTitle = new MutableLiveData<>();
    public MutableLiveData<Integer> quickViewImg = new MutableLiveData<>();

    public MutableLiveData<Integer> eventCurrentNum = new MutableLiveData<>();
    public MutableLiveData<Integer> eventTodayAllNum = new MutableLiveData<>();
    public MutableLiveData<Integer> eventTodayUncompletedNum = new MutableLiveData<>();
    public MutableLiveData<Integer> eventGoneNum = new MutableLiveData<>();
    public MutableLiveData<Integer> eventOngoingNum = new MutableLiveData<>();
    public MutableLiveData<Integer> eventWaitingNum = new MutableLiveData<>();

    private List<Event> mEvents;

    boolean hasLoadedEvents = false;

    private boolean isShowCompleted = false;

    public EventsViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        mRepository = DeadlineRepository.getInstance(application);
        initData();
    }

    private void initData() {
        mAllEvents = mRepository.getAllEvents();
        category.setValue(AppPreferences.getCurrentCategory(mApplication));
        sortType.setValue(AppPreferences.getCurrentSortType(mApplication));
        isOrderAsc.setValue(AppPreferences.isCurrentSortAsc(mApplication));
        isShowQuickView.setValue(AppPreferences.isEnableQuickView(mApplication));
        isShowCompleted = AppPreferences.isShowCompletedEvents(mApplication);

        updateQuickViewImg();

        updateFilterItems();
        updateCategoryItems();

        setIsExpendedCategory();
    }

    public void setListener(UpdateEventsDataListener listener) {
        mListener = listener;
    }

    private void updateFilterItems() {

        filterItems.clear();

        if (AppPreferences.isShowAllEventsCategory(mApplication)) {
            filterItems.add(new DrawerItem(R.string.all_events, R.drawable.ic_all_events, FilterType.ALL_EVENTS));
        }

        if (AppPreferences.isShowTodayEventsCategory(mApplication)) {
            filterItems.add(new DrawerItem(R.string.today, R.drawable.ic_today, FilterType.TODAY_EVENTS));
        }

        if (AppPreferences.isShowNext7DaysEventsCategory(mApplication)) {
            filterItems.add(new DrawerItem(R.string.next_7_days, R.drawable.ic_next_7_days, FilterType.NEXT_7_DAYS_EVENTS));
        }

        if (AppPreferences.isShowCompletedEventsCategory(mApplication)) {
            filterItems.add(new DrawerItem(R.string.completed, R.drawable.ic_completed_events, FilterType.COMPLETED_EVENTS));
        }

        filterItems.add(new DrawerItem(R.string.inbox, R.drawable.ic_inbox, FilterType.INBOX));
    }

    private void updateCategoryItems() {
        mRepository.getAllCategories(new CategoryDataSource.LoadCategoriesCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
                categoryItems.clear();
                categoryItems.addAll(categories);
            }

            @Override
            public void onDataNotAvailable() {
            }
        });
    }

    void setIsShowCompleted(boolean isShow) {
        isShowCompleted = isShow;
        AppPreferences.setIsShowCompletedEvents(mApplication, isShow);
    }

    private void setIsExpendedCategory() {
        isExpendCategories.setValue(false);
        for (Category item : categoryItems) {
            if (category.getValue().equals(item.getName())) {
                isExpendCategories.setValue(true);
            }
        }
    }

    private void setIsListEmpty(List<Event> events) {
        isListEmpty.setValue(events == null || events.isEmpty());
    }

    LiveData<List<Event>> getAllEvents() {
        return mAllEvents;
    }

    public List<Event> getEvents() {

        if (!isShowCompleted) {
            mEvents = FilterUtils.filterUncompletedEvents(mAllEvents.getValue());
        } else {
            mEvents = mAllEvents.getValue();
        }

        return getFilteredEvents();
    }

    private List<Event> getFilteredEvents() {
        return getFilteredEvents(category.getValue(), true);
    }

    private List<Event> getFilteredEvents(String filterName, boolean needSort) {

        List<Event> filteredEvents;

        switch (filterName) {
            case FilterType.ALL_EVENTS:
                filteredEvents = mEvents;
                break;
            case FilterType.TODAY_EVENTS:
                filteredEvents = FilterUtils.filterTodayTasks(mEvents);
                break;
            case FilterType.NEXT_7_DAYS_EVENTS:
                filteredEvents = FilterUtils.filterNext7DaysTasks(mEvents);
                break;
            case FilterType.COMPLETED_EVENTS:
                filteredEvents = FilterUtils.filterCompletedEvents(mAllEvents.getValue());
                break;
            default:
                filteredEvents = FilterUtils.filterCategoryEvents(mEvents, category.getValue());
        }

        if (needSort) {
            return getSortedEvents(filteredEvents);
        } else {
            return filteredEvents;
        }
    }

    private List<Event> getSortedEvents(List<Event> events) {
        List<Event> sortedEvents = events;

        switch (sortType.getValue()) {
            case SortType.PRIORITY:
                sortedEvents = SortUtils.sortByPriority(sortedEvents, isOrderAsc.getValue());
                break;
            case SortType.DUE_DATE:
                sortedEvents = SortUtils.sortByDueDate(sortedEvents, isOrderAsc.getValue());
                break;
            case SortType.CREATION_DATE:
                sortedEvents = SortUtils.sortByCreationDate(sortedEvents, isOrderAsc.getValue());
                break;
            case SortType.ALPHA:
                sortedEvents = SortUtils.sortByAlpha(sortedEvents, isOrderAsc.getValue());
                break;
            default:
                sortedEvents = events;

        }

        setIsListEmpty(sortedEvents);
        return sortedEvents;
    }

    private String getCategoryDesc(String category) {

        int res = ResourceValueUtils.getFilterChipTitle(category);

        return res == 0 ? category : mApplication.getString(res);
    }

    private String getSortOrderDesc(int sortType, boolean isAsc) {
        String order = isAsc ?
                mApplication.getString(R.string.chip_sort_order_asc) : mApplication.getString(R.string.chip_sort_order_desc);

        return sortType == SortType.CREATION_DATE ? "" : mApplication.getString(ResourceValueUtils.getSortChipTitle(sortType)) + order;
    }


    //关于仓库的操作

    void updateEventState(Event event, int state) {
        mRepository.updateEventState(event, state);
    }

    void deleteEvent(String id) {
        mRepository.deleteEvent(id);
        mListener.onEventDelete();
    }

    int getDeletedEventsNum() {
        return mRepository.countCacheDeletedEvents();
    }

    void undoDeletedEvents() {
        mRepository.undoCacheDeletedEvents();
    }

    void clearDeletedEvents() {
        mRepository.clearCacheDeletedEvents();
    }

    void addCategory(String categoryName) {
        mRepository.saveCategory(new Category(categoryName));
        updateCategoryItems();
    }


    //Quick View UI更新的操作

    void updateEventStateNum() {
        int gone = 0;
        int ongoing = 0;
        int waiting = 0;

        for (Event event : mAllEvents.getValue()) {
            switch (event.getState()) {
                case StateType.GONE:
                    gone++;
                    break;
                case StateType.ONGOING:
                    ongoing++;
                    break;
                case StateType.WAITING:
                    waiting++;
                    break;
            }
        }
        eventGoneNum.setValue(gone);
        eventOngoingNum.setValue(ongoing);
        eventWaitingNum.setValue(waiting);
    }

    void updateQuickViewTitle() {
        int timePeriod = DateTimeUtils.getTimePeriod();
        int titleRes = ResourceValueUtils.getQuickViewTitle(timePeriod);

        quickViewTitle.setValue(mApplication.getResources().getQuantityString(titleRes, eventTodayUncompletedNum.getValue(), eventTodayUncompletedNum.getValue()));
    }

    private void updateQuickViewImg() {
        int timePeriod = DateTimeUtils.getTimePeriod();
        int imgRes = ResourceValueUtils.getQuickViewPic(timePeriod);

        quickViewImg.setValue(imgRes);
    }

    void updateTodayEventNum() {

        List<Event> todayEvents = getFilteredEvents(FilterType.TODAY_EVENTS, false);

        int uncompleted = 0;

        for (Event event : todayEvents) {
            if (event.getState() != StateType.COMPLETED) {
                uncompleted++;
            }
        }

        eventTodayUncompletedNum.setValue(uncompleted);
        eventTodayAllNum.setValue(todayEvents.size());
    }

    public void setIsShowQuickView(boolean isShow) {
        isShowQuickView.setValue(isShow);
    }

    public void updateDisplayGotIt() {
        if (AppPreferences.getQuickViewBehavior(mApplication) == QuickViewBehType.STICKY) {
            displayGotIt.setValue(false);
        } else {
            displayGotIt.setValue(true);
        }
    }


    //标签UI更新的操作
    void updateCurrentCategory(String type) {
        AppPreferences.setCurrentCategory(mApplication, type);
        category.setValue(type);
        updateCurrentEventNum();
    }

    void updateCurrentSortType(int type) {
        AppPreferences.setCurrentSortType(mApplication, type);
        sortType.setValue(type);
    }

    void updateCurrentSortOrder(boolean isAsc) {
        AppPreferences.setIsCurrentSortAsc(mApplication, isAsc);
        isOrderAsc.setValue(isAsc);
    }

    void updateCurrentEventNum() {
        eventCurrentNum.setValue(getEventsNum(category.getValue()));
    }


    //以防用户改变分类内容，导致标签错误而更新
    void updateIfCategoriesChanged() {
        category.setValue(AppPreferences.getCurrentCategory(mApplication));
        updateFilterItems();
        updateCategoryItems();
    }

    //UI点击操作
    public void onCategoryExpandClicked() {
        isExpendCategories.setValue(!isExpendCategories.getValue());
    }

    public int getEventsNum(String type) {
        switch (type) {
            case FilterType.ALL_EVENTS:
                return mEvents.size();
            case FilterType.TODAY_EVENTS:
                return FilterUtils.filterTodayTasks(mEvents).size();
            case FilterType.NEXT_7_DAYS_EVENTS:
                return FilterUtils.filterNext7DaysTasks(mEvents).size();
            case FilterType.COMPLETED_EVENTS:
                return FilterUtils.filterCompletedEvents(mAllEvents.getValue()).size();
            default:
                return FilterUtils.filterCategoryEvents(mEvents, type).size();
        }
    }

    ArrayList<String> getCategoriesNames() {
        ArrayList<String> categoriesNames = new ArrayList<>(0);
        for (Category category : categoryItems) {
            categoriesNames.add(category.getName());
        }
        return categoriesNames;
    }

}
