package com.example.geminiv4.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.geminiv4.BuildConfig;
import com.example.geminiv4.MainActivity;
import com.example.geminiv4.R;
import com.example.geminiv4.devicedata.IncoisMessage;
import com.example.geminiv4.multilingual.LanguageNavigationHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Notifier {
    Context context;
    DateFormat dateFormat;
    NotificationManager notificationManager;
    int NOTIFICATION_ID;

    public Notifier(Context context) {
        this.context = context;
        dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("ContextCheck",(context==null)+"");
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NOTIFICATION_ID = 3;
    }




    public void showNotification(String title, String contenttext, String subtext){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_3", "Example channel 3", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true); // set false to disable badges, Oreo exclusive
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build();
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder notification =  new NotificationCompat.Builder(context, "CHANNEL_3");
            notification.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(contenttext).setSubText(subtext).setNumber(3);
            notification.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
            assert notificationManager != null;
            notificationManager.notify(NOTIFICATION_ID, notification.build());
        }else{
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setSmallIcon(R.mipmap.ic_notification);
            notification.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon));
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
            notification.setContentIntent(intent);
            notification.setContentTitle(title);
            notification.setContentText(contenttext);
            notification.setSubText(subtext);
            notificationManager.notify(NOTIFICATION_ID, notification.build());
        }

    }


    public void showNotification(String title, String contenttext, IncoisMessage frame0data){
        LanguageNavigationHelper lh = new LanguageNavigationHelper(context);
        int lid = lh.getSelectedLanguageIndex();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_3", "Example channel 3", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true); // set false to disable badges, Oreo exclusive
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build();
            if(getSoundFileURI(frame0data.getTypeofservice(),lid)!=null){channel.setSound(getSoundFileURI(frame0data.getTypeofservice(),lid), audioAttributes);}
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder notification =  new NotificationCompat.Builder(context, "CHANNEL_3");
            notification.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(contenttext).setNumber(3);
            notification.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
            assert notificationManager != null;
            notificationManager.notify(NOTIFICATION_ID, notification.build());
        }else{
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setSmallIcon(R.mipmap.ic_notification);
            notification.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon));
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
            if(getSoundFileURI(frame0data.getTypeofservice(),lid)!=null){
                notification.setSound(getSoundFileURI(frame0data.getTypeofservice(),lid));
            }
            notification.setContentIntent(intent);
            notification.setContentTitle(title);
            notification.setContentText(contenttext);
            notificationManager.notify(NOTIFICATION_ID, notification.build());
        }

    }


    public void showTempNotification(String title, String contenttext, String tos){
        LanguageNavigationHelper lh = new LanguageNavigationHelper(context);
        int lid = lh.getSelectedLanguageIndex();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build();;
            switch (tos){
                case "OSF":
                    NotificationChannel osfchannel = new NotificationChannel("CHANNEL_OSF_"+lid, "OSF Channel_"+lid, NotificationManager.IMPORTANCE_DEFAULT);
                    osfchannel.setSound(getSoundFileURI(tos,lid), audioAttributes);
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(osfchannel);
                    NotificationCompat.Builder notification =  new NotificationCompat.Builder(context, "CHANNEL_OSF_"+lid);
                    notification.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(contenttext).setNumber(3);
                    notification.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                    assert notificationManager != null;
                    notificationManager.notify(NOTIFICATION_ID, notification.build());
                    break;
                case "PFZ":
                    NotificationChannel pfzchannel = new NotificationChannel("CHANNEL_PFZ_"+lid, "PFZ Channel_"+lid, NotificationManager.IMPORTANCE_DEFAULT);
                    pfzchannel.setSound(getSoundFileURI(tos,lid), audioAttributes);
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(pfzchannel);
                    NotificationCompat.Builder pfznotification =  new NotificationCompat.Builder(context, "CHANNEL_PFZ_"+lid);
                    pfznotification.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(contenttext).setNumber(3);
                    pfznotification.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                    assert notificationManager != null;
                    notificationManager.notify(NOTIFICATION_ID, pfznotification.build());

                    break;
                case "CYCLONE":
                    NotificationChannel cycchannel = new NotificationChannel("CHANNEL_CYC_"+lid, "CYC Channel"+lid, NotificationManager.IMPORTANCE_DEFAULT);
                    cycchannel.setSound(getSoundFileURI(tos,lid), audioAttributes);
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(cycchannel);
                    NotificationCompat.Builder cycnotification =  new NotificationCompat.Builder(context, "CHANNEL_CYC_"+lid);
                    cycnotification.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(contenttext).setNumber(3);
                    cycnotification.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                    assert notificationManager != null;
                    notificationManager.notify(NOTIFICATION_ID, cycnotification.build());

                    break;
                case "TSUNAMI":
                    NotificationChannel tsuchannel = new NotificationChannel("CHANNEL_TSU_"+lid, "TSU Channel_"+lid, NotificationManager.IMPORTANCE_DEFAULT);
                    tsuchannel.setSound(getSoundFileURI(tos,lid), audioAttributes);
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(tsuchannel);
                    NotificationCompat.Builder tsunotification =  new NotificationCompat.Builder(context, "CHANNEL_TSU_"+lid);
                    tsunotification.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(contenttext).setNumber(3);
                    tsunotification.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                    assert notificationManager != null;
                    notificationManager.notify(NOTIFICATION_ID, tsunotification.build());

                    break;

                case "STRONGWINDALERT":
                    NotificationChannel swachannel = new NotificationChannel("CHANNEL_SWA_"+lid, "SWA Channel_"+lid, NotificationManager.IMPORTANCE_DEFAULT);
                    swachannel.setSound(getSoundFileURI(tos,lid), audioAttributes);
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(swachannel);
                    NotificationCompat.Builder swanotification =  new NotificationCompat.Builder(context, "CHANNEL_SWA_"+lid);
                    swanotification.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(contenttext).setNumber(3);
                    swanotification.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                    assert notificationManager != null;
                    notificationManager.notify(NOTIFICATION_ID, swanotification.build());

                    break;
            }



        }else{
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setSmallIcon(R.mipmap.ic_notification);
            notification.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon));
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
            if(getSoundFileURI(tos,lid)!=null){
                notification.setSound(getSoundFileURI(tos,lid));
            }
            notification.setContentIntent(intent);
            notification.setContentTitle(title);
            notification.setContentText(contenttext);
            notificationManager.notify(NOTIFICATION_ID, notification.build());
        }

    }


    public Uri getSoundFileURI(String typeofservice, int lid){
        Uri NOTIFICATION_SOUND_URI = null;
        switch (typeofservice){
            case "OSF":
                switch (lid) {
                    case 0:
//                       isocode = ("en");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_osf);
                        break;
                    case 1:
//                       isocode = ("hi");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.hi_osf);
                        break;
                    case 2:
//                      isocode = ("gu");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_osf);
                        break;
                    case 3:
//                        isocode = ("mr");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_osf);
                        break;
                    case 4:
//                        isocode = ("ka");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_osf);
                        break;
                    case 5:
//                        isocode = ("ml");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.ml_osf);
                        break;
                    case 6:
//                        isocode = ("ta");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.ta_hwa);
                        break;
                    case 7:
//                        isocode = ("te");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.te_osf);
                        break;
                    case 8:
//                        isocode = ("or");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_osf);
                        break;
                    case 9:
//                        isocode = ("bn");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_osf);
                        break;
                }
                break;
            case "PFZ":
                switch (lid) {
                    case 0:
//                       isocode = ("en");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_pfz);
                        break;
                    case 1:
//                       isocode = ("hi");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.hi_pfz);
                        break;
                    case 2:
//                      isocode = ("gu");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_pfz);
                        break;
                    case 3:
//                        isocode = ("mr");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_pfz);
                        break;
                    case 4:
//                        isocode = ("ka");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_pfz);
                        break;
                    case 5:
//                        isocode = ("ml");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.ml_pfz);
                        break;
                    case 6:
//                        isocode = ("ta");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.ta_pfz);
                        break;
                    case 7:
//                        isocode = ("te");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.te_pfz);
                        break;
                    case 8:
//                        isocode = ("or");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_pfz);
                        break;
                    case 9:
//                        isocode = ("bn");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_pfz);
                        break;
                }
                break;
            case "CYCLONE":
                switch (lid) {
                    case 0:
//                       isocode = ("en");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_cyclone);
                        break;
                    case 1:
//                       isocode = ("hi");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.hi_cyclone);
                        break;
                    case 2:
//                      isocode = ("gu");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_cyclone);
                        break;
                    case 3:
//                        isocode = ("mr");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_cyclone);
                        break;
                    case 4:
//                        isocode = ("ka");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_cyclone);
                        break;
                    case 5:
//                        isocode = ("ml");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.ml_cyclone);
                        break;
                    case 6:
//                        isocode = ("ta");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.ta_cyclone);
                        break;
                    case 7:
//                        isocode = ("te");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.te_cyclone);
                        break;
                    case 8:
//                        isocode = ("or");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_cyclone);
                        break;
                    case 9:
//                        isocode = ("bn");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_cyclone);
                        break;
                }
                break;
            case "TSUNAMI":
                switch (lid) {
                    case 0:
//                       isocode = ("en");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_tsunami);
                        break;
                    case 1:
//                       isocode = ("hi");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.hi_tsunami);
                        break;
                    case 2:
//                      isocode = ("gu");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_tsunami);
                        break;
                    case 3:
//                        isocode = ("mr");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_tsunami);
                        break;
                    case 4:
//                        isocode = ("ka");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_tsunami);
                        break;
                    case 5:
//                        isocode = ("ml");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.ml_tsunami);
                        break;
                    case 6:
//                        isocode = ("ta");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.ta_tsunami);
                        break;
                    case 7:
//                        isocode = ("te");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.te_tsunami);
                        break;
                    case 8:
//                        isocode = ("or");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_tsunami);
                        break;
                    case 9:
//                        isocode = ("bn");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_tsunami);
                        break;
                }
                break;
            case "STRONGWINDALERT":
                switch (lid) {
                    case 0:
//                       isocode = ("en");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_osf);
                        break;
                    case 1:
//                       isocode = ("hi");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.hi_osf);
                        break;
                    case 2:
//                      isocode = ("gu");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_osf);
                        break;
                    case 3:
//                        isocode = ("mr");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_osf);
                        break;
                    case 4:
//                        isocode = ("ka");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_osf);
                        break;
                    case 5:
//                        isocode = ("ml");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.ml_osf);
                        break;
                    case 6:
//                        isocode = ("ta");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.ta_hwa);
                        break;
                    case 7:
//                        isocode = ("te");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.te_osf);
                        break;
                    case 8:
//                        isocode = ("or");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_osf);
                        break;
                    case 9:
//                        isocode = ("bn");
                        NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.en_osf);
                        break;
                }
                break;
        }
        return NOTIFICATION_SOUND_URI;
    }


}
