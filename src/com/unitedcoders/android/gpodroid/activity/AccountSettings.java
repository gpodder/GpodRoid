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

import com.unitedcoders.android.gpodroid.R;
import com.unitedcoders.android.gpodroid.R.layout;

public class AccountSettings extends Activity {

    public static final String PREFS_NAME = "gpodroidPrefs";
    private String USERNAME = "username";
    private String PASSWORD = "password";
    private String DEVICE = "device";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(layout.accountsettings);

        Button save = (Button) findViewById(R.id.btn_save);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString(USERNAME, ((EditText) (findViewById(R.id.in_username))).getText().toString());
                editor.putString(PASSWORD, ((EditText) (findViewById(R.id.in_password))).getText().toString());
//                editor.putString(DEVICE, ((EditText) (findViewById(R.id.in_device))).getText().toString());

                editor.commit();

//                finish();
                
                Intent intent = new Intent(getApplicationContext(), SelectDevice.class);
                startActivity(intent);
            }

        });

    }

}
