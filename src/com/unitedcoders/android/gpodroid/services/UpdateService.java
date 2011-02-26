package com.unitedcoders.android.gpodroid.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.Preferences;
import com.unitedcoders.android.gpodroid.activity.AccountSettings;
import com.unitedcoders.android.gpodroid.database.GpodDB;
import com.unitedcoders.gpodder.GpodderAPI;
import com.unitedcoders.gpodder.GpodderPodcast;
import com.unitedcoders.gpodder.GpodderUpdates;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nheid
 * Date: 2/26/11
 * Time: 12:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpdateService extends Service {

    private Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        downloadProcessing();

    }

    public synchronized void downloadProcessing() {

        Preferences pref = Preferences.getPreferences(getApplicationContext());

        if (pref.getUsername().equals("") || pref.getPassword().equals("") || pref.getDevice().equals("")) {

            Toast toast = Toast.makeText(getApplicationContext(), "please enter your settings first",
                    Toast.LENGTH_SHORT);
            toast.show();

            Intent intent = new Intent(getApplicationContext(), AccountSettings.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

            return;

        }


        Thread thread = new Thread(null, doGetPodcastDownloadInfo, "Background");
        thread.start();
    }

    private Runnable doUpdateDownloadList = new Runnable() {
        public void run() {
//            lvDownloads.setAdapter(pcla);

        }
    };

    private Runnable doGetPodcastDownloadInfo = new Runnable() {
        public void run() {
            backgroundPodcastInfoFetcher();
        }
    };

    private void backgroundPodcastInfoFetcher() {
        GpodderUpdates podcast = GpodderAPI.getDownloadList();

        GpodDB gpdb = new GpodDB(getApplicationContext());

        if (podcast == null) {
            Log.e(GpodRoid.LOGTAG, "cant display downloads, got empty result");
            return;
        }

        List<GpodderPodcast> pcl = podcast.getUpdates();
        gpdb.addPodcasts(pcl);

        handler.post(doUpdateDownloadList);
    }


}
