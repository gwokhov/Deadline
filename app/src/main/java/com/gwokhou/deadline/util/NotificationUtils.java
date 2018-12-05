package com.gwokhou.deadline.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.gwokhou.deadline.AppPreferences;
import com.gwokhou.deadline.MainActivity;
import com.gwokhou.deadline.R;
import com.gwokhou.deadline.dataType.RemindType;

import androidx.core.app.NotificationCompat;

public class NotificationUtils {

    public static final String EXTRA_IS_REPEATED = "IS_REPEATED";

    public static final String EXTRA_EVENT_TITLE = "EVENT_TITLE";

    public static final String EXTRA_EVENT_DATE = "EVENT_DATE";

    public static final String EXTRA_EVENT_ID = "EVENT_ID";

    public static final String EXTRA_NOTIFICATION_TITLE = "NOTIFICATION_TITLE";

    public static final String EXTRA_NOTIFICATION_CONTENT = "NOTIFICATION_CONTENT";

    public static void buildNormalReminder(Context context, long date, String title, int type, int interval, String id) {

        long showDate;
        String dateUnit;

        switch (type) {
            case RemindType.SINGLE_DUE_DATE:
                showDate = date;
                dateUnit = context.getString(R.string.remind_unit_now);
                break;
            case RemindType.SINGLE_MIN:
                showDate = date - interval * DateTimeUtils.MINUTE;
                dateUnit = context.getResources().getQuantityString(R.plurals.remind_unit_minute, interval, interval);
                break;
            case RemindType.SINGLE_HOUR:
                showDate = date - interval * DateTimeUtils.HOUR;
                dateUnit = context.getResources().getQuantityString(R.plurals.remind_unit_hour, interval, interval);
                break;
            case RemindType.SINGLE_DAY:
                showDate = date - interval * DateTimeUtils.DAY;
                dateUnit = context.getResources().getQuantityString(R.plurals.remind_unit_day, interval, interval);
                break;
            case RemindType.SINGLE_WEEK:
                showDate = date - interval * DateTimeUtils.WEEK;
                dateUnit = context.getResources().getQuantityString(R.plurals.remind_unit_week, interval, interval);
                break;
            default:
                showDate = date;
                dateUnit = context.getString(R.string.remind_unit_now);
        }

        createAlarmManager(context, date, title, id, showDate, dateUnit, false);
    }

    public static void buildOngoingReminder(Context context) {

    }

    public static void buildRepeatedReminder(Context context, long date, String title, String id) {
        long showDate;
        if (System.currentTimeMillis() >= DateTimeUtils.getTodayStart() + DateTimeUtils.HOUR * 9) {
            showDate = DateTimeUtils.getTodayStart() + DateTimeUtils.DAY + DateTimeUtils.HOUR * 9;
        } else {
            long dailyRemindTime = AppPreferences.getDailyRemindTime(context);
            showDate = DateTimeUtils.getTodayStart() + dailyRemindTime;
        }
        String dateUnit = DateTimeUtils.convertTimeUnit(context, date - System.currentTimeMillis());
        createAlarmManager(context, date, title, id, showDate, dateUnit, true);
    }

    public static void cancelReminder(Context context, String id) {
        ComponentName componentName = new ComponentName(context, "com.gwokhou.deadline.ReminderReceiver");
        Intent intent = new Intent();
        intent.setData(Uri.parse(id));
        intent.setComponent(componentName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }

    /**
     * @param date     通知的内容显示的时间
     * @param title    通知标题
     * @param id       标志alarmmanager的id,即task的id
     * @param showDate 通知出现的时间
     * @param dateUnit 通知标题的单位
     */
    private static void createAlarmManager(Context context, long date, String title, String id, long showDate, String dateUnit, boolean isRepeated) {
        ComponentName componentName = new ComponentName(context, "com.gwokhou.deadline.ReminderReceiver");
        Intent intent = new Intent();
        intent.setData(Uri.parse(id));
        intent.setComponent(componentName);
        intent.putExtra(EXTRA_IS_REPEATED, isRepeated);
        intent.putExtra(EXTRA_NOTIFICATION_TITLE, context.getString(R.string.remind_title, title, dateUnit));
        intent.putExtra(EXTRA_NOTIFICATION_CONTENT, DateTimeUtils.longToString(date, DateTimeUtils.MEDIUM));

        if (isRepeated) {
            intent.putExtra(EXTRA_EVENT_TITLE, title);
            intent.putExtra(EXTRA_EVENT_DATE, date);
            intent.putExtra(EXTRA_EVENT_ID, id);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, showDate, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, showDate, pendingIntent);
        }
    }

    public static void showNotification(Context context, String title, String content) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context, MainActivity.EVENT_NOTIFICATION_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(1, notification);
    }

}
