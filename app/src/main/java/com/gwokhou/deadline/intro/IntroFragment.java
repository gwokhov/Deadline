package com.gwokhou.deadline.intro;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.gwokhou.deadline.AppPreferences;
import com.gwokhou.deadline.R;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

public class IntroFragment extends Fragment {

    private ViewPager mViewPager;

    private MaterialButton mIntroStartBtn;

    private InkPageIndicator mPageIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_introduction, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setupSystemUI();

        setupViewPager();

        setupPagerController();

    }

    private void setupSystemUI() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.teal_200));
        } else {
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.white, null));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void setupIconAnim(View view) {
        ImageView icon = view.findViewById(R.id.intro_icon);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.icon_anim);
        icon.startAnimation(animation);
    }

    private void setupViewPager() {
        mViewPager = getView().findViewById(R.id.intro_view_pager);

        List<View> introViewList = new ArrayList<>(0);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View welcomeView = inflater.inflate(R.layout.introduction_welcome, null);
        setupIconAnim(welcomeView);

        final View feature1View = inflater.inflate(R.layout.introduction_feature_1, null);
        introViewList.add(welcomeView);
        introViewList.add(feature1View);

        final IntroPagerAdapter introPagerAdapter = new IntroPagerAdapter(introViewList);
        mViewPager.setAdapter(introPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == introPagerAdapter.getCount() - 1) {
                    mIntroStartBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupPagerController() {
        mIntroStartBtn = getView().findViewById(R.id.intro_start);

        mIntroStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigateUp();
                AppPreferences.setIsFirstBoot(getContext(), false);
            }
        });
        mPageIndicator = getView().findViewById(R.id.page_indicator);
        mPageIndicator.setViewPager(mViewPager);
    }

}
