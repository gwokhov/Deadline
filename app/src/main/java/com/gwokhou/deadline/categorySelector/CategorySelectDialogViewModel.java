package com.gwokhou.deadline.categorySelector;

import android.app.Application;

import com.gwokhou.deadline.R;
import com.gwokhou.deadline.data.Category;
import com.gwokhou.deadline.data.CategoryDataSource;
import com.gwokhou.deadline.data.DeadlineRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;

public class CategorySelectDialogViewModel extends AndroidViewModel {

    private DeadlineRepository mRepository;

    private CategoriesLoadedListener mLoadedListener;

    private List<Category> mCategories = new ArrayList<>(0);

    private ArrayList<String> mCategoriesName = new ArrayList<>(0);

    String currentSelected = getApplication().getString(R.string.inbox);

    public CategorySelectDialogViewModel(Application application) {
        super(application);
        mRepository = DeadlineRepository.getInstance(application);
    }

    void updateSelected(String category) {
        currentSelected = category;
    }

    void updateCategories() {
        mRepository.getAllCategories(new CategoryDataSource.LoadCategoriesCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
                mCategories.clear();
                mCategories.addAll(categories);

                mCategoriesName.clear();
                for (Category category : mCategories) {
                    mCategoriesName.add(category.getName());
                }
            }

            @Override
            public void onDataNotAvailable() {
            }
        });
        if (mLoadedListener != null) {
            mCategories.add(0, new Category(getApplication().getString(R.string.inbox), "inbox"));
            mLoadedListener.onLoadedFinished(mCategories);
        }
    }

    ArrayList<String> getCategoriesNames() {
        return mCategoriesName;
    }

    void addCategory(String categoryName) {
        mRepository.saveCategory(new Category(categoryName));
        updateCategories();
    }

    void setLoadedListener(CategoriesLoadedListener loadedListener) {
        mLoadedListener = loadedListener;
    }
}
