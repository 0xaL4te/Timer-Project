package com.example.timer_application;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TimerDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timerHistory.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "timer_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_END_TIME = "end_time";

    public TimerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DURATION + " TEXT, " +
                COLUMN_END_TIME + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addTimerHistory(String duration, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verify the database is open
        if (db == null || !db.isOpen()) {
            Log.e("DatabaseError", "Database is not open");
            return;
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_END_TIME, endTime);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();

        // Debugging: Log the result of the insert operation
        if (result == -1) {
            Log.e("TimerHistory", "Failed to insert timer history");
        } else {
            Log.d("TimerHistory", "Timer history inserted successfully with ID: " + result);
        }
    }


    public Cursor getAllTimerHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
