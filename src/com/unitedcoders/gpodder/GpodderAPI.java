package com.unitedcoders.gpodder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.unitedcoders.android.gpodroid.Base64;
import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.Preferences;

/**
 * The calls against gpodder.net API
 * 
 * @author Nico Heid
 * 
 */
public class GpodderAPI {

    public static final String PREFS_NAME = "gpodroidPrefs";

    private HttpURLConnection connection;
    private URL urlGetNewPodcasts;

    static GpodderUpdates downloadListResponse = null;

    public GpodderAPI() {
    }

    public GpodderUpdates parseResponse(InputStream inputStream) {

        ObjectMapper mapper = new ObjectMapper();
        GpodderUpdates pc = null;
        try {
            pc = mapper.readValue(inputStream, GpodderUpdates.class);
        } catch (JsonParseException e) {
            Log.e(GpodRoid.LOGTAG, "error while parsing", e);
        } catch (JsonMappingException e) {
            Log.e(GpodRoid.LOGTAG, "error while parsing", e);
        } catch (IOException e) {
            Log.e(GpodRoid.LOGTAG, "error while parsing", e);
        }

        return pc;

    }

    public static GpodderUpdates getDownloadList() {

        if (downloadListResponse != null) {
            return downloadListResponse;
        }

        // get preferences

        URL url;
        // try to get the updates
        try {
            Long since = (new Date().getTime() / 1000) - 3600 * 24 * 14;

            String urlStr = "http://gpodder.net/api/2/updates/USERNAME/DEVICE.json?since=" + since;
            urlStr = urlStr.replace("USERNAME", GpodRoid.prefs.getUsername());
            urlStr = urlStr.replace("DEVICE", GpodRoid.prefs.getDevice());
            url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty(
                    "Authorization",
                    "Basic "
                            + Base64.encodeBytes((GpodRoid.prefs.getUsername() + ":" + GpodRoid.prefs.getPassword())
                                    .getBytes()));
            // + BasicAuth.encode(username, password));
            GpodderAPI api = new GpodderAPI();
            InputStream is = conn.getInputStream();
            downloadListResponse = api.parseResponse(is);
            is.close();

        } catch (MalformedURLException e) {
            Log.e(GpodRoid.LOGTAG, "getDownloadList", e);
        } catch (IOException e) {
            Log.e(GpodRoid.LOGTAG, "getDownloadList", e);
        } catch (Exception e) {
            Log.e(GpodRoid.LOGTAG, "getDownloadList", e);
        }

        return downloadListResponse;

    }

    public static void createDevice(Context context, String deviceName) {
        String urlStr = String
                .format("http://gpodder.net/api/2/devices/%s/gpodroid.json", GpodRoid.prefs.getUsername());
        JSONObject device = new JSONObject();

        try {
            device.put("caption", deviceName);
            device.put("id", deviceName);
            device.put("type", "mobile");
            URLConnection con = new URL(urlStr).openConnection();
            con.setDoOutput(true);
            Preferences pref = Preferences.getPreferences(context);
            con.setRequestProperty("Authorization",
                    "Basic " + Base64.encodeBytes((pref.getUsername() + ":" + pref.getPassword()).getBytes()));

            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(device.toString());
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String decodedString;

            while ((decodedString = in.readLine()) != null) {
                System.out.println(decodedString);
            }
            in.close();

        } catch (MalformedURLException e) {
            Log.e(GpodRoid.LOGTAG, "error creating device", e);
        } catch (IOException e) {
            Log.e(GpodRoid.LOGTAG, "error creating device", e);
        } catch (JSONException e) {
            Log.e(GpodRoid.LOGTAG, "error creating device", e);
        }

    }

    public ArrayList<String> getDevices(Context context) {

        // ObjectMapper mapper = new ObjectMapper();

        Preferences pref = Preferences.getPreferences(context);

        ArrayList<String> gpodderDevices = new ArrayList<String>();

        String urlStr = "http://gpodder.net/api/2/devices/USERNAME.json";
        urlStr = urlStr.replace("USERNAME", pref.getUsername());
        URL url;
        try {
            url = new URL(urlStr);

            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Authorization",
                    "Basic " + Base64.encodeBytes((pref.getUsername() + ":" + pref.getPassword()).getBytes()));

            String response = IOUtils.toString(conn.getInputStream());
            JSONArray devices = new JSONArray(response);

            for (int i = 0; i < devices.length(); i++) {
                String add = devices.getJSONObject(i).getString("id");
                gpodderDevices.add(add);

            }

        } catch (MalformedURLException e) {
            Log.e("Gpodroid", "error when getting devices " + e);
        } catch (IOException e) {
            Log.e("Gpodroid", "error when getting devices " + e);
        } catch (JSONException e) {
            Log.e("Gpodroid", "error when getting devices " + e);
        }
        return gpodderDevices;

    }

    public HashMap<String, String> getTopSubscriptions(Context context) {
        ObjectMapper mapper = new ObjectMapper();

        Preferences pref = Preferences.getPreferences(context);

        HashMap<String, String> subscriptions = new HashMap<String, String>();
        // ArrayList<String> gpodderDevices = new ArrayList<String>();

        String urlStr = "http://gpodder.net/toplist/25.json";
        urlStr = urlStr.replace("USERNAME", pref.getUsername());
        URL url;
        try {
            url = new URL(urlStr);

            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Authorization",
                    "Basic " + Base64.encodeBytes((pref.getUsername() + ":" + pref.getPassword()).getBytes()));

            String response = IOUtils.toString(conn.getInputStream());
            JSONArray top25 = new JSONArray(response);
            for (int i = 0; i < top25.length(); i++) {
                String title = top25.getJSONObject(i).getString("title");
                String subUrl = top25.getJSONObject(i).getString("url");
                subscriptions.put(title, subUrl);

            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return subscriptions;
    }

    public void addSubcription(Context context, String url) {
        String urlStr = "http://gpodder.net/api/1/subscriptions/USERNAME/DEVICE.json";
        JSONObject device = new JSONObject();
        JSONArray urls = new JSONArray();

        try {
            urls.put(url);
            device.put("add", urls);
            Preferences pref = Preferences.getPreferences(context);
            urlStr = urlStr.replace("USERNAME", pref.getUsername());
            urlStr = urlStr.replace("DEVICE", pref.getDevice());
            URLConnection con = new URL(urlStr).openConnection();
            con.setDoOutput(true);

            con.setRequestProperty("Authorization",
                    "Basic " + Base64.encodeBytes((pref.getUsername() + ":" + pref.getPassword()).getBytes()));

            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(device.toString());
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String decodedString;

            while ((decodedString = in.readLine()) != null) {
                System.out.println(decodedString);
            }
            in.close();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
