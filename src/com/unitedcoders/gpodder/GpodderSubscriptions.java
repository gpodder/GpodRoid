package com.unitedcoders.gpodder;

public class GpodderSubscriptions {

	private String title;
	private String url;
	private String description;
	private String subscribers;
	private String logo_url;
	private String website;
	private String mygpo_link;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(String subscribers) {
		this.subscribers = subscribers;
	}
	public String getLogo_url() {
		return logo_url;
	}
	public void setLogo_url(String logoUrl) {
		logo_url = logoUrl;
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

}
