package com.unitedcoders.android.gpodroid.services;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Service for Playing the podcasts Is a singleton
 * 
 * @author Nico Heid
 * 
 */
public class PlayerService extends Service {

    private static MediaPlayer player;
    private PlayerServiceBinder playerServiceBinder = new PlayerServiceBinder();
    
    public class PlayerServiceBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return playerServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (player == null) {
            player = new MediaPlayer();
            Toast.makeText(this, "player created", Toast.LENGTH_LONG).show();
        }
        

    }

    @Override
    public void onDestroy() {
        player.stop();

        Toast.makeText(this, "player stopped", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "player started", Toast.LENGTH_LONG).show();

        nextPodcast();

    }

    public void nextPodcast() {
//        PodcastElement pce = GpodRoid.getNextPlayback();
//        File podcastFile = new File(pce.getFile());

        try {
            player.reset();
//            player.setDataSource(podcastFile.getAbsolutePath());
            player.prepare();
            player.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
//        Intent intent = new Intent(this, com.unitedcoders.android.gpodroid.activity.Player.PlayPodcastReceiver.class);
//        intent.putExtra("title", "helloworld");
//        sendBroadcast(intent);
    }

}
