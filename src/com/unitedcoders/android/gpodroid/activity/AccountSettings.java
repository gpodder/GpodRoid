package com.unitedcoders.android.gpodroid.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unitedcoders.android.gpodroid.Preferences;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.R.layout;
import com.unitedcoders.gpodder.GpodderAPI;

/**
 * This class will present the user with an interface to enter their username and password and then login
 * or register a new user name and password
 * 
 * @author Brian Bourke-Martin
 */
public class AccountSettings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.accountsettings);
        final Context context = getApplicationContext();
        final Preferences pref = Preferences.getPreferences(context);

        final EditText etUsername = (EditText) findViewById(R.id.in_username);
        final EditText etPassword = (EditText) findViewById(R.id.in_password);
        final Button save = (Button) findViewById(R.id.btn_save);
        final Button register = (Button) findViewById(R.id.btn_register);
        
        etUsername.setText(pref.getUsername());
        etPassword.setText(pref.getPassword());
              
        etUsername.setOnKeyListener(new OnKeyListener() { 
            public boolean onKey(View v, int keyCode, KeyEvent event) {
            	switch(keyCode){
            	case KeyEvent.KEYCODE_ENTER:
            	case KeyEvent.KEYCODE_TAB:
            		etPassword.requestFocus();
            		return true;
            	default:
            		return false;
            	}
            } 
        }); 
                
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	pref.setUsername(etUsername.getText().toString());
                pref.setPassword(etPassword.getText().toString());
                pref.save();
                
                // check to see if the user name and password function to get a list of devices as an check
                SelectDevice.devices = GpodderAPI.getDevices();
                if(SelectDevice.devices == null){
                	// this is a failure to login using this user name and password so lets reset and continue
                	Toast.makeText(context, "Could not authenticate this username and password.", Toast.LENGTH_SHORT).show();
                }
                else{
                	startActivity(new Intent(context, SelectDevice.class));
                	finish();
                }
            }
        });
        
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://gpodder.net/register/"));
                startActivity(i);
            }
        });
    }
}
