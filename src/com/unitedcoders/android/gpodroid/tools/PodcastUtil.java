package com.unitedcoders.android.gpodroid.tools;

import java.io.File;
import java.io.IOException;

import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

import android.util.Log;

import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.PodcastElement;

public class PodcastUtil {

    /**
     * Returns a PodcastElement with id3 infos from a file
     * 
     * @param file
     * @return PodcastElement
     */
    public static PodcastElement getPodcastInfo(File file) {
        PodcastElement pce = new PodcastElement("", "");

        try {
            MusicMetadataSet id3set = new MyID3().read(file);
            MusicMetadata meta = (MusicMetadata) id3set.getSimplified();
            pce.setAlbum(meta.getAlbum());
            pce.setTitle(meta.getSongTitle());
            pce.setFile(file.getAbsolutePath());

        } catch (IOException e) {
            Log.e(GpodRoid.LOGTAG, "error reading mp3info", e);
        }

        return pce;

    }
}
