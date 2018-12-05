package com.gwokhou.deadline.categorySelector;

import com.gwokhou.deadline.data.Category;

import java.util.List;

public interface CategoriesLoadedListener {

    void onLoadedFinished(List<Category> categories);

}
