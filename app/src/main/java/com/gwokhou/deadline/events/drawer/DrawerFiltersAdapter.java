package com.gwokhou.deadline.events.drawer;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.gwokhou.deadline.R;
import com.gwokhou.deadline.data.DrawerItem;
import com.gwokhou.deadline.events.EventsViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DrawerFiltersAdapter extends RecyclerView.Adapter<DrawerFiltersAdapter.ViewHolder> {

    private EventsViewModel mViewModel;

    private List<DrawerItem> mDrawerItems;

    private DrawerItemActionListener mItemActionListener;

    private LayoutInflater mInflater;

    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView filterIcon;

        TextView filterName;

        Chip filterCount;

        ViewHolder(View view) {
            super(view);
            filterIcon = view.findViewById(R.id.drawer_item_icon);
            filterName = view.findViewById(R.id.drawer_item_name);
            filterCount = view.findViewById(R.id.drawer_item_count);
        }
    }

    public DrawerFiltersAdapter(Context context, EventsViewModel viewModel) {
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
        if (mDrawerItems != null) {
            final DrawerItem current = mDrawerItems.get(position);
            holder.filterIcon.setImageResource(current.getIconRes());
            holder.filterName.setText(current.getTitleRes());
            holder.filterCount.setText(String.valueOf(mViewModel.getEventsNum(current.getType())));

            if (mViewModel.category.getValue().equals(current.getType())) {
                holder.itemView.setBackgroundResource(R.drawable.custom_selected_item_bg);
                holder.filterIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.teal_600));
                holder.filterName.setTextColor(mContext.getResources().getColor(R.color.teal_600));
                holder.filterName.setTypeface(null, Typeface.BOLD);
                holder.filterCount.setChipBackgroundColorResource(R.color.teal_600);
                holder.filterCount.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                holder.itemView.setBackgroundResource(R.drawable.custom_ripple);
                holder.filterIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.teal_800));
                holder.filterName.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.filterName.setTypeface(null, Typeface.NORMAL);
                holder.filterCount.setChipBackgroundColorResource(R.color.teal_50);
                holder.filterCount.setTextColor(mContext.getResources().getColor(R.color.black_negative));
            }

            if (mItemActionListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemActionListener.onItemClicked(current.getType());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mDrawerItems != null) {
            return mDrawerItems.size();
        } else {
            return 0;
        }
    }

    public void setDrawerItems(List<DrawerItem> items) {
        mDrawerItems = items;
        notifyDataSetChanged();
    }

    public void setItemActionListener(DrawerItemActionListener itemActionListener) {
        mItemActionListener = itemActionListener;
    }
}
