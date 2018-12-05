package com.gwokhou.deadline.settings;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gwokhou.deadline.R;
import com.gwokhou.deadline.databinding.FragmentSettingsBinding;
import com.gwokhou.deadline.fragments.ChoiceDialogFragment;
import com.gwokhou.deadline.fragments.DateDialogFragment;
import com.gwokhou.deadline.util.DateTimeUtils;
import com.gwokhou.deadline.util.SystemUIUtils;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;

    private static final String DIALOG_DATE = "DATE";
    private static final String DIALOG_START_OF_WEEK = "START_OF_WEEK";
    private static final String DIALOG_QUICK_VIEW_BEHAVIOR = "QUICK_VIEW_BEHAVIOR";

    private static final int REQUEST_START_OF_WEEK = 0;
    private static final int REQUEST_DAILY_REMIND_TIME = 1;
    private static final int REQUEST_QUICK_VIEW_BEHAVIOR = 2;


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("fuck", "onCreateView: ");
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("fuck", "onViewCreated: ");
        setupSystemUI();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("fuck", "onActivityCreated: ");
        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        FragmentSettingsBinding binding = FragmentSettingsBinding.bind(getView());
        binding.setViewmodel(mViewModel);

        SettingItemActionListener settingItemActionListener = getSettingItemActionListener();
        binding.setListener(settingItemActionListener);
        binding.setLifecycleOwner(this);

        setupDefaultEventMode();
        setupEnableQuickView();
    }

    private SettingItemActionListener getSettingItemActionListener() {
        return new SettingItemActionListener() {
            @Override
            public void onStartOfWeekClicked() {
                int startOfWeek = mViewModel.isMondayWeekStart.getValue() ? 0 : 1;
                ChoiceDialogFragment dialogFragment =
                        ChoiceDialogFragment.newInstance(startOfWeek, R.array.week_start_options, R.string.start_of_the_week);
                dialogFragment.setTargetFragment(SettingsFragment.this, REQUEST_START_OF_WEEK);
                dialogFragment.show(getFragmentManager(), DIALOG_START_OF_WEEK);
            }

            @Override
            public void onDailyReminderClicked() {
                DateDialogFragment dialogFragment = DateDialogFragment.newInstance(mViewModel.dailyRemindTime.getValue(), true);
                dialogFragment.setTargetFragment(SettingsFragment.this, REQUEST_DAILY_REMIND_TIME);
                dialogFragment.show(getFragmentManager(), DIALOG_DATE);
            }

            @Override
            public void onQuickViewBehaviorClicked() {
                ChoiceDialogFragment dialogFragment =
                        ChoiceDialogFragment.newInstance(mViewModel.quickViewBehavior.getValue(), R.array.quick_view_behavior_options, R.string.quick_view_behavior);
                dialogFragment.setTargetFragment(SettingsFragment.this, REQUEST_QUICK_VIEW_BEHAVIOR);
                dialogFragment.show(getFragmentManager(), DIALOG_QUICK_VIEW_BEHAVIOR);
            }
        };
    }

    private void setupSystemUI() {
        Toolbar toolbar = getView().findViewById(R.id.settings_toolbar);
        SystemUIUtils.setupActionBar(getActivity(), true, R.color.white, R.color.teal_200, R.string.settings, toolbar);
    }

    private void setupDefaultEventMode() {
        mViewModel.isDurableModeDefault.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDurable) {
                mViewModel.updateDefaultEventMode(isDurable);
            }
        });
    }

    private void setupEnableQuickView() {
        mViewModel.enableQuickView.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isEnable) {
                mViewModel.updateEnableQuickView(isEnable);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_START_OF_WEEK:
                int day = data.getIntExtra(ChoiceDialogFragment.EXTRA_CHOICE, 0);
                boolean isMondayTheFirstDay = (day == 0);
                mViewModel.updateStartOfWeek(isMondayTheFirstDay);
                break;
            case REQUEST_DAILY_REMIND_TIME:
                long time = data.getLongExtra(DateDialogFragment.EXTRA_DATE, 9* DateTimeUtils.HOUR);
                mViewModel.updateDailyRemindTime(time);
                break;
            case REQUEST_QUICK_VIEW_BEHAVIOR:
                int behavior = data.getIntExtra(ChoiceDialogFragment.EXTRA_CHOICE, 0);
                mViewModel.updateQuickViewBehavior(behavior);
        }

    }

}
