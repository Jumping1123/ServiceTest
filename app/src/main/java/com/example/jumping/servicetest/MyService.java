package com.example.jumping.servicetest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Jumping on 2016/8/18.
 */
public class MyService extends Service {
    private DownloadBinder mbinder = new DownloadBinder();
    private boolean isrunning;
    String string;
    private CallbackListener callback;

    class DownloadBinder extends Binder {
        public void startdownload() {
            Log.d("MyService", "startdownload executed");
        }

        public int getprogress() {
            Log.d("MyService", "getprogress executed");
            return 0;
        }

        public void changetext(CallbackListener callbackListener) {
            callback = callbackListener;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isrunning = true;
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("Notification comes");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("This is title");
        builder.setContentText("This is content");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(1, notification);

        Log.d("MyService", "oncreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "onstartcommand executed");
        new Thread() {
            @Override
            public void run() {
                super.run();
                int data = 0;
                while (isrunning) {
                    data ++;

                    string = data + "";

                    if (callback != null) {
                        callback.onChanged(string);
                    }

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.d("MySertvice", data + "");
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "ondestory executed");
        isrunning = false;
    }


}
