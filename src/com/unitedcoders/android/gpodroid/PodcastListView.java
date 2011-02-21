package com.unitedcoders.android.gpodroid;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.unitedcoders.gpodder.GpodderPodcast;

public class PodcastListView extends LinearLayout {

    private TextView text;

    public PodcastListView(Context context, final GpodderPodcast element) {
        super(context);
        setOrientation(HORIZONTAL);

        text = new TextView(context);

        text.setText(element.getTitle());
        addView(text, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }

}
