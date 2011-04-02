package com.unitedcoders.android.gpodroid.activity;

import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.unitedcoders.android.gpodroid.*;
import com.unitedcoders.android.gpodroid.database.GpodDB;
import com.unitedcoders.android.gpodroid.services.DownloadService;
import roboguice.activity.RoboTabActivity;
import roboguice.inject.InjectView;

import java.io.File;
import java.util.List;

public class PodcastManager extends RoboTabActivity implements OnClickListener {

    private Context context;

    private TabHost mTabHost;

    // podcasts in archive
    @InjectView(R.id.lv_shows) private ListView lvShows;
    @InjectView(R.id.lv_podcasts) private ListView lvPodcasts;
    @InjectView(R.id.lv_downloads) private ListView lvDownloads;
//    private ListView lvDownloads;

    @InjectView(R.id.tabmgr_sdcard) private ViewFlipper sdcardFlipper;
    private PodcastListAdapter podcastAdapter;
    private PodcastListAdapter downloadAdapter;
    private ArrayAdapter showAdapter;


    private PodcastListAdapter pcla;
    private Preferences pref;

    private static String show = "";

    private boolean podcastSubmenu = false;

    public static boolean archiveDirty = false;
    private static IntentFilter filter = new IntentFilter();


    static {
        filter.addAction(GpodRoid.BROADCAST_SUBSCRIPTION_CHANGE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = getApplicationContext();
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.podcast_manager);

        sdcardFlipper.setOnClickListener(this);

        mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Podcasts").setContent(R.id.tabmgr_sdcard));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("New")
                .setContent(R.id.tabmgr_subscriptions));

        mTabHost.setCurrentTab(0);

//        showShows();
//        showDownloads();


    }

    @Override
    public void onResume() {
        context.registerReceiver(subscriptionChangeReceiver, filter);
        showShows(false);
        showDownloads();

        super.onResume();


    }

    @Override
    public void onPause() {
        context.unregisterReceiver(subscriptionChangeReceiver);
        super.onPause();
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


    private void showEpisodes(String show, boolean flipView) {

        GpodDB db = new GpodDB(getApplicationContext());
        List<Episode> shows = db.getEpisodes(show);

        podcastAdapter = new PodcastListAdapter(getApplicationContext(), shows);
        lvPodcasts.setAdapter(podcastAdapter);

        if (flipView) {
            podcastSubmenu = true;
            sdcardFlipper.showNext();
        }

        // download or play depending on if we have the show
        lvPodcasts.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ask for download if not present, or start playing
                final Episode episode = (Episode) parent.getItemAtPosition(position);
                final Intent downloadService = new Intent(getApplicationContext(), DownloadService.class);
                if (episode.getDownloaded() == 0) {
                    addToDownloadQueue(parent, position);
                } else {
                    Toast.makeText(getApplicationContext(), "starting podcast", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Player.class);
                    Player.pce = episode;
                    Player.switchPodcast = true;
                    startActivity(intent);
                }

            }
        });

        lvPodcasts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> av, View v, final int pos, long id) {

                final AlertDialog alert = new AlertDialog.Builder(lvPodcasts.getContext()).create();
                alert.setTitle("Delete");
                alert.setMessage("Delete Podcast?");
                alert.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Episode episode = (Episode) av.getItemAtPosition(pos);
                        File f = new File(episode.getFile());
                        f.delete();
                        episode.setDownloaded(0);
                        GpodDB db = new GpodDB(getApplicationContext());
                        db.updateEpisode(episode);

                    }
                });
                alert.setButton2("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert.cancel();
                    }
                });
                alert.show();
                return false;

            }

        });


    }

    private void showShows(boolean flipView) {

        if (flipView) {
            podcastSubmenu = false;
        }

        GpodDB db = new GpodDB(getApplicationContext());
        List<String> shows = db.getPodcasts();

        showAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, shows);
        lvShows.setAdapter(showAdapter);
        // lvAlbums.setOnClickListener(this);


        lvShows.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                show = (String) parent.getItemAtPosition(position);

                showEpisodes(show, true);

            }
        });


    }

    private void showDownloads() {

        GpodDB db = new GpodDB(getApplicationContext());
        List<Episode> shows = db.getDownloads();
        downloadAdapter = new PodcastListAdapter(getApplicationContext(), shows);
        lvDownloads.setAdapter(downloadAdapter);

        lvDownloads.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                addToDownloadQueue(parent, position);
            }
        });


    }

    /**
     * adds the show to the download queue
     *
     * @param parent
     * @param position
     */
    private void addToDownloadQueue(AdapterView<?> parent, int position) {
        final Episode episode = (Episode) parent.getItemAtPosition(position);
        final Intent downloadService = new Intent(getApplicationContext(), DownloadService.class);

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
    }

    @Override
    public void onClick(View view) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private BroadcastReceiver subscriptionChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(GpodRoid.LOGTAG, "received Broadcast, refreshing views");
            showDownloads();
            showShows(false);
            showEpisodes(show, false);
            podcastAdapter.notifyDataSetChanged();
//            downloadAdapter.notifyDataSetChanged();
            showAdapter.notifyDataSetChanged();

        }
    };


}
