package com.example.android.jobscheduler.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.jobscheduler.BuildConfig;
import com.example.android.jobscheduler.service.MyJobService;


/**
 * Created by simon on 2018/1/10.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();



    public static void createAlarm(Context context) {
        Log.d(TAG,"createAlarm");
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if(alarmMgr == null){
            Log.e(TAG,"create alarm failed");
            return;
        }
        int i;
        for(i = 0;i < 10;i++) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("id",i);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            (i+1)*5*60*1000, alarmIntent);
        }
//        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() +
//                        SYNC_FREQUENCY * 1000,alarmIntent);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id",-1);
        Log.d(TAG, String.format("onReceive(%d)",id ));
        new Thread(new Runnable() {
            public void run() {
                try {
                    MyJobService.performHttpRequest();;
                }catch (Exception e){
                    Log.d(TAG,"get error:",e);
                }
            }
        }).start();
    }




}
