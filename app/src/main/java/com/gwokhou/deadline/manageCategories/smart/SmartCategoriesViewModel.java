package com.gwokhou.deadline.manageCategories.smart;

import android.app.Application;

import com.gwokhou.deadline.AppPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SmartCategoriesViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> showAllEventsCategory = new MutableLiveData<>();
    public MutableLiveData<Boolean> showTodayEventsCategory = new MutableLiveData<>();
    public MutableLiveData<Boolean> showNext7DaysEventsCategory = new MutableLiveData<>();
    public MutableLiveData<Boolean> showCompletedEventsCategory = new MutableLiveData<>();

    public SmartCategoriesViewModel(@NonNull Application application) {
        super(application);
        initSwitch(application);
    }

    private void initSwitch(Application application) {
        showAllEventsCategory.setValue(AppPreferences.isShowAllEventsCategory(application));
        showTodayEventsCategory.setValue(AppPreferences.isShowTodayEventsCategory(application));
        showNext7DaysEventsCategory.setValue(AppPreferences.isShowNext7DaysEventsCategory(application));
        showCompletedEventsCategory.setValue(AppPreferences.isShowCompletedEventsCategory(application));
    }
}
