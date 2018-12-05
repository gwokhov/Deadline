package com.gwokhou.deadline.util;

import com.gwokhou.deadline.data.Event;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtils {

    public static List<Event> sortByPriority(List<Event> list, boolean isAsc) {
        final int asc = isAsc ? 1 : -1;
        Collections.sort(list, new Comparator<Event>() {
            @Override
            public int compare(Event t1, Event t2) {
                return Integer.compare(t2.getPriority(), t1.getPriority()) * asc;
            }
        });
        return list;
    }

    public static List<Event> sortByDueDate(List<Event> list, boolean isAsc) {
        final int asc = isAsc ? -1 : 1;
        Collections.sort(list, new Comparator<Event>() {
            @Override
            public int compare(Event t1, Event t2) {
                return Long.compare(t2.getEndDate(), t1.getEndDate()) * asc;
            }
        });
        return list;
    }

    public static List<Event> sortByAlpha(List<Event> list, boolean isAsc) {
        final int asc = isAsc ? 1 : -1;
        Collections.sort(list, new Comparator<Event>() {
            @Override
            public int compare(Event t1, Event t2) {
                return t1.getTitle().compareToIgnoreCase(t2.getTitle()) * asc;
            }
        });
        return list;
    }

    public static List<Event> sortByCreationDate(List<Event> list, boolean isAsc) {
        final int asc = isAsc ? -1 : 1;
        Collections.sort(list, new Comparator<Event>() {
            @Override
            public int compare(Event t1, Event t2) {
                return Long.compare(t2.getCreationDate(), t1.getCreationDate()) * asc;
            }
        });
        return list;
    }

}
