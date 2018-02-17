package com.example.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

public class RecordingConfirmation extends WearableActivity {

    private TextView mTextView;
    public static String EXTRA_MESSAGE = "com.example.newapp.MESSAGE";
    public static String recording_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_confirmation);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();
        Intent intent = getIntent();
        recording_details = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
    }

    public void updatedBird(View v) {
        Intent intent = new Intent(this, BirdAfter.class);
        String message = recording_details;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        finish();
    }
}
