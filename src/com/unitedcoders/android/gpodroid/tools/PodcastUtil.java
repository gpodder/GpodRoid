package com.unitedcoders.android.gpodroid.tools;

import java.io.File;
import java.io.IOException;

import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.unitedcoders.android.gpodroid.PodcastElement;

public class PodcastUtil {

    /**
     * Returns a PodcastElement with id3 infos from a file
     * @param file
     * @return PodcastElement
     */
    public static PodcastElement getPodcastInfo(File file) {
        // fill podcastinfo
        PodcastElement pce = new PodcastElement("", "");
        MP3File mp3 = null;

        try {
            MusicMetadataSet id3set = new MyID3().read(file);
            MusicMetadata meta = (MusicMetadata) id3set.getSimplified();
            pce.setAlbum(meta.getAlbum());
            pce.setTitle(meta.getSongTitle());
            pce.setFile(file.getAbsolutePath());
            return pce;
            
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        try {
            mp3 = new MP3File(file);

            Tag tag = mp3.getID3v2Tag();

            String title = tag.getFirst(FieldKey.TITLE);
            String download = "";
            String album = tag.getFirst(FieldKey.ALBUM);

            pce = new PodcastElement(title, download);
            pce.setAlbum(album);
            pce.setFile(file.getAbsolutePath());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TagException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return pce;

    }
}
