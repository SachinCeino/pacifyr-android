package com.pacifyr.client.netconnectionlistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.pacifyr.client.event.InternetConnectionEvent;

import org.greenrobot.eventbus.EventBus;

public class ConnectionCheckBroadcast extends BroadcastReceiver {

    Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context =context;
            sendNotification(CheckInternetConnection(context));
    }

    public static boolean CheckInternetConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void sendNotification(boolean isConnected) {
        EventBus.getDefault().post(new InternetConnectionEvent(isConnected));
      /*  NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "default")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());*/
    }
}
