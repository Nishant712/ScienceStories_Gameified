package com.example.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PromptChange extends WearableActivity {

    private TextView mTextView;
    private String isSame = "";
    public static String EXTRA_MESSAGE = "com.example.newapp.MESSAGE";
    public String recording_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_change);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();
        Intent intent = getIntent();
        recording_details = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
    }

    public void samePrompt(View view) throws IOException {
        Intent intent;
        isSame = "YES";
        String confirmTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
        recording_details = recording_details + ",Same prompt chosen at " + confirmTime;
        try {
            //open file for writing
            File file = new File("/sdcard/Memories/recording_details.txt");
            FileOutputStream fileinput = new FileOutputStream(file, true);
            PrintStream printstream = new PrintStream(fileinput);
            printstream.print(recording_details+"\n");
            fileinput.close();


        } catch (java.io.IOException e) {
            //if caught

        }
        intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE, isSame);
        startActivity(intent);
        this.finish();
    }

    public void diffPrompt(View view) throws IOException {
        Intent intent;
        isSame = "NO";
        String confirmTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
        recording_details = recording_details + ",Prompt changed at " + confirmTime;
        try {
            //open file for writing
            File file = new File("/sdcard/Memories/recording_details.txt");
            FileOutputStream fileinput = new FileOutputStream(file, true);
            PrintStream printstream = new PrintStream(fileinput);
            printstream.print(recording_details+"\n");
            fileinput.close();


        } catch (java.io.IOException e) {
            //if caught

        }
        intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE, isSame);
        startActivity(intent);
        this.finish();
    }
}
