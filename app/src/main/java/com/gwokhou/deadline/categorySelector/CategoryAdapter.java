package com.gwokhou.deadline.categorySelector;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.gwokhou.deadline.R;
import com.gwokhou.deadline.data.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private CategorySelectDialogViewModel mViewModel;

    private List<Category> mCategoryItems;

    private CategoryItemActionListener mItemActionListener;

    private LayoutInflater mInflater;

    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {

        CheckedTextView categoryItem;

        ViewHolder(View view) {
            super(view);
            categoryItem = view.findViewById(R.id.selectable_category_item);
        }
    }

    CategoryAdapter(Context context, CategorySelectDialogViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mViewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_category_select, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mCategoryItems != null) {
            final Category current = mCategoryItems.get(position);
            holder.categoryItem.setText(current.getName());

            if (mViewModel.currentSelected.equals(current.getName())) {
                holder.categoryItem.setChecked(true);
                holder.categoryItem.setTextColor(mContext.getResources().getColor(R.color.teal_600));
                holder.categoryItem.setTypeface(null, Typeface.BOLD);
                holder.categoryItem.setCheckMarkDrawable(R.drawable.ic_done);
            } else {
                holder.categoryItem.setChecked(false);
                holder.categoryItem.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.categoryItem.setTypeface(null, Typeface.NORMAL);
                holder.categoryItem.setCheckMarkDrawable(null);
            }

            if (mItemActionListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemActionListener.onItemClicked(current.getName());
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        if (mCategoryItems != null) {
            return mCategoryItems.size();
        } else {
            return 0;
        }
    }

    public void setCategoryItems(List<Category> items) {
        mCategoryItems = items;
        notifyDataSetChanged();
    }

    public void setItemActionListener(CategoryItemActionListener itemActionListener) {
        mItemActionListener = itemActionListener;
    }

}
