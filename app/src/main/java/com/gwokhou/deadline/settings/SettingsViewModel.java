package com.gwokhou.deadline.settings;

import android.app.Application;

import com.gwokhou.deadline.AppPreferences;
import com.gwokhou.deadline.util.ResourceValueUtils;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class SettingsViewModel extends AndroidViewModel {

    private Application mApplication;

    public MutableLiveData<Boolean> isDurableModeDefault = new MutableLiveData<>();

    public MutableLiveData<Boolean> enableQuickView = new MutableLiveData<>();

    public MutableLiveData<Boolean> isMondayWeekStart = new MutableLiveData<>();

    public MutableLiveData<Long> dailyRemindTime = new MutableLiveData<>();

    public MutableLiveData<Integer> quickViewBehavior = new MutableLiveData<>();
    public LiveData<String> quickViewBehDesc = Transformations.map(quickViewBehavior, new Function<Integer, String>() {
        @Override
        public String apply(Integer behavior) {
            return getQuickViewBehaviorDesc(behavior);
        }
    });

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        mApplication = getApplication();
        loadData();
    }

    private void loadData() {
        isDurableModeDefault.setValue(AppPreferences.isDurableEvent(mApplication));
        enableQuickView.setValue(AppPreferences.isEnableQuickView(mApplication));
        isMondayWeekStart.setValue(AppPreferences.isMondayTheFirstDay(mApplication));
        dailyRemindTime.setValue(AppPreferences.getDailyRemindTime(mApplication));
        quickViewBehavior.setValue(AppPreferences.getQuickViewBehavior(mApplication));
    }

    void updateStartOfWeek(boolean isMonday) {
        AppPreferences.setIsMondayTheFirstDay(mApplication, isMonday);
        isMondayWeekStart.setValue(isMonday);
    }

    void updateDailyRemindTime(long time) {
        AppPreferences.setDailyRemindTime(mApplication, time);
        dailyRemindTime.setValue(time);
    }

    void updateDefaultEventMode(boolean isDurableMode) {
        AppPreferences.setIsDurableEvent(mApplication, isDurableMode);
    }

    void updateEnableQuickView(boolean isEnable) {
        AppPreferences.setEnableQuickView(mApplication, isEnable);
    }

    void updateQuickViewBehavior(int behavior) {
        AppPreferences.setQuickViewBehavior(mApplication, behavior);
        quickViewBehavior.setValue(behavior);
    }

    private String getQuickViewBehaviorDesc(int behavior) {

        int res = ResourceValueUtils.getQuickViewBehDesc(behavior);

        return mApplication.getString(res);
    }

}
