package com.gwokhou.deadline.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

public class StartIntentUtils {

    public static boolean startIntentUrl(Activity activity, String intentFullUrl) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentFullUrl));
            activity.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
