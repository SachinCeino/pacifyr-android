package com.quickblox.sample.core;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.ServiceZone;
import com.quickblox.sample.core.models.QbConfigs;
import com.quickblox.sample.core.utils.configs.CoreConfigUtils;

public class CoreApp extends MultiDexApplication {
    //public static final String TAG = CoreApp.class.getSimpleName();
    public static final String TAG = "jp";
    private static CoreApp instance;
    private static final String QB_CONFIG_DEFAULT_FILE_NAME = "qb_config.json";
    private QbConfigs qbConfigs;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        instance = this;
        initQbConfigs();
        initCredentials();
    }

    private void initQbConfigs() {
        Log.e(TAG, "CoreApp QB CONFIG FILE NAME: " + getQbConfigFileName());
        qbConfigs = CoreConfigUtils.getCoreConfigsOrNull(getQbConfigFileName());
    }

    public static synchronized CoreApp getInstance() {
        return instance;
    }

    public void initCredentials(){
        if (qbConfigs != null) {
            QBSettings.getInstance().init(getApplicationContext(), qbConfigs.getAppId(), qbConfigs.getAuthKey(), qbConfigs.getAuthSecret());
            QBSettings.getInstance().setAccountKey(qbConfigs.getAccountKey());
            Log.d(TAG, "CoreApp QB initCredentials  qbConfigs.getAppId() " + qbConfigs.getAppId());
            if (!TextUtils.isEmpty(qbConfigs.getApiDomain()) && !TextUtils.isEmpty(qbConfigs.getChatDomain())) {
                QBSettings.getInstance().setEndpoints(qbConfigs.getApiDomain(), qbConfigs.getChatDomain(), ServiceZone.PRODUCTION);
                QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
            }
        }
    }

    public QbConfigs getQbConfigs(){
        return qbConfigs;
    }

    protected String getQbConfigFileName(){
        return QB_CONFIG_DEFAULT_FILE_NAME;
    }
}