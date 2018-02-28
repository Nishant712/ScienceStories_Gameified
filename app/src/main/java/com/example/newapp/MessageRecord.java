package com.example.newapp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Chronometer;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.newapp.Prompt.getPrevious;

public class MessageRecord extends Activity {

    private TextView mTextView;
    public static String EXTRA_MESSAGE = "com.example.newapp.MESSAGE";
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean isRecordingButtonPressed = false;
    private int numberOfRecordings = Prompt.numRecordings() + 1;
    private String recording_details = "";
    private String previous_recording_details = "";
    private String firstLine = "";
    private String secondLine = "";
    private String startTime;
    private String endTime;
    private int category;
    private Chronometer chronometer;
    private MediaPlayer mPlayer = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_record);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

        setContentView(R.layout.rect_activity_message_record);
        Log.d("Number of Recordings",String.format("number of recordings = %d", numberOfRecordings));

        Intent intent = getIntent();
        String temp = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String[] stringArr = temp.split(";");
        previous_recording_details = stringArr[0];
        recording_details = stringArr[0];
        firstLine = stringArr[1];
        secondLine = stringArr[2];
        //Log.d("Recording Details",recording_details);

        final ImageButton playButton = (ImageButton) findViewById(R.id.recordButton);
        playButton.setBackground(getResources().getDrawable(R.drawable.record_stop));
        final ImageView viewBackground = (ImageView) findViewById(R.id.messageRecordBackground);

        final TextView textView = (TextView) findViewById(R.id.recordText);
        final ImageButton button = (ImageButton) findViewById(R.id.recordButton);
        //final ImageView opacityImage = (ImageView) findViewById(R.id.opacityFilter);
        final Button uploadButton = (Button) findViewById(R.id.uploadRecordingButton);
        final Button discardButton = (Button) findViewById(R.id.discardRecordingButton);
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();


        final String currentRecordings = "Recording";
        String[] prompts;

        prompts = firstLine.split(",");
        category = Integer.parseInt(prompts[prompts.length-1]);

        Log.d("Current category ",Integer.toString(category));
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        //textView.setText(currentRecordings);

        //opacityImage.setVisibility(View.VISIBLE);

        try {
            startRecording();
        } catch(Exception e) {
            e.printStackTrace();
        }
        //Chapter.myVP.setPagingEnabled(false);
        startTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
        recording_details = recording_details + "," + category + "_" + "recording_" + numberOfRecordings + "," + startTime;
        isRecordingButtonPressed = true;


        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewBackground.setBackgroundColor(Color.parseColor("#FFB6C1"));

                chronometer.stop();
                if(event.getAction() == MotionEvent.ACTION_DOWN && isRecordingButtonPressed){
                    playButton.setBackground(getResources().getDrawable(R.drawable.record_play));
                    try {
                        stopRecording();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    //Chapter.myVP.setPagingEnabled(true);
                    endTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
                    recording_details = recording_details + "," + endTime;
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                    textView.setText(""); //finger was lifted

                    //STARTING FADE IN OF AUDIO SUBMIT MENU
                    //button.setEnabled(false);
                    uploadButton.startAnimation(animationFadeIn);
                    uploadButton.setVisibility(v.VISIBLE);
                    discardButton.startAnimation(animationFadeIn);
                    discardButton.setVisibility(v.VISIBLE);
                    uploadButton.setEnabled(true);
                    discardButton.setEnabled(true);
                    //Chapter.myVP.setPagingEnabled(false);
                    isRecordingButtonPressed = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN && !isRecordingButtonPressed && !isPlaying){
                    playButton.setBackground(getResources().getDrawable(R.drawable.record_pause));


                    try {
                        isPlaying = true;
                        startPlaying();
                        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                playButton.setBackground(getResources().getDrawable(R.drawable.record_play));
                                isPlaying = false;
                                stopPlaying(); // finish current activity
                            }
                        });
                    } catch(Exception e) {
                        e.printStackTrace();
                    }


                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN && !isRecordingButtonPressed && isPlaying){
                    playButton.setBackground(getResources().getDrawable(R.drawable.record_play));
                    
                    try {
                        pausePlaying();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    //Chapter.myVP.setPagingEnabled(true);
                    //endTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
                    //recording_details = recording_details + "," + endTime;
                    //textView.setTextColor(Color.parseColor("#FFFFFF"));
                    //textView.setText(""); //finger was lifted

                    //STARTING FADE IN OF AUDIO SUBMIT MENU
                    //button.setEnabled(false);
                    uploadButton.startAnimation(animationFadeIn);
                    uploadButton.setVisibility(v.VISIBLE);
                    discardButton.startAnimation(animationFadeIn);
                    discardButton.setVisibility(v.VISIBLE);
                    uploadButton.setEnabled(true);
                    discardButton.setEnabled(true);
                    //Chapter.myVP.setPagingEnabled(false);
                    isPlaying = false;
                }
                return true;
            }
        });
        discardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    String declineTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
                    uploadButton.startAnimation(animationFadeOut);
                    uploadButton.setVisibility(v.INVISIBLE);
                    discardButton.startAnimation(animationFadeOut);
                    discardButton.setVisibility(v.INVISIBLE);
                    uploadButton.setEnabled(false);
                    discardButton.setEnabled(false);
                    button.setEnabled(true);

                    recording_details = recording_details + ",Declined at " + declineTime + ",,,";
                    deleteRecording();
                }
                return true;
            }
        });
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Memories" + File.separator + category + "_" + "recording_" + numberOfRecordings + ".wav";
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();

        } catch (IOException e) {
            Log.e("MediaPlayer", "prepare() failed");
        }
    }

    private void pausePlaying() {
        mPlayer.pause();
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format

    private void startRecording() {

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);


        recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecord Thread");
        recordingThread.start();
    }

    private void deleteRecording(){
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Memories" + File.separator + category + "_" + "recording_" + numberOfRecordings;
        //String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Memories" + File.separator + category + "_" + "recording_" + numberOfRecordings;

        String message = previous_recording_details+";"+firstLine+";"+secondLine;
        File dir = new File("/sdcard/Memories");
        try{
            if(dir.mkdir()) {
                Log.d("Directory created: ", "Success");
            } else {
                Log.d("Directory not created: ", "Success");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        File newWavFile = new File (filePath + ".wav");
        File oldPcmFile = new File (filePath + ".pcm");
        newWavFile.delete();
        //oldPcmFile.delete();
        try {
            File file = new File("/sdcard/Memories/recording_details.txt");
            FileOutputStream fileinput = new FileOutputStream(file, true);
            PrintStream printstream = new PrintStream(fileinput);
            printstream.print(recording_details+"\n");
            fileinput.close();


        } catch (java.io.IOException e) {
            //if caught

        }
        Intent intent;
        intent = new Intent(this, BirdBefore.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        this.finish();
    }



    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte
        Log.d("Number of Recordings",String.format("number of recordings = %d", numberOfRecordings));
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Memories" + File.separator + category + "_" + "recording_" + numberOfRecordings + ".pcm";
        /*File dir = new File("/sdcard/Memories");
        try{
            if(dir.mkdir()) {
                Log.d("Directory created: ", "Success");
            } else {
                Log.d("Directory not created: ", "Success");
            }
        }catch(Exception e){
            e.printStackTrace();
        }*/
        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            recorder.read(sData, 0, BufferElements2Rec);
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        // stops the recording activity
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            Log.d("Number of Recordings",String.format("number of recordings = %d", numberOfRecordings));
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Memories" + File.separator + category + "_" + "recording_" + numberOfRecordings;
            File oldPcmFile = new File (filePath + ".pcm");
            File newWaveFile = new File(filePath + ".wav");
            try {
                rawToWave(oldPcmFile, newWaveFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //oldPcmFile.delete();
            recordingThread = null;
        }
    }

    private void rawToWave(final File rawFile, final File waveFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, RECORDER_SAMPLERATE); // sample rate
            writeInt(output, RECORDER_SAMPLERATE * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }

            output.write(fullyReadFileToBytes(rawFile));
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }

    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }

    public void writeToFile(String directory, String filename, String data){
        File out;
        OutputStreamWriter outStreamWriter = null;
        FileOutputStream outStream = null;

        out = new File(new File(directory), filename);
        try {
            if (out.exists() == false) {
                out.createNewFile();
            }

            outStream = new FileOutputStream(out, true);
            outStreamWriter = new OutputStreamWriter(outStream);

            outStreamWriter.append(data);
            outStreamWriter.flush();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void uploadRecording(View v){
        String confirmTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
        String message = recording_details + ",Confirmed at " + confirmTime;
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Memories" + File.separator + category + "_" + "recording_" + numberOfRecordings;
        File oldPcmFile = new File (filePath + ".pcm");
        oldPcmFile.delete();
        try {
            writeToOne(firstLine,secondLine);
        } catch (Exception e){
            System.out.println(e);
        }
        Intent intent = new Intent(this, RecordingConfirmation.class);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                0, v.getWidth(), v.getHeight());
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent, options.toBundle());
        finish();
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




}
