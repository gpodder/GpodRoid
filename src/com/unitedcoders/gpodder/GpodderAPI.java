package com.unitedcoders.gpodder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class GpodderAPI {

	private HttpURLConnection connection;
	private URL urlGetNewPodcasts;
	private String user;
	private String password;

	public GpodderAPI(String user, String password) {
		this.user = user;
		this.password = password;
	}

//	public Podcast getNewPodcasts(Date since)
//			throws IOException {
//
//		urlGetNewPodcasts = new URL("http://gpodder.net/api/1/episodes/"
//				+ user + ".json?since=" + since.getTime());
//
//		String userPassword = user + ":" + password;
//		String basicAuth = new sun.misc.BASE64Encoder().encode(userPassword
//				.getBytes());
//
//		connection = (HttpURLConnection) urlGetNewPodcasts.openConnection();
//		connection.setRequestProperty("Authorization", "Basic " + basicAuth);
//
//		InputStream response = connection.getInputStream();
//		
//		return parseResponse(response);
//
//	}
	
	public GpodderUpdates parseResponse(InputStream inputStream){
		
		ObjectMapper mapper = new ObjectMapper();
		GpodderUpdates pc = null;
		try {
			pc = mapper.readValue(inputStream, GpodderUpdates.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pc;
		
	}



}
