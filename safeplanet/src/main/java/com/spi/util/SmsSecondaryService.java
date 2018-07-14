package com.spi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spi.config.ApplicationConfig;

public final class SmsSecondaryService {
	Logger LOG = LoggerFactory.getLogger(SmsSecondaryService.class);
	
	private boolean isSendMessage = false;
	
	private final static String smsLinkUserName = "http://msg.clickblickdigital.com/http-api.php?username=";
	private final static String smsLinkPassword = "&password=";
	private final static String smsLinkDestination = "&senderid=iTrack&route=7&number=";
	private final static String smsLinkMessage = "&message=";

	
	//http://msg.clickblickdigital.com/http-api.php?username=itrack&password=123456&senderid=iTrack&route=7&number=9555344552&message=hello there
		
	private final static String smsUserName = "itrack";
	private final static String smsUserPassword = "52365236";// Password need to be changed to 123456
										// when need to run this

	private static SmsSecondaryService instance = null;

	public static SmsSecondaryService getInstance(ApplicationConfig config) {
		if (instance == null) {
			instance = new SmsSecondaryService();
			instance.isSendMessage = config.isSmsEnabled();
		}
		return instance;
	}
	
	public static void main(String[] args) {
//		SmsSecondaryService.getInstance().sendMessage("704200870", "Test Message");
	}

	/**
	 * On Success returns "Message Submitted Successfully<pre>msg-id : MTQ4MTkwNjY="
	 * @param destination
	 * @param message
	 * @return
	 */
	public String sendMessage(String destination, String message) {
		LOG.debug("BACKUP :: " + isSendMessage + "Sending message to " + destination + " Message ::" +message);
		String line = null;
		if (isSendMessage == false || destination == null) {
			return null;
		}
		String linkToSendMessage = smsLinkUserName + smsUserName + smsLinkPassword + smsUserPassword + smsLinkDestination
				+ destination + smsLinkMessage + message;
		try {
			URL url = new URL(linkToSendMessage.replace(" ", "%20"));
			InputStream is = url.openConnection().getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			line = reader.readLine();
			System.out.println(line);
			reader.close();
		} catch (IOException ioEx) {
			LOG.error("BACKUP :: Problem in sending message ", ioEx);
			line = "Exception";
		}
		return line;
	}
	
	public String sendAbsentMessage(String destination, String message) {
		LOG.debug("BACKUP :: Sending Absent message to " + destination + " Message ::" +message);
		String line = null;
		if (destination == null) {
			return null;
		}
		String linkToSendMessage = smsLinkUserName + smsUserName + smsLinkPassword + smsUserPassword + smsLinkDestination
				+ destination + smsLinkMessage + message;
		try {
			URL url = new URL(linkToSendMessage.replace(" ", "%20"));
			InputStream is = url.openConnection().getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			line = reader.readLine();
			System.out.println(line);
			reader.close();
		} catch (IOException ioEx) {
			LOG.error("BACKUP :: Problem in sending message ", ioEx);
			line = "Exception";
		}
		return line;
	}
	

}
