package com.unitedcoders.android.gpodroid.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.unitedcoders.android.gpodroid.*;
import com.unitedcoders.android.gpodroid.services.UpdateService;
import com.unitedcoders.android.gpodroid.tools.Tools;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * Shows the MediaPlayer and controls
 *
 * @author Nico Heid
 */
public class Player extends RoboActivity implements OnClickListener, SeekBar.OnSeekBarChangeListener {

    final Handler handler = new Handler();
    private int currentPosition;

    // private static File podcast;
    private static MediaPlayer mp = new MediaPlayer();

    // Element playing
    public static Episode pce;
    private int seekPosition = 0;

    // podcast information
    @InjectView(R.id.tv_podcast_title)
    private TextView tvTitle;
    @InjectView(R.id.tv_episode_name)
    private TextView tvEpisode;
    @InjectView(R.id.tv_total_time)
    private TextView tvTotalTime;
    @InjectView(R.id.tv_position_time)
    private TextView tvPositionTime;

    // cover art
    @InjectView(R.id.iv_cover)
    private ImageView ivCover;

    // buttons
    @InjectView(R.id.btn_forward)
    private ImageButton btnForward;
    @InjectView(R.id.btn_backward)
    private ImageButton btnBackward;
    @InjectView(R.id.btn_play)
    private ImageButton btnPlay;
    @InjectView(R.id.bar_playback)
    private SeekBar barProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerview);

        startService(new Intent(getApplicationContext(), UpdateService.class));

        SharedPreferences settings = getApplicationContext().getSharedPreferences("PLAYBACKSTATE", 0);
//        String file = settings.getString("FILE", "");
//        seekPosition = settings.getInt("SEEKPOSITION", 0);

//        if (!file.equals("")) {
////            pce = PodcastUtil.getPodcastInfo(new File(file));
//        }


        // load preferences
        GpodRoid.prefs = Preferences.getPreferences(getApplicationContext());


        barProgress.setOnSeekBarChangeListener(this);
        btnForward.setOnClickListener(this);
        btnBackward.setOnClickListener(this);

        btnPlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (pce == null) {
                    Intent intent = new Intent(getApplicationContext(), PodcastManager.class);
                    startActivity(intent);
                }

                if (mp.isPlaying()) {
                    mp.pause();
                    btnPlay.setImageResource(android.R.drawable.ic_media_play);

                } else {
                    mp.start();
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });


        play();

    }


    private void play() {

        if (pce == null) {
            return;
        }


        File SDCardRoot = Environment.getExternalStorageDirectory();
        String storageLocation = SDCardRoot.getAbsolutePath() + "/gpodroid/covers";
        Bitmap bm = BitmapFactory.decodeFile(storageLocation + "/" + pce.getPodcast_title().trim());

        ivCover.setImageBitmap(bm);

        if (pce == null) {
            return;
        }

        try {
            mp.reset();
            mp.setDataSource(pce.getFile());
            mp.prepare();
            mp.seekTo(seekPosition);
            mp.start();

            progressReader();

            btnPlay.setImageResource(android.R.drawable.ic_media_pause);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvEpisode.setText(pce.getTitle());
        tvTitle.setText(pce.getPodcast_title());
        tvTotalTime.setText(Tools.makeTimeString(this, mp.getDuration()/1000));

    }

    public void openPodcastManager(View v) {
        Intent intent = new Intent(this, PodcastManager.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {


        if (view == btnBackward) {
            seek(-mp.getDuration() / 50);
        } else if (view == btnForward) {
            seek(mp.getDuration() / 50);
        }


    }

    private void seek(int seek) {
        int position = mp.getCurrentPosition();
        mp.seekTo(position + seek);
    }

    final Runnable changeProgress = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    private void updateProgress() {
        barProgress.setProgress(currentPosition);
        tvPositionTime.setText(Tools.makeTimeString(this, currentPosition / 1000));
    }


    protected void progressReader() {
        Thread t = new Thread() {
            public void run() {
                int total = mp.getDuration();
                barProgress.setMax(total);
                while (mp != null && currentPosition < total) {
                    try {
                        Thread.sleep(1000);
                        currentPosition = mp.getCurrentPosition();
                        handler.post(changeProgress);
                    } catch (InterruptedException e) {
                        return;
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        };
        t.start();
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
