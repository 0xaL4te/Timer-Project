package com.example.timer_application;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;

public class SoundSettingsActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private RadioButton radioSound1, radioSound2, radioSound3;
    private Button previewSound1, previewSound2, previewSound3, saveButton;
    private String selectedSound; // Declare selectedSound as a String

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_settings);

        radioSound1 = findViewById(R.id.radioSound1);
        radioSound2 = findViewById(R.id.radioSound2);
        radioSound3 = findViewById(R.id.radioSound3);
        previewSound1 = findViewById(R.id.previewSound1);
        previewSound2 = findViewById(R.id.previewSound2);
        previewSound3 = findViewById(R.id.previewSound3);
        saveButton = findViewById(R.id.saveButton);

        previewSound1.setOnClickListener(v -> playSound(R.raw.alarm_sound));
        previewSound2.setOnClickListener(v -> playSound(R.raw.alarm_sound));
        previewSound3.setOnClickListener(v -> playSound(R.raw.alarm_sound));

        saveButton.setOnClickListener(v -> saveSelectedSound());

        loadSelectedSound();  // Load saved sound when activity starts
    }

    private void playSound(int soundResId) {
        // Stop any current playing sound
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, soundResId);
        mediaPlayer.start();
    }

    private void saveSelectedSound() {
        // Determine which sound is selected
        if (radioSound1.isChecked()) {
            selectedSound = "sound1";
        } else if (radioSound2.isChecked()) {
            selectedSound = "sound2";
        } else if (radioSound3.isChecked()) {
            selectedSound = "sound3";
        }

        // Save selected sound in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SoundSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selectedSound", selectedSound);
        editor.apply();
    }

    private void loadSelectedSound() {
        SharedPreferences sharedPreferences = getSharedPreferences("SoundSettings", Context.MODE_PRIVATE);
        selectedSound = sharedPreferences.getString("selectedSound", "sound1");

        switch (selectedSound) {
            case "sound1":
                radioSound1.setChecked(true);
                break;
            case "sound2":
                radioSound2.setChecked(true);
                break;
            case "sound3":
                radioSound3.setChecked(true);
                break;
            default:
                radioSound1.setChecked(true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
