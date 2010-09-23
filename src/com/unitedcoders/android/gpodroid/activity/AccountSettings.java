package com.unitedcoders.android.gpodroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unitedcoders.android.gpodroid.Preferences;
import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.R.layout;

public class AccountSettings extends Activity {

    Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(layout.accountsettings);
        
        pref = Preferences.getPreferences(getApplicationContext());
        
        EditText etUsername = (EditText) findViewById(R.id.in_username);
        EditText etPassword = (EditText) findViewById(R.id.in_password);
        etUsername.setText(pref.getUsername());
        etPassword.setText(pref.getPassword());
        

        Button save = (Button) findViewById(R.id.btn_save);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                
                pref.setUsername(((EditText) (findViewById(R.id.in_username))).getText().toString());
                pref.setPassword(((EditText) (findViewById(R.id.in_password))).getText().toString());
                pref.save();
                
                Intent intent = new Intent(getApplicationContext(), SelectDevice.class);
                startActivity(intent);
                finish();
            }

        });

    }

}
