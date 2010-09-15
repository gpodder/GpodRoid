package com.unitedcoders.android.gpodroid;

import java.util.ArrayList;

import com.unitedcoders.android.gpodroid.activity.AccountSettings;
import com.unitedcoders.android.gpodroid.activity.ArchiveActivity;
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

public class GpodRoid extends TabActivity {

    private static ArrayList<PodcastElement> downloadQueue = new ArrayList<PodcastElement>();

    public static PodcastElement getNextDownload() {

        PodcastElement element = downloadQueue.get(0);
        if (element != null) {
            downloadQueue.remove(0);
        }
        return element;
    }

    public static void addDownloadQueue(PodcastElement element) {
        downloadQueue.add(element);

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
                new Intent(this, ArchiveActivity.class)));
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