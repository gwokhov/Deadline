package com.gwokhou.deadline.manageCategories.custom;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gwokhou.deadline.R;
import com.gwokhou.deadline.fragments.CategoryEditDialogFragment;

public class CustomCategoriesFragment extends Fragment {

    private static final int REQUEST_CATEGORY = 0;

    private static final String DIALOG_CATEGORY = "CATEGORY";

    private CustomCategoriesViewModel mViewModel;

    private CustomCategoriesAdapter mAdapter;

    public static CustomCategoriesFragment newInstance() {
        return new CustomCategoriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_categories, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CustomCategoriesViewModel.class);

        setupRecyclerView();

        setupFab();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = getView().findViewById(R.id.custom_categories_list);
        mAdapter = new CustomCategoriesAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter.setCategoryList(mViewModel.items);
        mAdapter.setCustomCategoryItemActionListener(new CustomCategoryItemActionListener() {
            @Override
            public void onItemClicked(String id) {
                String name = mViewModel.getCategoryName(id);
                mViewModel.isOpenNewCategory = false;
                openEditDialog(name);
            }
        });
    }

    private void setupFab() {
        FloatingActionButton fab = getView().findViewById(R.id.fab_add_category);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.isOpenNewCategory = true;
                openEditDialog(null);
            }
        });
    }

    private void openEditDialog(String categoryName) {
        CategoryEditDialogFragment categoryEditDialogFragment = CategoryEditDialogFragment.newInstance(mViewModel.getCategoriesName(), categoryName);
        categoryEditDialogFragment.setTargetFragment(CustomCategoriesFragment.this, REQUEST_CATEGORY);
        categoryEditDialogFragment.show(getFragmentManager(), DIALOG_CATEGORY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CATEGORY) {
            boolean isDelete = data.getBooleanExtra(CategoryEditDialogFragment.EXTRA_DELETE_CATEGORY, true);
            if (isDelete) {
                mViewModel.deleteCategory();
            } else {
                String categoryName = data.getStringExtra(CategoryEditDialogFragment.EXTRA_CATEGORY_NAME);
                if (mViewModel.isOpenNewCategory) {
                    mViewModel.addCategory(categoryName);
                } else {
                    mViewModel.updateCategory(categoryName);
                }
            }
            mAdapter.setCategoryList(mViewModel.items);
        }
    }
}
