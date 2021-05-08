package com.pacifyr.client.quickblox;

import android.app.Activity;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.orginals.RemoteConfig;
import com.pacifyr.client.orginals.UserProperties;
import com.pacifyr.client.quickblox.chat.models.SampleConfigs;
import com.pacifyr.client.quickblox.chat.utils.Consts;
import com.pacifyr.client.quickblox.chat.utils.configs.ConfigUtils;
import com.pacifyr.client.quickblox.util.QBResRequestExecutor;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.utils.ActivityLifecycle;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class App extends CoreApp {
    private static final String TAG = App.class.getSimpleName();
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


        UtilityNetworkService uns = UtilityNetworkService.getInstance();//before this ,Remote config int must be called
        uns.setRemoteConfigHelper(RemoteConfig.init());
        UtilityNetworkService.init(this);
        initApplication();

        /*chat*/
        ActivityLifecycle.init(this);
//        initSampleConfigs();
        Auth.scanUpdates(this);
        UserProperties.setProperty(this);
        RemoteConfig.init();
        UtilityNetworkService.initFallback(this);







        /**/
    }

    /*private void initRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(RemoteConfig.REMOTE_FETCH_INTERVAL_SECONDS)
                .setFetchTimeoutInSeconds(RemoteConfig.REMOTE_FETCH_TIMEOUT_SECONDS)
                .build();
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }*/

    /*public FirebaseRemoteConfig getRemoteConfig() {
        if (mFirebaseRemoteConfig == null) {
            initRemoteConfig();
        }
        return mFirebaseRemoteConfig;
    }*/

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
