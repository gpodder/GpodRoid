package com.unitedcoders.android.gpodroid.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.Preferences;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.database.GpodDB;
import com.unitedcoders.android.gpodroid.services.UpdateService;
import com.unitedcoders.gpodder.GpodderAPI;

/**
 * Shows gpodder registered devices.
 * 
 * @author Nico Heid
 * 
 */
public class SelectDevice extends ListActivity implements OnClickListener {

    Button btnCustomName;
    Preferences pref;
    ArrayList<String> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_device);
        Context cont = getApplicationContext();

        devices = GpodderAPI.getDevices();
        if(devices == null){
    		Toast.makeText(cont, "Failed to download devices.", Toast.LENGTH_SHORT).show();
        	return;
    	}
        Log.i("GPR", "populating device list");

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devices));
        
        btnCustomName = (Button) findViewById(R.id.btn_customname);
        btnCustomName.setOnClickListener(this);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        // super.onListItemClick(l, v, position, id);

        String device = (String) l.getItemAtPosition(position);
        Log.d(GpodRoid.LOGTAG, "saving device " + device);
        saveDevice(device);
        GpodDB db = new GpodDB(getApplicationContext());
        db.wipeClean();
        startService(new Intent(getApplicationContext(), UpdateService.class));
        finish();
    }

    @Override
    public void onClick(View v) {

        if (v == btnCustomName) {
            customNameDialoge();
        }

    }

    private void customNameDialoge() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText customName = new EditText(this);
        alert.setView(customName);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	Context cont = getApplicationContext();
        		GpodderAPI.createDevice(cont, customName.getText().toString());
            	devices = GpodderAPI.getDevices();
            	if(devices == null){
            		Toast.makeText(cont, "Failed to download devices.", Toast.LENGTH_SHORT).show();
                	return;
            	}
            	setListAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, devices));
            }
        });
        alert.setNegativeButton("Cancel", null);
        alert.show();
    }

    private void saveDevice(String deviceName) {
        pref = Preferences.getPreferences(getApplicationContext());
        pref.setDevice(deviceName);
        pref.save();
    }

}
