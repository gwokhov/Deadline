package com.gwokhou.deadline.util;

import android.content.Context;

import com.gwokhou.deadline.AppPreferences;
import com.gwokhou.deadline.dataType.FilterType;

import java.util.ArrayList;
import java.util.List;

public class UpdateSelectedCategoryPreferences {

    public static void updateAppPreferences(Context context) {
        List<String> enableCategories = new ArrayList<>(0);

        if (AppPreferences.isShowAllEventsCategory(context)) {
            enableCategories.add(FilterType.ALL_EVENTS);
        }

        if (AppPreferences.isShowTodayEventsCategory(context)) {
            enableCategories.add(FilterType.TODAY_EVENTS);
        }

        if (AppPreferences.isShowNext7DaysEventsCategory(context)) {
            enableCategories.add(FilterType.NEXT_7_DAYS_EVENTS);
        }

        if (AppPreferences.isShowCompletedEventsCategory(context)) {
            enableCategories.add(FilterType.COMPLETED_EVENTS);
        }

        if (enableCategories.size() != 0) {
            AppPreferences.setCurrentCategory(context, enableCategories.get(0));
        }
    }


}
