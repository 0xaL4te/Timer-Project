package com.example.timer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    private TextView timerDisplay;
    private EditText hourInput, minuteInput, secondInput;
    private Button startButton, pauseButton, resetButton;
    private CountDownTimer countDownTimer;
    private long timeInMillis;
    private boolean isPaused = false;
    private long remainingTimeInMillis;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerDisplay = findViewById(R.id.timerDisplay);
        hourInput = findViewById(R.id.hourInput);
        minuteInput = findViewById(R.id.minuteInput);
        secondInput = findViewById(R.id.secondInput);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);

        // Initialize sound for alarm
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);

        // Initialize Notification Channel
        createNotificationChannel();

        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        resetButton.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        if (isPaused) {
            // Resume timer
            countDownTimer = new CountDownTimer(remainingTimeInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTimeInMillis = millisUntilFinished;
                    updateTimerDisplay();
                }

                @Override
                public void onFinish() {
                    endTimer();
                }
            }.start();
            isPaused = false;
        } else {
            // Start a new timer
            int hours = getIntFromEditText(hourInput);
            int minutes = getIntFromEditText(minuteInput);
            int seconds = getIntFromEditText(secondInput);
            timeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000L;
            remainingTimeInMillis = timeInMillis;

            countDownTimer = new CountDownTimer(timeInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTimeInMillis = millisUntilFinished;
                    updateTimerDisplay();
                }

                @Override
                public void onFinish() {
                    endTimer();
                }
            }.start();
        }
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isPaused = true;
        }
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerDisplay.setText("00:00:00");
        hourInput.setText("");
        minuteInput.setText("");
        secondInput.setText("");
        isPaused = false;
    }

    private void endTimer() {
        timerDisplay.setText("00:00:00");

        // Play sound
        mediaPlayer.start();

        // Display notification
        showNotification();

        // Display "Time's Up!!" message on screen
        timerDisplay.setText("Time's Up!!");
    }

    private int getIntFromEditText(EditText editText) {
        String input = editText.getText().toString();
        return input.isEmpty() ? 0 : Integer.parseInt(input);
    }


    private void updateTimerDisplay() {
        int hours = (int) (remainingTimeInMillis / 1000) / 3600;
        int minutes = (int) ((remainingTimeInMillis / 1000) % 3600) / 60;
        int seconds = (int) (remainingTimeInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerDisplay.setText(timeLeftFormatted);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TimerChannel";
            String description = "Channel for Timer End Notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("timerEnd", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "timerEnd")
                .setSmallIcon(R.drawable.amrita_logo)
                .setContentTitle("Timer Finished")
                .setContentText("Time's Up!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
