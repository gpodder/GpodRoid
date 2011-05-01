package com.unitedcoders.android.gpodroid.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.services.UpdateService;
import com.unitedcoders.gpodder.GpodderAPI;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.HashMap;

public class Subscribe extends RoboActivity implements View.OnClickListener {

    Context context;
    @InjectView(R.id.lv_search_results) ListView lvSearchResults;
    ProgressDialog searching;


    private final Handler handler = new Handler();
    ArrayList<String> top25;
    HashMap<String, String> top25hm;

    @InjectView(R.id.btn_podcast_search) ImageButton btnPodcastSearch;
    @InjectView(R.id.et_podcast_search) EditText etPodcastSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe);

        searching = ProgressDialog.show(this, "Searching ...", "digging through podcasts", true, false);
        displayTopPodcasts();

        btnPodcastSearch.setOnClickListener(this);
        registerForContextMenu(lvSearchResults);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("subscribe");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final String feed = (String) lvSearchResults.getItemAtPosition(info.position);
        Log.d(GpodRoid.LOGTAG, "subscribing to " + top25hm.get(feed));

        new Thread(new Runnable() {
            @Override
            public void run() {
                GpodderAPI.addSubcription(top25hm.get(feed));
                startService(new Intent(getApplicationContext(), UpdateService.class));
            }
        }).start();


        return super.onContextItemSelected(item);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_podcast_search:
                searching = ProgressDialog.show(this, "Searching ...", "digging through podcasts", true, false);
                searchPodcasts(etPodcastSearch.getText().toString());
                break;
        }
    }


    private void searchPodcasts(final String searchTerm) {
        Thread t = new Thread() {
            public void run() {
                top25hm = GpodderAPI.searchFeeds(searchTerm);
                top25 = new ArrayList<String>(top25hm.keySet());
                handler.post(displayResults);
            }
        };
        t.start();
    }

    private void displayTopPodcasts() {
        Thread t = new Thread() {
            public void run() {
                top25hm = GpodderAPI.getTopSubscriptions();
                top25 = new ArrayList<String>(top25hm.keySet());
                handler.post(displayResults);
            }
        };
        t.start();

    }

    final Runnable displayResults = new Runnable() {
        @Override
        public void run() {
            displayResultsinUI();
        }
    };

    private void displayResultsinUI() {
        lvSearchResults.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, top25));
        searching.dismiss();
    }
}
