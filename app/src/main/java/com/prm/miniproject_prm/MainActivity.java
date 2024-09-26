package com.prm.miniproject_prm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBarHorse1, seekBarHorse2, seekBarHorse3;
    private RadioButton betHorse1, betHorse2, betHorse3;
    private Button startButton, backToMenu;
    private TextView resultTextView, balanceTextView, playerNameview;
    private TextView percentageHorse1, percentageHorse2, percentageHorse3;
    private Handler handler = new Handler();
    private Random random = new Random();
    private boolean isRacing = false;
    private int balance = 1000;
    private int betAmount = 100;

    private MediaPlayer startRaceSound, endRaceSound;

    private static final int REQUEST_CODE_AD = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String playerName = getIntent().getStringExtra("PLAYER_NAME");


        seekBarHorse1 = findViewById(R.id.seekBarHorse1);
        seekBarHorse2 = findViewById(R.id.seekBarHorse2);
        seekBarHorse3 = findViewById(R.id.seekBarHorse3);
        betHorse1 = findViewById(R.id.betHorse1);
        betHorse2 = findViewById(R.id.betHorse2);
        betHorse3 = findViewById(R.id.betHorse3);
        startButton = findViewById(R.id.startButton);
        backToMenu = findViewById(R.id.backToMenu);
        resultTextView = findViewById(R.id.resultTextView);
        balanceTextView = findViewById(R.id.balanceTextView);
        playerNameview = findViewById(R.id.playerName1);

        percentageHorse1 = findViewById(R.id.horse1ProgressTextView);
        percentageHorse2 = findViewById(R.id.horse2ProgressTextView);
        percentageHorse3 = findViewById(R.id.horse3ProgressTextView);

        seekBarHorse1.setEnabled(false);
        seekBarHorse2.setEnabled(false);
        seekBarHorse3.setEnabled(false);

        startRaceSound = MediaPlayer.create(this, R.raw.start_race);
        endRaceSound = MediaPlayer.create(this, R.raw.end_race);


        if (startRaceSound == null) {
            Toast.makeText(this, "Could not load start race sound", Toast.LENGTH_SHORT).show();
        }
        if (endRaceSound == null) {
            Toast.makeText(this, "Could not load end race sound", Toast.LENGTH_SHORT).show();
        }

        updateBalanceDisplay();
        playerNameview.setText("Welcome: " + playerName);

        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LaunchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!betHorse1.isChecked() && !betHorse2.isChecked() && !betHorse3.isChecked()) {
                    resultTextView.setText("Please select a horse to bet on!");
                    return;
                }

                if (!isRacing && balance >= betAmount) {
                    isRacing = true;
                    placeBet();
                    if (startRaceSound != null) {
                        startRaceSound.start();
                    }
                    startRace();
                } else if (balance < betAmount) {
                    showAdDialog();
                }
            }
        });
    }

    private void updateBalanceDisplay() {
        balanceTextView.setText("Balance: " + balance + "$");
    }

    private void placeBet() {
        balance -= betAmount;
        updateBalanceDisplay();
    }

    private void startRace() {
        seekBarHorse1.setProgress(0);
        seekBarHorse2.setProgress(0);
        seekBarHorse3.setProgress(0);
        resultTextView.setText("");

        final int speedHorse1 = random.nextInt(10) + 5;
        final int speedHorse2 = random.nextInt(10) + 5;
        final int speedHorse3 = random.nextInt(10) + 5;

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isRacing) {
                    int horse1Progress = random.nextInt(5) + speedHorse1;
                    int horse2Progress = random.nextInt(5) + speedHorse2;
                    int horse3Progress = random.nextInt(5) + speedHorse3;

                    seekBarHorse1.setProgress(seekBarHorse1.getProgress() + horse1Progress);
                    seekBarHorse2.setProgress(seekBarHorse2.getProgress() + horse2Progress);
                    seekBarHorse3.setProgress(seekBarHorse3.getProgress() + horse3Progress);

                    updatePercentageDisplay();

                    if (seekBarHorse1.getProgress() >= seekBarHorse1.getMax()) {
                        isRacing = false;
                        checkResult(1);
                    } else if (seekBarHorse2.getProgress() >= seekBarHorse2.getMax()) {
                        isRacing = false;
                        checkResult(2);
                    } else if (seekBarHorse3.getProgress() >= seekBarHorse3.getMax()) {
                        isRacing = false;
                        checkResult(3);
                    } else {
                        handler.postDelayed(this, 800);
                    }
                }
            }
        });
    }

    private void updatePercentageDisplay() {
        percentageHorse1.setText(seekBarHorse1.getProgress() + "%");
        percentageHorse2.setText(seekBarHorse2.getProgress() + "%");
        percentageHorse3.setText(seekBarHorse3.getProgress() + "%");
    }

    private void checkResult(int winningHorse) {
        if ((betHorse1.isChecked() && winningHorse == 1) ||
                (betHorse2.isChecked() && winningHorse == 2) ||
                (betHorse3.isChecked() && winningHorse == 3)) {
            resultTextView.setText("You win!");
            balance += betAmount * 2;
        } else {
            resultTextView.setText("You lose! Horse " + winningHorse + " wins.");
        }
        if (endRaceSound != null) {
            endRaceSound.start();
        }

        updateBalanceDisplay();

        if (balance <= 0) {
            showAdDialog();
        }


        betHorse1.setChecked(false);
        betHorse2.setChecked(false);
        betHorse3.setChecked(false);
    }

    private void showAdDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("No Balance")
                .setMessage("You have no balance left. Would you like to watch an ad to get more balance?")
                .setPositiveButton("Watch Ad", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, AdActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_AD);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "You can’t play without balance.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_AD) {
            if (resultCode == RESULT_OK) {
                balance = 1000;
                updateBalanceDisplay();
                Toast.makeText(MainActivity.this, "Balance refilled. You have 1000$ to play!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "You didn't watch the ad. You can't play without balance.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (startRaceSound != null) {
            startRaceSound.release();
            startRaceSound = null;
        }
        if (endRaceSound != null) {
            endRaceSound.release();
            endRaceSound = null;
        }
    }
}



