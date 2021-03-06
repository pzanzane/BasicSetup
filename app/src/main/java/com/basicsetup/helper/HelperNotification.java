package com.basicsetup.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.basicsetup.R;

import java.util.Random;

/**
 * Created by terrilthomas on 05/07/15.
 */
public class HelperNotification {

    public static void showNotification(Context context,String title,String message,PendingIntent pIntent) {

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification n = null;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.common_signin_btn_icon_dark)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.common_signin_btn_icon_dark))
                .setContentIntent(pIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);


        if (AndroidVersionUtil.isBeforeHoneyComb()) {
            builder.setContentText(message);

        } else {
            builder.setContentText(message);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        }

        if (AndroidVersionUtil.getAndroidVersion() < 16) {
            n = builder.getNotification();
        } else {
            n = builder.build();
        }
        notificationManager.notify(new Random().nextInt(8999),n);
    }


    public static void registerToNotificationHub(Context context, String userId) {
        Log.d("WASTE", "In registerToNotificationHub");
        //NotificationHubRegistration registerHub = new NotificationHubRegistration(context, userId);
       // registerHub.registerNotificationHub();
    }


    public static void unRegisterToNotificationHubAsync(Context context, String userId,String registrationId){

        Log.d("WASTE","In unRegisterToNotificationHub");
     //   NotificationHubRegistration registerHub = new NotificationHubRegistration(context,userId);
     //   registerHub.unregisterUserAsync(registrationId);
    }

    public static void unRegisterToNotificationHub(Context context, String userId,String registrationId){

        Log.d("WASTE","In unRegisterToNotificationHub");
     //   NotificationHubRegistration registerHub = new NotificationHubRegistration(context,userId);
     //   registerHub.unregisterUser(registrationId);

    }
}
