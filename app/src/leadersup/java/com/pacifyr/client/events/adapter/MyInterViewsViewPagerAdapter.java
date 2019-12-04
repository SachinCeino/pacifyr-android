package com.pacifyr.client.events.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pacifyr.client.events.fragment.InterViewListListFragment;
import com.pacifyr.client.events.fragment.OfferssListFragment;

public class MyInterViewsViewPagerAdapter extends FragmentPagerAdapter {

    private String[] _tabTitles = new String[]{"Interviews", "Offers"};

    public MyInterViewsViewPagerAdapter(FragmentManager _mFragmentManager) {
        super(_mFragmentManager);
    }

    /**
     * Override two method getCount() and getItem() method;
     *
     * @return
     */

    //Tell the viewPager there are how many page;
    @Override
    public int getCount() {
        return 2;
    }

    //return each page corresponding Fragment;
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new InterViewListListFragment();
        } else
            return new OfferssListFragment();
    }

    //If use TabLayout, you should Override the getPageTitle() method
    @Override
    public CharSequence getPageTitle(int position) {
        return _tabTitles[position];
    }

}