package com.gwokhou.deadline.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.gwokhou.deadline.R;
import com.gwokhou.deadline.util.DateTimeUtils;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DateDialogFragment extends DialogFragment {

    public static final String EXTRA_DATE = "EXTRA_DATE";

    private static final String ARG_DATE = "DATE";

    private static final String ARG_ONLY_TIME_PICKER = "ONLY_TIME_PICKER";

    private Calendar mCalendar;

    private long extraDate;

    private boolean isDatePicker = true;

    public static DateDialogFragment newInstance(long date, boolean onlyTimePicker) {
        Bundle args = new Bundle();
        args.putLong(ARG_DATE, date);
        args.putBoolean(ARG_ONLY_TIME_PICKER, onlyTimePicker);
        DateDialogFragment fragment = new DateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        long argDate = getArguments().getLong(ARG_DATE);
        final boolean argOnlyTimePicker = getArguments().getBoolean(ARG_ONLY_TIME_PICKER);

        if (argDate == 0) {
            mCalendar = Calendar.getInstance();
        } else {
            mCalendar = DateTimeUtils.longToCal(argDate);
        }

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        final DatePicker datePicker = view.findViewById(R.id.dialog_date);
        final TimePicker timePicker = view.findViewById(R.id.dialog_time);

        datePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), null);
        timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(mCalendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(mCalendar.get(Calendar.MINUTE));
        } else {
            timePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
        }

        if (argOnlyTimePicker) {
            timePicker.setVisibility(View.VISIBLE);
            datePicker.setVisibility(View.GONE);
        }

        final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.DatePickerDialog)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setNeutralButton(R.string.date, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isDatePicker && !argOnlyTimePicker) {
                            mCalendar.set(Calendar.YEAR, datePicker.getYear());
                            mCalendar.set(Calendar.MONTH, datePicker.getMonth());
                            mCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                            datePicker.setVisibility(View.GONE);
                            timePicker.setVisibility(View.VISIBLE);
                            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);
                            isDatePicker = false;
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                mCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                                mCalendar.set(Calendar.MINUTE, timePicker.getMinute());
                            } else {
                                mCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                                mCalendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                            }
                            extraDate = DateTimeUtils.calToLong(mCalendar);
                            sendResult(Activity.RESULT_OK, extraDate);
                            dismiss();
                        }

                    }
                });

                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timePicker.setVisibility(View.GONE);
                        datePicker.setVisibility(View.VISIBLE);
                        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
                        isDatePicker = true;
                    }
                });
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        return dialog;
    }

    private void sendResult(int resultCode, long date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
