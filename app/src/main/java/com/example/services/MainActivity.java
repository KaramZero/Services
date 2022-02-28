package com.example.services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    TimeService myService;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TimeService.MyLocalBinder binder =(TimeService.MyLocalBinder) iBinder;
            myService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this,TimeService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);


    }

    public void showtime(View view){

        TextView textView =findViewById(R.id.textView);
        textView.setText(myService.getTime());


    }



        public void download(View view){

        IntentFilter filter =new IntentFilter("DataIsHere");
        MyReceiver myReceiver = new MyReceiver();
        registerReceiver(myReceiver,filter);

        Intent intent = new Intent(this,DownloadService.class);
        intent.putExtra("url","https://miro.medium.com/max/6018/1*3rewUBdM1VKZrBGd7UDoFA.png");
        startForegroundService(intent);

    }
}