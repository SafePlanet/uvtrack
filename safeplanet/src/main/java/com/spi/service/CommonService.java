package com.spi.service;

import java.util.List;

import com.spi.VM.EmailTemplateVM;
import com.spi.VM.MessageTemplateVM;
import com.spi.domain.Student;
import com.spi.gateway.EmailServicesGateway;
import com.spi.user.api.SendEmailModel;


public interface CommonService {
	
	public List<EmailTemplateVM> getAllEmailTemplates();
	
	public List<MessageTemplateVM> getMessageTemplates();
	
	public void sendEmail(EmailServicesGateway emailServicesGateway, String userId, SendEmailModel model);
	
	public List<Student> getStudentForSendingMessage(boolean isSendToAll, String routeUuid, String userId);

}