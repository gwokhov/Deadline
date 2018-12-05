package com.gwokhou.deadline.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.gwokhou.deadline.R;
import com.gwokhou.deadline.util.DonateUtils;
import com.gwokhou.deadline.util.SnackbarUtils;
import com.gwokhou.deadline.util.StartIntentUtils;
import com.gwokhou.deadline.util.SystemUIUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class AboutFragment extends Fragment {

    private final static String EMAIL_ADDRESS = "mailto:gwokhou.dev@gmail.com";

    private final static String EMAIL_SUBJECT = "{Deadline} Feedback";

    private final static String PACKAGE_NAME = "com.gwokhou.deadline";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupIconAnim();
        setupSystemUI();
        setupIntroduction();
        setupLicenses();
        setupFeedback();
        setupSourceCode();
        setupRate();
        setupDonate();
    }

    private void setupIconAnim() {
        ImageView icon = getView().findViewById(R.id.about_icon);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.icon_anim);
        icon.startAnimation(animation);
    }

    private void setupSystemUI() {
        Toolbar toolbar = getView().findViewById(R.id.about_toolbar);
        SystemUIUtils.setupActionBar(getActivity(), true, R.color.white, R.color.teal_200, R.string.about, toolbar);
    }

    private void setupIntroduction() {
        getView().findViewById(R.id.about_introduction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_about_to_intro);
            }
        });
    }

    private void setupFeedback() {
        getView().findViewById(R.id.about_send_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(EMAIL_ADDRESS));
                intent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
                startActivity(intent);
            }
        });
    }

    private void setupSourceCode() {
        getView().findViewById(R.id.about_source_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/Gwokhov/Deadline"));
                startActivity(intent);
            }
        });
    }

    private void setupLicenses() {
        getView().findViewById(R.id.about_licenses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setView(R.layout.dialog_licenses)
                        .setTitle(R.string.open_source_licenses)
                        .show();
            }
        });
    }

    private void setupRate() {
        getView().findViewById(R.id.about_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSucceed = StartIntentUtils.startIntentUrl(getActivity(), "market://details?id=" + PACKAGE_NAME);
                if (!isSucceed) {
                    SnackbarUtils.showSnackbar(getView(), getResources().getString(R.string.failed_to_open_app_store));
                }
            }
        });
    }

    private void setupDonate() {
        getView().findViewById(R.id.about_donate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DonateUtils.isInstalledAlipayClient(getActivity())) {
                    DonateUtils.startAlipayClient(getActivity());
                } else {
                    SnackbarUtils.showSnackbar(getView(), getResources().getString(R.string.no_alipay_client));
                }
            }
        });

    }


}
