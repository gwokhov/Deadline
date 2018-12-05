package com.gwokhou.deadline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import com.gwokhou.deadline.events.BackPressedHandler;
import com.gwokhou.deadline.events.EventsFragment;

public class MainActivity extends AppCompatActivity implements BackPressedHandler {

    public static final String EVENT_NOTIFICATION_ID = "EVENT_NOTIFICATION";

    private EventsFragment mEventsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(EVENT_NOTIFICATION_ID, getString(R.string.event_notification), NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        setupIntroPage();

    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

    private void setupIntroPage() {
        if (AppPreferences.isFirstBoot(this)) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_events_to_intro);
        }
    }

    @Override
    public void onBackPressed() {
        if (mEventsFragment == null || !mEventsFragment.onBackPressed() || !mEventsFragment.isVisible()) {
            super.onBackPressed();
        }
    }

    @Override
    public void setSelectedFragment(EventsFragment fragment) {
        mEventsFragment = fragment;
    }
}
