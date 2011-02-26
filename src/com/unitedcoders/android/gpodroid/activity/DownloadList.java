//package com.unitedcoders.android.gpodroid.activity;
//
//import java.util.List;
//
//import android.app.ListActivity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.unitedcoders.android.gpodroid.GpodRoid;
//import com.unitedcoders.android.gpodroid.PodcastListAdapter;
//import com.unitedcoders.android.gpodroid.Preferences;
//import com.unitedcoders.android.gpodroid.R;
//import com.unitedcoders.android.gpodroid.services.DownloadService;
//import com.unitedcoders.gpodder.GpodderAPI;
//import com.unitedcoders.gpodder.GpodderUpdates;
//
///**
// * Shows a list of available downloads
// *
// * @author Nico Heid
// */
//public class DownloadList extends ListActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.downloadview);
//
//        final PodcastListAdapter pcla = new PodcastListAdapter(this);
//        pcla.setShowCheckbox(true);
//
//        // get preferences
//        Preferences pref = GpodRoid.prefs;
//        if (pref.getUsername().equals("") || pref.getPassword().equals("") || pref.getDevice().equals("")) {
//
//            Toast toast = Toast.makeText(getApplicationContext(), "please enter your settings first",
//                    Toast.LENGTH_SHORT);
//            toast.show();
//
//            return;
//
//        }
//
//        GpodderUpdates podcast = GpodderAPI.getDownloadList();
//
//        // add items to download list
//        for (int i = 0; i < podcast.getUpdates().size(); i++) {
////            pcla.addItem(new PodcastElement(podcast.getUpdates().get(i).getTitle(), podcast.getUpdates().get(i)
////                    .getUrl()));
//        }
//
//        setListAdapter(pcla);
//
//        Button downloadButton = (Button) findViewById(R.id.downloadButton);
//        final Intent intent = new Intent(this, DownloadService.class);
//        downloadButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                String download = null;
//
////                List<PodcastElement> checkedItems = pcla.getCheckedItems();
////                for (PodcastElement pce : checkedItems) {
////                    GpodRoid.addDownloadQueue(pce);
////                }
//
//                intent.putExtra("podcast", download);
//                startService(intent);
//
//            }
//        });
//    }
//
//}
