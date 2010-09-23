package com.unitedcoders.android.gpodroid;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;

public class GPodderActions {

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            System.out.println("Exc=" + e);
            return null;
        }
    }

}
