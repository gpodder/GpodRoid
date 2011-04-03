package com.unitedcoders.android.gpodroid.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import com.unitedcoders.android.gpodroid.Episode;
import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.activity.PodcastManager;
import com.unitedcoders.android.gpodroid.database.GpodDB;
import com.unitedcoders.android.gpodroid.tools.Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadService extends Service {

    private final static int DOWNLOADSERVICE_NOTIFICATIONID = 42;

    private int downloaded, totalDownloadSize;
    private Intent intent;

    public static ArrayList<Episode> downloadQueue = new ArrayList<Episode>();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        this.intent = intent;

        downloadPodcast.start();

        stopSelf();

    }

    private Thread downloadPodcast = new Thread() {

        public void run() {
            Episode episode = null;
            while (downloadQueue.size() > 0 && (episode = downloadQueue.get(0)) != null) {
                try {
                    String downloadUrl = episode.getUrl();

                    File name = new File(downloadUrl);
                    String fileName = name.getName().trim();
                    File SDCardRoot = Environment.getExternalStorageDirectory();
                    String storageLocation = SDCardRoot.getAbsolutePath() + "/gpodroid/";

                    download(downloadUrl, storageLocation, fileName, "downloading " + episode.getTitle());

                    GpodDB db = new GpodDB(getApplicationContext());
                    episode.setFile(storageLocation + fileName);
                    episode.setDownloaded(1);
                    db.updateEpisode(episode);

                    // get imageurl from url
                    String imageLocation = Tools.getImageUrlFromFeed(getApplicationContext(), episode.getPodcast_url());
                    download(imageLocation, storageLocation + "covers/", episode.getPodcast_title().trim(), "downloading cover");


                } catch (IOException e) {
                    Log.e("Gpodroid", "error when downloading " + episode.getPodcast_url(), e);

                } finally {
                    if (downloadQueue.size() >= 1) {
                        downloadQueue.remove(0);
                    }
                }

                // Toast.makeText(this, "download ended", Toast.LENGTH_LONG);

            }
        }

    };

    /**
     * @param sourceLocation  location of the download in the web
     * @param storageLocation directory to store the data on sdcard
     * @param fileName        name of the file after download
     * @param displayText     what text to show in the notificationbar
     * @throws IOException
     */
    private void download(String sourceLocation, String storageLocation, String fileName, String displayText) throws IOException {
        Log.d("Gpodroid", "starting download " + sourceLocation);
        URL podcastURL = new URL(sourceLocation);
        HttpURLConnection urlConnection = (HttpURLConnection) podcastURL.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);

        urlConnection.connect();

        File SDCardRoot = Environment.getExternalStorageDirectory();
        new File(storageLocation).mkdir();

//        File name = new File(podcastURL.toString());
//        String saveName = name.getName().trim();
        File file = new File(storageLocation, fileName);
        FileOutputStream fileOutput = new FileOutputStream(file);
        InputStream inputStream = urlConnection.getInputStream();
        totalDownloadSize = urlConnection.getContentLength();

        byte[] buffer = new byte[1024];
        int bufferLength = 0;
        downloaded = 0;

        // put download info in notification bar
        final Notification notification = new Notification(R.drawable.icon, "downloading podcast", System
                .currentTimeMillis());
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        notification.contentView = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.download_progress);
        Intent intent = new Intent(getApplicationContext(), PodcastManager.class);
        final PendingIntent pendingIntent = PendingIntent
                .getActivity(getApplicationContext(), 0, intent, 0);

        notification.contentIntent = pendingIntent;

        notification.contentView.setImageViewResource(R.id.status_icon, android.R.drawable.ic_menu_save);
        notification.contentView.setTextViewText(R.id.status_text, displayText);

        notification.contentView.setProgressBar(R.id.status_progress, totalDownloadSize, downloaded,
                false);

        final NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        notificationManager.notify(DOWNLOADSERVICE_NOTIFICATIONID, notification);

        int nextNotification = 0;
        // now, read through the input buffer and write the contents
        // to the file
        while ((bufferLength = inputStream.read(buffer)) > 0) {
            // add the data in the buffer to the file in the file
            // output stream (the file on the sd card
            fileOutput.write(buffer, 0, bufferLength);
            // add up the size so we know how much is downloaded
            downloaded += bufferLength;

            // update notification
            if (downloaded >= nextNotification) {
                notification.contentView.setProgressBar(R.id.status_progress, totalDownloadSize,
                        downloaded, false);
                notificationManager.notify(DOWNLOADSERVICE_NOTIFICATIONID, notification);
                nextNotification += totalDownloadSize / 5;
            }

        }

        notificationManager.cancel(DOWNLOADSERVICE_NOTIFICATIONID);
        fileOutput.close();

        Log.d(GpodRoid.LOGTAG, "finished download " + sourceLocation + " in " + storageLocation + fileName);


    }

}


