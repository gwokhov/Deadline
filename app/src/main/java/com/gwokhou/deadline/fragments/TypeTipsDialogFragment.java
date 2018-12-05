package com.gwokhou.deadline.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.gwokhou.deadline.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class TypeTipsDialogFragment extends DialogFragment {

    public static final String EXTRA_HAS_OPEN_TYPE_TIPS = "EXTRA_HAS_OPEN_TYPE_TIPS";

    public static TypeTipsDialogFragment newInstance() {
        return new TypeTipsDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_about_durable_event, null);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.about_durable_event)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK, true);
                        dismiss();
                    }
                })
                .create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    private void sendResult(int resultCode, boolean hasOpen) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_HAS_OPEN_TYPE_TIPS, hasOpen);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
