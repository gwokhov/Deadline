package com.gwokhou.deadline.manageCategories;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.gwokhou.deadline.R;
import com.gwokhou.deadline.manageCategories.custom.CustomCategoriesFragment;
import com.gwokhou.deadline.manageCategories.smart.SmartCategoriesFragment;
import com.gwokhou.deadline.util.SystemUIUtils;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoriesFragment extends Fragment {

    public static ManageCategoriesFragment newInstance() {
        return new ManageCategoriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.manage_categories_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSystemUI();
        setupTabLayout();
    }

    private void setupSystemUI() {
        Toolbar toolbar = getView().findViewById(R.id.manage_categories_toolbar);
        SystemUIUtils.setupActionBar(getActivity(), true, R.color.white, R.color.teal_200, R.string.manage_categories, toolbar);
    }

    private void setupTabLayout() {
        TabLayout tabLayout = getView().findViewById(R.id.manage_categories_tab);
        ViewPager viewPager = getView().findViewById(R.id.manage_categories_view_pager);

        String[] tabTitles = {getString(R.string.custom_categories), getString(R.string.smart_categories)};

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(CustomCategoriesFragment.newInstance());
        fragments.add(SmartCategoriesFragment.newInstance());

        ManageCategoriesPagerAdapter pagerAdapter = new ManageCategoriesPagerAdapter(getChildFragmentManager(), fragments, tabTitles);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(R.string.custom_categories).setIcon(R.drawable.ic_category);
        tabLayout.getTabAt(1).setText(R.string.smart_categories).setIcon(R.drawable.ic_next_7_days);
    }

}
