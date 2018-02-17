package com.example.newapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.io.*;

public class MainActivity extends FragmentActivity {

    private TextView mTextView;
    SharedPreferences prefs = null;
    public static Context mContext;
    private String appEntryTime;
    public static String EXTRA_MESSAGE = "com.example.newapp.MESSAGE";
    //public int num = 0;
    private static final int REQUEST_PERMISSIONS = 1;
    private String firstLine, lastLine, temp = "";
    private static String[] PERMISSIONS_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.VIBRATE
    };
    private String isSame = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
        verifyStoragePermissions(this);

        Intent intent = getIntent();
        isSame = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if(isSame == null) {
            isSame = "";
        }

        setContentView(R.layout.rect_activity_main);




        /*num = numRecordings();
        Log.d("Number of Recordings",String.format("number of recordings = %d", num));*/
        appEntryTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
        /*long notificationDelay = (1000 * 60 * 60 * 5)/100; //5 hours, starts in milliseconds
        for(int i = 0; i <= 0;i++) {


                final int notificationNumber = i;
                setAlarm(notificationNumber, notificationDelay);


        }*/

    }

    public void showPrompt(View view) {
        Intent intent;
        String message;
        if(isSame.equals("")) {
            message = appEntryTime;
        }
        else {
            message = appEntryTime + "," + isSame;
        }
        intent = new Intent(this, Prompt.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        //writeToOne(firstLine, temp);
        startActivity(intent);
        this.finish();
    }

    public void setAlarm(final int notificationNumber, long notificationDelay) {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent _ ) {
                MessagePromptNotification mpn = new MessagePromptNotification(mContext);
                mpn.createNotification(notificationNumber);
                context.unregisterReceiver( this ); // this == BroadcastReceiver, not Activity
            }
        };

        this.registerReceiver( receiver, new IntentFilter("com.blah.blah.somemessage") );

        PendingIntent pintent = PendingIntent.getBroadcast( this, 0, new Intent("com.blah.blah.somemessage"), 0 );
        AlarmManager manager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));

        // set alarm to fire 5 sec (1000*5) from now (SystemClock.elapsedRealtime())
        manager.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + notificationDelay, pintent );
    }



    public boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRecord = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED || permissionRecord != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_LIST,
                    REQUEST_PERMISSIONS
            );
            SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("systemPermissionsGiven", false);
            edit.apply();
            return false;
        } else {

            SharedPreferences settings = getSharedPreferences("prefName", MODE_PRIVATE);
            //Boolean needToDeleteAudio = settings.getBoolean("audioFilesDeleted", false);

            SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("systemPermissionsGiven", true);
            edit.apply();
            return true;

            /*if(!needToDeleteAudio){
                deleteOldAudioFiles();
                edit.putBoolean("audioFilesDeleted", true);
                edit.apply();
            }*/
        }
    }

}
