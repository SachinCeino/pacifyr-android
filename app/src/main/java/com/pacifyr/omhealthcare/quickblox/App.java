package com.pacifyr.omhealthcare.quickblox;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.pacifyr.omhealthcare.quickblox.chat.models.SampleConfigs;
import com.pacifyr.omhealthcare.quickblox.chat.utils.Consts;
import com.pacifyr.omhealthcare.quickblox.chat.utils.configs.ConfigUtils;
import com.pacifyr.omhealthcare.quickblox.services.CallService;
import com.pacifyr.omhealthcare.quickblox.util.QBResRequestExecutor;
import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.utils.ActivityLifecycle;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class App extends CoreApp {
    private static App instance;
    private QBResRequestExecutor qbResRequestExecutor;


    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(getBaseContext());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        initApplication();
            /*chat*/
        ActivityLifecycle.init(this);
        initSampleConfigs();
        try {
            Intent intent = new Intent(this, CallService.class);
            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**/
    }

    private void initApplication(){
        instance = this;
    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }
    private static Picasso sPicasso;
    public static Picasso getPicasso() {
        if (sPicasso == null) {
            // Picasso with custom RequestHandler for loading from Layer MessageParts.
            sPicasso = new Picasso.Builder(instance)
                   // .addRequestHandler(new MessagePartRequestHandler(getLayerClient()))
                    .build();
        }
        return sPicasso;
    }


    public Activity g_currentActivity = null;
    //Bryan changes - SET/GET current top level activity
    public void setCurrentActivity(Activity activity) {
        g_currentActivity = activity;
    }
    public Activity getCurrentActivity() {
        return g_currentActivity;
    }






    /*chat*/
    private static SampleConfigs sampleConfigs;
    private void initSampleConfigs() {
        try {
            sampleConfigs = ConfigUtils.getSampleConfigs(Consts.SAMPLE_CONFIG_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SampleConfigs getSampleConfigs() {
        return sampleConfigs;
    }

    /**/
}
