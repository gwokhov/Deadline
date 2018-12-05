package com.gwokhou.deadline.events.drawer;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.gwokhou.deadline.R;
import com.gwokhou.deadline.data.Category;
import com.gwokhou.deadline.events.EventsViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DrawerCategoriesAdapter extends RecyclerView.Adapter<DrawerCategoriesAdapter.ViewHolder> {

    private EventsViewModel mViewModel;

    private List<Category> mCategoryItems;

    private DrawerItemActionListener mItemActionListener;

    private LayoutInflater mInflater;

    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;

        Chip categoryCount;

        ViewHolder(View view) {
            super(view);
            categoryName = view.findViewById(R.id.drawer_item_name);
            categoryCount = view.findViewById(R.id.drawer_item_count);
        }
    }

    public DrawerCategoriesAdapter(Context context, EventsViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mViewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_drawer, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mCategoryItems != null) {
            final Category current = mCategoryItems.get(position);
            holder.categoryName.setText(current.getName());
            holder.categoryCount.setText(String.valueOf(mViewModel.getEventsNum(current.getName())));

            if (mViewModel.category.getValue().equals(current.getName())) {
                holder.itemView.setBackgroundResource(R.drawable.custom_selected_item_bg);
                holder.categoryName.setTextColor(mContext.getResources().getColor(R.color.teal_600));
                holder.categoryName.setTypeface(null, Typeface.BOLD);
                holder.categoryCount.setChipBackgroundColorResource(R.color.teal_600);
                holder.categoryCount.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                holder.itemView.setBackgroundResource(R.drawable.custom_ripple);
                holder.categoryName.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.categoryName.setTypeface(null, Typeface.NORMAL);
                holder.categoryCount.setChipBackgroundColorResource(R.color.teal_50);
                holder.categoryCount.setTextColor(mContext.getResources().getColor(R.color.black_negative));
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

    public void setDrawerItems(List<Category> items) {
        mCategoryItems = items;
        notifyDataSetChanged();
    }

    public void setItemActionListener(DrawerItemActionListener itemActionListener) {
        mItemActionListener = itemActionListener;
    }
}
