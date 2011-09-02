package com.unitedcoders.gpodder;

public class GpodderSubscriptions {

    private String title;
    private String url;
    private String description;
    private String subscribers;
    private String subscribers_last_week;
    private String logo_url;
    private String website;
    private String mygpo_link;
    private String scaled_logo_url;
    
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
    public String getSubscribers_last_week() {
        return subscribers_last_week;
    }
    public void setSubscribers_last_week(String subscribers_last_week) {
        this.subscribers_last_week = subscribers_last_week;
    }
    public String getLogo_url() {
        return logo_url;
    }
    public void setLogo_url(String logoUrl) {
        logo_url = logoUrl;
    }
    public String getScaled_logo_url(){
    	return scaled_logo_url;
    }
    public void setScaled_logo_url(String scaledLogoUrl){
    	scaled_logo_url = scaledLogoUrl;
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