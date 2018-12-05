package com.gwokhou.deadline.reminderSelect;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.gwokhou.deadline.R;
import com.gwokhou.deadline.dataType.RemindType;
import com.gwokhou.deadline.databinding.DialogReminderBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

public class ReminderDialogFragment extends DialogFragment {

    public static final String EXTRA_REMIND = "EXTRA_REMIND";

    private static final String ARG_REMIND_DATA = "REMIND";

    private ReminderDialogViewModel mViewModel;

    private DialogReminderBinding mBinding;


    public static ReminderDialogFragment newInstance(int remindData) {
        Bundle args = new Bundle();
        args.putInt(ARG_REMIND_DATA, remindData);
        ReminderDialogFragment fragment = new ReminderDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final int argRemind = getArguments().getInt(ARG_REMIND_DATA);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_reminder, null);
        mBinding = DialogReminderBinding.bind(view);
        mViewModel = ViewModelProviders.of(this).get(ReminderDialogViewModel.class);

        mBinding.setViewmodel(mViewModel);

        mViewModel.loadData(argRemind);
        setupPopupMenu();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.reminder)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK, mViewModel.getReminder());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .create();
    }

    private void setupPopupMenu() {
        final PopupMenu popupMenu = new PopupMenu(getActivity(), mBinding.remindIntervalUnit, Gravity.CENTER, 0, android.R.style.Widget_Material_Light_PopupMenu_Overflow);
        popupMenu.inflate(R.menu.menu_reminder_units);
        mBinding.remindIntervalUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
                mViewModel.updateSelections(RemindType.SINGLE_REMIND);
            }
        });
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_remind_minutes:
                        mViewModel.singleRemindUnit.set(RemindType.SINGLE_MIN);
                        break;
                    case R.id.action_remind_hours:
                        mViewModel.singleRemindUnit.set(RemindType.SINGLE_HOUR);
                        break;
                    case R.id.action_remind_days:
                        mViewModel.singleRemindUnit.set(RemindType.SINGLE_DAY);
                        break;
                    case R.id.action_remind_weeks:
                        mViewModel.singleRemindUnit.set(RemindType.SINGLE_WEEK);
                        break;
                }
                return true;
            }
        });
    }

    private void sendResult(int resultCode, int typeData) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_REMIND, typeData);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
