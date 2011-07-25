package com.unitedcoders.android.gpodroid;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Saves and loads the user preferences
 * @author Nico Heid
 * 
 */
public class Preferences {

	private final static String USERNAME_COL = "USERNAME";
	private final static String ENCRYPTED_AUTHENTICATION_COL = "ENCRYPTED_AUTHENTICATION";
	private final static String DEVICE_COL = "DEVICE";
	
    private static final String PREFS_NAME = "gpodroidPrefs";
    private static String username = "";
    private static String encryptedAuthentication = "";
    private static String device = "";

    public static String getUsername() {
        return username;
    }

    public static void setUsernameAndPassword(String username, String password) {
        Preferences.username = username;
        String auth = Preferences.getUsername() + ":" + password;
        encryptedAuthentication = Base64.encodeBytes(auth.getBytes());
    }

    public static void setNewPassword(String password) {
    	setUsernameAndPassword(username, password);
    }

    public static String getDevice() {
        return device;
    }

    public static void setDevice(String device) {
    	Preferences.device = device;
    }
    
    public static Boolean hasAuthentication(){
    	return Preferences.encryptedAuthentication.isEmpty();
    }
    
    public static String getEncryptedAuthentication(){
    	return encryptedAuthentication;
    }

    public static void save() {
        SharedPreferences settings = GpodRoid.context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(USERNAME_COL, username);
        editor.putString(ENCRYPTED_AUTHENTICATION_COL, encryptedAuthentication);
        editor.putString(DEVICE_COL, device);

        editor.commit();
    }

    public static void initPreferences() {
        SharedPreferences settings = GpodRoid.context.getSharedPreferences(PREFS_NAME, 0);
        
        username = settings.getString(USERNAME_COL, "");
        encryptedAuthentication = settings.getString(ENCRYPTED_AUTHENTICATION_COL, "");
        device = settings.getString(DEVICE_COL, "");
    }

}
