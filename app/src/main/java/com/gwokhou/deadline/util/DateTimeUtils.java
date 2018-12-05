package com.gwokhou.deadline.util;

import android.content.Context;

import com.gwokhou.deadline.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {

    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long WEEK = DAY * 7;

    public static final int DAWN = 0;
    public static final int MORNING = 1;
//    public static final int NOON = 12;
    public static final int AFTERNOON = 2;
    public static final int EVENING = 3;

    public static final int DATE = 0;
    public static final int TIME = 1;
    public static final int MEDIUM = 2;

    public static String dateToString(Date date, int format) {
        switch (format) {
            case DATE:
                return new SimpleDateFormat("MM/d").format(date);
            case TIME:
                return SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(date);
            case MEDIUM:
                return SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(date);
            default:
                return SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(date);
        }
    }

    public static Calendar longToCal(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar;
    }

    public static String longToString(long date, int format) {
        if (date == 0) {
            return "";
        }

        Date date1 = new Date(date);
        return dateToString(date1, format);
    }

    public static long calToLong(Calendar date) {
        return date.getTimeInMillis();
    }

    public static float countPercent(long interval, long duration) {
        return ((float) (interval - duration) / interval) * 100;
    }

    public static long getCurrentTimeWithoutSec() {
        return (System.currentTimeMillis() / 10000) * 10000;
    }

    public static int getTimePeriod() {
        long now = System.currentTimeMillis();
        long todayStart = getTodayStart();

        if (todayStart + 2 * HOUR <= now && now < todayStart + 6 * HOUR) {
            return DAWN;
        } else if (todayStart + 6 * HOUR <= now && now < todayStart + 12 * HOUR) {
            return MORNING;
        } else if (todayStart + 12 * HOUR <= now && now < todayStart + 17 * HOUR) {
            return AFTERNOON;
        } else {
            return EVENING;
        }
    }

    public static long getTodayStart() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        return calToLong(todayStart);
    }

    public static long getTodayEnd() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        return calToLong(todayEnd);
    }

    public static long getDayOfWeekStart(boolean isMondayTheFirstDay) {
        Calendar weekStart = Calendar.getInstance();
        weekStart.set(Calendar.HOUR_OF_DAY, 0);
        weekStart.set(Calendar.MINUTE, 0);
        weekStart.set(Calendar.SECOND, 0);
        if (isMondayTheFirstDay) {
            int dayOfWeek = weekStart.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == 1) {
                dayOfWeek += 7;
            }
            weekStart.add(Calendar.DATE, 2 - dayOfWeek);
        } else {
            weekStart.set(Calendar.DAY_OF_WEEK, 1);
        }
        return calToLong(weekStart);
    }

    public static long getDayOfWeekEnd(boolean isMondayTheFirstDay) {
        Calendar weekEnd = Calendar.getInstance();
        weekEnd.setTimeInMillis(getDayOfWeekStart(isMondayTheFirstDay));
        weekEnd.add(Calendar.DAY_OF_WEEK, 6);
        weekEnd.set(Calendar.HOUR_OF_DAY, 23);
        weekEnd.set(Calendar.MINUTE, 59);
        weekEnd.set(Calendar.SECOND, 59);
        return calToLong(weekEnd);
    }

    public static long getMonthStart() {
        Calendar monthStart = Calendar.getInstance();
        monthStart.set(Calendar.DATE, 1);
        monthStart.set(Calendar.HOUR_OF_DAY, 0);
        monthStart.set(Calendar.MINUTE, 0);
        monthStart.set(Calendar.SECOND, 0);
        return calToLong(monthStart);
    }

    public static long getMonthEnd() {
        Calendar monthEnd = Calendar.getInstance();
        monthEnd.set(Calendar.DATE, 1);
        monthEnd.set(Calendar.HOUR_OF_DAY, 23);
        monthEnd.set(Calendar.MINUTE, 59);
        monthEnd.set(Calendar.SECOND, 59);
        monthEnd.add(Calendar.MONTH, 1);
        monthEnd.add(Calendar.DATE, -1);
        return calToLong(monthEnd);
    }

    public static long getYearStart() {
        Calendar yearStart = Calendar.getInstance();
        yearStart.set(Calendar.DAY_OF_YEAR, 1);
        yearStart.set(Calendar.HOUR_OF_DAY, 0);
        yearStart.set(Calendar.MINUTE, 0);
        yearStart.set(Calendar.SECOND, 0);
        return calToLong(yearStart);
    }

    public static long getYearEnd() {
        Calendar yearEnd = Calendar.getInstance();
        yearEnd.set(Calendar.MONTH, 11);
        yearEnd.set(Calendar.DATE, 31);
        yearEnd.set(Calendar.HOUR_OF_DAY, 23);
        yearEnd.set(Calendar.MINUTE, 59);
        yearEnd.set(Calendar.SECOND, 59);
        return calToLong(yearEnd);
    }

    public static long getNext7DaysEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, 1);
        return calToLong(calendar);
    }

    public static float getTodayProgress() {
        return ((float) (System.currentTimeMillis() - getTodayStart()) / (getTodayEnd() - getTodayStart())) * 100;
    }

    public static float getDayOfWeekProgress(boolean isMondayTheFirstDay) {
        return ((float) (System.currentTimeMillis() - getDayOfWeekStart(isMondayTheFirstDay)) / (getDayOfWeekEnd(isMondayTheFirstDay) - getDayOfWeekStart(isMondayTheFirstDay))) * 100;
    }

    public static float getMonthProgress() {
        return ((float) (System.currentTimeMillis() - getMonthStart()) / (getMonthEnd() - getMonthStart())) * 100;
    }

    public static float getYearProgress() {
        return ((float) (System.currentTimeMillis() - getYearStart()) / (getYearEnd() - getYearStart())) * 100;
    }

    public static String getCurrentDateTimeName(int field) {
        Calendar calendar = Calendar.getInstance();
        if (field == Calendar.YEAR) {
            return String.valueOf(calendar.get(Calendar.YEAR));
        } else {
            return calendar.getDisplayName(field, Calendar.SHORT, Locale.getDefault());
        }
    }

    public static String convertTimeUnit(Context context, long interval) {
        if (interval >= DAY) {
            int unit = (int) TimeUnit.DAYS.convert(interval, TimeUnit.MILLISECONDS);
            return context.getResources().getQuantityString(R.plurals.remind_unit_day, unit, unit);
        } else if (interval >= HOUR) {
            int unit = (int) TimeUnit.HOURS.convert(interval, TimeUnit.MILLISECONDS);
            return context.getResources().getQuantityString(R.plurals.remind_unit_hour, unit, unit);
        } else if (interval >= MINUTE) {
            int unit = (int) TimeUnit.MINUTES.convert(interval, TimeUnit.MILLISECONDS);
            return context.getResources().getQuantityString(R.plurals.remind_unit_minute, unit, unit);
        } else {
            int unit = (int) TimeUnit.SECONDS.convert(interval, TimeUnit.MILLISECONDS);
            return context.getResources().getQuantityString(R.plurals.remind_unit_second, unit, unit);
        }
    }

}
