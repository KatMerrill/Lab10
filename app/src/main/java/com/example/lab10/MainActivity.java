package com.example.lab10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private DrawView drawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawView) findViewById(R.id.drawView);
//        drawView.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawView.pause(); // also pauses the other thread
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawView.resume();
    }
}