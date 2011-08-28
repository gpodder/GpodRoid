package com.unitedcoders.android.gpodroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.unitedcoders.android.gpodroid.Episode;
import com.unitedcoders.gpodder.GpodderPodcast;

import java.util.ArrayList;
import java.util.List;


public class GpodDB {

    private Context context;
    private SQLiteDatabase db;
    private GpodDBHelper dbHelper;
    public static final String DATABASE_TABLE = "podcast";

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

    public synchronized void addPodcasts(List<GpodderPodcast> pce) {

        open();

        for (int i = 0; i < pce.size(); i++) {


            GpodderPodcast gpodderPodcast = pce.get(i);

            // do we know this one?
            String podcast = gpodderPodcast.getPodcast_title();
            String show = gpodderPodcast.getTitle();
            Cursor c = db.query(DATABASE_TABLE, new String[]{"title"}, "show=? and title=?",
                    new String[]{show, podcast}, null, null, null, "1");

//            Cursor c = db.rawQuery("select title from " + DATABASE_TABLE + " where show=? and title=? limit 1",
//                    new String[]{show, podcast});

            if (c.getCount() > 0) {
                c.close();
                continue;
            }


            ContentValues map = new ContentValues();
            map.put("file", "");
            map.put("show", gpodderPodcast.getTitle());
            map.put("title", gpodderPodcast.getPodcast_title());
            map.put("url", gpodderPodcast.getUrl());
            map.put("downloaded", 0);
            map.put("played", 0);
            map.put("podcast_url", gpodderPodcast.getPodcast_url());
            map.put("released", gpodderPodcast.getReleased());

            long l = db.insert(DATABASE_TABLE, null, map);
            c.close();
        }


        close();


    }

    public void updateEpisode(Episode episode) {
        open();
        ContentValues values = new ContentValues();
        values.put("downloaded", episode.getDownloaded());
        values.put("file", episode.getFile());

        db.update(DATABASE_TABLE, values, "show =? and title=?",
                new String[]{episode.getTitle(), episode.getPodcast_title()});

        close();

    }

    public List<Episode> getEpisodes(String title) {
        open();
        Cursor c = db.query(DATABASE_TABLE, new String[]{"show, title, downloaded, url, file, _id, podcast_url"},
                "title=?", new String[]{title}, null, null, null);

        ArrayList<Episode> podcasts = new ArrayList<Episode>();

        if (c.getCount() != 0) {
            while (!c.isLast()) {
                c.moveToNext();
                Episode pce = new Episode(new GpodderPodcast());
                pce.setTitle(c.getString(0));
                pce.setPodcast_title(c.getString(1));
                pce.setDownloaded(c.getInt(2));
                pce.setUrl(c.getString(3));
                pce.setFile(c.getString(4));
                pce.setId(c.getInt(5));
                pce.setPodcast_url(c.getString(6));

                podcasts.add(pce);
            }
        }

        c.close();
        close();
        return podcasts;

    }

    public Episode getEpisode(int id) {
        open();
        Cursor c = db.query(DATABASE_TABLE, new String[]{"show, title, downloaded, url, file, _id, podcast_url"},
                "_id=?", new String[]{Integer.toString(id)}, null, null, null);

        Episode pce = new Episode(new GpodderPodcast());
        if (c.getCount() != 0) {

            c.moveToFirst();

            pce.setTitle(c.getString(0));
            pce.setPodcast_title(c.getString(1));
            pce.setDownloaded(c.getInt(2));
            pce.setUrl(c.getString(3));
            pce.setFile(c.getString(4));
            pce.setId(c.getInt(5));
            pce.setPodcast_url(c.getString(6));

        }

        c.close();
        close();
        return pce;

    }

    public List<String> getPodcasts() {
        open();
        Cursor c = db.query(true, DATABASE_TABLE, new String[]{"title"}, null, null, null, null, null, null);

        ArrayList<String> shows = new ArrayList<String>();
        if (c.getCount() != 0) {
            while (!c.isLast()) {
                c.moveToNext();
                shows.add(c.getString(0));
            }
        }

        c.close();
        close();

        return shows;


    }

    public List<Episode> getDownloads() {
        open();
        Cursor c = db.query(DATABASE_TABLE, new String[]{"show, title, downloaded, url, file, _id, podcast_url"},
                "downloaded=?", new String[]{"0"}, null, null, "released desc", "20");

        ArrayList<Episode> podcasts = new ArrayList<Episode>();

        if (c.getCount() != 0) {
            while (!c.isLast()) {
                c.moveToNext();
                Episode pce = new Episode(new GpodderPodcast());
                pce.setTitle(c.getString(0));
                pce.setPodcast_title(c.getString(1));
                pce.setDownloaded(c.getInt(2));
                pce.setUrl(c.getString(3));
                pce.setFile(c.getString(4));
                pce.setId(c.getInt(5));
                pce.setPodcast_url(c.getString(6));

                podcasts.add(pce);
            }
        }

        c.close();
        close();
        return podcasts;
    }

    public void wipeClean() {
        open();
        db.delete(DATABASE_TABLE, "_id > ?" ,new String[]{"-1"});
        close();

    }
}
