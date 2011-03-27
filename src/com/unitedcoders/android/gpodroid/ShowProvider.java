package com.unitedcoders.android.gpodroid;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.unitedcoders.android.gpodroid.database.GpodDB;
import com.unitedcoders.android.gpodroid.database.GpodDBHelper;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: nheid
 * Date: 3/5/11
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShowProvider extends ContentProvider {

    private GpodDBHelper dbHelper;
    SQLiteDatabase db3;

    public static final String PROVIDER = "com.unitedcoders.android.gpodroid.Provider";

    @Override
    public boolean onCreate() {

        Context c = getContext();
        dbHelper = new GpodDBHelper(c);
        db3 = dbHelper.getWritableDatabase();

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(GpodDB.DATABASE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(GpodDB.DATABASE_TABLE, new String[]{"file"},
                "_id=?", new String[]{"281"}, null, null, null);

        c.moveToFirst();


        String filePath = c.getString(0);
        File file = new File(filePath);
        int imode = 0;
        if (mode.contains("r")) imode |= ParcelFileDescriptor.MODE_READ_ONLY;
        if (mode.contains("+")) imode |= ParcelFileDescriptor.MODE_APPEND;

        return ParcelFileDescriptor.open(file, imode);

    }

    @Override
    public String getType(Uri uri) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long rowId = db.insert(GpodDB.DATABASE_TABLE, "show", contentValues);

        if (rowId > 0) {
            Uri curi = Uri.parse("content://" + PROVIDER + "/show");
            Uri _uri = ContentUris.withAppendedId(curi, rowId);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }


        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
