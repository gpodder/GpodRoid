package com.unitedcoders.android.gpodroid.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.unitedcoders.android.gpodroid.Episode;
import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.Preferences;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.services.UpdateService;
import com.unitedcoders.gpodder.GpodderPodcast;

/**
 * Shows the MediaPlayer and controls
 *
 * @author Nico Heid
 */
public class Player extends Activity implements OnClickListener, Runnable, SeekBar.OnSeekBarChangeListener {

    // private static File podcast;
    private static MediaPlayer mp = new MediaPlayer();

    // Element playing
    public static Episode pce;
    private int seekPosition = 0;

    private TextView title;
    private TextView episode;
    private TextView tvTotalTime;
    private TextView tvPositionTime;

    // Buttons
    private ImageButton forward;
    private ImageButton backward;
    private ImageButton buttonStop;

    private SeekBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerview);

        startService(new Intent(getApplicationContext(), UpdateService.class));

        SharedPreferences settings = getApplicationContext().getSharedPreferences("PLAYBACKSTATE", 0);
        String file = settings.getString("FILE", "");
        seekPosition = settings.getInt("SEEKPOSITION", 0);

//        if (!file.equals("")) {
////            pce = PodcastUtil.getPodcastInfo(new File(file));
//        }


        // load preferences
        GpodRoid.prefs = Preferences.getPreferences(getApplicationContext());

        // Drawable d = GPodderActions
        // .LoadImageFromWebOperations("http://www.sysadminslife.com/wp-content/uploads/2009/07/tux.png");
        // ImageView image = (ImageView) findViewById(R.id.podcastPicture);
        // image.setImageDrawable(d);
        // image.setAdjustViewBounds(true);
        // image.setMaxHeight(200);
        // image.setMaxWidth(200);

        // getting state
        episode = (TextView) findViewById(R.id.EpisodeName);
        title = (TextView) findViewById(R.id.PodcastTitle);

        forward = (ImageButton) findViewById(R.id.btn_forward);
        backward = (ImageButton) findViewById(R.id.btn_backward);
        buttonStop = (ImageButton) findViewById(R.id.buttonPlay);

        progress = (SeekBar) findViewById(R.id.bar_playback);
        progress.setOnSeekBarChangeListener(this);

        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        tvPositionTime = (TextView) findViewById(R.id.tv_position_time);

        forward.setOnClickListener(this);
        backward.setOnClickListener(this);

        buttonStop.setBackgroundResource(R.drawable.play);

        buttonStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (pce == null) {
                    Intent intent = new Intent(getApplicationContext(), PodcastManager.class);
                    startActivity(intent);
                }

                if (mp.isPlaying()) {
                    mp.pause();
                    buttonStop.setBackgroundResource(R.drawable.play);

                } else {
                    mp.start();
                    buttonStop.setBackgroundResource(R.drawable.pause);
                }
            }
        });


        play();

    }


    private void play() {

        if (pce == null) {
            return;
        }

        try {
            mp.reset();
            mp.setDataSource(pce.getFile());
            mp.prepare();
            mp.seekTo(seekPosition);
            mp.start();
            new Thread(this).start();
            buttonStop.setBackgroundResource(R.drawable.pause);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        episode.setText(pce.getTitle());
        title.setText(pce.getPodcast_title());

    }

    public void openPodcastManager(View v) {
        Intent intent = new Intent(this, PodcastManager.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {


        if (view == backward) {
            seek(-mp.getDuration() / 50);
        } else if (view == forward) {
            seek(mp.getDuration() / 50);
        }


    }

    private void seek(int seek) {
        int position = mp.getCurrentPosition();
        mp.seekTo(position + seek);
    }

    @Override
    public void run() {
        int currentPosition = 0;
        int total = mp.getDuration();
        progress.setMax(total);
        while (mp != null && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mp.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }
            progress.setProgress(currentPosition);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mp.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
