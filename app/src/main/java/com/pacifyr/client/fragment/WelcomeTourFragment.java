package com.pacifyr.client.fragment;

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
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.utility.BaseFragment;

public class WelcomeTourFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_welcome_tour);
    }

    private ViewPager viewpager;
    private MainTextView skipCtxv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_welcome_tour, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.nav_welcome_title));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewpager = view.findViewById(R.id.viewpager);
        skipCtxv = view.findViewById(R.id.skip_ctxv);

        //skipCtxv=(MainTextView) findViewById(R.id.skip_ctxv);
        //ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        skipCtxv.setVisibility(View.INVISIBLE);
        viewpager.setAdapter(new CustomPagerAdapter(getContext()));

        skipCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*finish();
                Intent i=new Intent(getActivity(),DashboardActivity.class);
                startActivity(i);*/

               /* if(isCustomer()){*/
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    UserDashboardFragment fragment = new UserDashboardFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content_fragment, fragment).commit();
                // postEventOnMainThread(new ActionbarEvent("MY DASHBOARD"));
                /*}
                else {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    PacifyrDashboardFragment fragment = new PacifyrDashboardFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pacifyr_content_fragment, fragment).commit();
                }*/
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }

}
