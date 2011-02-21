package com.unitedcoders.android.gpodroid.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GpodDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gpodroid.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE \"podcast\" (\n" +
            "    \"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "    \"file\" TEXT NOT NULL,\n" +
            "    \"title\" TEXT,\n" +
            "    \"show\" TEXT,\n" +
            "    \"downloaded\" INTEGER,\n" +
            "    \"played\" INTEGER\n" +
            ")";



    public GpodDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS podcast");
        onCreate(sqLiteDatabase);
    }
}
