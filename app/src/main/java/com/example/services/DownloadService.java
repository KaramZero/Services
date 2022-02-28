package com.example.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class DownloadService extends Service {

    Handler handler;
    String url;
    Bitmap bitmap;

    private static final String NOTIFICATION_Service_CHANNEL_ID = "service_channel";

    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TAG", "onCreate:  Service");

        startForeground(1, this.getNotification());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TAG", "onStartCommand: ");

        // TODO: Return the communication channel to the service.

        url = intent.getStringExtra("url");

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Toast.makeText(DownloadService.this, "Downloaded", Toast.LENGTH_SHORT).show();

                Intent intent1 =new Intent();
                intent1.setAction("DataIsHere");
                intent1.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent1);
            }
        };

        new Thread(){
            @Override
            public void run() {
                bitmap= download(url);
                handler.sendEmptyMessage(0);
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.i("TAG", "onBind: ");

        return null;
    }


    public void saveData(byte[] byteArray) {
        try(FileOutputStream oFile = openFileOutput("Data", MODE_APPEND)) {
            oFile.write(byteArray);
            oFile.close();

            Log.i("TAG","Data Saved");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private Notification getNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Name";
            String description = "Desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_Service_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent1 = new Intent(this, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, NOTIFICATION_Service_CHANNEL_ID).setContentTitle("MyNotification")
                .setContentText("DownloadingImage")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Notification notify = notification.build();
        return notify;
    }


    Bitmap download(String url) {

        Bitmap res = null;
        URL urlobj;
        HttpsURLConnection connection;
        InputStream stream;

        try {
            urlobj = new URL(url);
            connection = (HttpsURLConnection) urlobj.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = connection.getInputStream();
                res = BitmapFactory.decodeStream(stream);

                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                res.compress(Bitmap.CompressFormat.JPEG,80,outstream);
                byte[] byteArray = outstream.toByteArray();
                saveData(byteArray);

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (res != null)
            Log.i("TAG", "doInBackground:  downloaded");
        return res;
    }



}