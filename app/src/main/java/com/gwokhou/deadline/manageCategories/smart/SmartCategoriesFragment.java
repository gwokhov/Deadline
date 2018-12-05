package com.gwokhou.deadline.manageCategories.smart;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gwokhou.deadline.AppPreferences;
import com.gwokhou.deadline.R;
import com.gwokhou.deadline.databinding.FragmentSmartCategoriesBinding;
import com.gwokhou.deadline.util.UpdateSelectedCategoryPreferences;

public class SmartCategoriesFragment extends Fragment{

    private SmartCategoriesViewModel mViewModel;

    public static SmartCategoriesFragment newInstance() {
        return new SmartCategoriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_smart_categories, container, false);
        FragmentSmartCategoriesBinding binding = FragmentSmartCategoriesBinding.bind(view);
        mViewModel = ViewModelProviders.of(this).get(SmartCategoriesViewModel.class);
        binding.setViewModel(mViewModel);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupSwitch();
    }

    private void setupSwitch() {
        mViewModel.showAllEventsCategory.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isShow) {
                AppPreferences.setIsShowAllEventsCategory(getContext(), isShow);
                UpdateSelectedCategoryPreferences.updateAppPreferences(getContext());
            }
        });

        mViewModel.showTodayEventsCategory.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isShow) {
                AppPreferences.setIsShowTodayEventsCategory(getContext(), isShow);
                UpdateSelectedCategoryPreferences.updateAppPreferences(getContext());
            }
        });

        mViewModel.showNext7DaysEventsCategory.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isShow) {
                AppPreferences.setIsShowNext7DaysEventsCategory(getContext(), isShow);
                UpdateSelectedCategoryPreferences.updateAppPreferences(getContext());
            }
        });

        mViewModel.showCompletedEventsCategory.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isShow) {
                AppPreferences.setIsShowCompletedEventsCategory(getContext(), isShow);
                UpdateSelectedCategoryPreferences.updateAppPreferences(getContext());
            }
        });
    }
}
