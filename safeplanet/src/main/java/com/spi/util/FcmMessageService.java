package com.spi.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spi.config.ApplicationConfig;

public class FcmMessageService {

	private Logger LOG = LoggerFactory.getLogger(FcmMessageService.class);
	
	boolean isSendMessage = true;
	
	private String fcmKey;
	
	private static FcmMessageService instance = null;
	
	public static FcmMessageService getInstance(ApplicationConfig config) {
		if (instance == null) {
			instance = new FcmMessageService();
			instance.fcmKey = config.getFcmAuthKey();
			instance.isSendMessage = config.isFcmEnabled();
		}
		return instance;
	}
	
	public void sendMessage(String fcmtoken, String message) {
		if(!isSendMessage || fcmtoken == null || fcmtoken == ""){
			return;
		}
		LOG.info("sending FCM Token = {} \n message = {}", fcmtoken, message);
		try {
			HttpClient httpClient = new DefaultHttpClient();
			String body = "{\r\n" + "  \"data\": {\r\n" + "    \"title\": \"iSafeTrack\",\r\n" + "    \"ll\":99,\r\n" + "  \"body\": \"" + message
					+ "\"\r\n" + "    \"icon\": \"firebase-logo.png\",\r\n" + " \"sound\": \"mydefault\",\r\n"  + " \"click_action\": \"myAction\"\r\n" + "  },\r\n"
					+ "  \"to\": \"" + fcmtoken + "\"\r\n" + "}";

			HttpPost request = new HttpPost("http://fcm.googleapis.com/fcm/send");
			request.addHeader("content-type", "application/json");
			request.addHeader("Authorization", fcmKey);
			HttpEntity entity = new ByteArrayEntity(body.getBytes("UTF-8"));
			request.setEntity(entity);
			org.apache.http.HttpResponse response = httpClient.execute(request);
			LOG.debug("FCM Response: " + response.toString());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
}
