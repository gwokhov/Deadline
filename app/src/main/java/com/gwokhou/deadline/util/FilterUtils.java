package com.gwokhou.deadline.util;

import com.gwokhou.deadline.data.Event;
import com.gwokhou.deadline.dataType.StateType;

import java.util.ArrayList;
import java.util.List;

public class FilterUtils {

    public static List<Event> filterTodayTasks(List<Event> events) {
        List<Event> mEvent = new ArrayList<>();
        for (Event event : events) {
            if (event.getEndDate() >= DateTimeUtils.getTodayStart() && event.getEndDate() <= DateTimeUtils.getTodayEnd()) {
                mEvent.add(event);
            }
        }
        return mEvent;
    }

    public static List<Event> filterNext7DaysTasks(List<Event> events) {
        List<Event> mEvent = new ArrayList<>();
        for (Event event : events) {
            if (event.getEndDate() >= DateTimeUtils.getTodayStart() && event.getEndDate() <= DateTimeUtils.getNext7DaysEnd()) {
                mEvent.add(event);
            }
        }
        return mEvent;
    }

    public static List<Event> filterCompletedEvents(List<Event> events) {
        List<Event> mEvent = new ArrayList<>();
        for (Event event : events) {
            if (event.getState() == StateType.COMPLETED) {
                mEvent.add(event);
            }
        }
        return mEvent;
    }

    public static List<Event> filterCategoryEvents(List<Event> events, String category) {
        List<Event> mEvent = new ArrayList<>();
        for (Event event : events) {
            if (event.getCategory().equals(category)) {
                mEvent.add(event);
            }
        }
        return mEvent;
    }

    public static List<Event> filterUncompletedEvents(List<Event> events) {
        List<Event> mEvent = new ArrayList<>();
        for (Event event : events) {
            if (event.getState() != StateType.COMPLETED) {
                mEvent.add(event);
            }
        }
        return mEvent;
    }

    public static List<Event> cloneEvents(List<Event> events) {
        return new ArrayList<>(events);
    }

}
