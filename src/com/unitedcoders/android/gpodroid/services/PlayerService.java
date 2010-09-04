package com.unitedcoders.android.gpodroid.services;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class PlayerService extends Service {

	private static MediaPlayer player;
	private static final String podcast = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (player == null) {
			player = new MediaPlayer();
			Toast.makeText(this, "player created", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		player.stop();

		Toast.makeText(this, "player stopped", Toast.LENGTH_LONG).show();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Toast.makeText(this, "player started", Toast.LENGTH_LONG).show();
		String podcast = (String) intent.getExtras().get("podcast");
		try {
			player.reset();
			player.setDataSource(podcast);
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
