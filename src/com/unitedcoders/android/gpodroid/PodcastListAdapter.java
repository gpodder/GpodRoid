package com.unitedcoders.android.gpodroid;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.unitedcoders.gpodder.GpodderPodcast;

public class PodcastListAdapter extends BaseAdapter {

    private boolean showCheckbox = false;

    public boolean isShowCheckbox() {
        return showCheckbox;
    }

    public void setShowCheckbox(boolean showCheckbox) {
        this.showCheckbox = showCheckbox;
    }

    private Context context;
    private List<GpodderPodcast> podcasts = new ArrayList<GpodderPodcast>();

    public PodcastListAdapter(Context context) {
        this.context = context;
    }

    public PodcastListAdapter(Context context, List<GpodderPodcast> podcasts) {
        this.context = context;
        this.podcasts = podcasts;
    }

    @Override
    public int getCount() {
        return podcasts.size();
    }

    @Override
    public Object getItem(int i) {
        return podcasts.get(i);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public void addItem(GpodderPodcast podcast) {
        podcasts.add(podcast);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (showCheckbox) {
            if (convertView == null) {
                view = new PodcastListView(context, podcasts.get(position));
            } else {
                view = (PodcastListView) convertView;
            }

        } else {
            if (convertView == null) {
                view = new PodcastListView(context, podcasts.get(position));
            } else {
                view = (PodcastListView) convertView;
            }
            
        }

        return view;

    }

    public List<GpodderPodcast> getCheckedItems() {
        List<GpodderPodcast> result = new ArrayList<GpodderPodcast>();
        for (GpodderPodcast pe : podcasts) {
            if (pe.isChecked()) {
                result.add(pe);
            }
        }

        return result;
    }

}
