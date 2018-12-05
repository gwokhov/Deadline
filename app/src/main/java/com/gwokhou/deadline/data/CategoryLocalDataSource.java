package com.gwokhou.deadline.data;

import com.gwokhou.deadline.util.AppExecutors;

import java.util.List;

import androidx.annotation.NonNull;

public class CategoryLocalDataSource implements CategoryDataSource{

    private static volatile CategoryLocalDataSource INSTANCE;

    private CategoriesDao mCategoriesDao;

    private AppExecutors mAppExecutors;

    private CategoryLocalDataSource(@NonNull AppExecutors appExecutors,
                                    @NonNull CategoriesDao categoriesDao) {
        mAppExecutors = appExecutors;
        mCategoriesDao = categoriesDao;
    }

    public static CategoryLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                      @NonNull CategoriesDao categoriesDao) {
        if (INSTANCE == null) {
            synchronized (CategoryLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CategoryLocalDataSource(appExecutors, categoriesDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllCategories(@NonNull final LoadCategoriesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Category> categories = mCategoriesDao.getAllCategories();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (categories.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onCategoriesLoaded(categories);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getCategory(@NonNull final String categoryId, @NonNull final GetCategoryCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Category category = mCategoriesDao.getCategoryById(categoryId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (category != null) {
                            callback.onCategoryLoaded(category);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveCategory(@NonNull final Category category) {
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mCategoriesDao.insertCategory(category);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void deleteAllCategories() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mCategoriesDao.deleteAllCategories();
            }
        };
        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteCategory(@NonNull final String categoryId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mCategoriesDao.deleteCategoryById(categoryId);
            }
        };
        mAppExecutors.diskIO().execute(deleteRunnable);
    }
}
