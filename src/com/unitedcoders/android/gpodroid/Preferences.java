package com.unitedcoders.android.gpodroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

/**
 * Saves and loads the user preferences
 * @author Nico Heid
 * 
 */
public class Preferences {

//    private static GpodRoid.prefs preferences;

    private Context context;
    public static void setPreferences(Preferences preferences) {
        GpodRoid.prefs = preferences;
    }

    public static final String PREFS_NAME = "gpodroidPrefs";
    private String username = "";
    private String password = "";
    private String device = "";

    private Preferences() {

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void save() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("USERNAME", username);
        editor.putString("PASSWORD", password);
        editor.putString("DEVICE", device);

        editor.commit();

    }

    public static Preferences getPreferences(Context context) {
        if (GpodRoid.prefs == null) {
            GpodRoid.prefs = new Preferences();
        }
        
        GpodRoid.prefs.setContext(context);
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        GpodRoid.prefs.setUsername(settings.getString("USERNAME", ""));
        GpodRoid.prefs.setPassword(settings.getString("PASSWORD", ""));
        GpodRoid.prefs.setDevice(settings.getString("DEVICE", ""));
        return GpodRoid.prefs;
    }

}
