package com.unitedcoders.android.gpodroid.activity;

import java.io.File;
import java.io.IOException;

//import org.farng.mp3.MP3File;
//import org.farng.mp3.TagException;

import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;

import com.unitedcoders.android.gpodroid.GPodderActions;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.R.id;
import com.unitedcoders.android.gpodroid.R.layout;
import com.unitedcoders.android.gpodroid.services.PlayerService;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;

// this shows the podcasts currently played and controls
public class PlayerActivity extends Activity {

    private static File podcast;
    private static MediaPlayer mp = new MediaPlayer();
    private static boolean mpIsPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerview);

        Drawable d = GPodderActions
                .LoadImageFromWebOperations("http://www.sysadminslife.com/wp-content/uploads/2009/07/tux.png");
        ImageView image = (ImageView) findViewById(R.id.podcastPicture);
        image.setImageDrawable(d);
        image.setAdjustViewBounds(true);
        image.setMaxHeight(200);
        image.setMaxWidth(200);

        Button buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // PodcastPlayerActivity.mp.stop();
                // mpIsPlaying = false;
                Intent intent = new Intent(PlayerActivity.this, PlayerService.class);
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

    public static void setPodcastAndPlay(File podcast) throws IllegalArgumentException, IllegalStateException,
            IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
//        PlayerActivity.podcast = podcast;
//        mp.reset();
//        mp.setDataSource(podcast.getPath());
//
//        // MP3File mp3File = new MP3File(new File(podcast.getPath()));
//        File podcastFile = new File(podcast.getPath());
//        AudioFile f = AudioFileIO.read(podcastFile);
//        Artwork firstArtwork = f.getTag().getFirstArtwork();
//        String image2 = firstArtwork.getImageUrl();
//
//        mp.prepare();
//        mp.start();
//        PlayerActivity.mpIsPlaying = true;

    }

}
