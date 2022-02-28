package com.example.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeService extends Service {
    private final IBinder myBinder = new MyLocalBinder();

    public TimeService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return myBinder;
    }

    public String getTime(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.US);
        return (dateFormat.format(new Date()));

    }

    public class MyLocalBinder extends Binder {
        TimeService getService(){
            return TimeService.this;
        }
    }
}


