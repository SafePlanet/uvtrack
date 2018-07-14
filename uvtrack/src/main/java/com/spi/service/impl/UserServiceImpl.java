/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.spi.VM.RouteVM;
import com.spi.VM.StudentVM;
import com.spi.VM.UserVM;
import com.spi.api.ServiceResponse;
import com.spi.config.ApplicationConfig;
import com.spi.config.SystemConstant;
import com.spi.dao.AbsentDAO;
import com.spi.dao.AbsentReasonDAO;
import com.spi.dao.AlertsConfigDAO;
import com.spi.dao.ClassGroupDAO;
import com.spi.dao.DeviceDAO;
import com.spi.dao.DeviceModelTypeDAO;
import com.spi.dao.FleetDAO;
import com.spi.dao.GroupsDAO;
import com.spi.dao.RouteDAO;
import com.spi.dao.SchoolConfigDAO;
import com.spi.dao.SchoolDAO;
import com.spi.dao.SchoolHolidayMasterDataDAO;
import com.spi.dao.SchoolHolidaysDAO;
import com.spi.dao.StudentsDAO;
import com.spi.dao.UserDAO;
import com.spi.dao.UserSchoolDAO;
import com.spi.dao.UsersDAO;
import com.spi.dao.VisitorRequestDAO;
import com.spi.dao.WayPointDAO;
import com.spi.domain.Absent;
import com.spi.domain.AbsentReason;
import com.spi.domain.Address;
import com.spi.domain.AddressType;
import com.spi.domain.AlertConfig;
import com.spi.domain.AuthorizationToken;
import com.spi.domain.ClassGroup;
import com.spi.domain.Complaints;
import com.spi.domain.DeviceModelType;
import com.spi.domain.Devices;
import com.spi.domain.Fleet;
import com.spi.domain.Groups;
import com.spi.domain.Role;
import com.spi.domain.Route;
import com.spi.domain.School;
import com.spi.domain.SchoolConfig;
import com.spi.domain.SchoolHoliday;
import com.spi.domain.SchoolHolidayMasterData;
import com.spi.domain.State;
import com.spi.domain.Student;
import com.spi.domain.User;
import com.spi.domain.UserSchool;
import com.spi.domain.Users;
import com.spi.domain.VerificationToken;
import com.spi.domain.VisitorRequest;
import com.spi.domain.WayPoint;
import com.spi.email.EmailServiceTokenModel;
import com.spi.exception.AuthenticationException;
import com.spi.exception.AuthorizationException;
import com.spi.exception.DuplicateUserException;
import com.spi.exception.UserNotFoundException;
import com.spi.gateway.EmailServicesGateway;
import com.spi.service.BaseService;
import com.spi.service.CommonService;
import com.spi.service.StudentService;
import com.spi.service.UserService;
import com.spi.user.api.AbsentReasonModel;
import com.spi.user.api.AlertNotificationsConfig;
import com.spi.user.api.AuthenticatedUserToken;
import com.spi.user.api.ChangePasswordRequest;
import com.spi.user.api.ClassGroupModel;
import com.spi.user.api.ComplaintMessageRequest;
import com.spi.user.api.CreateUserRequest;
import com.spi.user.api.DeviceModel;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.LoginRequest;
import com.spi.user.api.SchoolHolidayMasterDataModel;
import com.spi.user.api.SchoolHolidayModel;
import com.spi.user.api.SchoolHolidaysModel;
import com.spi.user.api.SchoolModel;
import com.spi.user.api.StudentRequest;
import com.spi.user.api.UpdateUserRequest;
import com.spi.user.api.visitorModel;
import com.spi.user.social.JpaUsersConnectionRepository;
import com.spi.util.SmsPrimaryService;
import com.spi.util.StringUtil;

import javassist.compiler.ast.Visitor;

