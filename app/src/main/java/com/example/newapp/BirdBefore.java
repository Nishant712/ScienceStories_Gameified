package com.example.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BirdBefore extends WearableActivity {

    private TextView mTextView;
    private String recording_details = "";
    public static String EXTRA_MESSAGE = "com.example.newapp.MESSAGE";
    public static int numberOfRecordings;
    private String lastLine;
    private String firstLine = "";
    private String secondLine = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_before);

        mTextView = (TextView) findViewById(R.id.text);

        Intent intent = getIntent();
        String temp = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String[] stringArr = temp.split(";");
        recording_details = stringArr[0];
        firstLine = stringArr[1];
        if(stringArr.length > 2) {
            secondLine = stringArr[2];
        }

        String[] prompts = firstLine.split(",");
        int last;

        //setContentView(R.layout.rect_activity_bird_before);

        // Enables Always-on
        setAmbientEnabled();
        numberOfRecordings = numRecordings();
        int birdLevel = numberOfRecordings/2;
        final ImageView birdImage = (ImageView) findViewById(R.id.bird);



        String[] arr;

        int[] numOccurrences = new int[3];


        lastLine = secondLine;


        if(lastLine != null && !lastLine.isEmpty()) {
            Log.d("lastLine is", lastLine);
            arr = lastLine.split(",");
            for(int i=0;i < arr.length;i++){
                Log.d("arr is", arr[i]);
                numOccurrences[i] = Integer.parseInt(arr[i]);
            }
        }
        last = Integer.parseInt(prompts[prompts.length-1]);
        numOccurrences[last-1] = numOccurrences[last-1] - 1;




        int observationLevel = numOccurrences[0]/2;
        int activityLevel = numOccurrences[1]/2;
        int experimentLevel = numOccurrences[2]/2;
        Log.d("birdLevel is", Integer.toString(birdLevel));
        Log.d("observationLevel is", Integer.toString(observationLevel));
        Log.d("activityLevel is", Integer.toString(activityLevel));
        Log.d("experimentLevel is", Integer.toString(experimentLevel));

        if(birdLevel == 0)
            birdImage.setBackground(getResources().getDrawable(R.drawable.egg));
        else if(birdLevel >= 5) {
            if(experimentLevel == 0)
                birdImage.setBackground(getResources().getDrawable(R.drawable.bird5));
            else if(experimentLevel >= 5)
                birdImage.setBackground(getResources().getDrawable(R.drawable.color5_5));
            else {
                switch (experimentLevel) {
                    case 1:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color1_5));
                        break;
                    case 2:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color2_5));
                        break;
                    case 3:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color3_5));
                        break;
                    case 4:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color4_5));
                        break;
                }
            }

        }
        else if(birdLevel == 1) {
            if(experimentLevel == 0)
                birdImage.setBackground(getResources().getDrawable(R.drawable.bird1));
            else if(experimentLevel >= 5)
                birdImage.setBackground(getResources().getDrawable(R.drawable.color5_1));
            else {
                switch (experimentLevel) {
                    case 1:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color1_1));
                        break;
                    case 2:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color2_1));
                        break;
                    case 3:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color3_1));
                        break;
                    case 4:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color4_1));
                        break;
                }
            }
        }
        else if(birdLevel == 2) {
            if(experimentLevel == 0)
                birdImage.setBackground(getResources().getDrawable(R.drawable.bird2));
            else if(experimentLevel >= 5)
                birdImage.setBackground(getResources().getDrawable(R.drawable.color5_2));
            else {
                switch (experimentLevel) {
                    case 1:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color1_2));
                        break;
                    case 2:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color2_2));
                        break;
                    case 3:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color3_2));
                        break;
                    case 4:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color4_2));
                        break;
                }
            }
        }
        else if(birdLevel == 3) {
            if(experimentLevel == 0)
                birdImage.setBackground(getResources().getDrawable(R.drawable.bird3));
            else if(experimentLevel >= 5)
                birdImage.setBackground(getResources().getDrawable(R.drawable.color5_3));
            else {
                switch (experimentLevel) {
                    case 1:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color1_3));
                        break;
                    case 2:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color2_3));
                        break;
                    case 3:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color3_3));
                        break;
                    case 4:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color4_3));
                        break;
                }
            }
        }
        else if(birdLevel == 4) {
            if(experimentLevel == 0)
                birdImage.setBackground(getResources().getDrawable(R.drawable.bird4));
            else if(experimentLevel >= 5)
                birdImage.setBackground(getResources().getDrawable(R.drawable.color5_4));
            else {
                switch (experimentLevel) {
                    case 1:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color1_4));
                        break;
                    case 2:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color2_4));
                        break;
                    case 3:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color3_4));
                        break;
                    case 4:
                        birdImage.setBackground(getResources().getDrawable(R.drawable.color4_4));
                        break;
                }
            }
        }


        final ImageView featherImage = (ImageView) findViewById(R.id.feather);
        final ImageView accImage = (ImageView) findViewById(R.id.accessory);

        if(observationLevel >= 5)
            featherImage.setBackground(getResources().getDrawable(R.drawable.feather5));
        switch(observationLevel) {
            case 1:
                featherImage.setBackground(getResources().getDrawable(R.drawable.feather1));
                break;
            case 2:
                featherImage.setBackground(getResources().getDrawable(R.drawable.feather2));
                break;
            case 3:
                featherImage.setBackground(getResources().getDrawable(R.drawable.feather3));
                break;
            case 4:
                featherImage.setBackground(getResources().getDrawable(R.drawable.feather4));
                break;
        }

        if(activityLevel >= 5)
            accImage.setBackground(getResources().getDrawable(R.drawable.acc5));
        switch(activityLevel) {
            case 1:
                accImage.setBackground(getResources().getDrawable(R.drawable.acc1));
                break;
            case 2:
                accImage.setBackground(getResources().getDrawable(R.drawable.acc2));
                break;
            case 3:
                accImage.setBackground(getResources().getDrawable(R.drawable.acc3));
                break;
            case 4:
                accImage.setBackground(getResources().getDrawable(R.drawable.acc4));
                break;
        }

    }

    public void startRecording(View view) throws IOException {
        Intent intent;
        String record;
        record = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
        recording_details = recording_details + "," + record;
        String message = recording_details+";"+firstLine+";"+secondLine;
        intent = new Intent(this, MessageRecord.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        //writeToOne(firstLine, temp);
        startActivity(intent);
        this.finish();
    }

    public static int numRecordings() {
        int numberOfRecordings = 0;
        File Memories = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "Memories");
        if(Memories.isDirectory()) {
            File[] foundFiles = Memories.listFiles(new FilenameFilter() {
                public boolean accept(File Memories, String name) {
                    return name.endsWith(".wav");
                }
            });
            numberOfRecordings = foundFiles.length;
            return numberOfRecordings;
        }
        return 0;
    }

    public static String getLastLine() throws IOException {

        String strLine = null, tmp;

        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "Memories" + File.separator + "pastSequence.txt");
        if(f.exists() && !f.isDirectory()) {
            try {
                FileInputStream in = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "Memories" + File.separator + "pastSequence.txt");
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    while ((tmp = br.readLine()) != null) {
                        strLine = tmp;
                    }
                } finally {
                    in.close();
                }

            } catch (IOException e) {
                // TODO: error handling
            }
        }
        return strLine;
    }

    public static String getFirstLine() throws IOException {

        String strLine = null, tmp;
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "Memories" + File.separator + "pastSequence.txt");
        if(f.exists() && !f.isDirectory()) {
            try {
                FileInputStream in = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "Memories" + File.separator + "pastSequence.txt");
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    while ((tmp = br.readLine()) != null) {
                        strLine = tmp;
                        break;
                    }
                } finally {
                    in.close();
                }

            } catch (IOException e) {
                // TODO: error handling
            }


        }
        return strLine;
    }

    public static int getLast() throws IOException {
        String numbers = getFirstLine();
        if(numbers == null)
            return 0;
        String[] arr = numbers.split(",");
        return Integer.parseInt(arr[arr.length-1]);
    }
}
