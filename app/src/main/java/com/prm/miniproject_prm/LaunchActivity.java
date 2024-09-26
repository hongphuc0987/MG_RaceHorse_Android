package com.prm.miniproject_prm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    private EditText playerNameEditText;
    private Button btnHowToPlay, btnStartGame, exitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        playerNameEditText = findViewById(R.id.editTextPlayerName);
        btnHowToPlay = findViewById(R.id.btnHowToPlay);
        btnStartGame = findViewById(R.id.btnStartGame);
        exitButton = findViewById(R.id.btnExit);


        btnHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent howToPlayIntent = new Intent(LaunchActivity.this, HowToPlayActivity.class);
                startActivity(howToPlayIntent);
            }
        });

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = playerNameEditText.getText().toString().trim();
                if (playerName.isEmpty()) {
                    Toast.makeText(LaunchActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                } else {
                    Intent mainGameIntent = new Intent(LaunchActivity.this, MainActivity.class);

                    mainGameIntent.putExtra("PLAYER_NAME", playerName);

                    startActivity(mainGameIntent);

                    finish();
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
