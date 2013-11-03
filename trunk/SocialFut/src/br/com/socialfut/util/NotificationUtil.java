package br.com.socialfut.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import br.com.socialfut.R;

public class NotificationUtil
{
    static int ID = R.drawable.icon;

    /**
     * Issues a notification to inform the user that server has sent a message.
     * 
     * @param notificationIntent
     */
    @SuppressLint("NewApi")
    public static void generateNotification(Context context, String message, Intent notificationIntent)
    {

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification.Builder builder = new Notification.Builder(context).setContentTitle("Fulado de Tal")
                .setContentText(message).setContentIntent(intent).setSmallIcon(R.drawable.icon);

        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

    @SuppressLint("NewApi")
    public static void generateNotification(Context context, String message, Intent notificationIntent, String from,
            Bitmap bitmap)
    {

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder builder = new Notification.Builder(context).setContentTitle(from).setContentText(message)
                .setContentIntent(intent).setLargeIcon(bitmap).setSmallIcon(R.drawable.nofiticationball)
                .setSound(alarmSound).setVibrate(new long[] { 100, 250, 100, 500 })
                .setStyle(new Notification.BigTextStyle().bigText(message));

        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }
}