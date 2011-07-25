package com.unitedcoders.android.gpodroid.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.unitedcoders.android.gpodroid.GpodRoid;
import com.unitedcoders.android.gpodroid.Preferences;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.database.GpodDB;
import com.unitedcoders.android.gpodroid.services.UpdateService;
import com.unitedcoders.gpodder.GpodderAPI;

import java.util.ArrayList;

/*
 * Shows gpodder registered devices.
 *
 * @author Nico Heid
 */
public class SelectDevice extends ListActivity implements OnClickListener {

    private Button btnCustomName;
    public static ArrayList<String> devices;
    private Handler handler;
    private ProgressDialog wheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();
        setContentView(R.layout.select_device);

        wheel = ProgressDialog.show(this, "", "fetching devices", true);
        getDevices();


        //devices = GpodderAPI.getDevices();
        //if (devices == null) {
        //Toast.makeText(getApplicationContext(), "Failed to download devices.", Toast.LENGTH_SHORT).show();
        //    return;
        //}
        //Log.i("GPR", "populating device list");

        //setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devices));

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
        Log.d(GpodRoid.LOGTAG, "Started service");
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
                if (devices == null) {
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
        Preferences.setDevice(deviceName);
        Preferences.save();
    }

    private void getDevices() {
        Thread t = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                
                // this check was added because the list of devices may have been retrieved already during the 
                // account settings activity, so we can skip it here to reduce redundancy
                if(devices == null){
                  devices = GpodderAPI.getDevices();
                }
                
                handler.post(displayResults);
                Looper.loop();
            }
        };
        t.start();
    }

    final Runnable displayResults = new Runnable() {
        @Override
        public void run() {
            displayResultsInUI();

        }
    };

    private void displayResultsInUI() {
        wheel.dismiss();
        if (devices == null) {
            Toast.makeText(getApplicationContext(), "Failed to download devices.", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(GpodRoid.LOGTAG, "populating device list");
        setListAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, devices));

    }

}
