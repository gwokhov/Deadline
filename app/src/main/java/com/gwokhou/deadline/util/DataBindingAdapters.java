package com.gwokhou.deadline.util;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.navigation.Navigation;

public class DataBindingAdapters {

    @BindingAdapter("imageId")
    public static void setImageRes(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter("navAction")
    public static void setNavAction(final View view, final int actionId) {
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(view).navigate(actionId);
                }
            });
        }
    }

    @BindingAdapter("typeface")
    public static void setTypeface(TextView textView, String style) {
        switch (style) {
            case "bold":
                textView.setTypeface(null, Typeface.BOLD);
                break;
            case "italic":
                textView.setTypeface(null, Typeface.ITALIC);
                break;
            default:
                textView.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }

}
