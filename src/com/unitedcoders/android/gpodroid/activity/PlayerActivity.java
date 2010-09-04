package com.unitedcoders.android.gpodroid.activity;

import java.io.File;
import java.io.IOException;

import com.unitedcoders.android.gpodroid.GPodderActions;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.R.id;
import com.unitedcoders.android.gpodroid.R.layout;
import com.unitedcoders.android.gpodroid.services.PlayerService;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;

// this shows the podcasts currently played and controls
public class PlayerActivity extends Activity{

	private static File podcast;
	private static MediaPlayer mp = new MediaPlayer();
	private static boolean mpIsPlaying = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playerview);
		
		Drawable d = GPodderActions.LoadImageFromWebOperations("http://www.sysadminslife.com/wp-content/uploads/2009/07/tux.png");
		ImageView image = (ImageView) findViewById(R.id.podcastPicture);
		image.setImageDrawable(d);
		image.setAdjustViewBounds(true);
		image.setMaxHeight(200);
		image.setMaxWidth(200);
		
		Button buttonStop = (Button) findViewById(R.id.buttonStop);
		buttonStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				PodcastPlayerActivity.mp.stop();
//				mpIsPlaying = false;
				Intent intent = new Intent(PlayerActivity.this,PlayerService.class);
				stopService(intent);
				
			}
		});
		
		final Button playButton = (Button) findViewById(R.id.buttonPlay);
		playButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
			}
		});
		
	}
	
	public static void setPodcastAndPlay(File podcast) throws IllegalArgumentException, IllegalStateException, IOException{
		PlayerActivity.podcast = podcast;
		mp.reset();
		mp.setDataSource(podcast.getPath());
		mp.prepare();
		mp.start();
		PlayerActivity.mpIsPlaying = true;
		
	}

}
