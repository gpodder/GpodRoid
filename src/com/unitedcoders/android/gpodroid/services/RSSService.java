/*Copyright 2010 NetCatch Team
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.unitedcoders.android.gpodroid.services;

import com.unitedcoders.android.gpodroid.tools.Tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;

/**
 * A simple service that takes an RSS feed and saves the data from the RSS feed.
 * <p/>
 * Requires either the show object to be pased in, or just a feed,
 * which will create a new show object
 *
 * @author Leif Andersen
 */
public class RSSService {

	
    private static String getImageUrlFromFeed(Context context, String url) {

        try {
            Document doc;
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            doc = builder.parse(response.getEntity().getContent());
//            return doc;

            // get Image
            NodeList item = doc.getElementsByTagName("channel");
            Element el = (Element) item.item(0);

            String imageUrl;
            NodeList imagNode = el.getElementsByTagName("image");
            if (imagNode != null) {
                Element ima = (Element) imagNode.item(0);
                if (ima != null) {
                    NodeList urlNode = ima.getElementsByTagName("url");
                    if (urlNode == null || urlNode.getLength() < 1)
                        imageUrl = null;
                    else
                        imageUrl =
                                urlNode.item(0).getFirstChild().getNodeValue();
                } else
                    imageUrl = null;
            } else
                imageUrl = null;

            return imageUrl;

        } catch (IOException e) {
            return null;  // The network probably died, just return null
        } catch (SAXException e) {
            // Problem parsing the XML, log and return nothing
            Log.e("NCRSS", "Error parsing XML", e);
            return null;
        } catch (Exception e) {
            // Anything else was probably another network problem, fail silently
            return null;
        }
    }
}

