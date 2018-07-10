package com.pacifyr.omhealthcare.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pacifyr.omhealthcare.R;


public class MainActivity extends AppCompatActivity {

    TextView text_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pacifyer);
        //text_tv=(TextView) findViewById(R.id.text_tv);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String str = prefs.getString("token", "");

    }
}
