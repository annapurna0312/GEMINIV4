package com.example.geminiv4.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.geminiv4.MainActivity;
import com.example.geminiv4.R;

public class Notification{
    private static final int NOTIFICATION_ID = 0;
    private static String TAG = "MediaPlayerSmaple";

    public void showNotification(Context context, String title, String text) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_5", "Example channel 5", NotificationManager.IMPORTANCE_MIN);
            channel.setShowBadge(true); // set false to disable badges, Oreo exclusive
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder notification =  new NotificationCompat.Builder(context, "CHANNEL_5");
            notification.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(text).setNumber(4);
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build();
            channel.setSound(null, audioAttributes);
            notification.setDefaults(android.app.Notification.DEFAULT_VIBRATE);
            assert notificationManager != null;
            notificationManager.notify(NOTIFICATION_ID, notification.build());
        }else{
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setSmallIcon(R.drawable.ic_satellite_icon);
            notification.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_satellite_icon));
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent notification_intent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
            notification.setContentIntent(notification_intent);
            notification.setContentTitle(title);
            notification.setContentText(text);
            notificationManager.notify(NOTIFICATION_ID, notification.build());
        }
    }

}
