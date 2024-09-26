package com.prm.miniproject_prm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdActivity extends AppCompatActivity {

    private Button skipButton;
    private TextView countdownTextView;
    private Handler handler = new Handler();
    private int countdownTime = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        ImageView adImage = findViewById(R.id.adImage);
        skipButton = findViewById(R.id.skipButton);
        countdownTextView = findViewById(R.id.countdownTextView);


        skipButton.setEnabled(false);
        countdownTextView.setText("Skip available in: " + countdownTime + "s");

        // Bắt đầu đếm ngược
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (countdownTime > 0) {
                    countdownTime--;
                    countdownTextView.setText("Skip available in: " + countdownTime + "s");
                    handler.postDelayed(this, 1000);
                } else {
                    skipButton.setEnabled(true);
                    countdownTextView.setText("You can skip now");
                }
            }
        }, 1000);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
