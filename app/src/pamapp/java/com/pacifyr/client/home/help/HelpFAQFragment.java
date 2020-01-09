package com.pacifyr.client.home.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.pacifyr.client.R;
import com.pacifyr.client.adapter.CustomPagerAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.fragment.UserDashboardFragment;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.utility.BaseFragment;

import me.relex.circleindicator.CircleIndicator;

public class HelpFAQFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_welcome_tour);
    }

    private ViewPager viewpager;
    private CircleIndicator indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_help_welcome, null);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewpager = view.findViewById(R.id.viewpager);
        indicator= view.findViewById(R.id.indicator);

        viewpager.setAdapter(new CustomPagerAdapter(getContext()));
        indicator.setViewPager(viewpager);

    }

    @Override
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }

}
