package com.gwokhou.deadline;

import android.content.Context;
import android.preference.PreferenceManager;

import com.gwokhou.deadline.dataType.FilterType;
import com.gwokhou.deadline.dataType.QuickViewBehType;
import com.gwokhou.deadline.dataType.SortType;
import com.gwokhou.deadline.util.DateTimeUtils;

public class AppPreferences {
    private static final String PREF_IS_FIRST_BOOT = "IS_FIRST_BOOT";
//    private static final String PREF_HAS_READ_TYPE_TIPS = "HAS_READ_TYPE_TIPS";
    private static final String PREF_IS_SHOW_COMPLETED_EVENTS = "IS_SHOW_COMPLETED_EVENTS";
    private static final String PREF_IS_SHOW_ALL_EVENTS_CATEGORY = "IS_SHOW_ALL_EVENTS_CATEGORY";
    private static final String PREF_IS_SHOW_TODAY_EVENTS_CATEGORY = "IS_SHOW_TODAY_EVENTS_CATEGORY";
    private static final String PREF_IS_SHOW_NEXT_7_DAYS_EVENTS_CATEGORY = "IS_SHOW_NEXT_7_DAYS_EVENTS_CATEGORY";
    private static final String PREF_IS_SHOW_COMPLETED_EVENTS_CATEGORY = "IS_SHOW_COMPLETED_EVENTS_CATEGORY";
    private static final String PREF_CURRENT_CATEGORY = "CURRENT_CATEGORY";
    private static final String PREF_CURRENT_SORT_TYPE = "CURRENT_SORT_TYPE";
    private static final String PREF_IS_CURRENT_SORT_ASC = "IS_CURRENT_SORT_ASC";
    private static final String PREF_IS_MONDAY_THE_FIRST_DAY = "IS_MONDAY_THE_FIRST_DAY";
    private static final String PREF_IS_DURABLE_EVENT = "IS_DURABLE_EVENT";
    private static final String PREF_ENABLE_QUICK_VIEW = "ENABLE_QUICK_VIEW";
    private static final String PREF_QUICK_VIEW_BEHAVIOR = "QUICK_VIEW_BEHAVIOR";
    private static final String PREF_HAD_SHOW_QUICK_VIEW = "HAD_SHOW_QUICK_VIEW";
    private static final String PREF_DAILY_REMIND_TIME = "DAILY_REMIND_TIME";

    private static final boolean DEF_IS_FIRST_BOOT = true;
//    private static final boolean DEF_HAS_READ_TYPE_TIPS = false;
    private static final boolean DEF_IS_SHOW_COMPLETED_EVENTS = false;
    private static final boolean DEF_IS_SHOW_ALL_EVENTS_CATEGORY = true;
    private static final boolean DEF_IS_SHOW_TODAY_EVENTS_CATEGORY = true;
    private static final boolean DEF_IS_SHOW_NEXT_7_DAYS_EVENTS_CATEGORY = true;
    private static final boolean DEF_IS_SHOW_UNCOMPLETED_EVENTS_CATEGORY = true;
    private static final String DEF_CURRENT_CATEGORY = FilterType.ALL_EVENTS;
    private static final int DEF_CURRENT_SORT_ORDER = SortType.CREATION_DATE;
    private static final boolean DEF_IS_CURRENT_SORT_ASC = true;
    private static final boolean DEF_IS_MONDAY_THE_FIRST_DAY = true;
    private static final boolean DEF_IS_DURABLE_EVENT = true;
    private static final boolean DEF_ENABLE_QUICK_VIEW = true;
    private static final int DEF_QUICK_VIEW_BEHAVIOR = QuickViewBehType.SHOW_WHEN_OPEN;
    private static final boolean DEF_HAD_SHOW_QUICK_VIEW = false;
    private static final long DEF_DAILY_REMIND_TIME = 9 * DateTimeUtils.HOUR;


    public static boolean isFirstBoot(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_FIRST_BOOT, DEF_IS_FIRST_BOOT);
    }

    public static void setIsFirstBoot(Context context, boolean isFirstBoot) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_FIRST_BOOT, isFirstBoot)
                .apply();
    }

