package com.unitedcoders.android.gpodroid.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.unitedcoders.android.gpodroid.Episode;
import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.gpodder.GpodderPodcast;

import java.util.ArrayList;
import java.util.List;

public final class GpodDB {

    /**
     * holds a reference to the SQLite database
     */
    private static SQLiteDatabase db;

    /**
     * The GpodDBHelper class reference
     */
    private static GpodDBHelper dbHelper;

    /**
     * the name of the data base table
     */
    public static final String DATABASE_TABLE = "podcast";

    /**
     * static Constructor
     */
    static {
        dbHelper = new GpodDBHelper(GpodRoid.context);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * This is just here to prevent the class from being initialized via the private visibility
     */
    private GpodDB() {
        // do nothing on purpose
    }

    /**
     * The finalizer to cleanup the database helper(non-Javadoc)
     *
     * @see java.lang.Object#finalize()
     *      <p/>
     *      protected void finalize() {
     *      if(db != null){
     *      db.close();
     *      }
     *      if(dbHelper != null){
     *      dbHelper.close();
     *      }
     *      }
     */

    public static synchronized void addPodcasts(List<GpodderPodcast> pce) {
        for (int i = 0; i < pce.size(); i++) {


            GpodderPodcast gpodderPodcast = pce.get(i);

            // do we know this one?
            String podcast = gpodderPodcast.getPodcast_title();
            String show = gpodderPodcast.getTitle();

            if (podcast == null || show == null) {
                Log.e(GpodRoid.LOGTAG,
                        String.format("did not insert show because either podcast(%s) or show(%s) missing",
                                podcast, show));
                continue;
            }

            Cursor c = db.query(DATABASE_TABLE, new String[]{"title"}, "show=? and title=?", new String[]{show, podcast}, null, null, null, "1");

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
    }

    public static void updateEpisode(Episode episode) {
        ContentValues values = new ContentValues();
        values.put("downloaded", episode.getDownloaded());
        values.put("file", episode.getFile());

        db.update(DATABASE_TABLE, values, "show =? and title=?", new String[]{episode.getTitle(), episode.getPodcast_title()});

    }

    public static List<Episode> getEpisodes(String title) {
        Cursor c = db.query(DATABASE_TABLE, new String[]{"show, title, downloaded, url, file, _id, podcast_url"}, "title=?", new String[]{title}, null, null, null);
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
        return podcasts;

    }

    public static Episode getEpisode(int id) {
        Cursor c = db.query(DATABASE_TABLE, new String[]{"show, title, downloaded, url, file, _id, podcast_url"}, "_id=?", new String[]{Integer.toString(id)}, null, null, null);

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
        return pce;

    }

    public static List<String> getPodcasts() {
        Cursor c = db.query(true, DATABASE_TABLE, new String[]{"title"}, null, null, null, null, null, null);

        ArrayList<String> shows = new ArrayList<String>();
        if (c.getCount() != 0) {
            while (!c.isLast()) {
                c.moveToNext();
                shows.add(c.getString(0));
            }
        }

        c.close();
        return shows;
    }

    public static List<Episode> getDownloads() {
        Cursor c = db.query(DATABASE_TABLE, new String[]{"show, title, downloaded, url, file, _id, podcast_url"}, "downloaded=?", new String[]{"0"}, null, null, "released desc", "20");

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
        return podcasts;
    }

    public static void wipeClean() {
        db.delete(DATABASE_TABLE, "_id > ?", new String[]{"-1"});
    }
}
