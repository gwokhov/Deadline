package com.gwokhou.deadline.events;

public interface UpdateEventsDataListener {

    void onSortUpdate(int type);

    void onIsAscUpdate(boolean isAsc);

    void onEventDelete();

}
