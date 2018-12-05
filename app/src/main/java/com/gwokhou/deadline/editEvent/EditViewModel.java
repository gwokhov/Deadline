package com.gwokhou.deadline.editEvent;

import android.app.Application;

import com.gwokhou.deadline.AppPreferences;
import com.gwokhou.deadline.R;
import com.gwokhou.deadline.SnackbarMessage;
import com.gwokhou.deadline.data.DeadlineRepository;
import com.gwokhou.deadline.data.Event;
import com.gwokhou.deadline.data.EventDataSource;
import com.gwokhou.deadline.dataType.FilterType;
import com.gwokhou.deadline.dataType.PriorityType;
import com.gwokhou.deadline.dataType.RemindType;
import com.gwokhou.deadline.dataType.StateType;
import com.gwokhou.deadline.util.NotificationUtils;
import com.gwokhou.deadline.util.ReminderUtils;
import com.gwokhou.deadline.util.ResourceValueUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

public class EditViewModel extends AndroidViewModel implements EventDataSource.GetEventCallback {

    private final SnackbarMessage mSnackbarText = new SnackbarMessage();

    private DeadlineRepository mRepository;

    private Application mApplication;

    private Event mEvent;

    public ObservableBoolean isNewTask = new ObservableBoolean(false);
    public ObservableBoolean isCompleted = new ObservableBoolean(false);
    public ObservableField<String> title = new ObservableField<>("");
    public ObservableField<String> note = new ObservableField<>("");
    public ObservableBoolean isDurableEvent = new ObservableBoolean(AppPreferences.isDurableEvent(getApplication()));
    public ObservableField<Long> startDate = new ObservableField<>(System.currentTimeMillis());
    public ObservableField<Long> dueDate = new ObservableField<>((long) 0);
    public ObservableField<String> category = new ObservableField<>(FilterType.INBOX);
    public ObservableField<Integer> priority = new ObservableField<>(PriorityType.NONE);
    public ObservableField<Integer> reminder = new ObservableField<>(RemindType.NONE_REMIND);

    public EditViewModel(@NonNull Application application) {
        super(application);
        mRepository = DeadlineRepository.getInstance(application);
        mApplication = application;
    }


    public void loadData(String taskId) {
        if (taskId == null) {
            isNewTask.set(true);
        } else {
            isNewTask.set(false);
            mRepository.getEvent(taskId, this);
        }
    }

    boolean saveEvent() {

        //判断数据是否合法
        if (title.get().trim().isEmpty()) {
            mSnackbarText.setValue(R.string.title_empty);
            return false;
        } else if (dueDate.get() == 0) {
            mSnackbarText.setValue(R.string.due_date_empty);
            return false;
        } else if (isDurableEvent.get()) {
            if (startDate.get() - dueDate.get() > 0) {
                mSnackbarText.setValue(R.string.date_invalid);
                return false;
            }
        }

        //判断事件状态
        int state;
        if (isCompleted.get()) {
            state = StateType.COMPLETED;
        } else {
            if (startDate.get() > System.currentTimeMillis()) {
                state = StateType.WAITING;
            } else if (dueDate.get() < System.currentTimeMillis()) {
                state = StateType.GONE;
            } else {
                state = StateType.ONGOING;
            }
        }

        //保存前判断是否为新事件
        if (isNewTask.get()) {
            mEvent = new Event(title.get().trim(), note.get(), startDate.get(), dueDate.get(), state, isDurableEvent.get(), priority.get(), reminder.get(), category.get());
            mRepository.saveEvent(mEvent);
            setReminder();
            return true;
        } else {
            mEvent = new Event(title.get().trim(), note.get(), startDate.get(), dueDate.get(), state, isDurableEvent.get(), priority.get(), mEvent.getId(), reminder.get(), category.get(), mEvent.getCreationDate());
            mRepository.saveEvent(mEvent);
            setReminder();
            return true;
        }

    }

    void deleteEvent() {
        mRepository.deleteEvent(mEvent.getId());
        NotificationUtils.cancelReminder(getApplication(), mEvent.getId());
    }

    @Override
    public void onEventLoaded(Event event) {
        mEvent = event;
        if (event.getState() == StateType.COMPLETED) {
            isCompleted.set(true);
        }
        title.set(event.getTitle());
        note.set(event.getNote());
        dueDate.set(event.getEndDate());
        category.set(event.getCategory());
        priority.set(event.getPriority());
        if (event.isDurableEvent()) {
            startDate.set(event.getStartDate());
            isDurableEvent.set(true);
        } else {
            startDate.set(event.getStartDate());
            isDurableEvent.set(false);
        }
        reminder.set(event.getReminder());
    }

    @Override
    public void onDataNotAvailable() {
    }

    private void setReminder() {
        int type = ReminderUtils.getRemindType(reminder.get());
        if (type >= RemindType.SINGLE_DUE_DATE && type <= RemindType.SINGLE_WEEK) {
            NotificationUtils.buildNormalReminder(getApplication(), dueDate.get(), title.get(), type, ReminderUtils.getSingleRemindInterval(reminder.get()), mEvent.getId());
        } else if (type == RemindType.NONE_REMIND) {
            NotificationUtils.cancelReminder(getApplication(), mEvent.getId());
        } else if (type == RemindType.REPEATED_EVERYDAY) {
            NotificationUtils.buildRepeatedReminder(getApplication(), dueDate.get(), title.get(), mEvent.getId());
        } else {

        }
    }

    public String getPriorityDesc(int priority) {
        return mApplication.getString(ResourceValueUtils.getPriorityDesc(priority));
    }

    public String getReminderDesc(int reminder) {
        int type = ReminderUtils.getRemindType(reminder);
        int res = ResourceValueUtils.getReminderDesc(type);

        if (type >= RemindType.SINGLE_MIN && type <= RemindType.SINGLE_WEEK) {
            int interval = ReminderUtils.getSingleRemindInterval(reminder);
            return mApplication.getResources().getQuantityString(res, interval, interval);
        } else {
            return mApplication.getString(res);
        }
    }

    public SnackbarMessage getSnackbarMessage() {
        return mSnackbarText;
    }

}
