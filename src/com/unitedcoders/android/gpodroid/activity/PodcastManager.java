package com.unitedcoders.android.gpodroid.activity;

import java.util.List;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.unitedcoders.android.gpodroid.*;
import com.unitedcoders.android.gpodroid.database.GpodDB;
import com.unitedcoders.android.gpodroid.services.DownloadService;
import com.unitedcoders.gpodder.GpodderAPI;
import com.unitedcoders.gpodder.GpodderPodcast;
import com.unitedcoders.gpodder.GpodderUpdates;

public class PodcastManager extends TabActivity implements OnClickListener {

    private Context context;

    private TabHost mTabHost;

    // podcasts in archive
    private ListView lvShows;
    private ListView lvPodcasts;
//    private ListView lvDownloads;

    private ViewFlipper sdcardFlipper;
    private PodcastListAdapter podcastAdapter;
    private ArrayAdapter showAdapter;

    private Handler handler = new Handler();
    private PodcastListAdapter pcla;
    private Preferences pref;

    private boolean podcastSubmenu = false;

    public static boolean archiveDirty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = getApplicationContext();
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.podcast_manager);

        lvShows = (ListView) findViewById(R.id.lv_shows);
        lvPodcasts = (ListView) findViewById(R.id.lv_podcasts);

        sdcardFlipper = (ViewFlipper) findViewById(R.id.tabmgr_sdcard);
        sdcardFlipper.setOnClickListener(this);

        mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Podcasts").setContent(R.id.tabmgr_sdcard));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("Subscriptions")
                .setContent(R.id.tabmgr_subscriptions));

        mTabHost.setCurrentTab(0);

//        downloadProcessing();
        showShows();
//        showDownloads();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

//        if (v == sdcardFlipper || v == lvShows) {
//            sdcardFlipper.showNext();
//        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // if we're in the podcast view bring us back to archive view
        // so it feels like regular behavior
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (podcastSubmenu) {
                sdcardFlipper.showNext();
                podcastSubmenu = !podcastSubmenu;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return true;
    }


    private void showEpisodes(String show) {
        podcastSubmenu = true;
//

        GpodDB db = new GpodDB(getApplicationContext());
        List<Episode> shows = db.getEpisodes(show);

        podcastAdapter = new PodcastListAdapter(getApplicationContext(), shows);
        lvPodcasts.setAdapter(podcastAdapter);
        sdcardFlipper.showNext();

        lvPodcasts.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ask for download if not present, or start playing
                final Episode episode = (Episode) parent.getItemAtPosition(position);
                final Intent downloadService = new Intent(getApplicationContext(), DownloadService.class);
                if (episode.getDownloaded() == 0) {

                    // get confirmation for download
                    final AlertDialog alert = new AlertDialog.Builder(lvPodcasts.getContext()).create();
                    alert.setTitle("Download");
                    alert.setMessage("Download Podcast now?");
                    alert.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DownloadService.downloadQueue.add(episode);
                            startService(downloadService);
                        }
                    });
                    alert.setButton2("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alert.cancel();
                        }
                    });
                    alert.show();


                } else {
                    Toast.makeText(getApplicationContext(), "starting podcast", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Player.class);
                    Player.pce = episode;
                    startActivity(intent);
                }

            }
        });

    }

    private void showShows() {
        podcastSubmenu = false;

        GpodDB db = new GpodDB(getApplicationContext());
        List<String> shows = db.getPodcasts();

        showAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, shows);
        lvShows.setAdapter(showAdapter);
        // lvAlbums.setOnClickListener(this);


        lvShows.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String show = (String) parent.getItemAtPosition(position);
                showEpisodes(show);

            }
        });


    }

    private void showDownloads() {

        // get preferences
        pref = Preferences.getPreferences(getApplicationContext());

        if (pref.getUsername().equals("") || pref.getPassword().equals("") || pref.getDevice().equals("")) {

            Toast toast = Toast.makeText(getApplicationContext(), "please enter your settings first",
                    Toast.LENGTH_SHORT);
            toast.show();

            startActivity(new Intent(getApplicationContext(), AccountSettings.class));

            return;

        }

        downloadProcessing();

    }

    private void downloadProcessing() {
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
