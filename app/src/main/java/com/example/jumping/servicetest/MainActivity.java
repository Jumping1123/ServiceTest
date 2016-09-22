package com.example.jumping.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button startservice;
    private Button stopservice;
    private Button bindservice;
    private Button unbindservice;
    private Button startintentservice;
    private TextView textView;
    private MyService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("MyService", "service connected");
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startdownload();
            downloadBinder.getprogress();
            downloadBinder.changetext(new CallbackListener() {
                @Override
                public void onChanged(final String data) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(data);
                        }
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("MyService", "service disconnected");
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        startservice = (Button) findViewById(R.id.start_service);
        stopservice = (Button) findViewById(R.id.stop_service);
        startservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);
            }
        });
        stopservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, MyService.class);
                stopService(intent2);
            }
        });

        bindservice = (Button) findViewById(R.id.bind_service);
        bindservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                bindService(intent, connection, BIND_AUTO_CREATE);
            }
        });

        unbindservice = (Button) findViewById(R.id.unbind_service);
        unbindservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(connection);
            }
        });

        startintentservice = (Button) findViewById(R.id.start_intentservice);
        startintentservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Thread id is " + Thread.currentThread().getId());
                Intent intent = new Intent(MainActivity.this, MyIntentService.class);
                startService(intent);
            }
        });
    }
}
