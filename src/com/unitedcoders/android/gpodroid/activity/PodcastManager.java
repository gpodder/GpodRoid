package com.unitedcoders.android.gpodroid.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.PodcastElement;
import com.unitedcoders.android.gpodroid.PodcastListAdapter;
import com.unitedcoders.android.gpodroid.Preferences;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.services.DownloadService;
import com.unitedcoders.android.gpodroid.tools.PodcastUtil;
import com.unitedcoders.gpodder.GpodderAPI;
import com.unitedcoders.gpodder.GpodderUpdates;

public class PodcastManager extends TabActivity implements OnClickListener {

    private TabHost mTabHost;

    // podcasts in archive
    private ArrayList<PodcastElement> podcastArchive = new ArrayList<PodcastElement>();
    private ListView lvAlbums;
    private ListView lvPodcasts;
    private ListView lvDownloads;

    public static PodcastListAdapter podcastAdapter;

    private ArrayAdapter adapter;
    private ViewFlipper sdcardFlipper;

    private Handler handler = new Handler();
    private PodcastListAdapter pcla;
    private Preferences pref;

    private boolean podcastSubmenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.podcast_manager);

        lvAlbums = (ListView) findViewById(R.id.lv_albums);
        lvPodcasts = (ListView) findViewById(R.id.lv_podcasts);
        lvDownloads = (ListView) findViewById(R.id.lv_downloads);

        sdcardFlipper = (ViewFlipper) findViewById(R.id.tabmgr_sdcard);
        sdcardFlipper.setOnClickListener(this);

        mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Archive").setContent(R.id.tabmgr_sdcard));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("New").setContent(R.id.tabmgr_newpodcasts));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("Subscriptions")
                .setContent(R.id.tabmgr_subscriptions));

        mTabHost.setCurrentTab(0);

        showArchive();
        showDownloads();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if (v == sdcardFlipper || v == lvAlbums) {
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

    private void showArchive() {
        podcastSubmenu = false;
        File podcastDir = new File("/sdcard/gpodder");
        String[] children = podcastDir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                String filename = children[i];
                PodcastElement pce = PodcastUtil.getPodcastInfo(new File("/sdcard/gpodder/" + filename));
                podcastArchive.add(pce);
            }
        }

        Iterator it = podcastArchive.iterator();
        ArrayList<String> albums = new ArrayList<String>();
        while (it.hasNext()) {
            String albs = ((PodcastElement) it.next()).getAlbum();
            if (albs != null && albs.length() > 0 && !albums.contains(albs)) {
                albums.add(albs);
            }
        }

        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, albums);
        lvAlbums.setAdapter(adapter);
        // lvAlbums.setOnClickListener(this);

        lvAlbums.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String album = (String) parent.getItemAtPosition(position);
                showPodcast(album);
                sdcardFlipper.showNext();
            }
        });

    }

    private void showPodcast(String album) {
        podcastSubmenu = true;

        // Toast.makeText(getApplicationContext(), "showing " + album, Toast.LENGTH_SHORT).show();

        PodcastListAdapter pcla = new PodcastListAdapter(getApplicationContext());
        ArrayList<PodcastElement> podcastElements = new ArrayList<PodcastElement>();

        try {
            Iterator it = podcastArchive.iterator();
            while (it.hasNext()) {
                PodcastElement pce = (PodcastElement) it.next();
                if (pce != null && pce.getAlbum() != null && pce.getAlbum().equals(album) && pce.getTitle() != null
                        && pce.getTitle().length() > 0) {
                    // podcastArchive.add(pce);
                    podcastElements.add(pce);
                }
            }

            // ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,
            // podcastNames);
            podcastAdapter = new PodcastListAdapter(getApplicationContext(), podcastElements);
            lvPodcasts.setAdapter(podcastAdapter);
            lvPodcasts.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getApplicationContext(), "starting media player", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Player.class);
                    Player.playbackQueue.add((PodcastElement) parent.getItemAtPosition(position));
                    startActivity(intent);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        pcla = new PodcastListAdapter(this);
        pcla.setShowCheckbox(true);
        downloadProcessing();

        Button downloadButton = (Button) findViewById(R.id.downloadButton);
        final Intent intent = new Intent(this, DownloadService.class);
        downloadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                List<PodcastElement> checkedItems = pcla.getCheckedItems();
                for (PodcastElement pce : checkedItems) {
                    DownloadService.downloadQueue.add(pce);
                }

                startService(intent);

            }
        });
    }

    private void downloadProcessing() {
        Thread thread = new Thread(null, doGetPodcastDownloadInfo, "Background");
        thread.start();
    }

    private Runnable doUpdateDownloadList = new Runnable() {
        public void run() {
            lvDownloads.setAdapter(pcla);

        }
    };

    private Runnable doGetPodcastDownloadInfo = new Runnable() {
        public void run() {
            backgroundPodcastInfoFetcher();
        }
    };

    private void backgroundPodcastInfoFetcher() {
        GpodderUpdates podcast = GpodderAPI.getDownloadList();

        if (podcast == null) {
            Log.e(GpodRoid.LOGTAG, "cant display downloads, got empty result");
            return;
        }

        // add items to download list
        for (int i = 0; i < podcast.getUpdates().size(); i++) {
            pcla.addItem(new PodcastElement(podcast.getUpdates().get(i).getTitle(), podcast.getUpdates().get(i)
                    .getUrl()));
        }

        handler.post(doUpdateDownloadList);
    }

}
