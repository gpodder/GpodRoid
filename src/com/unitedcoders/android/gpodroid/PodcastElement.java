package com.unitedcoders.android.gpodroid;

import java.io.File;

public class PodcastElement implements Comparable<PodcastElement> {
	
	private String title = "";
	private String downloadUrl;
	private boolean checked = false;
	
	
	public String getDownloadurl() {
		return downloadUrl;
	}


	public void setDownloadurl(String downloadurl) {
		this.downloadUrl = downloadurl;
		
	}


	public PodcastElement(String title, String downloadUrl) {
		this.title = title;
		this.downloadUrl = downloadUrl;
		
	}
	
	
	@Override
	public int compareTo(PodcastElement another) {
		// TODO Auto-generated method stub
		return 0;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public boolean isChecked() {
		return checked;
	}


	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	

}
