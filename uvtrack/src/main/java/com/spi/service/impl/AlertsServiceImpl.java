package com.spi.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spi.config.ApplicationConfig;
import com.spi.dao.AlertsDAO;
import com.spi.dao.PickDropRequestDAO;
import com.spi.dao.StudentsDAO;
import com.spi.dao.UserDAO;
import com.spi.dao.UserSchoolDAO;
import com.spi.domain.Alert;
import com.spi.domain.PickOrDropRequest;
import com.spi.domain.Student;
import com.spi.domain.UserSchool;
import com.spi.service.AlertsService;
import com.spi.service.BaseService;
import com.spi.user.api.PickUpRequestModel;

@Service("alertsService")
public class AlertsServiceImpl extends BaseService implements AlertsService {

	private static final Logger LOG = LoggerFactory.getLogger(AlertsServiceImpl.class);

	private AlertsDAO alertsRepository;

	private ApplicationConfig applicationConfig;

	@Autowired
	private PickDropRequestDAO pickUpRequestRepository;

	@Autowired
	private UserSchoolDAO userschoolRepository;

	@Autowired
	private StudentsDAO studentRepository;

	@Autowired
	private UserDAO userRepository;

	public AlertsServiceImpl(Validator validator) {
		super(validator);
	}

	@Autowired
	public AlertsServiceImpl(Validator validator, ApplicationConfig applicationConfig) {
		this(validator);
		this.applicationConfig = applicationConfig;
	}

	@Transactional
	public Page<Alert> getLatestAlerts(Long userId, Pageable topTen) {

		return alertsRepository.findByUserId(userId, topTen);
	}

	@Transactional
	public Page<PickOrDropRequest> getpickAlerts(Long userId, Pageable topTen) {
		UserSchool us = userschoolRepository.getSchoolByUser(userId);
		return pickUpRequestRepository.getPickAlerts(us.getSchool().getId(), topTen);

	}

	@Transactional
	public int getTotalAlerts(String userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Transactional
	public List<Alert> getAllAlerts(Long userId) {
		// TODO Auto-generated method stub
		return alertsRepository.findAllByUserId(userId);
	}

	public List<PickUpRequestModel> getPickUpAlerts(Long userId) {
		List<PickUpRequestModel> pickup = new ArrayList<PickUpRequestModel>();
		UserSchool us = userschoolRepository.getSchoolByUser(userId);
		List<PickOrDropRequest> pc = null;
		try {
			pc = pickUpRequestRepository.getPickUpAlertsByUser(us.getSchool().getId());

			for (PickOrDropRequest pick : pc) {
				PickUpRequestModel pickUpRequestModel = new PickUpRequestModel();
				Student st = studentRepository.findOne(pick.getStudentId());
				pickUpRequestModel.setStudentId(st.getId());
				pickUpRequestModel.setFirstName(st.getFirstName());
				pickUpRequestModel.setLastName(st.getLastName());
				pickUpRequestModel.setUserName(st.getUser().getFirstName());
				pickUpRequestModel.setUserLName(st.getUser().getLastName());
				pickUpRequestModel.setClassName(st.getStudentClass());
				Date time = pick.getPickUpDate();
				SimpleDateFormat spd = new SimpleDateFormat("HH:mm");
				pickUpRequestModel.setPickUpTime(pick.getTimeCreated());
				pickUpRequestModel.setPickTime(spd.format(time));
				if (pick.getPickOrDrop() == 'p') {
					pickUpRequestModel.setMessage("pick");
				} else if (pick.getPickOrDrop() == 'd') {
					pickUpRequestModel.setMessage("drop");
				}
				pickUpRequestModel.setPresentFlag(pick.getPickOrDrop());
				pickUpRequestModel.setUuid(pick.getIdentifier().toString());
				pickup.add(pickUpRequestModel);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return pickup;
	}

	@Autowired
	public void setAlertsRepository(AlertsDAO alertsRepository) {
		this.alertsRepository = alertsRepository;
	}

}
