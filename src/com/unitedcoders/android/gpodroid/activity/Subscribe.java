package com.unitedcoders.android.gpodroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.services.UpdateService;
import com.unitedcoders.gpodder.GpodderAPI;
import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.HashMap;

public class Subscribe extends RoboListActivity implements View.OnClickListener {

    String subscribeTo;
    ArrayList<String> top25;
    HashMap<String, String> top25hm;

    @InjectView(R.id.btn_podcast_search) ImageButton btnPodcastSearch;
    @InjectView(R.id.et_podcast_search) EditText etPodcastSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe);

        top25hm = GpodderAPI.getTopSubscriptions();
        top25 = new ArrayList<String>(top25hm.keySet());
        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, top25));
        registerForContextMenu(getListView());

        btnPodcastSearch.setOnClickListener(this);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("subscribe");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final String feed = (String) getListView().getItemAtPosition(info.position);
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
                top25hm = GpodderAPI.searchFeeds("search");
                top25 = new ArrayList<String>(top25hm.keySet());
                this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, top25));
                break;
        }
    }
}
