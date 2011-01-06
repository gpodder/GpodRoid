package com.unitedcoders.gpodder;

public class GpodderPodcast {

	private String title;
	private String podcast_url;
	private String podcast_title;
	private String description;
	private String website;
	private String mygpo_link;
	private String released;
	private String status;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPodcast_url() {
		return podcast_url;
	}

	public void setPodcast_url(String podcastUrl) {
		podcast_url = podcastUrl;
	}

	public String getPodcast_title() {
		return podcast_title;
	}

	public void setPodcast_title(String podcastTitle) {
		podcast_title = podcastTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getMygpo_link() {
		return mygpo_link;
	}

	public void setMygpo_link(String mygpoLink) {
		mygpo_link = mygpoLink;
	}

	public String getReleased() {
		return released;
	}

	public void setReleased(String released) {
		this.released = released;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
