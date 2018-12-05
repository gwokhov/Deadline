package com.gwokhou.deadline.sort;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.gwokhou.deadline.R;
import com.gwokhou.deadline.databinding.SortBottomSheetBinding;
import com.gwokhou.deadline.events.EventsViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

public class SortBottomSheetFragment extends BottomSheetDialogFragment {

    private EventsViewModel mViewModel;

    public static SortBottomSheetFragment newInstance() {

        return new SortBottomSheetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sort_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SortBottomSheetBinding binding = SortBottomSheetBinding.bind(view);
        mViewModel = ViewModelProviders.of(getActivity()).get(EventsViewModel.class);
        binding.setViewmodel(mViewModel);

        SortItemActionListener listener = getSortItemActionListener();

        binding.setListener(listener);
    }

    private SortItemActionListener getSortItemActionListener() {
        return new SortItemActionListener() {
            @Override
            public void onItemClicked(int type) {
                mViewModel.mListener.onSortUpdate(type);
                getDialog().dismiss();
            }

            @Override
            public void onSwitchedSortOrder(boolean isAsc) {
                mViewModel.mListener.onIsAscUpdate(isAsc);
                getDialog().dismiss();
            }
        };
    }

}
