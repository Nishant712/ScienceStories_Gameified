package com.example.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class PromptChange extends WearableActivity {

    private TextView mTextView;
    private String isSame = "";
    public static String EXTRA_MESSAGE = "com.example.newapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_change);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();
    }

    public void samePrompt(View view) throws IOException {
        Intent intent;
        isSame = "YES";
        intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE, isSame);
        startActivity(intent);
        this.finish();
    }

    public void diffPrompt(View view) throws IOException {
        Intent intent;
        isSame = "NO";
        intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE, isSame);
        startActivity(intent);
        this.finish();
    }
}
