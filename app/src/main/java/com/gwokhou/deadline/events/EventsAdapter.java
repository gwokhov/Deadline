package com.gwokhou.deadline.events;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.material.card.MaterialCardView;
import com.gwokhou.deadline.R;
import com.gwokhou.deadline.TimerUpdateListener;
import com.gwokhou.deadline.data.Event;
import com.gwokhou.deadline.dataType.PriorityType;
import com.gwokhou.deadline.dataType.StateType;
import com.gwokhou.deadline.events.item.EventItemActionListener;
import com.gwokhou.deadline.events.item.EventTouchHelperListener;
import com.gwokhou.deadline.util.DateTimeUtils;
import com.gwokhou.deadline.view.TimerView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> implements EventTouchHelperListener {

    private EventsViewModel mViewModel;
    private EventItemActionListener mEventItemActionListener;

    private final LayoutInflater mInflater;
    private List<Event> mEvents;
    private Context mContext;

    @Override
    public void onItemSwipeToStart(int position) {
        if (mEvents.get(position).getState() != StateType.COMPLETED) {
            mViewModel.updateEventState(mEvents.get(position), StateType.COMPLETED);
        } else {
            mViewModel.deleteEvent(mEvents.get(position).getId());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private Event mEvent;
        private TextView mTitle;
        private AppCompatImageView mPriority;
        private TextView mState;
        private ProgressBar mProgress;
        private TimelineView mMarker;
        private View mTimerContainer;
        private TimerView mTimer;
        private TextView mTimerState;
        private MaterialCardView mCard;
        private TextView mDueDate;
        private ImageView mSlideIcon;

        private ViewHolder(View itemView, int type) {
            super(itemView);

            mCard = itemView.findViewById(R.id.event_card);
            mTitle = itemView.findViewById(R.id.event_title);
            mPriority = itemView.findViewById(R.id.event_priority);
            mMarker = itemView.findViewById(R.id.event_marker);
            mTimerContainer = itemView.findViewById(R.id.event_timer_container);
            mTimer = itemView.findViewById(R.id.event_timer);
            mTimerState = itemView.findViewById(R.id.event_timer_state);
            mState = itemView.findViewById(R.id.event_state);
            mProgress = itemView.findViewById(R.id.event_progress);
            mDueDate = itemView.findViewById(R.id.event_due_date);
            mSlideIcon = itemView.findViewById(R.id.slide_icon);
            mMarker.initLine(type);
        }

        private void bind(Event event) {
            mEvent = event;
            mTitle.setText(mEvent.getTitle());
            mDueDate.setText(DateTimeUtils.longToString(mEvent.getEndDate(), DateTimeUtils.DATE));
            setPriority();

            if (mEvent.isDurableEvent()) {
                mState.setVisibility(View.VISIBLE);
                refreshState(mEvent.getState());
            } else {
                mState.setVisibility(View.INVISIBLE);
                mProgress.setVisibility(View.GONE);
                refreshNormal(mEvent.getState());
            }
        }

        private void refreshState(int state) {
            switch (state) {
                case StateType.ONGOING:
                    if (mEvent.getEndDate() - System.currentTimeMillis() <= 0) {
                        mViewModel.updateEventState(mEvent, StateType.GONE);
                    } else {
                        onStateOngoing(mEvent.getEndDate() - System.currentTimeMillis(), mEvent.getEndDate() - mEvent.getStartDate());
                    }
                    break;
                case StateType.GONE:
                    onStateGone();
                    break;
                case StateType.WAITING:
                    if (mEvent.getStartDate() - System.currentTimeMillis() <= 0) {
                        mViewModel.updateEventState(mEvent, StateType.ONGOING);
                    } else {
                        onStateWaiting(mEvent.getStartDate() - System.currentTimeMillis());
                    }
                    break;
                case StateType.COMPLETED:
                    onCompleted();
            }
        }

        private void refreshNormal(int state) {
            if (state == StateType.COMPLETED) {
                onCompleted();
            } else {
                activeEvent();
                mMarker.setMarker(mContext.getDrawable(R.drawable.ic_circle));
                if (mEvent.getEndDate() - System.currentTimeMillis() <= 0) {
                    mTimer.start(System.currentTimeMillis() - mEvent.getEndDate());
                    DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
                    dynamicConfigBuilder.setShowSecond(false);
                    dynamicConfigBuilder.setShowMinute(false);
                    mTimer.dynamicShow(dynamicConfigBuilder.build());
                    mTimerState.setText(R.string.timer_state_passed);
                } else {
                    mTimer.start(mEvent.getEndDate() - System.currentTimeMillis());
                    toTealTheme();
                    mTimerState.setText(R.string.timer_state_to_reach);
                }
            }

        }

        private void onCompleted() {
            mTitle.setTextColor(mContext.getResources().getColor(R.color.black_negative));
            mTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

            mProgress.setVisibility(View.GONE);
            mState.setText(mContext.getResources().getText(R.string.done));
            mDueDate.setTextColor(mContext.getResources().getColor(R.color.teal_600));
            if (mEvent.getPriority() != PriorityType.NONE) {
                mDueDate.setPadding(0, 0, 48, 0);
            } else {
                mDueDate.setPadding(0, 0, 0, 0);
            }
            mMarker.setMarker(mContext.getDrawable(R.drawable.ic_completed));
            mCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.yellow_500));
            mSlideIcon.setImageResource(R.drawable.ic_delete_dark);
            mTimerContainer.setVisibility(View.GONE);
            mTimer.stop();
        }

        private void onStateOngoing(long remain, final long total) {
            activeEvent();
            toTealTheme();
            mProgress.setVisibility(View.VISIBLE);
            mMarker.setMarker(mContext.getDrawable(R.drawable.ic_ongoing));
            mTimerState.setText(R.string.timer_state_to_end);
            mTimer.start(remain);

            mTimer.setTimerUpdateListener(new TimerUpdateListener() {
                @Override
                public void onUpdateView(long duration) {
                    float percent = DateTimeUtils.countPercent(total, duration);
                    String state = mContext.getString(R.string.percent, percent);
                    mState.setText(state);
                    mProgress.setProgress((int) percent);
                }
            });

            mTimer.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    onStateGone();
                    mViewModel.updateEventState(mEvent, StateType.GONE);
                }
            });
        }

        private void onStateGone() {
            mTitle.setTextColor(mContext.getResources().getColor(R.color.white));
            mTitle.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);

            mState.setText(mContext.getResources().getText(R.string.gone));
            mProgress.setVisibility(View.GONE);
            mDueDate.setTextColor(mContext.getResources().getColor(R.color.white));
            if (mEvent.getPriority() != PriorityType.NONE) {
                mDueDate.setPadding(0, 0, 48, 0);
            } else {
                mDueDate.setPadding(0, 0, 0, 0);
            }
            mMarker.setMarker(mContext.getDrawable(R.drawable.ic_gone));
            mTimerContainer.setVisibility(View.GONE);
            mTimer.stop();
            mCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.red_700));
            mSlideIcon.setImageResource(R.drawable.ic_done);
        }

        private void onStateWaiting(long duration) {
            activeEvent();
            mTitle.setTextColor(mContext.getResources().getColor(R.color.black_negative));

            mState.setText(mContext.getResources().getText(R.string.waiting));
            mDueDate.setTextColor(mContext.getResources().getColor(R.color.black_secondary));
            mProgress.setVisibility(View.GONE);
            mMarker.setMarker(mContext.getDrawable(R.drawable.ic_waiting));
            mCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.gray));

            mTimerState.setText(R.string.timer_state_to_start);
            mTimerState.setTextColor(mContext.getResources().getColor(R.color.black_a70));
            DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
            dynamicConfigBuilder
                    .setTimeTextColor(mContext.getResources().getColor(R.color.black_a70))
                    .setSuffixTextColor(mContext.getResources().getColor(R.color.black_a70));
            mTimer.dynamicShow(dynamicConfigBuilder.build());
            mTimer.start(duration);

            mTimer.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    refreshState(StateType.ONGOING);
                    mViewModel.updateEventState(mEvent, StateType.ONGOING);
                }
            });
        }

        private void setPriority() {
            switch (mEvent.getPriority()) {
                case PriorityType.NONE:
                    mPriority.setVisibility(View.GONE);
                    break;
                case PriorityType.LOW:
                    mPriority.setVisibility(View.VISIBLE);
                    mPriority.setImageResource(R.drawable.ic_priority_low);
                    break;
                case PriorityType.MEDIUM:
                    mPriority.setVisibility(View.VISIBLE);
                    mPriority.setImageResource(R.drawable.ic_priority_medium);
                    break;
                case PriorityType.HIGH:
                    mPriority.setVisibility(View.VISIBLE);
                    mPriority.setImageResource(R.drawable.ic_priority_high);
                    break;
                default:
                    mPriority.setVisibility(View.GONE);
            }
        }

        private void activeEvent() {
            mTitle.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
            mDueDate.setPadding(0, 0, 0, 0);
            mSlideIcon.setImageResource(R.drawable.ic_done);
            mTimerContainer.setVisibility(View.VISIBLE);
        }

        private void toTealTheme() {
            mTitle.setTextColor(mContext.getResources().getColor(R.color.white));
            mDueDate.setTextColor(mContext.getResources().getColor(R.color.yellow_300));
            mCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.teal_700));
            mTimerState.setTextColor(mContext.getResources().getColor(R.color.white_secondary));
            DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
            dynamicConfigBuilder
                    .setTimeTextColor(mContext.getResources().getColor(R.color.white_secondary))
                    .setSuffixTextColor(mContext.getResources().getColor(R.color.white_secondary));
            mTimer.dynamicShow(dynamicConfigBuilder.build());
        }
    }

    EventsAdapter(Context context, EventsViewModel viewModel) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mViewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_event, parent, false);
        return new ViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (mEvents != null) {
            final Event current = mEvents.get(position);
            holder.bind(current);

            if (mEventItemActionListener != null) {
                holder.mCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEventItemActionListener.onItemClicked(current.getId());
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        if (mEvents != null) {
            return mEvents.size();
        } else {
            return 0;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        holder.mTimer.stop();
    }

    public void setEvents(List<Event> events) {
        mEvents = events;
        notifyDataSetChanged();
    }

    public void setEventItemActionListener(EventItemActionListener listener) {
        mEventItemActionListener = listener;
    }
}
