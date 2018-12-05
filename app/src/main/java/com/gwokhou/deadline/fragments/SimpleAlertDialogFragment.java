package com.gwokhou.deadline.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SimpleAlertDialogFragment extends DialogFragment {

    public static final String EXTRA_IS_CONFIRM = "EXTRA_IS_CONFIRM";

    private static final String ARG_TITLE = "TITLE";

    private static final String ARG_CONTENT = "CONTENT";

    public static SimpleAlertDialogFragment newInstance(int titleRes, int contentRes) {
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, titleRes);
        args.putInt(ARG_CONTENT, contentRes);
        SimpleAlertDialogFragment fragment = new SimpleAlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int argTitle = getArguments().getInt(ARG_TITLE);
        int argContent = getArguments().getInt(ARG_CONTENT);

        return new AlertDialog.Builder(getContext())
                .setTitle(argTitle)
                .setMessage(argContent)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK, true);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK, false);
                        dismiss();
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, boolean isConfirm) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IS_CONFIRM, isConfirm);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
