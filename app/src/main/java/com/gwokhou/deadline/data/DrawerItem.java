package com.gwokhou.deadline.data;

public class DrawerItem {

    private int mTitleRes;

    private int mIconRes;

    private String mType;

    public DrawerItem(int title, int iconRes, String type) {
        mTitleRes = title;
        mIconRes = iconRes;
        mType = type;
    }

    public int getTitleRes() {
        return mTitleRes;
    }

    public int getIconRes() {
        return mIconRes;
    }

    public String getType() {
        return mType;
    }
}
