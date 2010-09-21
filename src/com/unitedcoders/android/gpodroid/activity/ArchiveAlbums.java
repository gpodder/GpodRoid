package com.unitedcoders.android.gpodroid.activity;

import java.util.ArrayList;
import java.util.Iterator;

import com.unitedcoders.android.gpodroid.PodcastElement;
import com.unitedcoders.android.gpodroid.PodcastListAdapter;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.services.PlayerService;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ArchiveAlbums extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archiveview);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Intent intent = getIntent();
        String album = intent.getExtras().getString("album");

        PodcastListAdapter pcla = new PodcastListAdapter(getApplicationContext());
        pcla.setShowCheckbox(false);

        try {
            Iterator it = ArchiveActivity.podcastArchive.iterator();
            ArrayList<String> podcasts = new ArrayList<String>();
            while (it.hasNext()) {
                PodcastElement pce = (PodcastElement) it.next();
                if (pce != null && pce.getAlbum() != null && pce.getAlbum().equals(album) && pce.getTitle() != null
                        && pce.getTitle().length() > 0) {
                    pcla.addItem(pce);
                }
            }

            // setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, podcasts));
            setListAdapter(pcla);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        PodcastElement pce = (PodcastElement) l.getItemAtPosition(position);
        Intent intent = new Intent(getApplicationContext(), PlayerService.class);
        intent.putExtra("podcast", pce.getFile());
        startService(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(getApplicationContext(), ArchiveActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            View view = ArchiveGroup.group.getLocalActivityManager().startActivity("ShowPodcasts", intent)
                    .getDecorView();
            ArchiveGroup.group.setContentView(view);
        }

        return true;
    }

}
