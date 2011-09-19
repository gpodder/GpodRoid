package com.unitedcoders.android.gpodroid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.unitedcoders.android.gpodroid.activity.AccountSettings;
import com.unitedcoders.android.gpodroid.services.UpdateService;
import roboguice.activity.RoboActivity;

/**
 * Activity with default menu
 *
 * @author nheid
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
//            case R.id.subscriptions:
//                Intent subscriptions = new Intent(getApplicationContext(), Subscribe.class);
//                startActivity(subscriptions);
//                return true;
            case R.id.fetch_updates:
                startService(new Intent(getApplicationContext(), UpdateService.class));
                return true;
            case R.id.send_feedback:
                sendFeedback();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Start intend to send a feedback email via installed mail application
     */
    private void sendFeedback() {
        String version = null;

        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(GpodRoid.LOGTAG, "could not determin version", e);
        }

        String message = String.format("\n\n--DEVELOPER INFO -- \nthis feedback is from version: %s on device: %s with android: %s",
                version , Build.MODEL, Build.VERSION.RELEASE);


        /* Create the Intent */
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        /* Fill it with Data */
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"gpodroid@united-coders.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "feedback from GPodRoid");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

        /* Send it off to the Activity-Chooser */
        GpodRoid.context.startActivity(Intent.createChooser(emailIntent, "Send mail...")
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
