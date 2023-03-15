package com.frostdev.malarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm")
                .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
                .setContentTitle("Alarm")
                .setContentText("Wake up!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Play ringtone
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        // Show notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}

