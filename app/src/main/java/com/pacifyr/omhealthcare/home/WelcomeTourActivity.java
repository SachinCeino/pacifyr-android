package com.pacifyr.omhealthcare.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CustomPagerAdapter;

public class WelcomeTourActivity extends AppCompatActivity {

    CustomFontTextView skipCtxv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_tour);

        skipCtxv = findViewById(R.id.skip_ctxv);
        ViewPager viewPager = findViewById(R.id.viewpager);
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
