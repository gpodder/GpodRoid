package com.unitedcoders.android.gpodroid;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.unitedcoders.gpodder.GpodderPodcast;

public class PodcastListAdapter extends BaseAdapter {

    private List<Episode> episodes;
    private Context context;

//    public PodcastListAdapter(Context context) {
//        this.context = context;
//    }

    public PodcastListAdapter(Context context, List<Episode> episodes) {
        this.context = context;
        this.episodes = episodes;
    }

    @Override
    public int getCount() {
        return episodes.size();
    }

    @Override
    public Object getItem(int i) {
        return episodes.get(i);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public void addItem(Episode episode) {
        episodes.add(episode);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Episode episode = episodes.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.podcast_list_item, null);
        }

        TextView tvEpisode = (TextView) convertView.findViewById(R.id.tv_podcastitem);
        tvEpisode.setText(episode.getTitle());
        if (episode.getDownloaded() == 0) {
            tvEpisode.setTextColor(Color.RED);
        } else {
            tvEpisode.setTextColor(Color.GREEN);
        }


        return convertView;

    }


}
