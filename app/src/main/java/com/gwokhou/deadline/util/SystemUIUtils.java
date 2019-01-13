package com.gwokhou.deadline.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import com.gwokhou.deadline.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SystemUIUtils {

    public static void setupActionBar(Activity activity, boolean isLightBar, int bgColorRes, int bgColorRes2, int titleRes, Toolbar toolbar) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.setStatusBarColor(activity.getResources().getColor(bgColorRes2));
            if (!isLightBar) {
                toolbar.setTitleTextColor(activity.getResources().getColor(R.color.white));
            }
        } else {
            window.setStatusBarColor(activity.getResources().getColor(bgColorRes, null));
            if (isLightBar) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                toolbar.setTitleTextColor(activity.getResources().getColor(R.color.white));
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }

        toolbar.setTitle(activity.getString(titleRes));
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public static void hideKeyBoard(Activity activity) {
        if ((activity.getCurrentFocus() != null) && (activity.getCurrentFocus().getWindowToken() != null)) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
