package com.gwokhou.deadline.reminderSelect;

import android.app.Application;

import com.gwokhou.deadline.R;
import com.gwokhou.deadline.dataType.RemindType;
import com.gwokhou.deadline.util.ReminderUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

public class ReminderDialogViewModel extends AndroidViewModel {

    public ObservableArrayMap<Integer, Boolean> remindSelections = new ObservableArrayMap<>();

    public ObservableField<String> singleRemindInterval = new ObservableField<>("10");

    public ObservableField<Integer> singleRemindUnit = new ObservableField<>(RemindType.SINGLE_MIN);

    public ReminderDialogViewModel(@NonNull Application application) {
        super(application);
        initSelections();
    }

    private void initSelections() {
        remindSelections.put(RemindType.NONE_REMIND, true);
        remindSelections.put(RemindType.SINGLE_DUE_DATE, false);
        remindSelections.put(RemindType.SINGLE_REMIND, false);
        remindSelections.put(RemindType.REPEATED_EVERYDAY, false);
        remindSelections.put(RemindType.REPEATED_ALWAYS, false);
    }

    public void updateSelections(int type) {
        for (int i : remindSelections.keySet()) {
            if (i == type) {
                remindSelections.put(i, true);
            } else {
                remindSelections.put(i, false);
            }
        }
    }

    private int getRemindType() {
        for (int i : remindSelections.keySet()) {
            if (remindSelections.get(i)) {
                if (i == RemindType.SINGLE_REMIND) {
                    return singleRemindUnit.get();
                } else {
                    return i;
                }
            }
        }
        return RemindType.NONE_REMIND;
    }

    public void loadData(int typeData) {
        int type = ReminderUtils.getRemindType(typeData);

        if (type >= RemindType.SINGLE_MIN && type <= RemindType.SINGLE_WEEK) {
            singleRemindInterval.set(String.valueOf(ReminderUtils.getSingleRemindInterval(typeData)));
            singleRemindUnit.set(type);
            updateSelections(RemindType.SINGLE_REMIND);
        } else {
            updateSelections(type);
        }
    }

    public String getRemindUnitString(int unit) {
        switch (unit) {
            case RemindType.SINGLE_MIN:
                return getApplication().getString(R.string.remind_minutes);
            case RemindType.SINGLE_HOUR:
                return getApplication().getString(R.string.remind_hours);
            case RemindType.SINGLE_DAY:
                return getApplication().getString(R.string.remind_days);
            case RemindType.SINGLE_WEEK:
                return getApplication().getString(R.string.remind_weeks);
            default:
                return getApplication().getString(R.string.remind_minutes);
        }
    }

    public int getReminder() {
        return ReminderUtils.buildReminder(getRemindType(), Integer.parseInt(singleRemindInterval.get()));
    }

}
