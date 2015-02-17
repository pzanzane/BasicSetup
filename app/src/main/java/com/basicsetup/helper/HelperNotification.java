package com.decos.fixi.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.decos.fixi.R;

/**
 * Created by pankaj on 18/11/14.
 */
public class HelperNotification {

    public static void showNotification(Context context, Intent intent, String title, String msg) {

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent == null ? new Intent() : intent, 0);

        Notification n = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setStyle(
                        new NotificationCompat.BigTextStyle()
                                .bigText(msg)
                )
                .setContentText(msg)
                .setSmallIcon(R.drawable.app_icon)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);

    }


    public static void registerToNotificationHub(Context context, String userId) {
        DebugHelper.d("WASTE", "In registerToNotificationHub");
        NotificationHubRegistration registerHub = new NotificationHubRegistration(context, userId);
        registerHub.registerNotificationHub();
    }


    public static void unRegisterToNotificationHubAsync(Context context, String userId,String registrationId){

        DebugHelper.d("WASTE","In unRegisterToNotificationHub");
        NotificationHubRegistration registerHub = new NotificationHubRegistration(context,userId);
        registerHub.unregisterUserAsync(registrationId);
    }

    public static void unRegisterToNotificationHub(Context context, String userId,String registrationId){

        DebugHelper.d("WASTE","In unRegisterToNotificationHub");
        NotificationHubRegistration registerHub = new NotificationHubRegistration(context,userId);
        registerHub.unregisterUser(registrationId);

    }
}
