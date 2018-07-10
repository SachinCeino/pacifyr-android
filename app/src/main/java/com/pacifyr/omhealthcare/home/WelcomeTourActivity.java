package com.pacifyr.omhealthcare.home;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CustomPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class WelcomeTourActivity extends AppCompatActivity {

    CustomFontTextView skipCtxv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_tour);

        skipCtxv=(CustomFontTextView) findViewById(R.id.skip_ctxv);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));

        skipCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(WelcomeTourActivity.this,DashboardActivity.class);
                startActivity(i);
            }
        });

    }
}
