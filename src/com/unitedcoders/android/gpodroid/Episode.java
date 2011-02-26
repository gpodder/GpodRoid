package com.unitedcoders.android.gpodroid;


import com.unitedcoders.gpodder.GpodderPodcast;

/**
 *
 */
public class Episode extends GpodderPodcast {

    int downloaded;
    int played;
    String file;


    public Episode(GpodderPodcast podcast) {
        this.downloaded = downloaded;
        this.played = played;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
