package com.pacifyr.omhealthcare.quickblox.chat.gcm;

import android.content.Intent;

import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.activity.SplachScreen;

import com.pacifyr.omhealthcare.quickblox.services.CallService;
import com.quickblox.sample.core.gcm.CoreGcmPushListenerService;
import com.quickblox.sample.core.utils.NotificationUtils;
import com.quickblox.sample.core.utils.ResourceUtils;

public class GcmChatPushListenerService extends CoreGcmPushListenerService {
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void showNotification(String message) {

        NotificationUtils.showNotification(this, SplachScreen.class,
                ResourceUtils.getString(R.string.notification_title), message,
                R.mipmap.ic_launcher, NOTIFICATION_ID);
        Intent intent = new Intent(this, CallService.class);
        startService(intent);



       /* NotificationUtils.showNotification(this, SplashActivity.class,
                ResourceUtils.getString(R.string.notification_title), message,
                R.mipmap.ic_launcher, NOTIFICATION_ID);*/
    }
}