package com.unitedcoders.android.gpodroid.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GpodDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gpodroid.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE_PODCASTINFO = "CREATE TABLE podcast (_ID INTEGER PRIMARY KEY, file TEXT, title TEXT, show TEXT, downloaded INTEGER, played INTEGER, url TEXT, podcast_url TEXT, released TEXT)";

    public GpodDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_PODCASTINFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS podcast");
        onCreate(sqLiteDatabase);
    }
}
