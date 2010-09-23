package com.unitedcoders.android.gpodroid;

import java.util.ArrayList;

import com.unitedcoders.android.gpodroid.activity.AccountSettings;
import com.unitedcoders.android.gpodroid.activity.ArchiveActivity;
import com.unitedcoders.android.gpodroid.activity.ArchiveGroup;
import com.unitedcoders.android.gpodroid.activity.DownloadList;
import com.unitedcoders.android.gpodroid.activity.DownloadProgress;
import com.unitedcoders.android.gpodroid.activity.PlayerActivity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TabHost;

/**
 * Host of TabActivity and general info of state.
 * @author Nico Heid
 *
 */
public class GpodRoid extends TabActivity {

    private static ArrayList<PodcastElement> downloadQueue = new ArrayList<PodcastElement>();
    private static ArrayList<PodcastElement> playbackQueue = new ArrayList<PodcastElement>();

    /**
     * 
     * @return next Element to download or null if empty
     */
    public static PodcastElement getNextDownload() {

        if (downloadQueue.size() > 0) {
            PodcastElement element = downloadQueue.get(0);
            downloadQueue.remove(0);
            return element;
        }
        return null;
    }

    /**
     * Add a PocastElement to the download queue
     * 
     * @param element
     *            PodcastElement to add
     */
    public static void addDownloadQueue(PodcastElement element) {
        downloadQueue.add(element);

    }

    /**
     * 
     * @return next Element to download or null if empty
     */
    public static PodcastElement getNextPlayback() {

        if (playbackQueue.size() > 0) {
            PodcastElement element = downloadQueue.get(0);
            playbackQueue.remove(0);
            return element;
        }
        return null;
    }

    /**
     * Add a PocastElement to the download queue
     * 
     * @param element
     *            PodcastElement to add
     */
    public static void addPlaybackQueue(PodcastElement element) {
        playbackQueue.add(element);

    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        TabHost tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Player").setContent(
                new Intent(this, PlayerActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Archive").setContent(
                new Intent(this, ArchiveGroup.class)));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Updates").setContent(
                new Intent(this, DownloadList.class)));

        tabHost.setCurrentTab(0);

        // Intent intent = new Intent(this,DownloadProgress.class);
        // startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        // return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.setHeaderTitle("Menu");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, AccountSettings.class);
        startActivity(intent);
        return true;
    }
}