//    public static boolean hasReadTypeTips(Context context) {
//        return PreferenceManager.getDefaultSharedPreferences(context)
//                .getBoolean(PREF_HAS_READ_TYPE_TIPS, DEF_HAS_READ_TYPE_TIPS);
//    }
//
//    public static void setHasReadTypeTips(Context context, boolean hasRead) {
//        PreferenceManager.getDefaultSharedPreferences(context)
//                .edit()
//                .putBoolean(PREF_HAS_READ_TYPE_TIPS, hasRead)
//                .apply();
//    }

    public static boolean isShowCompletedEvents(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_SHOW_COMPLETED_EVENTS, DEF_IS_SHOW_COMPLETED_EVENTS);
    }

    public static void setIsShowCompletedEvents(Context context, boolean isShow) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_SHOW_COMPLETED_EVENTS , isShow)
                .apply();
    }

    public static boolean isShowAllEventsCategory(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_SHOW_ALL_EVENTS_CATEGORY, DEF_IS_SHOW_ALL_EVENTS_CATEGORY);
    }

    public static void setIsShowAllEventsCategory(Context context, boolean isShow) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_SHOW_ALL_EVENTS_CATEGORY , isShow)
                .apply();
    }

    public static boolean isShowTodayEventsCategory(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_SHOW_TODAY_EVENTS_CATEGORY, DEF_IS_SHOW_TODAY_EVENTS_CATEGORY);
    }

    public static void setIsShowTodayEventsCategory(Context context, boolean isShow) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_SHOW_TODAY_EVENTS_CATEGORY , isShow)
                .apply();
    }

    public static boolean isShowNext7DaysEventsCategory(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_SHOW_NEXT_7_DAYS_EVENTS_CATEGORY, DEF_IS_SHOW_NEXT_7_DAYS_EVENTS_CATEGORY);
    }

    public static void setIsShowNext7DaysEventsCategory(Context context, boolean isShow) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_SHOW_NEXT_7_DAYS_EVENTS_CATEGORY, isShow)
                .apply();
    }

    public static boolean isShowCompletedEventsCategory(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_SHOW_COMPLETED_EVENTS_CATEGORY, DEF_IS_SHOW_UNCOMPLETED_EVENTS_CATEGORY);
    }

    public static void setIsShowCompletedEventsCategory(Context context, boolean isShow) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_SHOW_COMPLETED_EVENTS_CATEGORY, isShow)
                .apply();
    }

    public static String getCurrentCategory(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_CURRENT_CATEGORY, DEF_CURRENT_CATEGORY);
    }

    public static void setCurrentCategory(Context context, String category) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_CURRENT_CATEGORY, category)
                .apply();
    }

    public static int getCurrentSortType(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_CURRENT_SORT_TYPE, DEF_CURRENT_SORT_ORDER);
    }

    public static void setCurrentSortType(Context context, int sortType) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_CURRENT_SORT_TYPE, sortType)
                .apply();
    }

    public static boolean isCurrentSortAsc(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_CURRENT_SORT_ASC, DEF_IS_CURRENT_SORT_ASC);
    }

    public static void setIsCurrentSortAsc(Context context, boolean isSortAsc) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_CURRENT_SORT_ASC, isSortAsc)
                .apply();
    }

    public static boolean isMondayTheFirstDay(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_MONDAY_THE_FIRST_DAY, DEF_IS_MONDAY_THE_FIRST_DAY);
    }

    public static void setIsMondayTheFirstDay(Context context, boolean isMondayTheFirstDay) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_MONDAY_THE_FIRST_DAY, isMondayTheFirstDay)
                .apply();
    }

    public static boolean isDurableEvent(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_DURABLE_EVENT, DEF_IS_DURABLE_EVENT);
    }

    public static void setIsDurableEvent(Context context, boolean isDurableEvent) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_DURABLE_EVENT, isDurableEvent)
                .apply();
    }

    public static boolean isEnableQuickView(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_ENABLE_QUICK_VIEW, DEF_ENABLE_QUICK_VIEW);
    }

    public static void setEnableQuickView(Context context, boolean isEnable) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_ENABLE_QUICK_VIEW, isEnable)
                .apply();
    }

    public static int getQuickViewBehavior(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_QUICK_VIEW_BEHAVIOR, DEF_QUICK_VIEW_BEHAVIOR);
    }

    public static void setQuickViewBehavior(Context context, int behavior) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_QUICK_VIEW_BEHAVIOR, behavior)
                .apply();
    }

    public static boolean hadShowQuickView(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_HAD_SHOW_QUICK_VIEW, DEF_HAD_SHOW_QUICK_VIEW);
    }

    public static void setHadShowQuickView(Context context, boolean hadShow) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_HAD_SHOW_QUICK_VIEW, hadShow)
                .apply();
    }

    public static long getDailyRemindTime(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(PREF_DAILY_REMIND_TIME, DEF_DAILY_REMIND_TIME);
    }

    public static void setDailyRemindTime(Context context, long time) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(PREF_DAILY_REMIND_TIME, time)
                .apply();
    }

}
