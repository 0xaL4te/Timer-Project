package com.example.timer_application;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TimerHistoryActivity extends AppCompatActivity {
    private static final int DATABASE_VERSION = 2; // Increment this version

    private TimerDatabaseHelper dbHelper;
    private ListView timerHistoryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_history);

        dbHelper = new TimerDatabaseHelper(this);
        timerHistoryListView = findViewById(R.id.timerHistoryListView);

        displayTimerHistory();
    }

    private void displayTimerHistory() {
        Cursor cursor = dbHelper.getAllTimerHistory();

        if (cursor == null) {
            Toast.makeText(this, "Error loading timer history", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No timer history available", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] fromColumns = { "duration", "end_time" };
        int[] toViews = { android.R.id.text1, android.R.id.text2 };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                fromColumns,
                toViews,
                0
        );

        timerHistoryListView.setAdapter(adapter);
    }

}
