package com.unitedcoders.android.gpodroid.activity;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.unitedcoders.android.gpodroid.PodcastElement;
import com.unitedcoders.android.gpodroid.R;

/**
 * Shows the MediaPlayer and controls
 * 
 * @author Nico Heid
 * 
 */
public class Player extends Activity {

    // private static File podcast;
    private static MediaPlayer mp = new MediaPlayer();

    // the playback queue
    public static ArrayList<PodcastElement> playbackQueue = new ArrayList<PodcastElement>();

    // private static boolean mpIsPlaying = false;
    TextView episode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerview);

        // Drawable d = GPodderActions
        // .LoadImageFromWebOperations("http://www.sysadminslife.com/wp-content/uploads/2009/07/tux.png");
        // ImageView image = (ImageView) findViewById(R.id.podcastPicture);
        // image.setImageDrawable(d);
        // image.setAdjustViewBounds(true);
        // image.setMaxHeight(200);
        // image.setMaxWidth(200);

        // getting state
        episode = (TextView) findViewById(R.id.EpisodeName);
        // title = (TextView) findViewById(R.id.PodcastTitle);

        // pretend state is empty

        final Button buttonStop = (Button) findViewById(R.id.buttonPlay);
        buttonStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    mp.pause();
                    buttonStop.setText("Play");
                }else{
                    mp.start();
                    buttonStop.setText("Pause");
                }
                

            }
        });

        

        play();

    }

    private void play() {

        if (playbackQueue.size() == 0) {
            return;
        }
        
        PodcastElement playingNow = playbackQueue.get(0);
        
        try {
            mp.reset();
            mp.setDataSource(playingNow.getFile());
            mp.prepare();
            mp.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        episode.setText(playingNow.getAlbum());

    }

    public void openPodcastManager(View v) {
        Intent intent = new Intent(this, PodcastManager.class);
        startActivity(intent);
    }

}
