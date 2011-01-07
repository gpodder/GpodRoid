package com.unitedcoders.android.gpodroid;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PodcastView extends LinearLayout {

    private TextView text;

    public PodcastView(Context context, final PodcastElement element) {
        super(context);
        setOrientation(HORIZONTAL);

        text = new TextView(context);
        text.setText(element.getTitle());
        text.setTextSize(16);
        text.setPadding(2, 10, 2, 10);

        addView(text, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }

}
