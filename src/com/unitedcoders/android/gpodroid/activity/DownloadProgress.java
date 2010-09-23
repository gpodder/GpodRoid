package com.unitedcoders.android.gpodroid.activity;

import com.unitedcoders.android.gpodroid.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

/**
 * Download of podcasts with ProgressBar
 * @author Nico Heid
 *
 */
public class DownloadProgress extends Activity {

    ProgressBar progressBar;
    private int progress = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.download_progress);

        final Notification notification = new Notification(R.drawable.icon, "downloading podcast", System.currentTimeMillis());
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        notification.contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.download_progress);

        Intent intent = new Intent(this, DownloadProgress.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        notification.contentIntent = pendingIntent;

        notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.ic_menu_save);
        notification.contentView.setTextViewText(R.id.status_text, "downloading podcast");

        notification.contentView.setProgressBar(R.id.status_progress, 100, progress, false);
        

        final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(
                getApplicationContext().NOTIFICATION_SERVICE);

        notificationManager.notify(42, notification);

        //		
        //		
//        //		
        Thread download = new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                for (int i = 1; i < 100; i++) {
                    progress++;
                    notification.contentView.setProgressBar(R.id.status_progress, 100, progress, false);
                    notificationManager.notify(42, notification);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                
                notificationManager.cancel(42);

            }
        };

        download.run();
        //		

    }

}