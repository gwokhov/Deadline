package com.gwokhou.deadline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gwokhou.deadline.util.NotificationUtils;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isRepeated = intent.getBooleanExtra(NotificationUtils.EXTRA_IS_REPEATED, false);


        String notificationTitle = intent.getStringExtra(NotificationUtils.EXTRA_NOTIFICATION_TITLE);
        String content = intent.getStringExtra(NotificationUtils.EXTRA_NOTIFICATION_CONTENT);

        if (!isRepeated) {
            NotificationUtils.showNotification(context, notificationTitle, content);
        } else {
            NotificationUtils.showNotification(context, notificationTitle, content);
            String eventTitle = intent.getStringExtra(NotificationUtils.EXTRA_EVENT_TITLE);
            long eventDate = intent.getLongExtra(NotificationUtils.EXTRA_EVENT_DATE, 0);
            String id = intent.getStringExtra(NotificationUtils.EXTRA_EVENT_ID);

            NotificationUtils.buildRepeatedReminder(context, eventDate, eventTitle, id);
        }
    }

}
