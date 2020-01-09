package com.pacifyr.client.home.help;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.pacifyr.client.fragment.SupportFragment;
import com.pacifyr.client.fragment.WelcomeTourFragment;

public class HelpsViewPagerAdapter extends FragmentPagerAdapter {

    private String[] _tabTitles = new String[]{"FAQ", "CONTACT US"};

    public HelpsViewPagerAdapter(FragmentManager _mFragmentManager) {
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
            return new HelpFAQFragment();
        } else
            return new SupportFragment();
    }

    //If use TabLayout, you should Override the getPageTitle() method
    @Override
    public CharSequence getPageTitle(int position) {
        return _tabTitles[position];
    }

}