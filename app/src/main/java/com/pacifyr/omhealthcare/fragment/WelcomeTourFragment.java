package com.pacifyr.omhealthcare.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CustomPagerAdapter;
import com.pacifyr.omhealthcare.event.ActionbarEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
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

        viewpager = view.findViewById(R.id.viewpager);
        skipCtxv = view.findViewById(R.id.skip_ctxv);

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
                postEventOnMainThread(new ActionbarEvent("MY DASHBOARD"));
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
