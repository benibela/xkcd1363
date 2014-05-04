package de.benibela.xkcd1363;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class XKCDService extends Service implements SensorEventListener {
    private SensorManager sensorManager;

    float light = -1;


    static float THRESHOLD_BRIGHT = 35;
    static float THRESHOLD_BRIGHT_INC_FACTOR = 10;
    static float THRESHOLD_FALL = 2;

    long lastScream = 0, lastHi = 0;

    @Override
    public void onSensorChanged(SensorEvent e) {
        if(e.sensor.getType() == Sensor.TYPE_LIGHT) {

            if (e.values[0] >= THRESHOLD_BRIGHT) {
                if (light != -1 && light < THRESHOLD_BRIGHT) {
                    if (System.currentTimeMillis() - lastHi < 2*1000) return;
                    lastHi = System.currentTimeMillis();
                    playSound(new int[]{R.raw.hi_fs104453, R.raw.hi_fs109838, R.raw.hi_fs161266, R.raw.hi_fs167468});
                }
            } else if (light != -1 && e.values[0] > light * THRESHOLD_BRIGHT_INC_FACTOR)
                playSound(new int[]{R.raw.hi_fs104453, R.raw.hi_fs109838, R.raw.hi_fs161266, R.raw.hi_fs167468});
            light = e.values[0];
        } else if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (/*Math.sqrt*/(e.values[0]*e.values[0]+e.values[1]*e.values[1]+e.values[2]*e.values[2]) <
                    THRESHOLD_FALL * THRESHOLD_FALL) {
                if (System.currentTimeMillis() - lastScream < 2*1000) return;
                lastScream = System.currentTimeMillis();
                playSound(new int[]{R.raw.scream_fs132106, R.raw.scream_fs64939, R.raw.scream_fs222545});
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        XKCDService getService() {
            return XKCDService.this;
        }
    }

    static XKCDService instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     /*   pendingXKCDService = false;
        //Log.i("LocalService", "Received start id " + startId + ": " + intent);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sp.getBoolean("notifications", true)) return START_NOT_STICKY;


        NetworkInfo network = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (network != null && network.isConnected()){
            VideLibriApp.updateAccount(null, true, false);
            showLater(this, 1000*60*60*24); //check every day
        } else
            showLater(this, 1000*60*60); //wait 1 h

        showNotification(this);

        return START_NOT_STICKY;// START_STICKY;    */

        readConfig(this);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        try {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {
            Log.d("xkcd1363", "light failed");
        }




        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        // Cancel the persistent notification.
        //mNM.cancel(NOTIFICATION);
        instance = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();


    static void start(Context context){
        if (instance != null) return;
        context.startService(new Intent(context, XKCDService.class));
    }

    static void readConfig(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        THRESHOLD_BRIGHT = sp.getFloat("light", THRESHOLD_BRIGHT);
        THRESHOLD_BRIGHT_INC_FACTOR = sp.getFloat("lightIncFactor", THRESHOLD_BRIGHT_INC_FACTOR);
        THRESHOLD_FALL = sp.getFloat("fall", THRESHOLD_FALL);
    }







    public void playSound(int id){
        MediaPlayer mp = MediaPlayer.create(this, id);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }

        });
        mp.start();
    }

    public void playSound(int ids[]){
        playSound (ids[(new Random()).nextInt(ids.length)]);
    }


}
       /*
    static final int SHOWNOW = 14355;
    static boolean pendingXKCDService = false;
    static void showLater(Context context, int delayMs){
        pendingXKCDService = true;
        PendingIntent intent = PendingIntent.getBroadcast(
                context, SHOWNOW,
                new Intent(context, NotificationShowNow.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC, System.currentTimeMillis() + delayMs, intent);
    }

    static void startIfNecessary(Context context){
        if (pendingXKCDService) return;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (!sp.getBoolean("notifications", true)) return;
        showLater(context, 1000 * 60 * sp.getInt("notificationsServiceDelay", 15)); //wait 15 min
    }
                 */
