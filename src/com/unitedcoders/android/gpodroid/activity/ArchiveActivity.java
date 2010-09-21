package com.unitedcoders.android.gpodroid.activity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.PodcastElement;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.R.layout;
import com.unitedcoders.android.gpodroid.services.PlayerService;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

public class ArchiveActivity extends ListActivity {

    public static ArrayList<PodcastElement> podcastArchive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        podcastArchive = new ArrayList<PodcastElement>();
        setContentView(R.layout.archiveview);

        ArrayList<String> podcasts = new ArrayList<String>();

        File externalStorage = Environment.getExternalStorageDirectory();

        File podcastDir = new File("/sdcard/gpodder");
        String[] children = podcastDir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                String filename = children[i];
                PodcastElement pce = getPodcastInfo(new File("/sdcard/gpodder/" + filename));
                podcastArchive.add(pce);
            }
        }

        // podcasts.add("mygpodder.mp3");
        Iterator it = podcastArchive.iterator();
        ArrayList<String> albums = new ArrayList<String>();
        while (it.hasNext()) {
            String albs = ((PodcastElement) it.next()).getAlbum();
            if (albs != null && albs.length() > 0 && !albums.contains(albs)) {
                albums.add(albs);
            }
        }

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, albums));

    }

    // play podcast on click
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);

        String album = (String) getListView().getItemAtPosition(position);
        Intent intent = new Intent(getApplicationContext(), ArchiveAlbums.class);
        intent.putExtra("album", album);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        View view = ArchiveGroup.group.getLocalActivityManager().startActivity("ShowPodcasts", intent).getDecorView();
        
        ArchiveGroup.group.setContentView(view);
        
    }

    private PodcastElement getPodcastInfo(File file) {
        // fill podcastinfo
        PodcastElement pce = new PodcastElement("", "");
        MP3File mp3 = null;
        try {
            mp3 = new MP3File(file);

            Tag tag = mp3.getID3v2Tag();

            String title = tag.getFirst(FieldKey.TITLE);
            String download = "";
            String album = tag.getFirst(FieldKey.ALBUM);

            pce = new PodcastElement(title, download);
            pce.setAlbum(album);
            pce.setFile(file.getAbsolutePath());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TagException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return pce;

    }

    
    
    
}
