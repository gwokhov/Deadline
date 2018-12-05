package com.gwokhou.deadline.manageCategories.custom;

import android.app.Application;

import com.gwokhou.deadline.data.Category;
import com.gwokhou.deadline.data.CategoryDataSource;
import com.gwokhou.deadline.data.DeadlineRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.AndroidViewModel;

public class CustomCategoriesViewModel extends AndroidViewModel {

    public ObservableList<Category> items = new ObservableArrayList<>();

    private Category cacheCategory;

    public boolean isOpenNewCategory = false;

    private DeadlineRepository mRepository;

    private ArrayList<String> mCategoriesName = new ArrayList<>(0);

    public CustomCategoriesViewModel(@NonNull Application application) {
        super(application);
        mRepository = DeadlineRepository.getInstance(application);
        initCategories();
    }

    private void initCategories() {
        mRepository.getAllCategories(new CategoryDataSource.LoadCategoriesCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
                items.clear();
                items.addAll(categories);

                mCategoriesName.clear();
                for (Category category : categories) {
                    mCategoriesName.add(category.getName());
                }
            }

            @Override
            public void onDataNotAvailable() { }
        });
    }

    public ArrayList<String> getCategoriesName() {
        return mCategoriesName;
    }

    private void getCategory(String id) {
        mRepository.getCategory(id, new CategoryDataSource.GetCategoryCallback() {
            @Override
            public void onCategoryLoaded(Category category) {
                cacheCategory = category;
            }

            @Override
            public void onDataNotAvailable() { }
        });
    }

    public String getCategoryName(String id) {
        getCategory(id);
        return cacheCategory.getName();
    }

    public void addCategory(String categoryName) {
        mRepository.saveCategory(new Category(categoryName));
        initCategories();
    }

    public void updateCategory(String categoryName) {
        mRepository.saveCategory(new Category(categoryName, cacheCategory.getId()));
        initCategories();
    }

    public void deleteCategory() {
        mRepository.deleteCategory(cacheCategory.getId());
        initCategories();
    }
}
