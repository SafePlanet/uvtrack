package com.spi.user.api;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendEmailModel extends RouteScheduleModel {

	private String emailTemplateId = null;
	private String subject = null;
	private String message = null;

	public String getEmailTemplateId() {
		return emailTemplateId;
	}

	public void setEmailTemplateId(String emailTemplateId) {
		this.emailTemplateId = emailTemplateId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
