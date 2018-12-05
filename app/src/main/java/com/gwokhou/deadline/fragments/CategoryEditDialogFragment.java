package com.gwokhou.deadline.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.gwokhou.deadline.R;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CategoryEditDialogFragment extends DialogFragment {

    public static final String EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME";

    public static final String EXTRA_DELETE_CATEGORY = "EXTRA_DELETE_CATEGORY";

    private static final String ARG_CATEGORY_LIST = "CATEGORY_LIST";

    private static final String ARG_CATEGORY_NAME = "CATEGORY_NAME";

    private boolean isNew = false;

    private TextInputEditText mCategoryTextField;

    public static CategoryEditDialogFragment newInstance(ArrayList<String> categories, String name) {
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_CATEGORY_LIST, categories);
        args.putString(ARG_CATEGORY_NAME, name);

        CategoryEditDialogFragment fragment = new CategoryEditDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final ArrayList<String> argCategories = getArguments().getStringArrayList(ARG_CATEGORY_LIST);
        final String argName = getArguments().getString(ARG_CATEGORY_NAME);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_category_edit, null);

        mCategoryTextField = view.findViewById(R.id.category_edit);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK, true, null);
                    }
                })
                .create();

        if (argName == null) {
            isNew = true;
            dialog.setTitle(R.string.add_category);
        } else {
            mCategoryTextField.setText(argName);
            dialog.setTitle(R.string.edit_category);
        }

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(mCategoryTextField.getText())) {
                            mCategoryTextField.setError(getString(R.string.category_empty));
                        } else {
                            sendResult(Activity.RESULT_OK, false, mCategoryTextField.getText().toString());
                            dismiss();
                        }
                    }
                });

                if (isNew) {
                    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
                }
            }
        });



        mCategoryTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isNew) {
                    for (String title : argCategories) {
                        if (title.equals(s.toString())) {
                            mCategoryTextField.setError(getString(R.string.category_exists));
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return dialog;

    }

    private void sendResult(int resultCode, boolean isDelete, String category) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DELETE_CATEGORY, isDelete);
        intent.putExtra(EXTRA_CATEGORY_NAME, category);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
