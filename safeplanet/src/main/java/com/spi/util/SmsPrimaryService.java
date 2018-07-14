package com.spi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spi.config.ApplicationConfig;

public final class SmsPrimaryService {
	Logger LOG = LoggerFactory.getLogger(SmsPrimaryService.class);
	
	boolean isSendMessage = false;
	
	private final static String smsLinkUserName = "http://103.16.101.52:8080/sendsms/bulksms?username=";
	private final static String smsLinkPassword = "&password=";
	private final static String smsLinkDestination = "&type=0&dlr=1&destination=";
	private final static String smsLinkSource = "&source=";
	private final static String smsLinkMessage = "&message=";

	private final static String smsUserName = "clik-spinv";
	private final static String smsUserPassword = "23652365";// Password need to be changed to 123456
										// when need to run this
	private final static String sourceId = "iTrack";

	private static SmsPrimaryService instance = null;
	ApplicationConfig config;
	
	public static void main(String[] args) {
//		SmsPrimaryService.getInstance().sendMessage("7042100870", "Test Message");
	}

	public static SmsPrimaryService getInstance(ApplicationConfig config) {
		if (instance == null) {
			instance = new SmsPrimaryService();
			instance.isSendMessage = config.isSmsEnabled();
			instance.config = config;
		}
		return instance;
	}

	/**
	 * On Success it returns "1701|917042100870:92fdc32c-c375-44a3-b704-8186c9f7e304"
	 * On Faliure it returns "1701|917042100870"
	 * @param destination
	 * @param message
	 * @return
	 */
	public String sendMessage(String destination, String message) {
		LOG.debug(isSendMessage + "Sending message to " + destination + " Message ::" +message);
		String line = null;
		if (isSendMessage == false || destination == null) {
			return null;
		}
		String linkToSendMessage = smsLinkUserName + smsUserName + smsLinkPassword + smsUserPassword + smsLinkDestination
				+ destination + smsLinkSource + sourceId + smsLinkMessage + message;
		try {
			// java.awt.Desktop.getDesktop().browse(java.net.URI.create(linkToSendMessage));
			URL url = new URL(linkToSendMessage.replace(" ", "%20"));
			InputStream is = url.openConnection().getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			line = reader.readLine();
			System.out.println(line);
			reader.close();
			// on Failure try sending using secondary SMS service
			if(line != null && line.length() < 20)  SmsSecondaryService.getInstance(this.config).sendMessage(destination, message);
			
		} catch (IOException ioEx) {
			LOG.error("Problem in sending message ", ioEx);
			SmsSecondaryService.getInstance(this.config).sendMessage(destination, message);
			line = "Exception";
		}
		return line;
	}
	
}
