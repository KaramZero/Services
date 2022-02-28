package com.example.services;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageView imageView = findViewById(R.id.imageView);

        Bitmap bitmap = null;

        try {
            openFileOutput("Data", MODE_APPEND).close();

            FileInputStream input = openFileInput("Data");
            bitmap = BitmapFactory.decodeStream(input);

            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        imageView.setImageBitmap(bitmap);


    }
}