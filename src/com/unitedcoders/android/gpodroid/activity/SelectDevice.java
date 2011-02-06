package com.unitedcoders.android.gpodroid.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.unitedcoders.android.gpodroid.Preferences;
import com.unitedcoders.android.gpodroid.R;
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

        devices = new GpodderAPI().getDevices(getApplicationContext());
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devices));

        btnCustomName = (Button) findViewById(R.id.btn_customname);
        btnCustomName.setOnClickListener(this);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        // super.onListItemClick(l, v, position, id);

        String device = (String) l.getItemAtPosition(position);
        saveDevice(device);
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
                GpodderAPI.createDevice(getApplicationContext(), customName.getText().toString());
                devices = new GpodderAPI().getDevices(getApplicationContext());
                setListAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,
                        devices));

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
