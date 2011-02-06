package com.unitedcoders.android.gpodroid.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.unitedcoders.android.gpodroid.Preferences;
import com.unitedcoders.gpodder.GpodderAPI;

/**
 * Shows gpodder registered devices.
 * @author Nico Heid
 *
 */
public class SelectDevice extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        ArrayList<String> devices = new GpodderAPI("", "").getDevices(getApplicationContext());
        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devices));

    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
//        super.onListItemClick(l, v, position, id);
        
        Preferences pref = Preferences.getPreferences(getApplicationContext());
        String device = (String) l.getItemAtPosition(position);
        pref.setDevice(device);
        pref.save();
        
        finish();
    }

}
