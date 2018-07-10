package com.pacifyr.omhealthcare.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CustomPagerAdapter;
import com.pacifyr.omhealthcare.event.ActionbarEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.home.DashboardActivity;
import com.pacifyr.omhealthcare.utility.BaseFragment;

public class WelcomeTourFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_welcome_tour);
    }

    private ViewPager viewpager;
    private CustomFontTextView skipCtxv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_welcome_tour, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        skipCtxv = (CustomFontTextView) view.findViewById(R.id.skip_ctxv);

        //skipCtxv=(CustomFontTextView) findViewById(R.id.skip_ctxv);
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
                postEventOnMainThread(new ActionbarEvent("My Dashboard"));
                /*}
                else {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    PacifyrDashboardFragment fragment = new PacifyrDashboardFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pacifyr_content_fragment, fragment).commit();
                }*/
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
