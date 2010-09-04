package com.unitedcoders.android.gpodroid.activity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

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

public class ArchiveActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
				podcasts.add(filename);
			}
		}

//		podcasts.add("mygpodder.mp3");
		
//		ListView lv = new ListView(this);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, podcasts));
		
		
	}
	
	
	//play podcast on click
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		try {
//			mp.setDataSource("/sdcard/gpodder/mypod.mp3");
//			mp.prepare();
//			mp.start();
			File file = new File("/sdcard/gpodder/"+(String) getListView().getItemAtPosition(position));
//			PodcastPlayerActivity.setPodcastAndPlay(file);
			
			Intent intent = new Intent(ArchiveActivity.this,PlayerService.class);
			intent.putExtra("podcast", file.getPath());
			
			startService(intent);
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
