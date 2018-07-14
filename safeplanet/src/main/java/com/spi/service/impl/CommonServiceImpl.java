package com.spi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spi.VM.EmailTemplateVM;
import com.spi.VM.MessageTemplateVM;
import com.spi.config.SystemConstant;
import com.spi.dao.EmailTemplateDAO;
import com.spi.dao.MessageTemplateDAO;
import com.spi.dao.RouteDAO;
import com.spi.dao.SchoolConfigDAO;
import com.spi.dao.StudentsDAO;
import com.spi.dao.UserDAO;
import com.spi.domain.EmailTemplate;
import com.spi.domain.MessageTemplate;
import com.spi.domain.RouteScheduleDetails;
import com.spi.domain.Student;
import com.spi.domain.User;
import com.spi.gateway.EmailServicesGateway;
import com.spi.service.BaseService;
import com.spi.service.CommonService;
import com.spi.service.UserService;
import com.spi.user.api.SendEmailModel;
import com.spi.util.DateUtil;

@Service("commonService")
public class CommonServiceImpl extends BaseService implements CommonService {
	
	private static Logger LOG = LoggerFactory.getLogger(CommonServiceImpl.class);

	@Autowired
	public EmailTemplateDAO emailTemplateDAO;
	
	@Autowired
	public MessageTemplateDAO messageTemplateDAO;

	@Autowired
	public UserService userService;

	@Autowired
	private UserDAO userRepository;
	
	@Autowired
	private RouteDAO routeRepository;

	@Autowired
	private SchoolConfigDAO schoolConfigDAO;

	@Autowired
	private StudentsDAO studentRepository;

	public CommonServiceImpl(Validator validator, EmailTemplateDAO emailTemplateDAO) {
		super(validator);
		this.emailTemplateDAO = emailTemplateDAO;
	}

	@Autowired
	public CommonServiceImpl(Validator validator) {
		super(validator);
	}

	public List<EmailTemplateVM> getAllEmailTemplates() {
		List<EmailTemplate> emailTemplates = emailTemplateDAO.findUserInitiatedTemplated();

		List<EmailTemplateVM> emailTemplateVms = new ArrayList<EmailTemplateVM>();
		for (EmailTemplate emailTemplate : emailTemplates) {
			emailTemplateVms.add(new EmailTemplateVM(emailTemplate));
		}
		return emailTemplateVms;
	}
	
	@Override
	public List<MessageTemplateVM> getMessageTemplates() {
		List<MessageTemplate> messageTemplates = messageTemplateDAO.findUserInitiatedTemplates();

		List<MessageTemplateVM> emailTemplateVms = new ArrayList<MessageTemplateVM>();
		for (MessageTemplate messageTemplate : messageTemplates) {
			emailTemplateVms.add(new MessageTemplateVM(messageTemplate));
		}
		return emailTemplateVms;
	}

	public void sendEmail(EmailServicesGateway emailServicesGateway, String userId, SendEmailModel model) {
		int count = 0;
		
		RouteScheduleDetails routeScheduleDetails = routeRepository.findRouteScheduleDetailByPrimaryKey(model.getScheduleId());
		Date startTime = new Date();
		Date endTime = new Date();
		if(routeScheduleDetails != null){
			startTime.setHours(routeScheduleDetails.getStartTime().getHours());
			startTime.setMinutes(routeScheduleDetails.getStartTime().getMinutes());
			startTime.setSeconds(0);
			startTime = DateUtil.getTimeOnly(startTime);
			endTime.setHours(routeScheduleDetails.getEndTime().getHours());
			endTime.setMinutes(routeScheduleDetails.getEndTime().getMinutes());
			endTime.setSeconds(0);
			endTime = DateUtil.getTimeOnly(endTime);
		}
		
//		boolean isPickTimeSlot = false;
//		User user = userRepository.findByUuid(userId);
//		if (model.getScheduleId() == 1) {
//			isPickTimeSlot = true;
//		}
		List<Student> students = getStudentForSendingMessage(model.isSendToAll(), model.getRouteUuid(), userId);

		for (Student student : students) {
			
			Date pickTime = student.getWayPoint().getTimePick();
			Date dropTime = student.getWayPoint().getTimeDrop();
			
			if ((model.isSendToAll() || ((startTime.before(pickTime) && endTime.after(pickTime))
					|| (startTime.before(dropTime) && endTime.after(dropTime)))) && student.getIsApproved() == 'Y' 
					&& (student.getUser() != null && student.getUser().getIsEnable() == 1)) {
				sendAllEmailTypes(emailServicesGateway, student.getUser(), model, count == (students.size() - 2));
				LOG.info("Sending email to " + student.getUser().getEmailAddress());
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					LOG.error("Error in sending email for student " + student.getFirstName() + " " + student.getLastName() + "("+ student.getId() + ")", e);
				}
				count++;
			}
		}
	}

	public List<Student> getStudentForSendingMessage(boolean isSendToAll, String routeUuid, String userId) {
		List<Student> students;
		if (isSendToAll) {
			students = studentRepository.getAllStudentDetailsAdmin(userId);
		} else {
			students = routeRepository.findStudentsForRoute(routeUuid);
		}
		
		return students;
	}

	private void sendAllEmailTypes(EmailServicesGateway emailServicesGateway, User user, SendEmailModel model, boolean keepSchoolInCc) {
		if (model.getEmailTemplateId().equals("1000")) {// Welcome email
			userService.sendWelcomeEmail(user, keepSchoolInCc);
		} else if (model.getEmailTemplateId().equals("1001")) {// Custom Email
			userService.sendCustomEmail(user, model.getSubject(), model.getMessage(), keepSchoolInCc);
		}
	}

	public void setEmailTemplateDAO(EmailTemplateDAO emailTemplateDAO) {
		this.emailTemplateDAO = emailTemplateDAO;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setUserRepository(UserDAO userRepository) {
		this.userRepository = userRepository;
	}

	public void setRouteRepository(RouteDAO routeRepository) {
		this.routeRepository = routeRepository;
	}

	public void setStudentRepository(StudentsDAO studentRepository) {
		this.studentRepository = studentRepository;
	}


}