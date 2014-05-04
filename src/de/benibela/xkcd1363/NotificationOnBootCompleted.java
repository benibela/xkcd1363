package de.benibela.xkcd1363;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class NotificationOnBootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        XKCDService.start(context);
    }
}