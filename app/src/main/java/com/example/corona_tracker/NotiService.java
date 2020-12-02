package com.example.corona_tracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class NotiService extends Service {
    Intent mainIntent;
    PendingIntent pendingIntent;

    public NotiService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "main");
//        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);

        mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 945, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Title")
                .setContentText("Content")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentIntent(pendingIntent)
                .setNumber(0);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nCh = new NotificationChannel("main", "메인 채널", NotificationManager.IMPORTANCE_LOW);
            nCh.setShowBadge(false);
            notificationManager.createNotificationChannel(nCh);
        }

        startForeground(678, builder.build());

        return super.onStartCommand(intent, flags, startId);
    }
}
