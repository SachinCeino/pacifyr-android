package com.pacifyr.client.quickblox;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;


import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.pacifyr.client.R;


import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.orginals.DynamicLink;
import com.pacifyr.client.orginals.RemoteConfig;
import com.pacifyr.client.orginals.UserProperties;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;

import com.pacifyr.client.quickblox.chat.models.SampleConfigs;
import com.pacifyr.client.quickblox.chat.utils.Consts;
import com.pacifyr.client.quickblox.chat.utils.configs.ConfigUtils;
import com.pacifyr.client.quickblox.services.Call_Service;
import com.pacifyr.client.quickblox.util.QBResRequestExecutor;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.utils.ActivityLifecycle;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.ACCOUNT_KEY;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.APP_ID;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.AUTH_KEY;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.AUTH_SECRET;

public class App extends CoreApp {
    private static String TAG = App.class.getSimpleName();
    private static App instance;
    private QBResRequestExecutor qbResRequestExecutor;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;


    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(getBaseContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        RemoteConfig.init();

        UtilityNetworkService.init(this);//before this ,Remote config int must be called
        initApplication();

            /*chat*/
        ActivityLifecycle.init(this);
//        initSampleConfigs();

        QBSettings.getInstance().init(this, APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        try {
            Intent intent = new Intent(this, Call_Service.class);
            startService(intent);
        } catch (Exception e) {
            Log.e(TAG, "Exception " + Call_Service.class.getSimpleName() + " cannot be started");
            e.printStackTrace();
        }


        Auth.scanUpdates(this);
        UserProperties.setProperty(this);
        RemoteConfig.init();

        /**/
    }


    private void initApplication() {
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
