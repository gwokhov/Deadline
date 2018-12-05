package com.gwokhou.deadline.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CategoriesDao {

    @Query("SELECT * FROM category_table")
    List<Category> getAllCategories();

    @Query("SELECT * FROM category_table WHERE entry_id = :categoryId")
    Category getCategoryById(String categoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(Category category);

    @Query("DELETE FROM category_table WHERE entry_id = :categoryId")
    int deleteCategoryById(String categoryId);

    @Query("DELETE FROM category_table")
    void deleteAllCategories();

}
