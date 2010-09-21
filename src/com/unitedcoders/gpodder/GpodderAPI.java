package com.unitedcoders.gpodder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.unitedcoders.android.gpodroid.Base64;

import android.content.Context;
import android.content.SharedPreferences;

public class GpodderAPI {

    public static final String PREFS_NAME = "gpodroidPrefs";

    private HttpURLConnection connection;
    private URL urlGetNewPodcasts;
    private String username;
    private String password;

    public GpodderAPI(String user, String password) {
        this.username = user;
        this.password = password;
    }

    public GpodderUpdates parseResponse(InputStream inputStream) {

        ObjectMapper mapper = new ObjectMapper();
        GpodderUpdates pc = null;
        try {
            pc = mapper.readValue(inputStream, GpodderUpdates.class);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return pc;

    }

    public ArrayList<String> getDevices(Context context) {

        ObjectMapper mapper = new ObjectMapper();

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        this.username = settings.getString("username", "");
        this.password = settings.getString("password", "");

        ArrayList<String> gpodderDevices = new ArrayList<String>();

        String urlStr = "http://gpodder.net/api/2/devices/USERNAME.json";
        urlStr = urlStr.replace("USERNAME", username);
        URL url;
        try {
            url = new URL(urlStr);

            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Authorization", "Basic "
                    + Base64.encodeBytes((username + ":" + password).getBytes()));

            String response = IOUtils.toString(conn.getInputStream());
            JSONArray devices = new JSONArray(response);

           
            for (int i = 0; i < devices.length(); i++) {
                String add = devices.getJSONObject(i).getString("id");
                gpodderDevices.add(add);

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
        return gpodderDevices;

    }

}
