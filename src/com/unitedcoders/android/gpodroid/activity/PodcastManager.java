package com.unitedcoders.android.gpodroid.activity;

import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.PodcastListAdapter;
import com.unitedcoders.android.gpodroid.Preferences;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.database.GpodDB;
import com.unitedcoders.android.gpodroid.services.DownloadService;
import com.unitedcoders.gpodder.GpodderAPI;
import com.unitedcoders.gpodder.GpodderPodcast;
import com.unitedcoders.gpodder.GpodderUpdates;

public class PodcastManager extends TabActivity implements OnClickListener {

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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.podcast_manager);

        lvShows = (ListView) findViewById(R.id.lv_shows);
        lvPodcasts = (ListView) findViewById(R.id.lv_podcasts);

        sdcardFlipper = (ViewFlipper) findViewById(R.id.tabmgr_sdcard);
        sdcardFlipper.setOnClickListener(this);

        mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Podcasts").setContent(R.id.tabmgr_sdcard));
//        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("New").setContent(R.id.tabmgr_newpodcasts));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("Subscriptions")
                .setContent(R.id.tabmgr_subscriptions));

        mTabHost.setCurrentTab(0);

        downloadProcessing();
        showShows();
//        showDownloads();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if (v == sdcardFlipper || v == lvShows) {
            sdcardFlipper.showNext();
        }

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


    private void showPodcasts(String show) {
        podcastSubmenu = true;
//

        GpodDB db = new GpodDB(getApplicationContext());
        List<GpodderPodcast> shows = db.getPodcasts(show);

//        podcastAdapter = new  ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, shows);
        podcastAdapter = new PodcastListAdapter(getApplicationContext(), shows);
        lvPodcasts.setAdapter(podcastAdapter);
        sdcardFlipper.showNext();
//        lvAlbums.setOnClickListener(this);

        lvPodcasts.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String album = (String) parent.getItemAtPosition(position);
//                showPodcast(album);
//                sdcardFlipper.showNext();

                Toast.makeText(getApplicationContext(), "starting podcast", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Player.class);
                Player.pce = (GpodderPodcast) parent.getItemAtPosition(position);
                startActivity(intent);

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
                showPodcasts(show);

            }
        });
//
//        // Toast.makeText(getApplicationContext(), "showing " + album, Toast.LENGTH_SHORT).show();
//
//        PodcastListAdapter pcla = new PodcastListAdapter(getApplicationContext());
//        ArrayList<PodcastElement> podcastElements = new ArrayList<PodcastElement>();
//
//        try {
//            Iterator it = podcastArchive.iterator();
//            while (it.hasNext()) {
//                PodcastElement pce = (PodcastElement) it.next();
//                if (pce != null && pce.getAlbum() != null && pce.getAlbum().equals(album) && pce.getTitle() != null
//                        && pce.getTitle().length() > 0) {
//                    // podcastArchive.add(pce);
//                    podcastElements.add(pce);
//                }
//            }
//
//            // ArrayAdapter podcastAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,
//            // podcastNames);
//            podcastAdapter = new PodcastListAdapter(getApplicationContext(), podcastElements);
//            lvPodcasts.setAdapter(podcastAdapter);
//            lvPodcasts.setOnItemClickListener(new OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
//                    Toast.makeText(getApplicationContext(), "starting podcast", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(), Player.class);
//                    PodcastElement pce = (PodcastElement) parent.getItemAtPosition(position);
//                    Player.pce = pce;
//
//                    // set new playback state
//                    SharedPreferences settings = getApplicationContext().getSharedPreferences("PLAYBACKSTATE", 0);
//                    SharedPreferences.Editor editor = settings.edit();
//
//                    editor.putString("FILE", pce.getFile());
//                    editor.putInt("SEEKPOSITION", 0);
//                    editor.commit();
//
//
//                    startActivity(intent);
//
//                }
//            }
//    );
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

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

//        pcla = new PodcastListAdapter(this);
//        pcla.setShowCheckbox(true);
        downloadProcessing();

//        // show what we have from db
//        GpodDB db = new GpodDB(getApplicationContext());
//        List<String> podcasts = db.getPodcasts();
//
//        ArrayAdapter downloads = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, podcasts);
//        lvDownloads.setAdapter(downloads);


//        Button downloadButton = (Button) findViewById(R.id.downloadButton);
//        final Intent intent = new Intent(this, DownloadService.class);
//        downloadButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
////                List<PodcastElement> checkedItems = pcla.getCheckedItems();
////                for (PodcastElement pce : checkedItems) {
////                    DownloadService.downloadQueue.add(pce);
////                }
//
//                startService(intent);
//
//            }
//        });
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


        // add items to download list
//        for (int i = 0; i < podcast.getUpdates().size(); i++) {
////            pcla.addItem(new PodcastElement(podcast.getUpdates().get(i).getTitle(), podcast.getUpdates().get(i)
////                    .getUrl()));
//
//
//        }

        handler.post(doUpdateDownloadList);
    }

}
