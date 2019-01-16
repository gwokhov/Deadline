package com.gwokhou.deadline.editEvent;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.gwokhou.deadline.dataType.PriorityType;
import com.gwokhou.deadline.R;
import com.gwokhou.deadline.SnackbarMessage;
import com.gwokhou.deadline.dataType.RemindType;
import com.gwokhou.deadline.categorySelector.CategorySelectorDialog;
import com.gwokhou.deadline.databinding.FragmentEditBinding;
import com.gwokhou.deadline.fragments.ChoiceDialogFragment;
import com.gwokhou.deadline.fragments.DateDialogFragment;
import com.gwokhou.deadline.reminderSelect.ReminderDialogFragment;
import com.gwokhou.deadline.fragments.TypeTipsDialogFragment;
import com.gwokhou.deadline.util.DateTimeUtils;
import com.gwokhou.deadline.util.SnackbarUtils;
import com.gwokhou.deadline.util.SystemUIUtils;

public class EditFragment extends Fragment {

    public static final String ARG_EDIT_EVENT_ID = "EDIT_EVENT_ID";
    private static final String DIALOG_DATE = "DATE";
    private static final String DIALOG_CATEGORY = "CATEGORY";
    private static final String DIALOG_PRIORITY = "PRIORITY";
    private static final String DIALOG_REMINDER = "REMINDER";
    private static final String DIALOG_TYPE_TIPS = "TYPE_TIPS";

    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_END_DATE = 1;
    private static final int REQUEST_CATEGORY = 2;
    private static final int REQUEST_PRIORITY = 3;
    private static final int REQUEST_REMINDER = 4;

    private EditViewModel mViewModel;

    private FragmentEditBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        mViewModel = ViewModelProviders.of(this).get(EditViewModel.class);
        mBinding = FragmentEditBinding.bind(view);
        mBinding.setViewmodel(mViewModel);
        mBinding.setLifecycleOwner(this);

        EditActionListener actionListener = getEditActionListener();
        mBinding.setListener(actionListener);

        return view;
    }

    private EditActionListener getEditActionListener() {
        return new EditActionListener() {
            @Override
            public void onCheckBoxClicked() {
                checkIfCompleted();
            }

            @Override
            public void onTipsClicked() {
                setupTips();
            }

            @Override
            public void onStartDateClicked() {
                setupStartDate();
            }

            @Override
            public void onDueDateClicked() {
                setupDueDate();
            }

            @Override
            public void onCategoryClicked() {
                setupCategory();
            }

            @Override
            public void onPriorityClicked() {
                setupPriority();
            }

            @Override
            public void onReminderClicked() {
                setupReminder();
            }

            @Override
            public void onFabClicked() {
                setupFab();
            }
        };
    }

    private void checkIfCompleted() {
        if (mViewModel.isCompleted.get()) {
            TextInputEditText title = getView().findViewById(R.id.title_edit);
            title.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else {
            TextInputEditText title = getView().findViewById(R.id.title_edit);
            title.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSystemUI();
        setupSnackbar();
        fixEditTextHint();
        loadData();
        checkIfCompleted();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getArguments() != null && !getArguments().isEmpty()) {
            inflater.inflate(R.menu.menu_edit, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_del:
                mViewModel.deleteEvent();
                Navigation.findNavController(getView()).navigateUp();
                break;
            case android.R.id.home:
                Navigation.findNavController(getView()).navigateUp();
                break;
        }
        return true;
    }

    private void setupSnackbar() {
        mViewModel.getSnackbarMessage().observe(this, new SnackbarMessage.SnackbarObserver() {
            @Override
            public void onNewMessage(@StringRes int snackbarMessageResourceId) {
                SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId));
            }
        });
    }

    private void setupSystemUI() {
        Toolbar toolbar = getView().findViewById(R.id.edit_toolbar);
        SystemUIUtils.setupActionBar(getActivity(), false, R.color.teal_700, R.color.teal_700, R.string.edit, toolbar);
    }

    private void fixEditTextHint() {
        mBinding.titleEdit.setHint("");
        mBinding.noteEdit.setHint("");
    }

    private void setupFab() {
        if (mViewModel.saveEvent()) {
            Navigation.findNavController(getView()).navigateUp();
        }
    }

    private void loadData() {
        if (getArguments() != null && !getArguments().isEmpty()) {
            mViewModel.loadData(getArguments().getString(ARG_EDIT_EVENT_ID));
        } else {
            mViewModel.loadData(null);
        }
    }

    private void setupStartDate() {
        DateDialogFragment dialogFragment = DateDialogFragment.newInstance(mViewModel.startDate.get(), false);
        dialogFragment.setTargetFragment(EditFragment.this, REQUEST_START_DATE);
        dialogFragment.show(getFragmentManager(), DIALOG_DATE);
    }

    private void setupDueDate() {
        long date = mViewModel.dueDate.get() == 0 ? DateTimeUtils.getCurrentTimeWithoutSec() : mViewModel.dueDate.get();
        DateDialogFragment dialogFragment = DateDialogFragment.newInstance(date, false);
        dialogFragment.setTargetFragment(EditFragment.this, REQUEST_END_DATE);
        dialogFragment.show(getFragmentManager(), DIALOG_DATE);
    }

    private void setupTips() {
        TypeTipsDialogFragment dialogFragment = TypeTipsDialogFragment.newInstance();
        dialogFragment.show(getFragmentManager(), DIALOG_TYPE_TIPS);
    }

    private void setupCategory() {
        CategorySelectorDialog dialogFragment =
                CategorySelectorDialog.newInstance(mViewModel.category.get());
        dialogFragment.setTargetFragment(EditFragment.this, REQUEST_CATEGORY);
        dialogFragment.show(getFragmentManager(), DIALOG_CATEGORY);
    }

    private void setupPriority() {
        ChoiceDialogFragment dialogFragment =
                ChoiceDialogFragment.newInstance(mViewModel.priority.get(), R.array.priority_options, R.string.event_priority);
        dialogFragment.setTargetFragment(EditFragment.this, REQUEST_PRIORITY);
        dialogFragment.show(getFragmentManager(), DIALOG_PRIORITY);
    }

    private void setupReminder() {
        ReminderDialogFragment dialogFragment = ReminderDialogFragment.newInstance(mViewModel.reminder.get());
        dialogFragment.setTargetFragment(EditFragment.this, REQUEST_REMINDER);
        dialogFragment.show(getFragmentManager(), DIALOG_REMINDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_START_DATE:
                long startDate = data.getLongExtra(DateDialogFragment.EXTRA_DATE, 0);
                mViewModel.startDate.set(startDate);
                break;
            case REQUEST_END_DATE:
                long endDate = data.getLongExtra(DateDialogFragment.EXTRA_DATE, 0);
                mViewModel.dueDate.set(endDate);
                break;
            case REQUEST_CATEGORY:
                String category = data.getStringExtra(CategorySelectorDialog.EXTRA_CATEGORY);
                mViewModel.category.set(category);
                break;
            case REQUEST_PRIORITY:
                int priority = data.getIntExtra(ChoiceDialogFragment.EXTRA_CHOICE, PriorityType.NONE);
                mViewModel.priority.set(priority);
                break;
            case REQUEST_REMINDER:
                int remindData = data.getIntExtra(ReminderDialogFragment.EXTRA_REMIND, RemindType.NONE_REMIND);
                mViewModel.reminder.set(remindData);

        }

    }

}
