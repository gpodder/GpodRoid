package com.unitedcoders.android.gpodroid;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PodcastView extends LinearLayout {

    private CheckBox cbox;
    private TextView text;

    public PodcastView(Context context, final PodcastElement element, boolean showCheckboxes) {
        super(context);
        setOrientation(HORIZONTAL);

        if (showCheckboxes) {

            cbox = new CheckBox(context);
            cbox.setChecked(false);

            cbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    element.setChecked(isChecked);

                }
            });

            addView(cbox, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }

        text = new TextView(context);

        text.setText(element.getTitle());
        addView(text, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

    }

    public void setChecked(Boolean b) {
        cbox.setChecked(b);
    }

    public Boolean isChecked() {
        return cbox.isChecked();
    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }

}
