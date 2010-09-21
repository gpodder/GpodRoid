package com.unitedcoders.android.gpodroid.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.unitedcoders.android.gpodroid.Base64;
import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.PodcastElement;
import com.unitedcoders.android.gpodroid.PodcastListAdapter;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.services.DownloadService;
import com.unitedcoders.gpodder.GpodderAPI;
import com.unitedcoders.gpodder.GpodderUpdates;

public class DownloadList extends ListActivity {
    public static final String PREFS_NAME = "gpodroidPrefs";

    private String username = "";
    private String password = "";
    private String device = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloadview);

        final PodcastListAdapter pcla = new PodcastListAdapter(this);
        pcla.setShowCheckbox(true);

        GpodderUpdates podcast = null;

        // get preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        this.username = settings.getString("username", "");
        this.password = settings.getString("password", "");
        this.device = settings.getString("device", "");

        if (username.equals("") || password.equals("") || device.equals("")) {

            Toast toast = Toast.makeText(getApplicationContext(), "please enter your settings first",
                    Toast.LENGTH_SHORT);
            toast.show();
            
            return;

        }

        URL url;
        // try to get the updates
        try {
            Long since = (new Date().getTime() / 1000) - 3600*24*14;
            
            String urlStr = "http://gpodder.net/api/2/updates/USERNAME/DEVICE.json?since="+since;
            urlStr = urlStr.replace("USERNAME", username);
            urlStr = urlStr.replace("DEVICE", device);
            url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Authorization", "Basic "
                    + Base64.encodeBytes((username + ":" + password).getBytes()));
            // + BasicAuth.encode(username, password));
            GpodderAPI api = new GpodderAPI("", "");
            podcast = api.parseResponse(conn.getInputStream());

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // // add items to download list
        for (int i = 0; i < podcast.getUpdates().size(); i++) {
            pcla.addItem(new PodcastElement(podcast.getUpdates().get(i).getTitle(), podcast.getUpdates().get(i)
                    .getUrl()));
        }

        setListAdapter(pcla);

//        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setNeutralButton("let's pretend it's done", null);
        Button downloadButton = (Button) findViewById(R.id.downloadButton);
        final Intent intent = new Intent(this, DownloadService.class);
        downloadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String download = null;

                List<PodcastElement> checkedItems = pcla.getCheckedItems();
                for(PodcastElement pce : checkedItems){
                    GpodRoid.addDownloadQueue(pce);
                }
                
                intent.putExtra("podcast", download);
                startService(intent);

            }
        });
        //
    }

}
