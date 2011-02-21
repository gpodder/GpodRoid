//package com.unitedcoders.android.gpodroid;
//
//import java.io.File;
//
//
//public class PodcastElement implements Comparable<PodcastElement> {
//
//    private String title = "";
//    private String downloadUrl;
//    private String album;
//    private String file;
//    private int seekPosition = 0;
//
//
//    public PodcastElement(String title, String downloadUrl) {
//        this.title = title;
//        this.downloadUrl = downloadUrl;
//
//    }
//
//    public PodcastElement(File file) {
//
//    }
//
//    public int getSeekPosition() {
//        return seekPosition;
//    }
//
//    public void setSeekPosition(int seekPosition) {
//        this.seekPosition = seekPosition;
//    }
//
//    public String getFile() {
//        return file;
//    }
//
//    public void setFile(String file) {
//        this.file = file;
//    }
//
//    public String getAlbum() {
//        return album;
//    }
//
//    public void setAlbum(String album) {
//        this.album = album;
//    }
//
//    private boolean checked = false;
//
//    public String getDownloadurl() {
//        return downloadUrl;
//    }
//
//    public void setDownloadurl(String downloadurl) {
//        this.downloadUrl = downloadurl;
//
//    }
//
//
//    @Override
//    public int compareTo(PodcastElement another) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public boolean isChecked() {
//        return checked;
//    }
//
//    public void setChecked(boolean checked) {
//        this.checked = checked;
//    }
//
//}
