package com.gwokhou.deadline.manageCategories.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gwokhou.deadline.R;
import com.gwokhou.deadline.data.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomCategoriesAdapter extends RecyclerView.Adapter<CustomCategoriesAdapter.ViewHolder> {

    private List<Category> mCategoryList;

    private CustomCategoryItemActionListener mCustomCategoryItemActionListener;

    private LayoutInflater mInflater;


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;

        ViewHolder(View view) {
            super(view);
            categoryName = view.findViewById(R.id.custom_category_name);
        }
    }

    public CustomCategoriesAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_custom_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mCategoryList != null) {
            final Category current = mCategoryList.get(position);
            holder.categoryName.setText(current.getName());

            if (mCustomCategoryItemActionListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCustomCategoryItemActionListener.onItemClicked(current.getId());
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        if (mCategoryList != null) {
            return mCategoryList.size();
        } else {
            return 0;
        }
    }

    public void setCategoryList(List<Category> categories) {
        mCategoryList = categories;
        notifyDataSetChanged();
    }

    public void setCustomCategoryItemActionListener(CustomCategoryItemActionListener listener) {
        mCustomCategoryItemActionListener = listener;
    }
}
