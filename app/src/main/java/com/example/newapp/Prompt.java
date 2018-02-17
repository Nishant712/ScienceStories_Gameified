package com.example.newapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Prompt extends WearableActivity {

    private TextView mTextView;
    public static Context mContext;
    private String appEntryTime;
    public static String EXTRA_MESSAGE = "com.example.newapp.MESSAGE";
    private String isSame = "";
    private String firstLine, lastLine, temp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        mTextView = (TextView) findViewById(R.id.text);

        String[] arr_msg;
        String msg;
        Intent intent = getIntent();
        msg = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        arr_msg = msg.split(",");
        if(arr_msg.length == 2) {
            isSame = arr_msg[1];
            appEntryTime = arr_msg[0];
        }
        else {
            isSame = "NO";
            appEntryTime = arr_msg[0];
        }



        int randomNum = 0;
        int last = 0;

        String[] arr, previous;
        int[] numOccurrences = new int[3];
        int[] lastPrompt = new int[2];

        //prefs = getSharedPreferences("com.example.newapp", MODE_PRIVATE);


        if((isSame.equals("NO")) || (isSame.equals(""))) {
            try {
                firstLine = getFirstLine();
                lastLine = getLastLine();

                if (lastLine != null && !lastLine.isEmpty()) {
                    Log.d("lastLine is", lastLine);
                    arr = lastLine.split(",");
                    for (int i = 0; i < arr.length; i++) {
                        Log.d("arr is", arr[i]);
                        numOccurrences[i] = Integer.parseInt(arr[i]);

                    }

                }

                if (firstLine != null && !firstLine.isEmpty()) {
                    Log.d("firstLine is", firstLine);
                    previous = getPrevious();
                    if (previous != null) {
                        if (previous.length == 1) {
                            Log.d("previous length is", "1");
                            last = Integer.parseInt(previous[0]);
                            Log.d("If. last prompt is", Integer.toString(last));
                            randomNum = getRandom(last);
                            Log.d("If. randomNum is", Integer.toString(randomNum));
                        } else if (previous.length >= 2) {
                            Log.d("previous length is", Integer.toString(previous.length));
                            lastPrompt[0] = Integer.parseInt(previous[previous.length - 2]);
                            lastPrompt[1] = Integer.parseInt(previous[previous.length - 1]);
                            Log.d("If. Previous prompts:", Integer.toString(lastPrompt[0])+","+Integer.toString(lastPrompt[1]));
                            if (lastPrompt[0] == 1 && lastPrompt[1] == 2)
                                randomNum = 3;
                            else if (lastPrompt[0] == 2 && lastPrompt[1] == 1)
                                randomNum = 3;
                            else if (lastPrompt[0] == 1 && lastPrompt[1] == 3)
                                randomNum = 2;
                            else if (lastPrompt[0] == 3 && lastPrompt[1] == 1)
                                randomNum = 2;
                            else if (lastPrompt[0] == 2 && lastPrompt[1] == 3)
                                randomNum = 1;
                            else if (lastPrompt[0] == 3 && lastPrompt[1] == 2)
                                randomNum = 1;
                            else if (lastPrompt[0] == lastPrompt[1])
                                randomNum = getRandom(lastPrompt[0]);
                            Log.d("Else. randomNum is", Integer.toString(randomNum));
                        }
                    } else {
                        randomNum = getRandomNumberInRange(1, 3);
                    }

                    //Log.d("If. last is ", Integer.toString(last));

                    Log.d("If. Prompt number is ", Integer.toString(randomNum));
                    firstLine = firstLine + "," + randomNum;
                    numOccurrences[randomNum - 1] = numOccurrences[randomNum - 1] + 1;
                    for (int j = 0; j < numOccurrences.length; j++) {
                        temp = temp + numOccurrences[j] + ",";
                    }
                    temp = temp.substring(0, temp.length() - 1);
                    //writeToOne(firstLine, temp);
                } else {
                    randomNum = getRandomNumberInRange(1, 3);
                    Log.d("Else. Prompt number is ", Integer.toString(randomNum));
                    firstLine = Integer.toString(randomNum);
                    numOccurrences[randomNum - 1] = numOccurrences[randomNum - 1] + 1;
                    for (int j = 0; j < numOccurrences.length; j++) {
                        temp = temp + numOccurrences[j] + ",";
                    }
                    temp = temp.substring(0, temp.length() - 1);
                    //writeToOne(firstLine, temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(isSame.equals("YES")) {
            try {
                firstLine = getFirstLine();
                lastLine = getLastLine();
                if (lastLine != null && !lastLine.isEmpty()) {

                    arr = lastLine.split(",");
                    for (int i = 0; i < arr.length; i++) {

                        numOccurrences[i] = Integer.parseInt(arr[i]);

                    }

                }
                previous = getPrevious();
                int numPrompts = previous.length;
                if(numPrompts == 0)
                    randomNum = getRandomNumberInRange(1, 3);
                else
                    randomNum = Integer.parseInt(previous[numPrompts - 1]);
                if (firstLine != null && !firstLine.isEmpty()) {
                    firstLine = firstLine + "," + randomNum;
                    numOccurrences[randomNum - 1] = numOccurrences[randomNum - 1] + 1;
                    for (int j = 0; j < numOccurrences.length; j++) {
                        temp = temp + numOccurrences[j] + ",";
                    }
                    temp = temp.substring(0, temp.length() - 1);
                }
                else  {
                    firstLine = Integer.toString(randomNum);
                    numOccurrences[randomNum - 1] = numOccurrences[randomNum - 1] + 1;
                    for (int j = 0; j < numOccurrences.length; j++) {
                        temp = temp + numOccurrences[j] + ",";
                    }
                    temp = temp.substring(0, temp.length() - 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        TextView tv1 = (TextView)findViewById(R.id.prompt);
        if(randomNum == 1)
            tv1.setText(R.string.prompt1);
        else if(randomNum == 2)
            tv1.setText(R.string.prompt2);
        else if(randomNum == 3)
            tv1.setText(R.string.prompt3);
        //TextView tv1 = (TextView)findViewById(R.id.prompt);
        //tv1.setText(R.string.prompt3);



        /*num = numRecordings();
        Log.d("Number of Recordings",String.format("number of recordings = %d", num));*/
        //appEntryTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
        /*long notificationDelay = (1000 * 60 * 60 * 5)/100; //5 hours, starts in milliseconds
        for(int i = 0; i <= 0;i++) {


                final int notificationNumber = i;
                setAlarm(notificationNumber, notificationDelay);


        }*/

        // Enables Always-on
        setAmbientEnabled();
    }

    public void initialBird(View view) throws IOException {
        Intent intent;
        String message = appEntryTime;
        intent = new Intent(this, BirdBefore.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        writeToOne(firstLine, temp);
        startActivity(intent);
        this.finish();
    }

    public void anotherPrompt(View view) throws IOException {
        Intent intent;
        String message = "NO";
        intent = new Intent(this, MainActivity.class);
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
                    return name.contains("_recording_");
                }
            });
            numberOfRecordings = foundFiles.length;
            return numberOfRecordings;
        }
        return 0;
    }

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
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

    public static void writeToOne(String firstLine, String secondLine) throws IOException {
        String FILENAME = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Memories" + File.separator + "pastSequence.txt";
        BufferedWriter bw = null;
        FileWriter fw = null;
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "Memories" + File.separator + "pastSequence.txt");
        if(f.exists() && !f.isDirectory()) {
            try {
                //open file for writing
                File file = new File("/sdcard/Memories/pastSequence.txt");
                FileOutputStream fileinput = new FileOutputStream(file, false);
                PrintStream printstream = new PrintStream(fileinput);
                printstream.print(firstLine+"\n");
                printstream.print(secondLine+"\n");
                fileinput.close();


            } catch (java.io.IOException e) {
                //if caught

            }
        }
        else {
            File dir = new File("/sdcard/Memories");
            try{
                if(dir.mkdir()) {
                    Log.d("Directory created: ", "Success");
                } else {
                    Log.d("Directory not created: ", "Failure");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try {
                //open file for writing
                File file = new File("/sdcard/Memories/pastSequence.txt");
                FileOutputStream fileinput = new FileOutputStream(file, false);
                PrintStream printstream = new PrintStream(fileinput);
                printstream.print(firstLine+"\n");
                printstream.print(secondLine+"\n");
                fileinput.close();


            } catch (java.io.IOException e) {
                //if caught

            }
        }

    }

    public static String[] getPrevious() throws IOException {
        String numbers = getFirstLine();
        if(numbers == null)
            return null;
        String[] arr = numbers.split(",");
        return arr;
    }

    public static int getRandom(int n) {
        ArrayList<Integer> numbers=new ArrayList<Integer>();
        for(int i=1; i <= 3; i++) {
            if(i == n)
                continue;
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        return numbers.get(0);
    }
}
