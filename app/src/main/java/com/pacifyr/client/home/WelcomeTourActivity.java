package com.pacifyr.client.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.CustomPagerAdapter;

public class WelcomeTourActivity extends AppCompatActivity {

    MainTextView skipCtxv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_tour);

        skipCtxv = findViewById(R.id.skip_ctxv);
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getApplicationContext(),getSupportFragmentManager()));

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
