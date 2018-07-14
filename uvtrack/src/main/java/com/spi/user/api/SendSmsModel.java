package com.spi.user.api;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendSmsModel extends RouteScheduleModel {

	private String messageTemplateId = null;
	private String message = null;
	private long studentId;
	private boolean sendSms;
	private boolean sendAppMessage;

	public String getMessageTemplateId() {
		return messageTemplateId;
	}

	public void setMessageTemplateId(String messageTemplateId) {
		this.messageTemplateId = messageTemplateId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSendSms() {
		return sendSms;
	}

	public void setSendSms(boolean sendSms) {
		this.sendSms = sendSms;
	}

	public boolean isSendAppMessage() {
		return sendAppMessage;
	}

	public void setSendAppMessage(boolean sendAppMessage) {
		this.sendAppMessage = sendAppMessage;
	}
	

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	@Override
	public String toString() {
		return "messageTemplateId = " + this.messageTemplateId + "\n sendSms = " + this.sendSms + "\n sendAppMessage = "
				+ this.sendAppMessage + "\n message = " + this.message;
	}

}
