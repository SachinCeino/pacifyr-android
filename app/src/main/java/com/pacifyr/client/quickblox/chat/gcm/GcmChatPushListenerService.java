package com.pacifyr.client.quickblox.chat.gcm;

import com.quickblox.sample.core.gcm.CoreGcmPushListenerService;

public class GcmChatPushListenerService extends CoreGcmPushListenerService {
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void showNotification(String message) {

        /*NotificationUtils.showNotification(this, SplashScreen.class,
                ResourceUtils.getString(R.string.notification_title), message,
                R.mipmap.ic_launcher, NOTIFICATION_ID);*/
       /* Intent intent = new Intent(this, CallService.class);
        startService(intent);*/



       /* NotificationUtils.showNotification(this, SplashActivity.class,
                ResourceUtils.getString(R.string.notification_title), message,
                R.mipmap.ic_launcher, NOTIFICATION_ID);*/
    }
}