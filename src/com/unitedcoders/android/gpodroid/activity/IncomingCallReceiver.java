package com.unitedcoders.android.gpodroid.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.unitedcoders.android.gpodroid.GpodRoid;
import android.util.Log;

/**
 * Author: nheid
 * Date: 10.06.2011
 */
public class IncomingCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // stop the playback on incoming call
        Log.d(GpodRoid.LOGTAG, "incoming phone call, stopping player if necessary");
        if (Player.mp != null && Player.mp.isPlaying()) {
            Player.mp.pause();
        }

    }
}
