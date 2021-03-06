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
    private static int alarmId = 0;


    public static void createAlarm(Context context) {
        Log.d(TAG,String.format("createAlarm(%d)",alarmId));
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if(alarmMgr == null){
            Log.e(TAG,"create alarm failed");
            return;
        }
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("id",alarmId);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        long timeout = SystemClock.elapsedRealtime() + 5*60*1000;
        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                timeout, alarmIntent);
//        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                timeout,alarmIntent);
//        long timeout = System.currentTimeMillis() + 3*60*1000;
//        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(timeout, alarmIntent);
//        alarmMgr.setAlarmClock(alarmClockInfo,alarmIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id",-1);
        Log.d(TAG, String.format("onReceiveAlarm(%d)",id ));
        alarmId++;
        createAlarm(context);
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
