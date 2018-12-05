package com.gwokhou.deadline.data;

import java.util.List;

import androidx.annotation.NonNull;

public interface CategoryDataSource {

    interface LoadCategoriesCallback {

        void onCategoriesLoaded(List<Category> categories);

        void onDataNotAvailable();

    }

    interface GetCategoryCallback {

        void onCategoryLoaded(Category category);

        void onDataNotAvailable();

    }

    void getAllCategories(@NonNull LoadCategoriesCallback  callback);

    void getCategory(@NonNull String categoryId, @NonNull GetCategoryCallback callback);

    void saveCategory(@NonNull Category category);

    void deleteAllCategories();

    void deleteCategory(@NonNull String categoryId);


}
