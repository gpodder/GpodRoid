package com.unitedcoders.android.gpodroid;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.unitedcoders.android.gpodroid.activity.AccountSettings;
import com.unitedcoders.android.gpodroid.activity.Subscribe;
import roboguice.activity.RoboActivity;

/**
 * Created by IntelliJ IDEA.
 * User: nheid
 * Date: 4/2/11
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoboActivityDefaultMenu extends RoboActivity {

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.account:
                Intent account = new Intent(getApplicationContext(), AccountSettings.class);
                startActivity(account);
                return true;
            case R.id.subscriptions:
                Intent subscriptions = new Intent(getApplicationContext(), Subscribe.class);
                startActivity(subscriptions);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
