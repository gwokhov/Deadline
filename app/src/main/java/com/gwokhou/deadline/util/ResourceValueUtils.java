package com.gwokhou.deadline.util;

import com.gwokhou.deadline.R;
import com.gwokhou.deadline.dataType.FilterType;
import com.gwokhou.deadline.dataType.RemindType;

public class ResourceValueUtils {

    public static int getFilterChipTitle(String type) {
        if (type != null) {

            switch (type) {
                case FilterType.ALL_EVENTS:
                    return R.string.chip_all;
                case FilterType.TODAY_EVENTS:
                    return R.string.chip_today;
                case FilterType.NEXT_7_DAYS_EVENTS:
                    return R.string.chip_next_7_days;
                case FilterType.COMPLETED_EVENTS:
                    return R.string.chip_completed;
                case FilterType.INBOX:
                    return R.string.chip_inbox;
                default:
                    return 0;
            }
        } else {
            return R.string.chip_all;
        }
    }

    //PRIORITY = 0;
    //DUE_DATE = 1;
    //CREATION_DATE = 2;
    //ALPHA = 3;
    public static int getSortChipTitle(int grade) {
        int[] resArray = new int[]{
                R.string.chip_sort_priority,
                R.string.chip_sort_due_date,
                R.string.chip_sort_creation_date,
                R.string.chip_sort_alpha
        };
        return resArray[grade];
    }

    //DAWN = 0;
    //MORNING = 1;
    //AFTERNOON = 2;
    //EVENING = 3;
    public static int getQuickViewTitle(int period) {
        int[] resArray = new int[]{
                R.plurals.quick_view_dawn_desc,
                R.plurals.quick_view_morning_desc,
                R.plurals.quick_view_afternoon_desc,
                R.plurals.quick_view_evening_desc
        };
        return resArray[period];
    }

    public static int getQuickViewPic(int period) {
        int[] resArray = new int[]{
                R.drawable.ils_morning,
                R.drawable.ils_morning,
                R.drawable.ils_noon,
                R.drawable.ils_night
        };
        return resArray[period];
    }

    public static int getReminderDesc(int type) {
        switch (type) {
            case RemindType.SINGLE_DUE_DATE:
                return R.string.single_remind_at_due_date;
            case RemindType.SINGLE_MIN:
                return R.plurals.single_remind_minutes_desc;
            case RemindType.SINGLE_HOUR:
                return R.plurals.single_remind_hours_desc;
            case RemindType.SINGLE_DAY:
                return R.plurals.single_remind_days_desc;
            case RemindType.SINGLE_WEEK:
                return R.plurals.single_remind_weeks_desc;
            case RemindType.REPEATED_EVERYDAY:
                return R.string.repeated_remind_everyday;
            case RemindType.REPEATED_ALWAYS:
                return R.string.repeated_remind_always;
            default:
                return R.string.single_remind_none;
        }
    }

    public static int getPriorityDesc(int priority) {
        int[] resArray = new int[]{
                R.string.priority_none,
                R.string.priority_low,
                R.string.priority_medium,
                R.string.priority_high
        };
        return resArray[priority];
    }

    public static int getQuickViewBehDesc(int behavior) {
        int[] resArray = new int[]{
                R.string.quick_view_show_every_time,
//                R.string.quick_view_show_once_per_day,
                R.string.quick_view_sticky
        };
        return resArray[behavior];
    }

}
