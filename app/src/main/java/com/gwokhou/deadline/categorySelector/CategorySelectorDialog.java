package com.gwokhou.deadline.categorySelector;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gwokhou.deadline.R;
import com.gwokhou.deadline.data.Category;
import com.gwokhou.deadline.fragments.CategoryEditDialogFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class CategorySelectorDialog extends DialogFragment{

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY ";

    private static final String DIALOG_EDIT_CATEGORY = "EDIT_CATEGORY";

    private static final int REQUEST_EDIT_CATEGORY = 0;

    private static final String ARG_CATEGORY = "CATEGORY";

    private CategorySelectDialogViewModel mViewModel;

    private View mView;

    private CategoryAdapter mAdapter;

    private String mArgCategory;

    public static CategorySelectorDialog newInstance(String category) {
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);

        CategorySelectorDialog fragment = new CategorySelectorDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mArgCategory = getArguments().getString(ARG_CATEGORY);
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_category_select, null);

        return new AlertDialog.Builder(getActivity())
                .setView(mView)
                .setTitle(R.string.select_category)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(CategorySelectDialogViewModel.class);
        mViewModel.updateSelected(mArgCategory);

        setupCategoryList();
        setupAddCategoryBtn();

        mViewModel.updateCategories();
    }

    private void setupCategoryList() {
        RecyclerView recyclerView = mView.findViewById(R.id.category_list);
        mAdapter = new CategoryAdapter(getContext(), mViewModel);
        recyclerView.setAdapter(mAdapter);

        mViewModel.setLoadedListener(new CategoriesLoadedListener() {
            @Override
            public void onLoadedFinished(List<Category> categories) {
                mAdapter.setCategoryItems(categories);
            }
        });

        mAdapter.setItemActionListener(new CategoryItemActionListener() {
            @Override
            public void onItemClicked(String category) {
                mViewModel.updateSelected(category);
                sendResult(Activity.RESULT_OK, mViewModel.currentSelected);
                dismiss();
            }
        });

    }

    private void setupAddCategoryBtn() {
        TextView textView = mView.findViewById(R.id.select_add_category);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryEditDialogFragment dialogFragment = CategoryEditDialogFragment.newInstance(mViewModel.getCategoriesNames(), null);
                dialogFragment.setTargetFragment(CategorySelectorDialog.this, REQUEST_EDIT_CATEGORY);
                dialogFragment.show(getFragmentManager(), DIALOG_EDIT_CATEGORY);
            }
        });
    }

    private void sendResult(int resultCode, String category) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CATEGORY, category);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_EDIT_CATEGORY) {
            String category = data.getStringExtra(CategoryEditDialogFragment.EXTRA_CATEGORY_NAME);
            mViewModel.addCategory(category);
        }
    }

}
