package com.unitedcoders.gpodder;

import java.util.List;

public class GpodderUpdates {

	private String timestamp;
	private List<GpodderSubscriptions> add;
	private List<GpodderSubscriptions> remove;
	private List<GpodderPodcast> updates;
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public List<GpodderSubscriptions> getAdd() {
		return add;
	}
	public void setAdd(List<GpodderSubscriptions> add) {
		this.add = add;
	}
	public List<GpodderSubscriptions> getRemove() {
		return remove;
	}
	public void setRemove(List<GpodderSubscriptions> remove) {
		this.remove = remove;
	}
	public List<GpodderPodcast> getUpdates() {
		return updates;
	}
	public void setUpdates(List<GpodderPodcast> updates) {
		this.updates = updates;
	}

	
}