/**
 * Service for managing User accounts
 *
 * @author:
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseService implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	ApplicationConfig config;
	/**
	 * For Social API handling
	 */
	private UsersConnectionRepository jpaUsersConnectionRepository;

	private UserDAO userRepository;

	private UserSchoolDAO userSchoolRepository;

	private UsersDAO usersRepository;

	private GroupsDAO groupsRepository;

	private StudentsDAO studentsRepository;

	private RouteDAO routeRepository;

	private AbsentReasonDAO absentReasonRepository;

	private SchoolDAO schoolRepository;

	@Autowired
	private DeviceDAO deviceRepository;

	@Autowired
	private SchoolConfigDAO schoolConfigRepository;

	private SchoolHolidaysDAO schoolHolidayRepository;

	private SchoolHolidayMasterDataDAO schoolHolidayMasterDataRepository;

	private ApplicationConfig applicationConfig;

	@Autowired
	protected StudentService studentService;
	
	@Autowired
	private AbsentDAO absentRepository;
	
	@Autowired
	private VisitorRequestDAO visitorRepository;
	
	@Autowired
	private ClassGroupDAO classGroupRepository;

	@Autowired
	private SchoolConfigDAO schoolConfigDAO;

	@Autowired
	private DeviceModelTypeDAO deviceModelTypeRepository;

	@Autowired
	private AlertsConfigDAO alertsConfigDAO;

	@Autowired
	private FleetDAO fleetDAO ;
	
	@Autowired
	protected EmailServicesGateway emailServicesGateway;

	@Autowired
	protected CommonService commonService;

	@Autowired
	protected WayPointDAO wayPointDao;

	public UserServiceImpl(Validator validator) {
		super(validator);
	}

	@Autowired
	public UserServiceImpl(UsersConnectionRepository usersConnectionRepository, Validator validator, ApplicationConfig applicationConfig) {
		this(validator);
		this.jpaUsersConnectionRepository = usersConnectionRepository;
		((JpaUsersConnectionRepository) this.jpaUsersConnectionRepository).setUserService(this);
		this.applicationConfig = applicationConfig;
	}

	/**
	 * {@inheritDoc}
	 *
	 * This method creates a User with the given Role. A check is made to see if
	 * the username already exists and a duplication check is made on the email
	 * address if it is present in the request.
	 * <P>
	 * </P>
	 * The password is hashed and a AuthorizationToken generated for subsequent
	 * authorization of role-protected requests.
	 *
	 */

	@Transactional
	public AuthenticatedUserToken createUser(CreateUserRequest request, Role role) {
		validate(request);
		User searchedForUser = userRepository.findByEmailAddress(request.getUser().getEmailAddress());
		if (searchedForUser != null) {
			throw new DuplicateUserException();
		}

		User newUser = createNewUser(request, role);
		AuthenticatedUserToken token = new AuthenticatedUserToken(newUser.getUuid().toString(), createAuthorizationToken(newUser).getToken());
		userRepository.save(newUser);
		return token;
	}

	@Transactional
	public AuthenticatedUserToken createUser(Role role) {
		User user = new User();
		user.setRole(role);
		AuthenticatedUserToken token = new AuthenticatedUserToken(user.getUuid().toString(), createAuthorizationToken(user).getToken());
		userRepository.save(user);
		return token;
	}

	@Transactional
	public Role getUserRole(String userUUID) {
		User user = ensureUserIsLoaded(userUUID);
		if (user != null) {
			return user.getRole();
		} else {
			return Role.anonymous;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Login supports authentication against an email attribute. If a User is
	 * retrieved that matches, the password in the request is hashed and
	 * compared to the persisted password for the User account.
	 */
	@Transactional
	public AuthenticatedUserToken login(LoginRequest request) {
		validate(request);
		AuthenticatedUserToken token=new AuthenticatedUserToken();
		LOG.debug(request.getUsername() + " user trying to access the system.");
		User user = null;
		UserSchool school=null;
		user = userRepository.findByEmailAddress(request.getUsername());
		
		if (user == null || user.getIsEnable() == 0) {
			token.setMessage("User not found");
		}else{
		String hashedPassword = null;
		try {
			hashedPassword = user.hashPassword(request.getPassword());
		} catch (Exception e) {
			token.setMessage("User not found");
		}
		if (hashedPassword.equals(user.getHashedPassword()) && user.getRole().equals(com.spi.domain.Role.administrator)) {
			school=userSchoolRepository.getSchoolByUser(user.getId());
			long days=getvalidDays(school);
			token= new AuthenticatedUserToken(user.getUuid().toString(), createAuthorizationToken(user).getToken(),String.valueOf(days));
		} else if(hashedPassword.equals(user.getHashedPassword()) && user.getRole().equals(com.spi.domain.Role.superAdmin)){
			token= new AuthenticatedUserToken(user.getUuid().toString(), createAuthorizationToken(user).getToken(),String.valueOf(100));
		}else if(hashedPassword.equals(user.getHashedPassword()) && user.getRole().equals(com.spi.domain.Role.authenticated)){
			token= new AuthenticatedUserToken(user.getUuid().toString(), createAuthorizationToken(user).getToken(),String.valueOf(100));
		}else if(hashedPassword.equals(user.getHashedPassword()) && user.getRole().equals(com.spi.domain.Role.teacher)){
			token= new AuthenticatedUserToken(user.getUuid().toString(), createAuthorizationToken(user).getToken(),String.valueOf(100));
		} else if(hashedPassword.equals(user.getHashedPassword()) && user.getRole().equals(com.spi.domain.Role.gateKeeper)){
			token= new AuthenticatedUserToken(user.getUuid().toString(), createAuthorizationToken(user).getToken(),String.valueOf(100));
		} else {
		     token.setMessage("Password not matched");
		}
		
	}return token;
	}
	public Long getvalidDays(UserSchool school){
		Date fromDate=new Date();
		long days=0;
		    Date serviceEndDate=school.getSchool().getServiceEndDate();
		    days=serviceEndDate.compareTo(fromDate);
		    if(days>0){
		    	long diff = Math.abs(serviceEndDate.getTime() - fromDate.getTime());
				days = diff / (24 * 60 * 60 * 1000);
		    }
		return days;
		
		
	}

	/**
	 * {@inheritDoc}
	 *
	 * Associate a Connection with a User account. If one does not exist a new
	 * User is created and linked to the {@link com.spi.domain.SocialUser}
	 * represented in the Connection details.
	 *
	 * <P>
	 * </P>
	 *
	 * A AuthorizationToken is generated and any Profile data that can be
	 * collected from the Social account is propagated to the User object.
	 *
	 */
	@Transactional
	public AuthenticatedUserToken socialLogin(Connection<?> connection) {

		List<String> userUuids = jpaUsersConnectionRepository.findUserIdsWithConnection(connection);
		if (userUuids.size() == 0) {
			throw new AuthenticationException();
		}
		User user = userRepository.findByUuid(userUuids.get(0)); // take the
		// first one
		// if there
		// are
		// multiple
		// userIds
		// for this
		// provider
		// Connection
		if (user == null || user.getIsEnable() == 0) {
			throw new AuthenticationException();
		}
		updateUserFromProfile(connection, user);
		return new AuthenticatedUserToken(user.getUuid().toString(), createAuthorizationToken(user).getToken());
	}

	public ExternalUser getUser(ExternalUser requestingUser, String userIdentifier) {
		Assert.notNull(requestingUser);
		Assert.notNull(userIdentifier);
		User user = ensureUserIsLoaded(userIdentifier);
		return new ExternalUser(user);
	}

	public void deleteUser(ExternalUser userMakingRequest, String userId) {
		Assert.notNull(userMakingRequest);
		Assert.notNull(userId);
		User userToDelete = ensureUserIsLoaded(userId);
		if (userMakingRequest.getRole().equalsIgnoreCase(Role.administrator.toString())
				&& (userToDelete.hasRole(Role.anonymous) || userToDelete.hasRole(Role.authenticated))) {
			userRepository.delete(userToDelete);
		} else {
			throw new AuthorizationException("User cannot be deleted. Only users with anonymous or authenticated role can be deleted.");
		}
	}

	@Transactional
	public ExternalUser saveUser(String userId, UpdateUserRequest request) {
		validate(request);
		User user = ensureUserIsLoaded(userId);
		if (request.getFirstName() != null) {
			user.setFirstName(request.getFirstName());
		}
		if (request.getLastName() != null) {
			user.setLastName(request.getLastName());
		}
		if (request.getEmailAddress() != null) {
			if (!request.getEmailAddress().equals(user.getEmailAddress())) {
				user.setEmailAddress(request.getEmailAddress());
				user.setVerified(false);
			}
		}
		userRepository.save(user);
		return new ExternalUser(user);
	}

	@Transactional
	public ExternalUser updateUser(String userId, UpdateUserRequest request) {
		validate(request);
		// User user = new User(request);
		User existingUser = ensureUserIsLoaded(userId);

		if (request.getEmailAddress() != null) {
			if (!request.getEmailAddress().equals(existingUser.getEmailAddress())) {
				existingUser.setEmailAddress(request.getEmailAddress());
				existingUser.setVerified(false);
			}
		}
		existingUser.setUserDetails(request);
		userRepository.save(existingUser);
		return new ExternalUser(existingUser);
	}

	@Override
	public AuthorizationToken createAuthorizationToken(User user) {
		if (user.getAuthorizationToken() == null) {
			user.setAuthorizationToken(new AuthorizationToken(user, applicationConfig.getAuthorizationExpiryTimeInSeconds()));
		} else if (user.getAuthorizationToken() != null && user.getAuthorizationToken().hasExpired()) {
			// user.setAuthorizationToken(new AuthorizationToken(user,
			// applicationConfig.getAuthorizationExpiryTimeInSeconds()));
			user.getAuthorizationToken()
					.setExpirationDate(new Date(System.currentTimeMillis() + (applicationConfig.getAuthorizationExpiryTimeInSeconds() * 1000L)));
		}
		userRepository.save(user);

		return user.getAuthorizationToken();
	}

	private User createNewUser(CreateUserRequest request, Role role) {
		User userToSave = new User(request.getUser());
		Address addressToSave = new Address(request.getAddress(), userToSave);
		try {
			ArrayList<Address> addresses = new ArrayList<Address>();
			addresses.add(addressToSave);
			userToSave.setAddresses(addresses);
			userToSave.setHashedPassword(userToSave.hashPassword(request.getPassword().getPassword()));
		} catch (Exception e) {
			throw new AuthenticationException();
		}
		userToSave.setRole(role);
		userToSave.setUsageConsent('1');
		userToSave.setIsEnable(1);
		return userToSave;
	}

	private void updateUserFromProfile(Connection<?> connection, User user) {
		UserProfile profile = connection.fetchUserProfile();
		user.setEmailAddress(profile.getEmail());
		user.setFirstName(profile.getFirstName());
		user.setLastName(profile.getLastName());
		// users logging in from social network are already verified
		user.setVerified(true);
		if (user.hasRole(Role.anonymous)) {
			user.setRole(Role.authenticated);
		}
		userRepository.save(user);
	}

	private User ensureUserIsLoaded(String userIdentifier) {
		User user = null;
		if (StringUtil.isValidUuid(userIdentifier)) {
			user = userRepository.findByUuid(userIdentifier);
		} else {
			user = userRepository.findByEmailAddress(userIdentifier);
		}
		if (user == null) {
			throw new UserNotFoundException();
		}
		return user;
	}

	@Autowired
	public void setUserRepository(UserDAO userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setUserSchoolRepository(UserSchoolDAO userSchoolRepository) {
		this.userSchoolRepository = userSchoolRepository;
	}

	@Autowired
	public void setUsersRepository(UsersDAO usersRepository) {
		this.usersRepository = usersRepository;
	}

	@Autowired
	public void setGroupsRepository(GroupsDAO groupsRepository) {
		this.groupsRepository = groupsRepository;
	}

	@Autowired
	public void setStudentsRepository(StudentsDAO studentsRepository) {
		this.studentsRepository = studentsRepository;
	}

	@Autowired
	public void setAbsentReasonRepository(AbsentReasonDAO absentReasonRepository) {
		this.absentReasonRepository = absentReasonRepository;
	}

	@Autowired
	public void setSchoolRepository(SchoolDAO schoolRepository) {
		this.schoolRepository = schoolRepository;
	}

	@Autowired
	public void setSchoolHolidayRepository(SchoolHolidaysDAO schoolHolidayRepository) {
		this.schoolHolidayRepository = schoolHolidayRepository;
	}

	@Autowired
	public void setSchoolHolidayMasterDataRepository(SchoolHolidayMasterDataDAO schoolHolidayMasterDataRepository) {
		this.schoolHolidayMasterDataRepository = schoolHolidayMasterDataRepository;
	}

	@Autowired
	public void setRouteRepository(RouteDAO routeRepository) {
		this.routeRepository = routeRepository;
	}

	@Override
	public List<Student> getStudentDetails(String userId) {

		List<Student> students = userRepository.getStudentDetails(userId);
		return students;

	}

	public ExternalUser updateUserNotificationSettings(String userId, String configType, AlertNotificationsConfig config) {
		User user = ensureUserIsLoaded(userId);

		AlertConfig alertConfig = new AlertConfig();
		// user.addAlertConfigs(alertConfig);

		// alertConfig.setUser(user);
		if ("pickTime".equals(configType)) {
			// alertConfig.setPickStartTime(config.getPickStartTime());
			// alertConfig.setPickEndTime(config.getPickEndTime());
		} else if ("dropTime".equals(configType)) {
			// alertConfig.setDropStartTime(config.getDropStartTime());
			// alertConfig.setDropEndTime(config.getDropEndTime());
		} else if ("alertDays".equals(configType)) {
			// alertConfig.setActiveDays(config.getActiveDays());
		} else if ("alertTypesSel".equals(configType)) {
			// alertConfig.setAlertTypes(config.getAlertTypes());
		} else if ("enableNotification".equals(configType)) {
			alertConfig.setActiveTime(config.getActiveTime());
		}

		userRepository.save(user);

		return new ExternalUser(user);
	}

	public List<AlertNotificationsConfig> getUserNotificationSettings(String userId) {
		// User user = ensureUserIsLoaded(userId);

		List<AlertNotificationsConfig> alertNotificationsConfigs = new ArrayList<>();
		// List<AlertConfig> alertConfigs = user.getAlertConfigs();

		// for (AlertConfig alertConfig : alertConfigs) {
		// // if (alertConfig.getStudentId() != 0) {
		// // alertNotificationsConfigs.add(new
		// AlertNotificationsConfig(alertConfig));
		// // }
		// }

		return alertNotificationsConfigs;
	}

	public void addComplaintMessage(String userId, String message) {
		User user = ensureUserIsLoaded(userId);

		Complaints complaint = new Complaints();
		complaint.setUser(user);
		complaint.setMessage(message);
		complaint.setDate(new java.util.Date());
		List<Complaints> complaintList = new ArrayList<Complaints>();
		complaintList.add(complaint);
		user.setComplaints(complaintList);
		userRepository.save(user);

	}

	@Override
	public List<AbsentReasonModel> getAbsentReason() {

		List<AbsentReason> absentReasonList = absentReasonRepository.findAll().subList(0, 2);
		List<AbsentReasonModel> reasonList = new ArrayList<AbsentReasonModel>();

		for (AbsentReason reason : absentReasonList) {
			AbsentReasonModel rm = new AbsentReasonModel(reason);
			reasonList.add(rm);
		}

		return reasonList;
	}

	// ================End=============================
	public List<AbsentReasonModel> getStudentAbsentRecord(String userId) {
		List<AbsentReasonModel> studentlist = new ArrayList<AbsentReasonModel>();
		List<Student> students = studentsRepository.getStudentDetails(userId);

		for (Student student : students) {
			List<Absent> studentabsent = student.getAbsent();
			for (Absent ab : studentabsent) {
				AbsentReasonModel arm = new AbsentReasonModel();
				arm.setFirstName(ab.getStudent().getFirstName());
				arm.setLastName(ab.getStudent().getLastName());
				
				Date fromDate = ab.getFromDate();
				arm.setFromDate(fromDate.toString());

				Date toDate = ab.getToDate();
				arm.setToDate(toDate.toString());
				
				arm.setReason(ab.getAbsentReason().getReason());
				studentlist.add(arm);
			}
		}
//		studentlist.sort((p1, p2) -> p1.fromDate.compareTo(p2.fromDate));
		return studentlist;
	}
	
	@Override
	public List<UserVM> getAllUsersAdmin(String userUuid) {

		LOG.info("inside getAllUsersAdmin");
		List<User> users = new ArrayList<User>();
		List<UserVM> userVms = new ArrayList<UserVM>();

		Role userRole = getUserRole(userUuid);
		try {
			if (userRole.equals(Role.administrator)) {
				users = userRepository.findUsersForAdmin(userUuid);
			} else if (userRole.equals(Role.superAdmin)) {
				users = userRepository.findUsersForSuperAdmin();
			}

		} catch (Exception e) {
			LOG.info(e.toString());
			e.printStackTrace();
		}
		UserVM userVM = null;
		for (User user : users) {
			userVM = new UserVM();
			if (user.getAddresses() != null && user.getAddresses().size() > 0) {
				Address address = user.getAddresses().get(0);
				userVM.setAddress(address.getAddressLine1() + " " + address.getAddressLine2());
				userVM.setCity(address.getCity());
				userVM.setState(address.getState());
			}

			if (user.getStudent() != null && user.getStudent().size() > 0) {
				userVM.setRouteName(user.getStudent().get(0).getWayPoint().getRoute().getRouteName());
			}
			userVM.setName(user.getFirstName() + " " + user.getLastName());
			userVM.setEmailAddress(user.getEmailAddress());
			userVM.setMobileNumber(user.getMobileNumber());
			userVM.setIdentifier(user.getUuid());
			userVM.setIsEnable(user.getIsEnable());
			StringBuffer studentName = new StringBuffer();
			for (Student student : user.getStudent()) {
				if (studentName.length() > 0)
					studentName.append(" and ");
				studentName.append(student.getFirstName() + " " + student.getLastName());
			}
			userVM.setStudentName(studentName.toString());
			userVms.add(userVM);
		}

		return userVms;

	}

	@Override
	public void deactivateUser(String userUUID, String action) {
		User user = userRepository.findByUuid(userUUID);
		if (user != null) {
			if ("enable".equals(action)) {
				user.setIsEnable(1);
			} else if ("disable".equals(action)) {
				user.setIsEnable(0);
			}
		}
		userRepository.save(user);
	}

	@Override
	public Route getUserRoute(long routeId) {

		Route route = routeRepository.findRouteByPK(routeId);
		return route;

	}

	@Override
	public UserSchool getSchoolbyUser(long userId) {
		UserSchool us = userSchoolRepository.getSchoolByUser(userId);
		return us;
	}

	public UserSchool getSchoolbyUser(String userUuid){
		UserSchool us = userSchoolRepository.getSchoolByUser(userUuid);
		return us;
	}
	
	
	@Override
	public List<School> getAllSchoolDetails() {
		List<School> schoolList = schoolRepository.getAllSchool();
		return schoolList;
	}

	@Override
	public boolean urlStatus() {
		boolean status = true;
		return status;
	}

	@Override
	public String readLogFile(String file) {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(config.getAlertServerLogFile()));
			String line = null;
			while ((line = reader.readLine()) != null)
				builder.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// closeQuietly(reader);
		}
		// JSONObject jsonObj = new JSONObject(builder.toString());
		// JSONTokener obj = new JSONTokener(builder.toString());
		return builder.toString();
	}

	@Override
	public List<School> getSchoolDetails(String userUuId) {
		String userRole = getUserRole(userUuId).name();
		List<School> schoolList = null;
		if (Role.administrator.toString().equals(userRole) || Role.teacher.toString().equals(userRole)) {
			schoolList = schoolRepository.getSchoolBySchoolAdminUserId(userUuId);
		} else if (Role.superAdmin.toString().equals(userRole)) {
			schoolList = schoolRepository.getAllSchool();
		}
		return schoolList;
	}

	@Override
	public SchoolHolidaysModel getSchoolHolidaysModelByUserId(String userUuId) {
		List<School> schoolList = schoolRepository.getSchoolBySchoolAdminUserId(userUuId);

		List<SchoolHolidayModel> holidays = new ArrayList<SchoolHolidayModel>();

		SchoolHolidaysModel mHolidays = new SchoolHolidaysModel();

		for (School school : schoolList) {
			SchoolModel schoolModel = new SchoolModel();
			schoolModel.setId(school.getId());
			schoolModel.setName(school.getName());

			if (mHolidays.getDefaultSchool() == null) {
				mHolidays.setDefaultSchool(schoolModel);
			}

			for (SchoolHoliday schoolHoliday : school.getHolidays()) {
				SchoolHolidayModel temp = new SchoolHolidayModel(schoolHoliday);
				temp.setSchool(schoolModel);
				holidays.add(temp);
			}
		}
		Collections.sort(holidays, new SortByFromDateOfSchoolHoliday());
		mHolidays.setHolidays(holidays);

		return mHolidays;
	}
	
	class SortByFromDateOfSchoolHoliday implements Comparator<SchoolHolidayModel>
	{

		@Override
		public int compare(SchoolHolidayModel o1, SchoolHolidayModel o2) {
			// TODO Auto-generated method stub
			return o2.getFromDate().compareTo(o1.getFromDate());
		}
	}

	@Override
	public SchoolHolidaysModel getSchoolHolidaysModelByUserIdForParent(String userUuId) {
		List<School> schoolList = schoolRepository.getSchoolByParentUserId(userUuId);

		List<SchoolHolidayModel> holidays = new ArrayList<SchoolHolidayModel>();
		SchoolHolidaysModel mHolidays = new SchoolHolidaysModel();

		for (School school : schoolList) {
			SchoolModel schoolModel = new SchoolModel();
			schoolModel.setId(school.getId());
			schoolModel.setName(school.getName());

			if (mHolidays.getDefaultSchool() == null) {
				mHolidays.setDefaultSchool(schoolModel);
			}

			Date now = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(now);
			c.add(Calendar.DATE, 90); // number of days to add
			Date maxDate = c.getTime();

			List<SchoolHoliday> schoolHolidays = schoolHolidayRepository.getSchoolHolidaysForParent(school.getId(), now, maxDate);
			for (SchoolHoliday schoolHoliday : schoolHolidays) {
				SchoolHolidayModel temp = new SchoolHolidayModel(schoolHoliday);
				temp.setSchool(schoolModel);
				holidays.add(temp);
			}
		}
		mHolidays.setHolidays(holidays);
		return mHolidays;
	}

	@Override
	public SchoolHolidaysModel getAllSchoolHolidaysModel() {
		List<SchoolHoliday> schoolList = schoolHolidayRepository.findAll();

		List<SchoolHolidayModel> holidays = new ArrayList<SchoolHolidayModel>();

		if (schoolList.size() > 0) {
			for (SchoolHoliday holiday : schoolList) {
				SchoolHolidayModel temp = new SchoolHolidayModel(holiday);
				SchoolModel tempSchool = new SchoolModel();
				tempSchool.setId(holiday.getSchool().getId());
				tempSchool.setName(holiday.getSchool().getName());
				temp.setSchool(tempSchool);
				holidays.add(temp);
			}
		}

		SchoolHolidaysModel mHolidays = new SchoolHolidaysModel();
		mHolidays.setDefaultSchool(null);
		mHolidays.setHolidays(holidays);

		return mHolidays;
	}

	@Override
	public School getSchoolDetailById(Long id) {
		School school = schoolRepository.getSchoolDetailById(id);

		return school;
	}

	@Override
	public SchoolHoliday getSchoolHolidayById(Long id) {
		SchoolHoliday holiday = schoolHolidayRepository.getSchoolHolidayById(id);
		return holiday;
	}

	@Override
	public SchoolModel getSchoolModelDetailById(Long id) {
		SchoolModel school = new SchoolModel(schoolRepository.getSchoolDetailById(id));
		return school;
	}

	@Override
	public SchoolHolidayModel getSchoolHolidayModelDetailById(Long id) {
		SchoolHolidayModel holiday = new SchoolHolidayModel(schoolHolidayRepository.getSchoolHolidayById(id));
		return holiday;
	}

	@Override
	public void addSchool(School school) {
		schoolRepository.save(school);

		addSchoolConfigWithDeafultValues(school);
		addSchoolConfigWithAlertDeafultValues(school);

	}
	@Override
	public void updateSchool(School school) {
		schoolRepository.save(school);
	}

	private void addSchoolConfigWithDeafultValues(School school) {
		SchoolConfig schoolconfig = new SchoolConfig();
		schoolconfig.setConfigKey("season");
		schoolconfig.setSchool(school);
		schoolconfig.setValue("summer");
		schoolconfig.setValueType("String");
		try {
			schoolConfigRepository.save(schoolconfig);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}private void addSchoolConfigWithAlertDeafultValues(School school) {
		SchoolConfig schoolconfig = new SchoolConfig();
		schoolconfig.setConfigKey("AlertSystem");
		schoolconfig.setSchool(school);
		schoolconfig.setValue("byDistance");
		schoolconfig.setValueType("String");
		try {
			schoolConfigRepository.save(schoolconfig);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public Groups addGroup(School school) {
		Groups groups = new Groups();
		groups.setName(school.getName());
		return groupsRepository.save(groups);
	}

	@Override
	public SchoolHolidayModel addUpdateSchoolHoliday(SchoolHolidayModel holiday) {

		List<School> schools = new ArrayList<School>();
		if (holiday.getAddToAllSchools()) {
			schools = schoolRepository.getAllSchool();
		} else {
			schools.add(schoolRepository.findOne(holiday.getSchool().getId()));
		}

		for (School school : schools) {
			SchoolHoliday schoolHoliday = null;

			if (holiday.getId() != 0) {
				schoolHoliday = schoolHolidayRepository.getSchoolHolidayById(holiday.getId());
			} else {
				schoolHoliday = new SchoolHoliday();
			}

			schoolHoliday.setDescription(holiday.getDescription());
			schoolHoliday.setFromDate(holiday.getFromDate());
			schoolHoliday.setToDate(holiday.getToDate());
			schoolHoliday.setSjaFlag(holiday.getSjaFlag());
			schoolHoliday.setSchool(school);

			schoolHolidayRepository.save(schoolHoliday);
		}
		return new SchoolHolidayModel();
	}

	@Override
	public Boolean deleteSchoolHoliday(Long holidayId) {
		try {
			SchoolHoliday holiday = schoolHolidayRepository.getSchoolHolidayById(holidayId);
			if (holiday == null)
				return false;

			schoolHolidayRepository.delete(holiday);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<SchoolHolidayMasterDataModel> getSchoolHolidayMasterData(String query) {
		List<SchoolHolidayMasterData> masterData = schoolHolidayMasterDataRepository.getSchoolHolidaysMasterData();

		List<SchoolHolidayMasterDataModel> masterDataModel = new ArrayList<SchoolHolidayMasterDataModel>();

		if (masterData != null) {
			for (SchoolHolidayMasterData schoolHolidayMasterData : masterData) {
				masterDataModel.add(new SchoolHolidayMasterDataModel(schoolHolidayMasterData));
			}
		}

		return masterDataModel;
	}

	@Override
	public SchoolHolidayMasterDataModel getSchoolHolidayMasterDataById(Long id) {
		SchoolHolidayMasterData masterData = schoolHolidayMasterDataRepository.getSchoolHolidayMasterDataById(id);

		SchoolHolidayMasterDataModel masterDataModel = null;

		if (masterData != null) {
			masterDataModel = new SchoolHolidayMasterDataModel(masterData);
		}

		return masterDataModel;
	}

	@Override
	public SchoolHolidayMasterDataModel addUpdateSchoolHolidayMasterData(SchoolHolidayMasterDataModel holidayMasterDataModel) {
		SchoolHolidayMasterData holidayMasterData = null;

		if (holidayMasterDataModel.getId() != 0) {
			holidayMasterData = schoolHolidayMasterDataRepository.getSchoolHolidayMasterDataById(holidayMasterDataModel.getId());
		} else {
			holidayMasterData = new SchoolHolidayMasterData();
		}

		holidayMasterData.setDescription(holidayMasterDataModel.getDescription());
		holidayMasterData.setFromDate(holidayMasterDataModel.getFromDate());
		holidayMasterData.setToDate(holidayMasterDataModel.getToDate());

		schoolHolidayMasterDataRepository.save(holidayMasterData);

		return new SchoolHolidayMasterDataModel(holidayMasterData);
	}

	@Override
	public Boolean deleteSchoolHolidayMasterData(Long holidayMasterDataId) {
		try {
			schoolHolidayMasterDataRepository.delete(holidayMasterDataId);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public void addUser(String lastName, String firstName, String houseNo, String addressLine, String pinCode, String city, String state, String mobile,
			String emailAddress, String userUUID, String studentsList) {
		User user = null;
		if (userUUID != null && userUUID.length() > 0) {
			user = userRepository.findByUuid(userUUID);
		} else {
			user = new User();
		}

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setMobileNumber(mobile);
		user.setEmailAddress(emailAddress);
		user.setIsEnable(1);
		user.setVerified(true);
		user.setUsageConsent('1');
		user.setRole(Role.authenticated);
		try {
			user.setHashedPassword(user.hashPassword(SystemConstant.COMMON_NEW_USER_PASSWORD));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Address address = null;
		if (user.getAddresses() != null && user.getAddresses().size() > 0) {
			address = user.getAddresses().get(0);
		} else if (user.getAddresses() != null && user.getAddresses().size() == 0) {
			address = new Address();
		}
		address.setAddressLine1(houseNo);
		address.setAddressLine2(addressLine);
		address.setPinCode(pinCode);
		address.setCity(city);
		address.setState(state);
		address.setVoidInd('0');
		address.setAddressType(AddressType.Residence.toString());
		address.setCountry("India");
		address.setUser(user);
		List<Address> adList = new ArrayList<Address>();
		adList.add(address);
		user.setAddresses(adList);

		user = userRepository.save(user);

		if (studentsList != null) {
			String[] studentArray = studentsList.split(",");
			for (String studentId : studentArray) {
				Student student = studentService.getStudent(Integer.parseInt(studentId));
				createAlertConfig(student);
				student.setUser(user);
				user.getStudent().add(student);
				studentsRepository.save(student);
			}
		}

		sendWelcomeEmail(user, true);
	}

	@SuppressWarnings("deprecation")
	private void createAlertConfig(Student student) {
		AlertConfig alertConfig = new AlertConfig();

		alertConfig.setActiveTime(1);
		alertConfig.setAlerts("Arrival|Departure|Long Halt|Breakdown");

		alertConfig.setStudent(student);

		Date pickStartTime = new Date();
		pickStartTime.setHours(06);
		pickStartTime.setMinutes(45);
		pickStartTime.setSeconds(00);
		Date pickEndTime = new Date();
		pickEndTime.setHours(9);
		pickEndTime.setMinutes(00);
		pickEndTime.setSeconds(0);
		Date dropStartTime = new Date();
		Date dropEndTime = new Date();

		if (student.getStudentClass().equals("Pre Primary") || student.getStudentClass().equals("Nursery")) {
			alertConfig.setDays("Mon|Tue|Wed|Thu|Fri");
			dropStartTime.setHours(12);
			dropStartTime.setMinutes(30);
			dropEndTime.setHours(13);
			dropEndTime.setMinutes(30);
			dropEndTime.setSeconds(0);
		} else {
			alertConfig.setDays("Mon|Tue|Wed|Thu|Fri|Sat");
			dropStartTime.setHours(14);
			dropStartTime.setMinutes(00);
			dropEndTime.setHours(15);
			dropEndTime.setMinutes(30);
			dropEndTime.setSeconds(0);
		}
		alertConfig.setPickStartTime(pickStartTime);
		alertConfig.setPickEndTime(pickEndTime);
		alertConfig.setDropStartTime(dropStartTime);
		alertConfig.setDropEndTime(dropEndTime);
		alertsConfigDAO.save(alertConfig);
	}

	@Transactional
	public User getUserById(String userUUId) {
		return userRepository.findByUuid(userUUId);
	}

	@Transactional
	public List<StudentVM> retrieveStudentDetails(String userId) {

		List<Student> studentDetailList = studentService.getStudentDetails(userId);

		List<StudentVM> studentList = new ArrayList<StudentVM>();
		for (Student student : studentDetailList) {
			// for(Absent absent : student.getAbsent()){
			// if((absent.getFromDate().compareTo(new Date()) >=0 &&
			// absent.getFromDate().before((DateUtils.addMonths(new Date(),
			// 1))))
			// || (absent.getToDate().compareTo(new Date()) >=0 &&
			// absent.getToDate().before((DateUtils.addMonths(new Date(),
			// 1))))){
			// StudentVM svm = new StudentVM();
			// svm.setFirstName(student.getFirstName());
			// svm.setAbsentFromDate(absent.getFromDate());
			// svm.setAbsentToDate(absent.getToDate());
			// studentList.add(svm);
			// }
			// }
			StudentVM svm = new StudentVM();
			svm.setFirstName(student.getFirstName());
			svm.setLastName(student.getLastName());
			svm.setRegId(student.getRegId());
			svm.setStudentClass(student.getStudentClass());
			svm.setSection(student.getSection());
			svm.setSchoolId(student.getSchool() != null ? student.getSchool().getId() : 0);
			svm.setStudentId(student.getId());
			svm.setUserId(student.getUser().getId());
			svm.setStudentUUID(student.getUuid().toString());
			svm.setWayPointId(student.getWayPoint().getId());
			svm.setLattitude(student.getWayPoint().getLattitude());
			svm.setLongitude(student.getWayPoint().getLongitude());

			SchoolConfig schoolConfig = schoolConfigDAO.findSchoolConfiguration(student.getSchool().getId(), SystemConstant.SCHOOL_CONFIG_SEASON_KEY);
			if (schoolConfig.getValue().equals("winter")) {
				svm.setPickTimeSummer(student.getWayPoint().getTimePickWinter());
				svm.setDrop(student.getWayPoint().getTimeDrop());
			} else {
				svm.setPickTimeSummer(student.getWayPoint().getTimePick());
				svm.setDrop(student.getWayPoint().getTimeDrop());
			}

			studentList.add(svm);
		}
		return studentList;
	}

	public ServiceResponse changePassword(String userId, ChangePasswordRequest request) {

		User user = userRepository.findByUuid(userId);
		ServiceResponse response = new ServiceResponse();

		String hashedPassword = null;
		try {
			hashedPassword = user.hashPassword(request.getoPassword());
		} catch (Exception e) {
			response.setMessage("Invalid old password");
		}
		try {
			if (hashedPassword.equals(user.getHashedPassword())) {
				user.setHashedPassword(user.hashPassword(request.getPassword()));
				userRepository.save(user);
				response.setIsSuccess(true);
				response.setMessage("Password changed successfully");
			} else {
				response.setMessage("Invalid old password");
			}
		} catch (Exception e) {
			response.setMessage("Invalid old password");
		}
		return response;
	}

	@Override
	public void sendWelcomeEmailToUser() {
		LOG.info("Inside sendWelcomeEmailToUser");
		User user = userRepository.findByUuid("c51a48af-3eed-455e-950b-45b2931fd26c");

		sendWelcomeEmail(user, false);

	}

	public void sendWelcomeEmail(User user, boolean keepSchoolInCc) {
		if (!user.getEmailAddress().contains("@"))
			return;
		LOG.info("Sending welcome email to email address " + user.getEmailAddress() + " ::: " + user.getFirstName() + " " + user.getLastName() + "(" + user.getId() + ")");
		try {
			String schoolName = user.getStudent().get(0).getSchool().getName();
			String schoolAddress = user.getStudent().get(0).getSchool().getDisplayAddress();
			String schoolContact = user.getStudent().get(0).getSchool().getPhoneNumber1() + ", " + user.getStudent().get(0).getSchool().getPhoneNumber2();
			String schoolEmailAddress = keepSchoolInCc ? user.getStudent().get(0).getSchool().getEmailId() : null;

			List<Student> students = user.getStudent();

			Double lattitude = new Double(0);
			Double longitude = new Double(0);
			try {
				lattitude = user.getStudent().get(0).getWayPoint().getLattitude();
				longitude = user.getStudent().get(0).getWayPoint().getLongitude();
			} catch (Throwable e) {
				LOG.error("Waypoint is not avilable for student " + user.getStudent().get(0).getId());
			}

			StringBuffer sb = new StringBuffer("http://maps.google.com/maps?q=");
			sb.append("n").append(lattitude).append(",e").append(longitude);

			VerificationToken token = new VerificationToken(user, VerificationToken.VerificationTokenType.welcomeMessage,
					applicationConfig.getLostPasswordTokenExpiryTimeInMinutes());
			Fleet fleet= null;
			for(Student student : user.getStudent()){
				fleet = fleetDAO.findFleetByRouteId(student.getWayPoint().getRoute().getId());
				break;
			}

			EmailServiceTokenModel model = new EmailServiceTokenModel(token, user.getFirstName(), students, user.getEmailAddress(), schoolName, schoolContact,
					schoolAddress, schoolEmailAddress, sb.toString(), fleet);
			emailServicesGateway.sendVerificationToken(model);
		} catch (Throwable t) {
			LOG.error("Error in sendWelcomeEmail", t);
			throw t;
		}
	}

	public void sendCustomEmail(User user, String emailSubject, String emailBody, boolean keepSchoolInCc) {
		LOG.info("Inside sendCustomEmail for user " + user.getFirstName() + " " + user.getLastName() + "(" + user.getId() + ")");
		if (!user.getEmailAddress().contains("@"))
			return;
		try {
			String schoolName = user.getStudent().get(0).getSchool().getName();
			String schoolAddress = user.getStudent().get(0).getSchool().getDisplayAddress();
			String schoolContact = user.getStudent().get(0).getSchool().getPhoneNumber1() + ", " + user.getStudent().get(0).getSchool().getPhoneNumber2();
			String schoolEmailAddress = keepSchoolInCc ? user.getStudent().get(0).getSchool().getEmailId() : null;

			VerificationToken token = new VerificationToken(user, VerificationToken.VerificationTokenType.customMessage,
					applicationConfig.getLostPasswordTokenExpiryTimeInMinutes());
			EmailServiceTokenModel model = new EmailServiceTokenModel(token, user.getFirstName(), user.getEmailAddress(), schoolName, schoolContact,
					schoolAddress, schoolEmailAddress, emailSubject, emailBody);
			emailServicesGateway.sendVerificationToken(model);
		} catch (Throwable t) {
			LOG.error("Error in sendCustomEmail", t);
			throw t;
		}
	}

	@Override
	public List<SchoolModel> getSchoolsForDropDown() {
		List<SchoolModel> schoolsModel = new ArrayList<SchoolModel>();

		List<School> schools = schoolRepository.getAllSchool();

		if (schools != null) {
			for (School school : schools) {
				SchoolModel schoolModel = new SchoolModel();
				schoolModel.setId(school.getId());
				schoolModel.setName(school.getName());
				schoolsModel.add(schoolModel);
			}
		}
		return schoolsModel;
	}

	public void sendComplaintMessage(String userId, ComplaintMessageRequest complaintMessage, ExternalUser userMakingRequest) {
		addComplaintMessage(userId, complaintMessage.getMessage());
		User user = getUserById(userId);
		List<Student> student=studentsRepository.getStudentDetails(userId);
		String schoolEmailAddress = user.getStudent().get(0).getSchool().getEmailId();
		try {
			VerificationToken token = new VerificationToken(user, VerificationToken.VerificationTokenType.complaintMessage,
					applicationConfig.getLostPasswordTokenExpiryTimeInMinutes());
			EmailServiceTokenModel emailService = new EmailServiceTokenModel(userMakingRequest, token, applicationConfig.getHostNameUrl(),
					userMakingRequest.getFirstName() + " " + userMakingRequest.getLastName(), complaintMessage.getMessage(),
					applicationConfig.getTransportAdminEmail(), schoolEmailAddress,student);
			emailServicesGateway.sendVerificationToken(emailService);
		} catch (Exception e) {
			LOG.debug("Error while adding Complaint Message", e);
		}
	}

	@Override
	public void sendmailForLatLongApproval(String userId, StudentRequest student, ExternalUser userMakingRequest) {

		WayPoint waypoint = wayPointDao.getWayPoint(student.getWayPointId());
		String messageForLatLongUpdate = "I request you to modify the current latitude and longitude from " + student.getLattitude() + " and "
				+ student.getLongitude() + " to " + waypoint.getLattitude() + " and " + waypoint.getLattitude() + "";

		User user = getUserById(userId);
		String schoolEmailAddress = user.getStudent().get(0).getSchool().getEmailId();
		VerificationToken token = new VerificationToken(user, VerificationToken.VerificationTokenType.customMessage);
		EmailServiceTokenModel model = new EmailServiceTokenModel(token, "School Admin", user.getEmailAddress(), user.getFirstName(), user.getMobileNumber(),
				(user.getAddresses().get(0).getAddressLine1() + " " + user.getAddresses().get(0).getAddressLine2()), schoolEmailAddress,
				"Request to update Location", messageForLatLongUpdate);

		emailServicesGateway.sendVerificationToken(model);
	}

	@Transactional
	@Override
	public User findAdminByDeviceId(Long deviceId) {
		return userRepository.findAdminByDeviceId(deviceId);
	}

	@Override
	public List<UserVM> getAllAdministrator() {

		LOG.info("inside getAllAdmin");
		List<User> users = new ArrayList<User>();
		List<UserVM> userVms = new ArrayList<UserVM>();

		try {
			users = userRepository.findUsersForSuperAdmin();

		} catch (Exception e) {
			LOG.info(e.toString());
			e.printStackTrace();
		}
		UserVM userVM = null;
		for (User user : users) {
			userVM = new UserVM();
			if (user.getAddresses() != null && user.getAddresses().size() > 0) {
				Address address = user.getAddresses().get(0);
				userVM.setAddress(address.getAddressLine1() + " " + address.getAddressLine2());
				userVM.setCity(address.getCity());
				userVM.setState(address.getState());
			}

			if (user.getStudent() != null && user.getStudent().size() > 0) {
				userVM.setRouteName(user.getStudent().get(0).getWayPoint().getRoute().getRouteName());
			}
			userVM.setName(user.getFirstName() + " " + user.getLastName());
			userVM.setEmailAddress(user.getEmailAddress());
			userVM.setMobileNumber(user.getMobileNumber());
			userVM.setIdentifier(user.getUuid());
			userVM.setIsEnable(user.getIsEnable());
			StringBuffer studentName = new StringBuffer();
			for (Student student : user.getStudent()) {
				if (studentName.length() > 0)
					studentName.append(" and ");
				studentName.append(student.getFirstName() + " " + student.getLastName());
			}
			userVM.setStudentName(studentName.toString());
			userVms.add(userVM);
		}

		return userVms;

	}

	public List<UserVM> getAllTeachers(String userId) {

		List<User> users = new ArrayList<User>();
		List<UserVM> userVms = new ArrayList<UserVM>();

		List<School> schools = getSchoolDetails(userId);
		Long schoolId = 0L;
		if (schools != null && schools.size() > 0) {
			schoolId = schools.get(0).getId();
		}

		try {
			users = userRepository.findTeachers(schoolId);

		} catch (Exception e) {
			LOG.error("Error in getAllTeachers", e);
		}
		UserVM userVM = null;
		for (User user : users) {
			userVM = new UserVM();
			if (user.getAddresses() != null && user.getAddresses().size() > 0) {
				Address address = user.getAddresses().get(0);
				userVM.setAddress(address.getAddressLine1() + " " + address.getAddressLine2());
				userVM.setCity(address.getCity());
				userVM.setState(address.getState());
			}

			if (user.getStudent() != null && user.getStudent().size() > 0) {
				userVM.setRouteName(user.getStudent().get(0).getWayPoint().getRoute().getRouteName());
			}
			userVM.setName(user.getFirstName() + " " + user.getLastName());
			userVM.setEmailAddress(user.getEmailAddress());
			userVM.setMobileNumber(user.getMobileNumber());
			userVM.setIdentifier(user.getUuid());
			userVM.setIsEnable(user.getIsEnable());
			StringBuffer studentName = new StringBuffer();
			for (Student student : user.getStudent()) {
				if (studentName.length() > 0)
					studentName.append(" and ");
				studentName.append(student.getFirstName() + " " + student.getLastName());
			}
			userVM.setStudentName(studentName.toString());
			userVms.add(userVM);
		}

		return userVms;

	}

	@Override
	public void addAdmin(String lastName, String firstName, String houseNo, String addressLine, String pinCode, String city, State state, String mobile,
			String emailAddress, String userUUID, String selectedSchoolId) {
		User user = null;
		if (userUUID != null && userUUID.length() > 0) {
			user = userRepository.findByUuid(userUUID);
		} else {
			user = new User();
		}

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setMobileNumber(mobile);
		user.setEmailAddress(emailAddress);
		user.setIsEnable(1);
		user.setVerified(true);
		user.setUsageConsent('1');
		user.setRole(Role.administrator);
		try {
			user.setHashedPassword(user.hashPassword(SystemConstant.COMMON_NEW_USER_PASSWORD));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Address address = null;
		if (user.getAddresses() != null && user.getAddresses().size() > 0) {
			address = user.getAddresses().get(0);
		} else if (user.getAddresses() != null && user.getAddresses().size() == 0) {
			address = new Address();
		}
		address.setAddressLine1(houseNo);
		address.setAddressLine2(addressLine);
		address.setPinCode(pinCode);
		address.setCity(city);
		// address.setState(state.getStateName());
		address.setVoidInd('0');
		address.setAddressType(AddressType.Residence.toString());
		address.setCountry("India");
		address.setUser(user);
		List<Address> adList = new ArrayList<Address>();
		adList.add(address);
		user.setAddresses(adList);

		user = userRepository.save(user);
		Users userTracko = createAndUpdateUserForTrackho(user);
		School school = null;
		if (selectedSchoolId != null) {
			school = schoolRepository.findOne(Long.parseLong(selectedSchoolId));
		}

		if (school != null) {
			createUserSchool(school, user);
		}

		// sendWelcomeEmail(user, true);
	}

	public void createUserSchool(School school, User user) {
		UserSchool userSchool = new UserSchool();
		userSchool.setSchool(school);
		userSchool.setUser(user);
		userSchoolRepository.save(userSchool);
	}

	private Users createAndUpdateUserForTrackho(User user) {
		Users users = usersRepository.findOne(user.getId());
		if (users == null) {
			users = new Users();
		}
		users.setName(user.getFirstName() + " " + user.getLastName());
		users.setAdmin(false);
		users.setEmail(user.getEmailAddress());
		users.setPassword(SystemConstant.COMMON_NEW_USER_PASSWORD);
		users.setId(user.getId());
		usersRepository.save(users);
		return users;
	}

	public List<RouteVM> getUnassignedDevice(String userUuid) {
		LOG.debug("inside getUnassignedSchool");

		List<RouteVM> routeVMList = new ArrayList<RouteVM>();

		List<Devices> devices = deviceRepository.findAllDeviceListNotMapped(userUuid);
		for (Devices device : devices) {

			RouteVM svm = new RouteVM();
			svm.setName(device.getName());
			svm.setDeviceId(device.getId());
			routeVMList.add(svm);

		}
		return routeVMList;
	}

	public List<RouteVM> getUnassignedDevice() {
		LOG.debug("inside getUnassignedSchool");

		List<RouteVM> routeVMList = new ArrayList<RouteVM>();

		List<Devices> schools = deviceRepository.findAllDeviceListNotMapped();
		for (Devices device : schools) {

			RouteVM svm = new RouteVM();
			svm.setName(device.getName());
			svm.setDeviceId(device.getId());
			routeVMList.add(svm);
		}
		return routeVMList;
	}

	@Override
	public ServiceResponse addSchoolTeacher(Long schoolId, ExternalUser teacher) {
		User user = new User();
		ServiceResponse response = new ServiceResponse();
		try {
			if (schoolId == 0L) {
				throw new Exception("School is required");
			}

			School school = schoolRepository.findOne(schoolId);

			user.setFirstName(teacher.getFirstName());
			user.setLastName(teacher.getLastName());
			user.setMobileNumber(teacher.getMobile());
			user.setEmailAddress(teacher.getEmailAddress());
			user.setIsEnable(1);
			user.setVerified(true);
			user.setUsageConsent('1');
			user.setRole(Role.teacher);
			user.setHashedPassword(user.hashPassword(SystemConstant.COMMON_NEW_USER_PASSWORD));

			Address addr = new Address();

			addr.setAddressLine1(teacher.getHouseNo());
			addr.setAddressLine2(teacher.getAddress());
			addr.setPinCode(teacher.getPinCode());
			addr.setCity(teacher.getCity());
			addr.setState(teacher.getState());
			addr.setVoidInd('0');
			addr.setAddressType(AddressType.Residence.toString());
			addr.setCountry("India");
			addr.setUser(user);
			List<Address> adList = new ArrayList<Address>();
			adList.add(addr);
			user.setAddresses(adList);
			user = userRepository.save(user);
			Users userTracko = createAndUpdateUserForTrackho(user);

			UserSchool userSchool = new UserSchool();

			userSchool.setSchool(school);
			userSchool.setUser(user);

			userSchoolRepository.save(userSchool);

			if (school != null) {
				createUserSchool(school, user);
			}

			response.setIsSuccess(true);
			response.setMessage("Teacher added successfully");
		} catch (Exception ex) {
			response.setIsSuccess(false);
			response.setMessage(ex.getMessage());
		}
		return response;
	}

	@Override
	public ServiceResponse updateSchoolTeacher(ExternalUser teacher) {
		User user = new User();
		ServiceResponse response = new ServiceResponse();
		try {
			user = userRepository.findByUuid(teacher.getId());

			if (user == null)
				throw new Exception("User not found");

			user.setFirstName(teacher.getFirstName());
			user.setLastName(teacher.getLastName());
			user.setMobileNumber(teacher.getMobile());
			user.setEmailAddress(teacher.getEmailAddress());
			// user.setIsEnable(1);
			// user.setVerified(true);
			// user.setUsageConsent('1');
			// user.setRole(Role.teacher);
			// user.setHashedPassword(user.hashPassword(SystemConstant.COMMON_NEW_USER_PASSWORD));

			Address addr = user.getAddresses().get(0);

			addr.setAddressLine1(teacher.getHouseNo());
			addr.setAddressLine2(teacher.getAddress());
			addr.setPinCode(teacher.getPinCode());
			addr.setCity(teacher.getCity());
			addr.setState(teacher.getState());
			addr.setVoidInd('0');
			addr.setAddressType(AddressType.Residence.toString());
			addr.setCountry("India");
			addr.setUser(user);

			user = userRepository.save(user);

			response.setIsSuccess(true);
			response.setMessage("Teacher updated successfully");
		} catch (Exception ex) {
			response.setIsSuccess(false);
			response.setMessage(ex.getMessage());
		}
		return response;
	}

	@Override
	public ServiceResponse deleteSchoolTeacher(String teacherUUID) {
		User user = new User();
		ServiceResponse response = new ServiceResponse();
		try {
			user = userRepository.findByUuid(teacherUUID);

			if (user == null)
				throw new Exception("User not found");

			Boolean isActive = user.getIsEnable() == 1;

			user.setIsEnable(isActive ? 0 : 1);

			user = userRepository.save(user);

			response.setIsSuccess(true);
			response.setMessage("Teacher " + (isActive ? "deactivated" : "activated") + " successfully");
		} catch (Exception ex) {
			response.setIsSuccess(false);
			response.setMessage(ex.getMessage());
		}
		return response;
	}
	@Override
	public ServiceResponse saveVisitorRequest(String userId,visitorModel visitorRq) {
		VisitorRequest visitorRequest=new VisitorRequest();
		ServiceResponse response = new ServiceResponse();
		try {
			String mobileno=visitorRq.getUserNumber();
			String output =null;
			String random=StringUtil.generateRandomNumber(6);
			String message="your One time password is-"+random;
			if(visitorRq.getUserNumber()!=null) {
				output = SmsPrimaryService.getInstance(config).sendMessage(visitorRq.getUserNumber(), message);
			}
			visitorRequest.setVisitorName(visitorRq.getUserName());
			visitorRequest.setVisitorNumber(visitorRq.getUserNumber());
			visitorRequest.setPurpose(visitorRq.getMessage());
			visitorRequest.setOtpNumber(random);
			visitorRequest.setToken("spi1245");
			visitorRepository.save(visitorRequest);
			visitorRequest=visitorRepository.getVisitor(random,visitorRq.getUserNumber());
			response.setUuid(visitorRequest.getUuid().toString());
			response.setMessage(visitorRq.getUserNumber());
		} catch (Exception ex) {
			response.setIsSuccess(false);
			response.setMessage(ex.getMessage());
		}
		return response;
	}
	public ServiceResponse sendOTPMessageToUser(visitorModel visitorModel) {
		ServiceResponse res = new ServiceResponse();
		String output=null;
		VisitorRequest req=visitorRepository.getVisitorById(visitorModel.getUuid());
		if(req!=null) {
			String random=StringUtil.generateRandomNumber(6);
			output = SmsPrimaryService.getInstance(config).sendMessage(visitorModel.getUserNumber(),"Your One Time password is-"+random);
			req.setOtpNumber(random);
			visitorRepository.save(req);
			res.setMessage("OTP sent to Visitor's Mobile Number");
		}else {
			res.setMessage("SomeThing Went wrong ,Please Try Again");
		}
		return res;
	}
	public ServiceResponse varifiedOTPRequest(String userId,visitorModel visitorModel) {
		ServiceResponse res = new ServiceResponse();
		//VisitorRequest vr= new VisitorRequest();
		User user=userRepository.findByUuid(userId);
		String output=null;
		String responseToken=null;
		VisitorRequest req=visitorRepository.getVisitor(visitorModel.getOtpNumber(),visitorModel.getUserNumber());
		if(req!=null) {
		     responseToken=StringUtil.generatePasswordKey(4);
		    req.setToken(responseToken);
			visitorRepository.save(req);
			res.setMessage("OTP Request Verified & Response Token send to Visitor Number");
		         String message="Visitor token is-"+responseToken;
				output = SmsPrimaryService.getInstance(config).sendMessage(user.getMobileNumber(), message);
				output = SmsPrimaryService.getInstance(config).sendMessage(visitorModel.getUserNumber(), message);
		}
		if(req==null) {
			res.setMessage("OTP no is inccorect");
		}
		return res;
	}

	@Override
	public void saveFCMToken(String fcmToken, String userId) {
		ServiceResponse response = new ServiceResponse();
		try {

			String Id = userId.replaceAll("\\s", "");

			User user = userRepository.findByUuid(Id);

			user.setFcmtoken(fcmToken);

			userRepository.save(user);

			response.setIsSuccess(true);
			response.setMessage("Saved");

		} catch (Exception ex) {
			response.setIsSuccess(false);
			response.setMessage(ex.getMessage());
		}
	}

	@Override
	public List<DeviceModel> getDeviceModelId(String userId) {
		List<DeviceModelType> deviceMeodelType = deviceModelTypeRepository.findAllDeviceModelTypeList();
		List<DeviceModel> dm = new ArrayList<DeviceModel>();
		for (DeviceModelType dmt : deviceMeodelType) {
			DeviceModel dmodel = new DeviceModel();
			dmodel.setDeviceModelId(dmt.getId());
			dmodel.setDeviceType(dmt.getDeviceModel());
			dm.add(dmodel);
		}
		return dm;
	}
	public void addClassGroup(ClassGroupModel request ,String userId) {
		ClassGroup cg= new ClassGroup();
		String className=removeLastChar(request.getClassName());
		if(request.getUuid().equals("newState")) {
		School sc= schoolRepository.getSchoolBySchoolAdminId(userId);
		cg.setClassName(className);
		cg.setGroupName(request.getGroupName());
		cg.setSchoolId(sc.getId());
		classGroupRepository.save(cg);
		}else  {
			ClassGroup c=classGroupRepository.getClassGroupByUUID(request.getUuid());
			c.setClassName(className);
			c.setGroupName(request.getGroupName());
			classGroupRepository.save(c);
		}
	}
	public String removeLastChar(String s) {
	        s = s.substring(0, s.length()-1);
	    return s;
	}
	@Override
	public List<ClassGroupModel> getclassGroupRecord(String userId){
		List<ClassGroupModel> list=new ArrayList<ClassGroupModel>();
		School sc= schoolRepository.getSchoolBySchoolAdminId(userId);
		List<ClassGroup> classgroup= classGroupRepository.getClassGroupBySchoolId(sc.getId());
		for(ClassGroup c :classgroup) {
			ClassGroupModel cgmodel=new ClassGroupModel();
			cgmodel.setClassName(c.getClassName());
			cgmodel.setGroupName(c.getGroupName());
			cgmodel.setSchoolId(c.getSchoolId());
			cgmodel.setUuid(c.getUuid().toString());
			list.add(cgmodel);
		}
		return list;
	}


}
