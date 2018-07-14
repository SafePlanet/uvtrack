package com.spi.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FcmMessageServiceTester {

	private Logger LOG = LoggerFactory.getLogger(FcmMessageServiceTester.class);
	
	boolean isSendMessage = true;
	
	private String fcmKey = "key=AAAAc-7Dbuc:APA91bELEfTJi-mzHOsrR1dHD9BV4T60ZuRndmRzvI07g2SbwYyXc3L9xn_wmvu3Oy0f3RjnRRkND8muao2qn54bhBosp9AcmBFO67iD33xrpsaW0oFPEK6eCx23owGXz3wPtZpU6XMd";
	
	private static FcmMessageServiceTester instance = null;
	
	public static FcmMessageServiceTester getInstance() {
		if (instance == null) {
			instance = new FcmMessageServiceTester();
		}
		return instance;
	}
	
	public static void main(String[] args) {
		FcmMessageServiceTester.getInstance().sendMessage("fdrOlVLZceI:APA91bE2HCJiW58IHSxHVqAU9kiErtn6xDML_eoPftQk5Vo3NW6ypC3syeL-lPiocydeRc1YoZSiEuKOX1uihuxXLhb7w6ct8kFwh1p8vgVtZnE1rpXxSg6zmrjVAjQFeT3D59NKXXq7", 
				"This is test Message");
	}
	
	public void sendMessage(String fcmtoken, String message) {
		LOG.info("sending FCM Token {} \n message {}", fcmtoken, message);
		if(!isSendMessage || fcmtoken == null || fcmtoken == ""){
			return;
		}
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
			LOG.info("FCM Response: " + response.toString());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
}
