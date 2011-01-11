package com.unitedcoders.android.gpodroid.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.unitedcoders.gpodder.GpodderAPI;

public class Subscribe extends ListActivity {

    String subscribeTo;
    ArrayList<String> top25;
    HashMap<String, String> top25hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        top25hm = new GpodderAPI().getTopSubscriptions(getApplicationContext());
        top25 = new ArrayList<String>(top25hm.keySet());
        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, top25));
        registerForContextMenu(getListView());

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        String itemAtPosition = (String) getListView().getItemAtPosition(info.position);
        Log.d("GPR", "subscribing to " + top25hm.get(itemAtPosition));
        new GpodderAPI().addSubcription(getApplicationContext(), top25hm.get(itemAtPosition));
        menu.add("subscribe");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        // Log.d("GPR", "selected "+item.getTitle().toString());
        return super.onContextItemSelected(item);

    }

}
