package com.unitedcoders.android.gpodroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.unitedcoders.gpodder.GpodderPodcast;

import java.util.ArrayList;
import java.util.List;


public class GpodDB {

    private Context context;
    private SQLiteDatabase db;
    private GpodDBHelper dbHelper;
    private static final String DATABASE_TABLE = "podcast";

    public GpodDB(Context context) {
        this.context = context;
    }

    public void open() {
        dbHelper = new GpodDBHelper(context);
        db = dbHelper.getWritableDatabase();

    }

    public void close() {
        db.close();
        dbHelper.close();

    }

    public void addPodcasts(List<GpodderPodcast> pce) {

        open();

        for (int i = 0; i < pce.size(); i++) {
            GpodderPodcast gpodderPodcast = pce.get(i);

            ContentValues map = new ContentValues();
            map.put("file", "");
            map.put("show", gpodderPodcast.getPodcast_title());
            map.put("title", gpodderPodcast.getTitle());
            map.put("downloaded", 0);
            map.put("played", 0);

            long l = db.insert(DATABASE_TABLE, null, map);
        }

        close();

    }

    public List<GpodderPodcast> getPodcasts(String show) {
        open();
        Cursor c = db.query(DATABASE_TABLE, new String[]{"id", "title"}, "show = '"+show+"'", null, null, null, null);

        ArrayList<GpodderPodcast> podcasts = new ArrayList<GpodderPodcast>();

        if (c.getCount() != 0) {
            while (!c.isLast()) {
                c.moveToNext();
                GpodderPodcast pce = new GpodderPodcast();
                pce.setTitle(c.getString(1));
                pce.setId(c.getInt(0));
                podcasts.add(pce);
            }
        }


        close();
        return podcasts;

    }

    public List<String> getShows() {
        open();
        Cursor c = db.query(true, DATABASE_TABLE, new String[]{"show"}, null, null, null, null, null, null);

        ArrayList<String> shows = new ArrayList<String>();
        if (c.getCount() != 0) {
            while (!c.isLast()) {
                c.moveToNext();
                shows.add(c.getString(0));
            }
        }

        close();

        return shows;


    }

}
