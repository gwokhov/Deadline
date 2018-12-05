package com.gwokhou.deadline.manageCategories;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ManageCategoriesPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private String[] mTitles;

    public ManageCategoriesPagerAdapter(FragmentManager manager, List<Fragment> list, String[] titles) {
        super(manager);
        mFragments = list;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mTitles[position];
//
//    }
}
