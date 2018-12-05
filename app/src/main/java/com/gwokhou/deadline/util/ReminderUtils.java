package com.gwokhou.deadline.util;

import com.gwokhou.deadline.dataType.RemindType;

public class ReminderUtils {

    //个位数0代表无重复通知，1代表每天显示，2代表常驻通知栏
    //十位数0代表无，1代表分，2代表时，3代表天，4代表月
    //间隔单位乘100

    public static int getSingleRemindInterval(int data) {
        if (data < 10) {
            return RemindType.NONE_REMIND;
        } else {
            return data / 10;
        }
    }

    public static int getRemindType(int data) {
        return data % 10;
    }

    public static int buildReminder(int remindType, int singleRemindInterval) {
        if (remindType == RemindType.NONE_REMIND) {
            return RemindType.NONE_REMIND;
        } else {
            return singleRemindInterval * 10 + remindType;
        }

    }

}
