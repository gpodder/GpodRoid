package com.unitedcoders.android.gpodroid.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.unitedcoders.gpodder.GpodderAPI;

public class SelectDevice extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        ArrayList<String> devices = new GpodderAPI("", "").getDevices(getApplicationContext());
        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devices));

    }

}
