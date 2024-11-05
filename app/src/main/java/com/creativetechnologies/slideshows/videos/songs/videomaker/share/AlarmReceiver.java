package com.creativetechnologies.slideshows.videos.songs.videomaker.share;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.SplashMenuActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.AdModel;

import java.util.ArrayList;

/**
 * Created by Bansi on 20-02-2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static int MID = 0;
    private static ArrayList<AdModel> datalist = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        SharedPreferences preferences = context.getSharedPreferences("name_on_cake_shared_prefs", Context.MODE_PRIVATE);
        MID = preferences.getInt("noti_count", 0);

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mNotifyBuilder;
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, SplashMenuActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent;

        pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotifyBuilder = new NotificationCompat.Builder(context, context.getResources().getString(R.string.app_name) + " ChannelId")
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(context.getText(R.string.app_name))
                .setContentText("Click here to open " + context.getText(R.string.app_name).toString().toLowerCase()).setSound(alarmSound)
                .setWhen(when)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;

      /* Create or update. */
            NotificationChannel channel = new NotificationChannel(context.getResources().getString(R.string.app_name) + " ChannelId",
                    context.getResources().getString(R.string.app_name) + " Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(MID, mNotifyBuilder.build());
        MID++;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("noti_count", MID);
        editor.commit();
    }

    private boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}