package com.unitedcoders.android.gpodroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
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
 * Get updates from gpodder
 */
public class UpdateService extends Service {

    private Handler handler = new Handler();
    
    private Thread downloadUpdatesThread = null;

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
        if (Preferences.getUsername().equals("") || Preferences.hasAuthentication() || Preferences.getDevice().equals("")) {

            Toast.makeText(getApplicationContext(), "please enter your settings first", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), AccountSettings.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

            return;

        }
        
        if(downloadUpdatesThread == null){
	        downloadUpdatesThread = new Thread(null, doGetPodcastDownloadInfo, "Background");
	        downloadUpdatesThread.start();
        }
    }

    private Runnable doUpdateDownloadList = new Runnable() {
        public void run() {

        }
    };

    private Runnable doGetPodcastDownloadInfo = new Runnable() {
        public void run() {
            Looper.prepare();
            backgroundPodcastInfoFetcher();
            Looper.loop();
            
            // now that the fetching is done, reset the download thread reference
            downloadUpdatesThread = null;
        }
    };

    private void backgroundPodcastInfoFetcher() {
    	Log.d(GpodRoid.LOGTAG, "Starting background thread to get info.");
        GpodderUpdates podcast = GpodderAPI.getDownloadList();
        Log.d(GpodRoid.LOGTAG, "Got podcast updates.");

        if (podcast == null) {
        	Toast.makeText(GpodRoid.context, "Failed to fetch updates", Toast.LENGTH_LONG);
            Log.e(GpodRoid.LOGTAG, "cant display downloads, got empty result");
            return;
        }

        List<GpodderPodcast> pcl = podcast.getUpdates();
        GpodDB.addPodcasts(pcl);

        handler.post(doUpdateDownloadList);

        // notify views of new content
        Log.d(GpodRoid.LOGTAG, "UpdateService broadcasting changes");
        Intent subscriptionChanged = new Intent();
        subscriptionChanged.setAction(GpodRoid.BROADCAST_SUBSCRIPTION_CHANGE);
        sendBroadcast(subscriptionChanged);
    }
}
