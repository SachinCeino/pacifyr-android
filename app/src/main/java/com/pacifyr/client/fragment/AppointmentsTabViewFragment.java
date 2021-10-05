package com.pacifyr.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pacifyr.client.R;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.utility.BaseFragment;
import com.squareup.otto.Subscribe;


public class AppointmentsTabViewFragment extends BaseFragment {
public AppointmentsTabViewFragment() {

        }

@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        }

@Subscribe
public void onNotifyEvent(NotifyEvent event) {

        }

@Override
public void onSetValues(View view) throws Exception {

        }

private TabLayout mTabLayout;
private ViewPager mViewPager;

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View root = inflater.inflate(R.layout.fragment_appointments_tab_view, container, false);

    mTabLayout = root.findViewById(R.id.mTabLayout);
    mViewPager = root.findViewById(R.id.mViewPager);

    mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.upcoming)));
    mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.past)));
    mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.rejected_cancelled)));
    mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    //Creating our pager adapter
    Pager adapter = new Pager(getChildFragmentManager(), mTabLayout.getTabCount());

    //Adding adapter to pager
    mViewPager.setAdapter(adapter);
    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        @Override
public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
        }

@Override
public void onTabUnselected(TabLayout.Tab tab) {

        }

@Override
public void onTabReselected(TabLayout.Tab tab) {

        }
        });
        return root;
        }


@Override
public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        }

@Override
public void onAttach(Context context) {
        super.onAttach(context);

        }

@Override
public void onDetach() {
        super.onDetach();

        }

public Boolean isValid(Object object) {
        return object != null;

        }



@Override
public void onStart() {
        super.onStart();

        }

@Override
public void onStop() {
        super.onStop();

        }




public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                AppointmentsFragment tab1 = new AppointmentsFragment();
                return tab1;
            case 1:
                AppointmentsPastFragment tab2 = new AppointmentsPastFragment();
                return tab2;
            case 2:
                AppointmentsRejOrCancelledFragment tab3 = new AppointmentsRejOrCancelledFragment();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
}

