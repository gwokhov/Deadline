package com.gwokhou.deadline.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.gwokhou.deadline.R;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChoiceDialogFragment extends DialogFragment {

    public static final String EXTRA_CHOICE = "EXTRA_CHOICE";

    private static final String ARG_CHOICE = "CHOICE";

    private static final String ARG_CHOICE_ARR = "CHOICE_ARR";

    private static final String ARG_TITLE = "TITLE";

    public static ChoiceDialogFragment newInstance(int choice, int choiceArray, int title) {
        Bundle args = new Bundle();
        args.putInt(ARG_CHOICE, choice);
        args.putInt(ARG_CHOICE_ARR, choiceArray);
        args.putInt(ARG_TITLE, title);
        ChoiceDialogFragment fragment = new ChoiceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int argChoice = getArguments().getInt(ARG_CHOICE);
        int argChoiceArr = getArguments().getInt(ARG_CHOICE_ARR);
        int argTitle = getArguments().getInt(ARG_TITLE);
        String[] stringArray = getContext().getResources().getStringArray(argChoiceArr);
        List<String> stringList = Arrays.asList(stringArray);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_simple_choice, null);
        RecyclerView recyclerView = view.findViewById(R.id.simple_choice_list);
        recyclerView.setAdapter(new SimpleChoiceAdapter(stringList, argChoice));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(argTitle)
                .create();

    }

    public class SimpleChoiceAdapter extends RecyclerView.Adapter<SimpleChoiceAdapter.ViewHolder> {

        List<String> mStringList;

        int mChoice;

        private LayoutInflater mInflater;

        class ViewHolder extends RecyclerView.ViewHolder {

            CheckedTextView mText;

            ViewHolder(View view) {
                super(view);
                mText = view.findViewById(R.id.simple_choice_item);
            }

        }

        SimpleChoiceAdapter(List<String> list, int choice) {
            mInflater = LayoutInflater.from(getContext());
            mStringList = list;
            mChoice = choice;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.item_simple_choice, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            if (mStringList != null) {
                String current = mStringList.get(position);
                holder.mText.setText(current);

                if (mChoice == position) {
                    holder.mText.setChecked(true);
                    holder.mText.setCheckMarkDrawable(R.drawable.ic_done);
                    holder.mText.setTextColor(getResources().getColor(R.color.teal_600));
                    holder.mText.setTypeface(null, Typeface.BOLD);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendResult(Activity.RESULT_OK, position);
                        dismiss();
                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            if (mStringList != null) {
                return mStringList.size();
            } else {
                return 0;
            }
        }

    }

    private void sendResult(int resultCode, int choice) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CHOICE, choice);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
