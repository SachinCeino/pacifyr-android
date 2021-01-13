package com.pacifyr.client.application;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

/**
 * Created by ceino on 21/6/17.
 */

public class PacifyrApplication extends MultiDexApplication {
    private static PacifyrApplication instance;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        instance=this;
        super.onCreate();
        MultiDex.install(this);

    }
    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }
}
