package com.gwokhou.deadline.events;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import devlight.io.library.ArcProgressStackView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.gwokhou.deadline.AppPreferences;
import com.gwokhou.deadline.R;
import com.gwokhou.deadline.data.Event;
import com.gwokhou.deadline.dataType.FilterType;
import com.gwokhou.deadline.databinding.FragmentEventsBinding;
import com.gwokhou.deadline.editEvent.EditFragment;
import com.gwokhou.deadline.events.drawer.DrawerCategoriesAdapter;
import com.gwokhou.deadline.events.drawer.DrawerFiltersAdapter;
import com.gwokhou.deadline.events.drawer.DrawerItemActionListener;
import com.gwokhou.deadline.events.item.EventItemActionListener;
import com.gwokhou.deadline.events.item.EventTouchHelperCallback;
import com.gwokhou.deadline.fragments.CategoryEditDialogFragment;
import com.gwokhou.deadline.sort.SortBottomSheetFragment;
import com.gwokhou.deadline.util.DateTimeUtils;
import com.gwokhou.deadline.util.SystemUIUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventsFragment extends Fragment {

    private static final int REQUEST_SORT = 0;

    private static final int REQUEST_ADD_CATEGORY = 1;

    private static final String DIALOG_SORT = "SORT";

    private static final String DIALOG_ADD_CATEGORY = "ADD_CATEGORY";

    private EventsViewModel mViewModel;
    private FragmentEventsBinding mBinding;

    private EventsAdapter mEventsAdapter;
    private DrawerFiltersAdapter mFiltersAdapter;
    private DrawerCategoriesAdapter mCategoriesAdapter;

    private DrawerLayout mDrawerLayout;
    private BottomAppBar mAppBar;
    private Snackbar mSnackbar;

    private ArcProgressStackView mDateProgress;
    private TextView mTodayProgress;
    private TextView mWeekProgress;
    private TextView mMonthProgress;
    private TextView mYearProgress;

    private BackPressedHandler mBackPressedHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(getActivity() instanceof BackPressedHandler)) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        } else {
            mBackPressedHandler = (BackPressedHandler) getActivity();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(getActivity()).get(EventsViewModel.class);
        mBinding = FragmentEventsBinding.bind(getView());
        mBinding.setViewmodel(mViewModel);
        mBinding.setLifecycleOwner(this);

        mViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                if (events != null) {
                    mViewModel.hasLoadedEvents = true;
                    updateShowedEventList();
                    mViewModel.updateEventStateNum();
                    mViewModel.updateTodayEventNum();
                    mViewModel.updateQuickViewTitle();
                }
            }
        });

        setupSystemUI();
        setupActionBar();
        setupEventList();
        setupDrawer();
        setupSnackbar();
        setupQuickView();

        setupDrawerFilterList();
        setupDrawerCategoryList();
        setupDateProgress();

        setupEventsDataUpdateListener();

    }

    @Override
    public void onStart() {
        super.onStart();
        mBackPressedHandler.setSelectedFragment(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mViewModel.hasLoadedEvents) {
            mViewModel.updateIfCategoriesChanged();
            mViewModel.updateDisplayGotIt();
            checkIfDeletedEvents();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.getAllEvents().removeObservers(this);
    }

    public boolean onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private void setupSystemUI() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.teal_300));
        } else {
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.white, null));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        SystemUIUtils.hideKeyBoard(getActivity());
    }

    private void setupActionBar() {
        mAppBar = mBinding.bottomBar;

        mAppBar.inflateMenu(R.menu.menu_events);

        MenuItem showOrHideCompleted = mAppBar.getMenu().findItem(R.id.action_is_show_completed_events);
        boolean isShow = AppPreferences.isShowCompletedEvents(getContext());
        showOrHideCompleted.setTitle(isShow ? R.string.hide_completed_events : R.string.show_completed_events);

        mAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_sort:
                        SortBottomSheetFragment bottomSheetFragment = SortBottomSheetFragment.newInstance();
                        bottomSheetFragment.setTargetFragment(EventsFragment.this, REQUEST_SORT);
                        bottomSheetFragment.show(getFragmentManager(), DIALOG_SORT);
                        break;
                    case R.id.action_is_show_completed_events:
                        boolean isShow = AppPreferences.isShowCompletedEvents(getContext());
                        item.setTitle(!isShow ? R.string.hide_completed_events : R.string.show_completed_events);
                        mViewModel.setIsShowCompleted(!isShow);
                        updateShowedEventList();
                }
                return true;

            }
        });

    }

    private void setupDrawer() {
        mDrawerLayout = mBinding.eventsDrawer;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, mAppBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    updateDrawerData();
                }
                super.onDrawerStateChanged(newState);
            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        TextView addCategory = mBinding.drawerAddCategory;

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryEditDialogFragment dialogFragment = CategoryEditDialogFragment.newInstance(mViewModel.getCategoriesNames(), null);
                dialogFragment.setTargetFragment(EventsFragment.this, REQUEST_ADD_CATEGORY);
                dialogFragment.show(getFragmentManager(), DIALOG_ADD_CATEGORY);
            }
        });

    }

    private void setupEventList() {
        RecyclerView recyclerView = mBinding.listEvents;
        mEventsAdapter = new EventsAdapter(getContext(), mViewModel);

        recyclerView.setAdapter(mEventsAdapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(new EventTouchHelperCallback(mEventsAdapter));
        touchHelper.attachToRecyclerView(recyclerView);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_fall_down);
        recyclerView.setLayoutAnimation(animationController);

        mEventsAdapter.setEventItemActionListener(new EventItemActionListener() {
            @Override
            public void onItemClicked(String eventId) {
                Bundle bundle = new Bundle();
                bundle.putString(EditFragment.ARG_EDIT_EVENT_ID, eventId);
                Navigation.findNavController(getView()).navigate(R.id.action_events_to_edit, bundle);
            }
        });
    }

    private void setupQuickView() {
//        mViewModel.isShowQuickView.setValue(AppPreferences.isEnableQuickView(getContext()));
        if (mViewModel.isShowQuickView.getValue()) {
            MaterialButton button = getView().findViewById(R.id.check_today_event);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.updateCurrentCategory(FilterType.TODAY_EVENTS);
                    updateShowedEventList();
                }
            });
        }

    }

    private void setupDateProgress() {
        String[] fgColors = getResources().getStringArray(R.array.date_progress_fg);
        String[] bgColors = getResources().getStringArray(R.array.date_progress_bg);

        float todayProgress = DateTimeUtils.getTodayProgress();
        float weekProgress = DateTimeUtils.getDayOfWeekProgress(AppPreferences.isMondayTheFirstDay(getContext()));
        float monthProgress = DateTimeUtils.getMonthProgress();
        float yearProgress = DateTimeUtils.getYearProgress();

        ArrayList<ArcProgressStackView.Model> dateModels = new ArrayList<>();
        dateModels.add(new ArcProgressStackView.Model(getString(R.string.day), todayProgress, Color.parseColor(bgColors[0]), Color.parseColor(fgColors[0])));
        dateModels.add(new ArcProgressStackView.Model(getString(R.string.week), weekProgress, Color.parseColor(bgColors[1]), Color.parseColor(fgColors[1])));
        dateModels.add(new ArcProgressStackView.Model(getString(R.string.month), monthProgress, Color.parseColor(bgColors[2]), Color.parseColor(fgColors[2])));
        dateModels.add(new ArcProgressStackView.Model(getString(R.string.year), yearProgress, Color.parseColor(bgColors[3]), Color.parseColor(fgColors[3])));

        mDateProgress = getView().findViewById(R.id.progress_date_arc);
        mDateProgress.setModels(dateModels);

        mTodayProgress = getView().findViewById(R.id.progress_today);
        mWeekProgress = getView().findViewById(R.id.progress_week);
        mMonthProgress = getView().findViewById(R.id.progress_month);
        mYearProgress = getView().findViewById(R.id.progress_year);

        mTodayProgress.setText(getString(R.string.progress_today_summary, DateTimeUtils.getCurrentDateTimeName(Calendar.DAY_OF_WEEK), todayProgress));
        mWeekProgress.setText(getString(R.string.progress_week_summary, weekProgress));
        mMonthProgress.setText(getString(R.string.progress_month_summary, DateTimeUtils.getCurrentDateTimeName(Calendar.MONTH), monthProgress));
        mYearProgress.setText(getString(R.string.progress_year_summary, DateTimeUtils.getCurrentDateTimeName(Calendar.YEAR), yearProgress));
    }

    private void setupSnackbar() {
        mSnackbar = Snackbar.make(getView(), "", Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.snack_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.undoDeletedEvents();
                    }
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (!mSnackbar.isShown()) {
                            mViewModel.clearDeletedEvents();
                        }
                    }
                });

        View snackbarView = mSnackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, 256);
        snackbarView.setLayoutParams(params);
    }

    private void setupDrawerFilterList() {
        RecyclerView recyclerView = mBinding.listDrawerFilter;
        mFiltersAdapter = new DrawerFiltersAdapter(getContext(), mViewModel);
        recyclerView.setAdapter(mFiltersAdapter);
        mFiltersAdapter.setItemActionListener(new DrawerItemActionListener() {
            @Override
            public void onItemClicked(String type) {
                clickedDrawerItem(type);
            }
        });
    }

    private void setupDrawerCategoryList() {
        RecyclerView recyclerView = mBinding.listDrawerCategory;
        mCategoriesAdapter = new DrawerCategoriesAdapter(getContext(), mViewModel);
        recyclerView.setAdapter(mCategoriesAdapter);
        mCategoriesAdapter.setItemActionListener(new DrawerItemActionListener() {
            @Override
            public void onItemClicked(String type) {
                clickedDrawerItem(type);
            }
        });
    }

    private void setupEventsDataUpdateListener() {
        mViewModel.setListener(new UpdateEventsDataListener() {
            @Override
            public void onSortUpdate(int type) {
                mViewModel.updateCurrentSortType(type);
                updateShowedEventList();
            }

            @Override
            public void onIsAscUpdate(boolean isAsc) {
                mViewModel.updateCurrentSortOrder(isAsc);
                updateShowedEventList();
            }

            @Override
            public void onEventDelete() {
                checkIfDeletedEvents();
            }
        });
    }

    private void clickedDrawerItem(String type) {
        mViewModel.updateCurrentCategory(type);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        updateShowedEventList();
    }

    private void updateDrawerData() {
        mFiltersAdapter.setDrawerItems(mViewModel.filterItems);
        mCategoriesAdapter.setDrawerItems(mViewModel.categoryItems);
    }

    private void updateShowedEventList() {
        mEventsAdapter.setEvents(mViewModel.getEvents());
        mViewModel.updateCurrentEventNum();
    }

    private void checkIfDeletedEvents() {
        int deletedEventsNum = mViewModel.getDeletedEventsNum();

        if (deletedEventsNum != 0) {
            mSnackbar.setText(getResources().getQuantityString(R.plurals.snack_deleted_events, deletedEventsNum, deletedEventsNum))
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_ADD_CATEGORY) {
            String category = data.getStringExtra(CategoryEditDialogFragment.EXTRA_CATEGORY_NAME);
            mViewModel.addCategory(category);
            updateDrawerData();
        }
    }

}
