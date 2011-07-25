package com.unitedcoders.gpodder;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.webkit.WebView;
import com.unitedcoders.android.gpodroid.*;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * The calls against gpodder.net API
 *
 * @author Nico Heid
 */
public class GpodderAPI {

    private static final String GPODDER_BASE = "http://gpodder.net";
    public static final String PREFS_NAME = "gpodroidPrefs";
    private static GpodderUpdates downloadListResponse = null;

    /**
     * This member is meant to be populated right at the beginning of the application in the "main" function
     * and will be referenced for the duration of the application
     */
    public static Context context = null;

    /**
     * The custom User-Agent we send for http requests
     * uses GpodroidVersion + default Webview agent information
     */
    private static String customUserAgent = null;


    public static URLConnection createUrlConnection(String urlStr, Boolean useAuthentication, Boolean setOutput) {
        Log.i(GpodRoid.LOGTAG, "Creating Connection to " + urlStr);

        // set custom User-Agent string
        if (customUserAgent == null) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                String webviewAgent = new WebView(context).getSettings().getUserAgentString();
                customUserAgent = "GpodRoid " + packageInfo.versionName+" "+webviewAgent;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(GpodRoid.LOGTAG, "error setting version info in header", e);
                customUserAgent = "GpodRoid undefined";
            }
        }

        URLConnection conn = null;
        try {
            conn = new URL(urlStr).openConnection();
            conn.setDoOutput(setOutput);
            conn.setDoInput(true);

            if (useAuthentication) {
                String encoded = Preferences.getEncryptedAuthentication();
                conn.setRequestProperty("Authorization", "basic " + encoded);
            }
            conn.addRequestProperty("User-Agent", customUserAgent);
        } catch (Exception e) {
            Log.e(GpodRoid.LOGTAG, "Error creating Connection: " + stackTrace(e));
            return null;
        }

        return conn;
    }

    public static String stackTrace(Exception e) {
        String eol = System.getProperty("line.separator");
        String stack = eol + e.getMessage() + eol;

        StackTraceElement[] stackTraces = e.getStackTrace();
        for (int cste = 0; cste < stackTraces.length; cste++) {
            stack += stackTraces[cste].toString() + eol;
        }

        return stack;
    }

    public static GpodderUpdates getDownloadList() {
        // try to get the updates
        try {
            Long since = (new Date().getTime() / 1000) - 3600 * 24 * 56;

            String urlStr = GPODDER_BASE + "/api/2/updates/USERNAME/DEVICE.json?since=" + since;
            urlStr = urlStr.replace("USERNAME", Preferences.getUsername());
            urlStr = urlStr.replace("DEVICE", Preferences.getDevice());

            URLConnection conn = createUrlConnection(urlStr, true, false);
            InputStream is = conn.getInputStream();
            downloadListResponse = new ObjectMapper().readValue(is, GpodderUpdates.class);
            is.close();
        } catch (Exception e) {
            Log.e(GpodRoid.LOGTAG, "Failed to download subscription list: " + stackTrace(e));
        }

        //experimental, fill sql
        return downloadListResponse;
    }


    public static void createDevice(Context context, String deviceName) {
        String urlStr = String.format(GPODDER_BASE + "/api/2/devices/%s/gpodroid.json", Preferences.getUsername());
        JSONObject device = new JSONObject();

        try {
            device.put("caption", deviceName);
            device.put("id", deviceName);
            device.put("type", "mobile");
            URLConnection con = createUrlConnection(urlStr, true, true);
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(device.toString());
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String decodedString;

            while ((decodedString = in.readLine()) != null) {
                System.out.println(decodedString);
            }
            in.close();

        } catch (Exception e) {
            Log.e(GpodRoid.LOGTAG, "error creating device", e);
        }

    }

    /**
     * gets the devices associated with the user
     *
     * @return ArrayList<String> A list of strings which are the names of the devices
     */
    public static ArrayList<String> getDevices() {
        ArrayList<String> gpodderDevices = new ArrayList<String>();
        String urlStr = GPODDER_BASE + "/api/2/devices/" + Preferences.getUsername() + ".json";
        try {
            URLConnection conn = createUrlConnection(urlStr, true, false);
            InputStream stream = conn.getInputStream();
            String response = IOUtils.toString(stream);
            JSONArray devices = new JSONArray(response);

            for (int i = 0; i < devices.length(); i++) {
                String add = devices.getJSONObject(i).getString("id");
                gpodderDevices.add(add);
            }
            Log.i(GpodRoid.LOGTAG, "Successfully downloaded devices.");

        } catch (Exception e) {
            Log.e(GpodRoid.LOGTAG, "Error downloading devices: " + stackTrace(e));
            return null;
        }
        return gpodderDevices;
    }

    public static HashMap<String, String> getTopSubscriptions() {
        HashMap<String, String> subscriptions = new HashMap<String, String>();

        String urlStr = GPODDER_BASE + "/toplist/25.json";
        try {
            URLConnection conn = createUrlConnection(urlStr, false, false);
            String response = IOUtils.toString(conn.getInputStream());
            JSONArray top25 = new JSONArray(response);
            for (int i = 0; i < top25.length(); i++) {
                String title = top25.getJSONObject(i).getString("title");
                String subUrl = top25.getJSONObject(i).getString("url");
                subscriptions.put(title, subUrl);

            }
            Log.i(GpodRoid.LOGTAG, "Successfully downloaded top subscriptions.");
        } catch (Exception e) {
            Log.e(GpodRoid.LOGTAG, "Error getting Top Subscriptions:" + stackTrace(e));
        }

        return subscriptions;
    }

    public static HashMap<String, String> searchFeeds(String searchTerm) {
        HashMap<String, String> subscriptions = new HashMap<String, String>();
        String urlStr = GPODDER_BASE + "/search.json?q=SEARCHTERM";
        urlStr = urlStr.replace("SEARCHTERM", searchTerm);
        try {
            URLConnection conn = createUrlConnection(urlStr, false, false);
            String response = IOUtils.toString(conn.getInputStream());
            JSONArray top25 = new JSONArray(response);
            for (int i = 0; i < top25.length(); i++) {
                String title = top25.getJSONObject(i).getString("title");
                String subUrl = top25.getJSONObject(i).getString("url");
                subscriptions.put(title, subUrl);

            }
        } catch (Exception e) {
            Log.e(GpodRoid.LOGTAG, "Error searching feeds:" + stackTrace(e));
        }

        return subscriptions;

    }

    public static void addSubcription(String url) {
        String urlStr = GPODDER_BASE + "/api/1/subscriptions/USERNAME/DEVICE.json";
        JSONObject device = new JSONObject();
        JSONArray urls = new JSONArray();

        try {
            urls.put(url);
            device.put("add", urls);
            urlStr = urlStr.replace("USERNAME", Preferences.getUsername());
            urlStr = urlStr.replace("DEVICE", Preferences.getDevice());
            URLConnection con = createUrlConnection(urlStr, true, false);
            con.setDoOutput(true);

            Log.d(GpodRoid.LOGTAG, "sending subscription: " + urlStr + " with body " + device.toString());
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(device.toString());
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String decodedString;

            StringBuffer response = new StringBuffer();
            while ((decodedString = in.readLine()) != null) {
                response.append(decodedString);
//              System.out.println(decodedString);

            }
            in.close();

            Log.d(GpodRoid.LOGTAG, "Result of subscription request: "+response.toString());

        } catch (Exception e) {
            Log.e(GpodRoid.LOGTAG, "Error adding subscription:" + stackTrace(e));
        }
    }
}
