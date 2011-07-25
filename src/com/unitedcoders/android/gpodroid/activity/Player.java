package com.unitedcoders.android.gpodroid.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.unitedcoders.android.gpodroid.*;
import com.unitedcoders.android.gpodroid.database.GpodDB;
import com.unitedcoders.android.gpodroid.services.UpdateService;
import com.unitedcoders.android.gpodroid.tools.Tools;
import com.unitedcoders.gpodder.GpodderAPI;
import roboguice.inject.InjectView;

import java.io.File;
import java.io.IOException;

/**
 * Shows the MediaPlayer and controls
 *
 * @author Nico Heid
 */
public class Player extends RoboActivityDefaultMenu implements OnClickListener, SeekBar.OnSeekBarChangeListener {

    final Handler handler = new Handler();
    private static int playbackPosition;

    public static MediaPlayer mp;

    // Element playing
    public static Episode pce;
    public static boolean switchPodcast;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerview);

        // set the GpodderAPI context here so that we will never have to
        // access it again, otherwise there are calls over
        GpodderAPI.context = getApplicationContext();

        startService(new Intent(getApplicationContext(), UpdateService.class));

        if (!switchPodcast) {
            loadPlaybackState();
        } else {
            playbackPosition = 0;
            switchPodcast = false;
        }

        barProgress.setOnSeekBarChangeListener(this);
        btnForward.setOnClickListener(this);
        btnBackward.setOnClickListener(this);
        btnPlay.setOnClickListener(this);

        if (pce != null) {
            play();
        }
    }


    @Override
    protected void onDestroy() {

        savePlaybackState();
        super.onDestroy();

    }


    private void play() {

        if (pce == null) {
            return;
        }


        try {
            ivCover.setImageResource(R.drawable.cd);
            File SDCardRoot = Environment.getExternalStorageDirectory();
            String storageLocation = SDCardRoot.getAbsolutePath() + "/gpodroid/covers";
            File cover = new File(storageLocation + "/" + pce.getPodcast_title().trim());
            Bitmap bm;
            if (cover.exists()) {
                bm = BitmapFactory.decodeFile(storageLocation + "/" + pce.getPodcast_title().trim());
                ivCover.setImageBitmap(bm);
            }
        } catch (Exception e) {
            Log.d(GpodRoid.LOGTAG, "cannot set cover", e);
            ivCover.setImageResource(R.drawable.cd);

        }


        if (pce == null) {
            return;
        }

        try {
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(pce.getFile());
            mp.prepare();
            mp.start();
            mp.seekTo(playbackPosition);

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
        tvTotalTime.setText(Tools.makeTimeString(this, mp.getDuration() / 1000));

    }

    public void openPodcastManager(View v) {
        Intent intent = new Intent(this, PodcastManager.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (pce == null) {
            Intent intent = new Intent(getApplicationContext(), PodcastManager.class);
            startActivity(intent);
        }


        switch (view.getId()) {
            case R.id.btn_backward:
                seek(-mp.getDuration() / 50);
                break;
            case R.id.btn_forward:
                seek(mp.getDuration() / 50);
                break;
            case R.id.btn_play:
                if (mp == null) {
                    play();
                }
                if (mp.isPlaying()) {
                    savePlaybackState();
                    btnPlay.setImageResource(android.R.drawable.ic_media_play);
                    mp.pause();
                } else {
                    mp.start();
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    progressReader();
                }
                break;
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
        if (mp != null) {
            int position = mp.getCurrentPosition();
            barProgress.setProgress(position);
            tvPositionTime.setText(Tools.makeTimeString(this, position / 1000));
        }
    }


    protected void progressReader() {
        Thread t = new Thread() {
            public void run() {
                int total = mp.getDuration();
                barProgress.setMax(total);
                while (mp != null && mp.isPlaying() && playbackPosition < total) {
                    try {
                        Thread.sleep(1000);
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
            playbackPosition = progress;
            if (mp != null && mp.isPlaying()) {
                mp.seekTo(progress);
            }
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

    private void savePlaybackState() {
        if (pce != null) {
            // save playback state
            SharedPreferences settings = getApplicationContext().getSharedPreferences("PLAYBACKSTATE", 0);
            SharedPreferences.Editor editor = settings.edit();

            editor.putInt("PCE", pce.getId());
            editor.putInt("SEEKPOSITION", mp.getCurrentPosition());
            editor.commit();
        }
    }

    private void loadPlaybackState() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("PLAYBACKSTATE", 0);
        int id = settings.getInt("PCE", 0);
        playbackPosition = settings.getInt("SEEKPOSITION", 0);

        if (id > 0) {
            GpodDB db = new GpodDB(getApplicationContext());
            pce = db.getEpisode(id);
        }
    }

    //TODO release mediaplayer if its not in use for a while
    // workaround for http://code.google.com/p/android/issues/detail?id=4124


}
