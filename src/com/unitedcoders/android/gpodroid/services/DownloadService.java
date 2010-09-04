package com.unitedcoders.android.gpodroid.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class DownloadService extends Service {
    private int podDownloadedSize, podTotalSize;
    private Intent intent;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        this.intent=intent;
        
        downloadPodcast.start();
        
        stopSelf();

    }
    
    
    private Thread downloadPodcast = new Thread(){
        
        public void run(){
//            Toast.makeText(this, "download started", Toast.LENGTH_LONG);
            try {
                URL podcastURL = new URL(intent.getExtras().getString("podcast"));

                HttpURLConnection urlConnection = (HttpURLConnection) podcastURL.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                urlConnection.connect();

                File SDCardRoot = Environment.getExternalStorageDirectory();
                new File("/sdcard/gpodder/").mkdir();

                File name = new File(podcastURL.toString());
                String saveName = name.getName().trim();
                File file = new File("/sdcard/gpodder/", saveName);
                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();
                podTotalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                podDownloadedSize = 0;

                // now, read through the input buffer and write the contents
                // to the file
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    // add the data in the buffer to the file in the file
                    // output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                    // add up the size so we know how much is downloaded
                    podDownloadedSize += bufferLength;
                    // this is where you would do something to report the progress
                    Log.d("gpodder", "progress " + podDownloadedSize);
                    System.out.println("progress" + podDownloadedSize);

                }

                fileOutput.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//            Toast.makeText(this, "download ended", Toast.LENGTH_LONG);

           
            
        }
        
    };

}

// public void downloadPodcast(String podcast) {
// try {
//	
// // Context context, PodcastListAdapter pcla
// // URL podcastUrl = new URL(pcel.get(0).getTitle());
// // URL podcastUrl = new URL("http://nicoheid.com/pc.mp3");
// // List<PodcastElement> downloadList = pcla.getCheckedItems();
// // URL podcastUrl = new URL(downloadList.get(0).getDownloadurl());
//			
// URL podcastURL = new URL(podcast);
//	
// HttpURLConnection urlConnection = (HttpURLConnection) podcastUrl.openConnection();
// urlConnection.setRequestMethod("GET");
// urlConnection.setDoOutput(true);
//	
// urlConnection.connect();
//	
// File SDCardRoot = Environment.getExternalStorageDirectory();
// // --
// new File("/sdcard/gpodder/").mkdir();
//	
// // --
// File name = new File(downloadList.get(0).getDownloadurl());
// String saveName = name.getName().trim();
// File file = new File("/sdcard/gpodder/", saveName);
// FileOutputStream fileOutput = new FileOutputStream(file);
// InputStream inputStream = urlConnection.getInputStream();
// podTotalSize = urlConnection.getContentLength();
//	
// byte[] buffer = new byte[1024];
// int bufferLength = 0;
// podDownloadedSize = 0;
//	
// // pd = new ProgressDialog(context);
// // pd.setMessage("Downloading ...");
// // pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
// // pd.setMax(podTotalSize);
// // pd.setProgress(0);
// // pd.show();
// //
// // Thread background = new Thread (new Runnable() {
// //
// // @Override
// // public void run() {
// // while(podDownloadedSize < podTotalSize){
// //
// // try {
// // Thread.sleep(1000);
// // progressHandler.sendMessage(progressHandler.obtainMessage());
// // } catch (InterruptedException e) {
// // // TODO Auto-generated catch block
// // e.printStackTrace();
// // }
// //
// // }
// // pd.dismiss();
// //
// // }
// // });
// //
// // background.start();
//	
// // now, read through the input buffer and write the contents
// // to the file
// while ((bufferLength = inputStream.read(buffer)) > 0) {
// // add the data in the buffer to the file in the file
// // output stream (the file on the sd card
// fileOutput.write(buffer, 0, bufferLength);
// // add up the size so we know how much is downloaded
// podDownloadedSize += bufferLength;
// // this is where you would do something to report the
// // prgress, like this maybe
// // updateProgress(downloadedSize, totalSize);
// Log.d("gpodder", "progress " + podDownloadedSize);
// System.out.println("progress" + podDownloadedSize);
// // progDia.setProgress((podDownloadedSize*100)/podTotalSize);
// // pd.setProgress(3);
//	
// }
// // pd.dismiss();
// fileOutput.close();
//	
// } catch (MalformedURLException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// } catch (IOException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
//	
// }

