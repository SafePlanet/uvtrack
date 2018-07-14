/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spi.VM.AlertConfigVM;
import com.spi.VM.RouteVM;
import com.spi.VM.StudentVM;
import com.spi.VM.UserVM;
import com.spi.api.ServiceResponse;
import com.spi.config.ApplicationConfig;
import com.spi.domain.Role;
import com.spi.domain.School;
import com.spi.domain.Student;
import com.spi.domain.UserSchool;
import com.spi.domain.VisitorRequest;
import com.spi.exception.AuthorizationException;
import com.spi.exception.ValidationException;
import com.spi.gateway.EmailServicesGateway;
import com.spi.service.StudentService;
import com.spi.service.UserService;
import com.spi.service.VerificationTokenService;
import com.spi.user.api.AbsentReasonModel;
import com.spi.user.api.AlertNotificationsConfig;
import com.spi.user.api.AuthenticatedUserToken;
import com.spi.user.api.ChangePasswordRequest;
import com.spi.user.api.ClassGroupModel;
import com.spi.user.api.ComplaintMessageRequest;
import com.spi.user.api.CreateUserRequest;
import com.spi.user.api.DashboardLinks;
import com.spi.user.api.DeviceModel;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.LoginRequest;
import com.spi.user.api.OAuth2Request;
import com.spi.user.api.PickUpRequestModel;
import com.spi.user.api.SchoolHolidayMasterDataModel;
import com.spi.user.api.SchoolHolidayModel;
import com.spi.user.api.SchoolHolidaysModel;
import com.spi.user.api.SchoolModel;
import com.spi.user.api.StudentAssignment;
import com.spi.user.api.StudentRequest;
import com.spi.user.api.UpdateUserRequest;
import com.spi.user.api.visitorModel;
import com.spi.util.StringUtil;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/**
 */
@Path("/user")
@Component
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
public class UserResource {

	private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	protected UserService userService;

	@Autowired
	protected StudentService studentService;

	@Autowired
	protected VerificationTokenService verificationTokenService;

	@Autowired
	protected EmailServicesGateway emailServicesGateway;

	@Autowired
	ApplicationConfig applicationConfig;

	@Context
	protected UriInfo uriInfo;

	@Autowired
	ApplicationConfig config;

	public UserResource() {
	}

	@Autowired
	public UserResource(ConnectionFactoryLocator connectionFactoryLocator) {
		this.connectionFactoryLocator = connectionFactoryLocator;
	}

	@PermitAll
	@Path("signup")
	@POST
	public Response signupUser(CreateUserRequest request) {
		AuthenticatedUserToken token = userService.createUser(request, Role.authenticated);
		verificationTokenService.sendEmailRegistrationToken(token.getUserId());
		URI location = uriInfo.getAbsolutePathBuilder().path(token.getUserId()).build();
		return Response.created(location).entity(token).build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher", "superAdmin", "transporter" })
	@Path("{userId}")
	@DELETE
	public Response deleteUser(@Context SecurityContext sc, @PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		userService.deleteUser(userMakingRequest, userId);
		return Response.ok().build();
	}

	@PermitAll
	@Path("saveFCMToken/{userId}/{fcmToken}")
	@GET
	public Response saveFCMToken(@PathParam("userId") String userId, @PathParam("fcmToken") String fcmToken) {

		userService.saveFCMToken(fcmToken, userId);
		return Response.ok().build();
	}

	@PermitAll
	@Path("login/")
	@POST
	public Response login(LoginRequest request, @Context HttpServletRequest httpRequest) {

		AuthenticatedUserToken token = userService.login(request);
		if(token.getUserId()!=null){
		Role userRole = userService.getUserRole(token.getUserId());
		if (token != null && token.getToken() != null
				&& (userRole.equals(Role.administrator) || userRole.equals(Role.superAdmin))) {
			httpRequest.getSession().setAttribute("AUTH_TOKEN", token);
			httpRequest.getSession().setAttribute("IS_ADMIN", "Yes");
			httpRequest.getSession().setAttribute("USER_ID", token.getUserId());
		}
		LOG.debug(request.getUsername() + " user got access to the system.");
		}
		return getLoginResponse(token);
	}

	@PermitAll
	@Path("changePassword/{userId}")
	@PUT
	public Response changePassword(@Context SecurityContext sc, @PathParam("userId") String userId,
			ChangePasswordRequest cpRequest) {

		if (!cpRequest.Validate()) {
			Response.serverError().build();
		}

		return Response.ok().entity(userService.changePassword(userId, cpRequest)).build();
	}

	@PermitAll
	@Path("login/{providerId}")
	@POST
	public Response socialLogin(@PathParam("providerId") String providerId, OAuth2Request request) {
		OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator
				.getConnectionFactory(providerId);
		Connection<?> connection = connectionFactory.createConnection(new AccessGrant(request.getAccessToken()));
		AuthenticatedUserToken token = userService.socialLogin(connection);
		return getLoginResponse(token);
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher", "individual", "superAdmin", "transporter","gateKeeper" })
	@Path("{userId}")
	@GET
	public Response getUser(@Context SecurityContext sc, @PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		ExternalUser user = userService.getUser(userMakingRequest, userId);
		return Response.ok().entity(user).build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher", "individual", "superAdmin", "transporter" })
	@Path("saveUserDetails/{userId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@POST
	public Response saveUserDetails(@Context SecurityContext sc, @PathParam("userId") String userId,
			@FormDataParam("userPhoto") InputStream fileInputStream,
			@FormDataParam("userPhoto") FormDataContentDisposition contentDispositionHeader,
			@FormDataParam("userDetails") String userDetails) {

		String fileName = contentDispositionHeader.getFileName();
		System.out.println(fileName);
		String filePath = applicationConfig.getUserImageDirectory() + fileName;
		Map paramValues = contentDispositionHeader.getParameters();

		ObjectMapper mapper = new ObjectMapper();
		// mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		UpdateUserRequest request = null;
		try {
			request = mapper.readValue(userDetails, UpdateUserRequest.class);
			request.setUserImage(fileName);

		} catch (Exception e) {
			LOG.error("Error in fetching user Image ", e);
		}

		if (StringUtil.isValid(fileName)) {
			saveFile(fileInputStream, filePath);
		}
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();

		if (!userMakingRequest.getId().equals(userId)) {
			throw new AuthorizationException("User not authorized to modify this profile");
		}

		try {
			if (request.getMobile() != null && !request.getMobile().trim().equals("")
					&& !"null".equals(request.getMobile())) {
				Long.parseLong(request.getMobile());
			}
		} catch (Exception e) {
			LOG.error("Invalid Mobile Number.", e);
			throw new ValidationException("Invalid Mobile Number.", "mobile");
		}

		ExternalUser savedUser = null;
		if (request != null) {
			boolean sendVerificationToken = StringUtils.hasLength(request.getEmailAddress())
					&& !request.getEmailAddress().equals(userMakingRequest.getEmailAddress());
			savedUser = userService.updateUser(userId, request);
			if (sendVerificationToken) {
				verificationTokenService.sendEmailVerificationToken(savedUser.getId());
			}
		}
		return Response.status(200).entity(savedUser).build();
		// return Response.ok().build();
	}

	// save uploaded file to a defined location on the server

	private void saveFile(InputStream uploadedInputStream, String serverLocation) {
		try {
			OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			outpuStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
			outpuStream.flush();
			outpuStream.close();
		} catch (IOException e) {
			LOG.error("Error while saving the file", e);
		}
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher", "individual", "superAdmin", "transporter" })
	@Path("{userId}")
	@PUT
	public Response updateUser(@Context SecurityContext sc, @PathParam("userId") String userId,
			UpdateUserRequest request) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();

		if (!userMakingRequest.getId().equals(userId)) {
			throw new AuthorizationException("User not authorized to modify this profile");
		}

		boolean sendVerificationToken = StringUtils.hasLength(request.getEmailAddress())
				&& !request.getEmailAddress().equals(userMakingRequest.getEmailAddress());
		ExternalUser savedUser = userService.saveUser(userId, request);
		if (sendVerificationToken) {
			verificationTokenService.sendEmailVerificationToken(savedUser.getId());
		}
		return Response.ok().build();
	}

	private Response getLoginResponse(AuthenticatedUserToken token) {
		URI location = UriBuilder.fromPath(uriInfo.getBaseUri() + "user/" + token.getUserId()).build();
		return Response.ok().entity(token).contentLocation(location).build();
	}

	// @PermitAll
	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("getStudentDetails/{userId}")
	@GET
	public Response getStudentDetails(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<StudentVM> studentDetails = userService.retrieveStudentDetails(userId);
		return Response.ok().entity(studentDetails).build();
	}

	@RolesAllowed({ "authenticated", "administrator" })
	@Path("addUpdateStudent/{userId}")
	@PUT
	public Response addUpdateStudentDetails(@Context SecurityContext sc, @PathParam("userId") String userId,
			StudentRequest studentReq) {
		try {
			studentReq.setIsApproved('N');
			if (studentReq.getStudentId() <= 0) {
				studentReq = studentService.addStudentDetails(studentReq, userId);
			} else {
				studentReq = studentService.updateStudentDetails(studentReq);
			}

			return Response.ok().entity(studentReq).build();
		} catch (Exception ex) {
			return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
		}
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher", "individual", "superAdmin", "transporter","gateKeeper" })
	@Path("getUserDashboard/{userId}")
	@GET
	public Response getUserDashboard(@HeaderParam("user-agent") String userAgent, @Context SecurityContext sc,
			@PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		DashboardLinks dashboardLinks = new DashboardLinks(userMakingRequest.getRole(), deviceUserAgent(userAgent));
		return Response.ok().entity(dashboardLinks).build();
	}

	private boolean deviceUserAgent(String userAgent) {

		String[] devices = { "iPhone", "iPod", "iPad", "Android", "BlackBerry" };

		for (String device : devices) {
			if (userAgent.contains(device)) {
				return true;
			}
		}
		return false;

	}

	@GET
	@Path("userHome/{userId}")
	@RolesAllowed({ "authenticated", "teacher", "administrator", "individual", "superAdmin", "transporter","gateKeeper" })
	@Produces(MediaType.TEXT_HTML)
	public Response getUserHome(@HeaderParam("user-agent") String userAgent, @Context SecurityContext sc,
			@PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		DashboardLinks dashboardLinks = new DashboardLinks(userMakingRequest.getRole(), deviceUserAgent(userAgent));
		userMakingRequest.setApp(deviceUserAgent(userAgent));
		return Response.ok(new Viewable("/welcome", userMakingRequest)).build();
	}

	@Path("userPicture/{userId}")
	@Produces({ "image/png", "image/jpg", "image/gif" })
	public Response getFullImage(@Context SecurityContext sc, @PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();

		File userImage = new File(applicationConfig.getUserImageDirectory() + userMakingRequest.getUserImage());

		// uncomment line below to send non-streamed
		return Response.ok(userImage).build();

		// uncomment line below to send streamed
		// return Response.ok(new ByteArrayInputStream(imageData)).build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("absentMessage/{userId}")
	@PUT
	public Response absentMessage(@Context SecurityContext sc, @PathParam("userId") String userId,
			StudentRequest student) {
		if (student.getStudentId() == 1) {
			studentService.updateStudentPresence(userId, student);
		} else {
			studentService.updateStudentPresence(student, student.getStudentId());
		}
		return Response.ok().build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("PickUpRequest/{userId}")
	@PUT
	public Response PickUpRequest(@Context SecurityContext sc, @PathParam("userId") String userId,
			PickUpRequestModel request) {
		if(request.getStudentId()==1){
			studentService.sendPickUpRequest(request,userId);
		}else{
		studentService.sendPickUpRequest(request,request.getStudentId());
		}
		return Response.ok().build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("saveClassGroupRequest/{userId}")
	@PUT
	public Response saveClassGroupRequest(@Context SecurityContext sc, @PathParam("userId") String userId,
		ClassGroupModel request) {
		System.out.println(request.getClassName());
		userService.addClassGroup(request,userId);
		
		return Response.ok().build();
	}
	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("aprovePickUpRequest/{userId}")
	@PUT
	public Response aprovePickUpRequest(@Context SecurityContext sc, @PathParam("userId") String userId,
			PickUpRequestModel request) {
		studentService.aprovePickRequest(request);
		return Response.ok().build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("updatePickUpRequest/{userId}")
	@PUT
	public Response updatePickUpRequest(@Context SecurityContext sc, @PathParam("userId") String userId,
			PickUpRequestModel request) {
	          studentService.updatePickRequest(request, userId);
		return Response.ok().build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("getPickUpRecord/{userId}")
	@GET
	public Response getPickUpRecord(@Context SecurityContext sc, @PathParam("userId") String userId) {
		
		List<PickUpRequestModel> pickModel = studentService.getpickAlerts(userId);
		return Response.ok().entity(pickModel).build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher", "individual", "superAdmin" })
	@Path("updateAlertSettings/{userId}")
	@PUT
	public Response updateAlertSettings(@Context SecurityContext sc, @PathParam("userId") String userId,
			List<AlertConfigVM> alertConfigs) {

		try {
			studentService.updateAlertSettings(userId, alertConfigs);
			return Response.ok().build();

		} catch (Exception ex) {
			LOG.debug("Error while updating alert settings", ex);
			return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
		}
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher", "individual", "superAdmin" })
	@Path("getAlertSettings/{userId}")
	@GET
	public Response getAlertSettings(@Context SecurityContext sc, @PathParam("userId") String userId) {

		try {
			List<AlertConfigVM> alertConfigs = studentService.getAlertSettings(userId);
			return Response.ok().entity(alertConfigs).build();

		} catch (Exception ex) {
			LOG.debug("Error while getting alert settings", ex);
			return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
		}
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher", "individual", "superAdmin", "transporter" })
	@Path("updateNotificationSettings/{userId}")
	@PUT
	public Response updateNotificationSettings(@Context SecurityContext sc, @PathParam("userId") String userId,
			AlertNotificationsConfig config) {

		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();

		String alertUpdateType = config.getAlertUpdateType();

		Date pickStartTime = config.getPickStartTime();
		Date pickEndTime = config.getPickEndTime();

		Date dropStartTime = config.getDropStartTime();
		Date dropEndTime = config.getDropEndTime();

		ArrayList<String> activeDays = config.getActiveDays();

		ArrayList<String> alertTypes = config.getAlertTypes();

		userService.updateUserNotificationSettings(userId, alertUpdateType, config);

		return Response.ok().build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher", "individual", "superAdmin" })
	@Path("getUserNotificationSettings/{userId}")
	@GET
	public Response getUserNotificationSettings(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<AlertNotificationsConfig> alertNotificationsConfigs = userService.getUserNotificationSettings(userId);
		return Response.ok().entity(alertNotificationsConfigs).build();
	}

	@RolesAllowed({ "authenticated" })
	@Path("addComplaintMessage/{userId}")
	@PUT
	public Response addComplaintMessage(@Context SecurityContext sc, @PathParam("userId") String userId,
			ComplaintMessageRequest complaintMessage) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();

		userService.sendComplaintMessage(userId, complaintMessage, userMakingRequest);

		return Response.ok().entity(complaintMessage).build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher", "superAdmin" })
	@Path("getSchoolInfo/{userId}")
	@GET
	public Response getSchoolInfo(@Context SecurityContext sc, @PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();

		List<HashMap> schoolList = new ArrayList<HashMap>();
		List schoolIdList = new ArrayList();

		if (Role.authenticated.toString().equals(userMakingRequest.getRole())) {
			List<Student> studentDetailList = studentService.getStudentDetails(userId);
			for (Student student : studentDetailList) {
				School school = student.getSchool();

				if (school != null && !schoolIdList.contains(school.getId())) {
					String busNumber = student.getWayPoint().getRoute().getRouteFleetDeviceXREF().getFleet()
							.getRegNumber();
					String driverName = student.getWayPoint().getRoute().getRouteFleetDeviceXREF().getFleet()
							.getDriverName();
					String driverMobile = student.getWayPoint().getRoute().getRouteFleetDeviceXREF().getFleet()
							.getDriverMobile();
					String navigatorName = student.getWayPoint().getRoute().getRouteFleetDeviceXREF().getFleet()
							.getNavigatorName();
					String navigatorMobile = student.getWayPoint().getRoute().getRouteFleetDeviceXREF().getFleet()
							.getNavigatorMobile();

					String conductor2Name = student.getWayPoint().getRoute().getRouteFleetDeviceXREF().getFleet()
							.getConductor2Name();
					String conductor2Mobile = student.getWayPoint().getRoute().getRouteFleetDeviceXREF().getFleet()
							.getConductor2Mobile();

					HashMap schoolDetail = new HashMap();
					schoolDetail.put("name", school.getName());
					schoolDetail.put("type", school.getSchoolType());

					schoolDetail.put("phoneNumber1", school.getPhoneNumber1());
					schoolDetail.put("phoneNumber2", school.getPhoneNumber2());
					schoolDetail.put("phoneNumber3", school.getPhoneNumber3());
					schoolDetail.put("emailId", school.getEmailId());
					schoolDetail.put("displayAddress", school.getDisplayAddress());

					if (busNumber != null && !busNumber.equals(""))
						schoolDetail.put("busNumber", busNumber);
					if (driverName != null && !driverName.equals(""))
						schoolDetail.put("driverName", driverName);
					if (driverMobile != null && !driverMobile.equals(""))
						schoolDetail.put("driverMobile", driverMobile);
					if (navigatorName != null && !navigatorName.equals(""))
						schoolDetail.put("navigatorName", navigatorName);
					if (navigatorMobile != null && !navigatorMobile.equals(""))
						schoolDetail.put("navigatorMobile", navigatorMobile);
					if (conductor2Name != null && !"".equals(conductor2Name))
						schoolDetail.put("conductor2Name", conductor2Name);
					if (conductor2Mobile != null && !"".equals(conductor2Mobile))
						schoolDetail.put("conductor2Mobile", conductor2Mobile);
					// schoolDetail.put("address",
					// school.getAddress().getAddressLine1()+"
					// "+school.getAddress().getAddressLine2());
					schoolIdList.add(school.getId());
					schoolList.add(schoolDetail);
				}
			}
		} else if (Role.teacher.toString().equals(userMakingRequest.getRole())) {
			UserSchool us = userService.getSchoolbyUser(userMakingRequest.getUserId());

			School school = us.getSchool();

			if (school != null && !schoolIdList.contains(school.getId())) {
				HashMap schoolDetail = new HashMap();
				schoolDetail.put("name", school.getName());
				schoolDetail.put("type", school.getSchoolType());
				schoolDetail.put("phoneNumber1", school.getPhoneNumber1());
				schoolDetail.put("phoneNumber2", school.getPhoneNumber2());
				schoolDetail.put("phoneNumber3", school.getPhoneNumber3());
				schoolDetail.put("emailId", school.getEmailId());
				schoolDetail.put("displayAddress", school.getDisplayAddress());
				schoolIdList.add(school.getId());
				schoolList.add(schoolDetail);
			}
		}

		return Response.ok().entity(schoolList).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("getSchoolsForDropdown")
	@GET
	public Response getSchoolsForDropdown(@Context SecurityContext sc) {
		List<SchoolModel> schools = userService.getSchoolsForDropDown();
		return Response.ok().entity(schools).build();
	}

	@RolesAllowed({ "administrator", "superAdmin", "authenticated" })
	@Path("getSchoolHolidaysByUserId/{userId}")
	@GET
	public Response getSchoolHolidaysByUserId(@Context SecurityContext sc, @PathParam("userId") String userId) {

		SchoolHolidaysModel holidays = new SchoolHolidaysModel();

		String userRole = userService.getUserRole(userId).name();

		if (userRole.equalsIgnoreCase("superAdmin")) {
			holidays = userService.getAllSchoolHolidaysModel();
		} else if (userRole.equalsIgnoreCase("administrator")) {
			holidays = userService.getSchoolHolidaysModelByUserId(userId);
		} else {
			holidays = userService.getSchoolHolidaysModelByUserIdForParent(userId);
		}

		return Response.ok().entity(holidays).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("addSchoolHoliday")
	@POST
	public Response addSchoolHoliday(SchoolHolidayModel model) {
		try {
			model = userService.addUpdateSchoolHoliday(model);
		} catch (Exception e) {
			return Response.ok().entity(e).build();
		}
		return Response.ok().entity(model).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("deleteSchoolHoliday/{holidayId}")
	@GET
	public Response deleteSchoolHoliday(@Context SecurityContext sc, @PathParam("holidayId") Long holidayId) {
		try {
			userService.deleteSchoolHoliday(holidayId);
		} catch (Exception e) {
			return Response.ok().entity(e).build();
		}
		return Response.ok().entity(true).build();
	}

	// //school holiday master data

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("getSchoolHolidayMasterData/{query}")
	@GET
	public Response getSchoolHolidayMasterData(@Context SecurityContext sc, @PathParam("query") String query) {
		List<SchoolHolidayMasterDataModel> masterData = userService.getSchoolHolidayMasterData(query);
		return Response.ok().entity(masterData).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("getSchoolHolidayMasterDataById/{masterDataId}")
	@GET
	public Response getSchoolHolidayMasterDataById(@Context SecurityContext sc,
			@PathParam("masterDataId") Long masterDataId) {
		SchoolHolidayMasterDataModel masterData = userService.getSchoolHolidayMasterDataById(masterDataId);
		return Response.ok().entity(masterData).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("addSchoolHolidayMasterData")
	@POST
	public Response addSchoolHolidayMasterData(SchoolHolidayMasterDataModel model) {
		try {

			model = userService.addUpdateSchoolHolidayMasterData(model);
		} catch (Exception e) {
			return Response.ok().entity(e).build();
		}
		return Response.ok().entity(model).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("deleteSchoolHolidayMasterData/{masterDataId}")
	@GET
	public Response deleteSchoolHolidayMasterData(@Context SecurityContext sc,
			@PathParam("masterDataId") Long masterDataId) {
		try {
			userService.deleteSchoolHolidayMasterData(masterDataId);
		} catch (Exception e) {
			return Response.ok().entity(e).build();
		}
		return Response.ok().entity(true).build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("absentReasons/{userId}")
	@GET
	public Response absentReasons(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<AbsentReasonModel> reasonList = userService.getAbsentReason();

		return Response.ok().entity(reasonList).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@GET
	@Path("getSchoolDetail/{userId}/{schoolID}")
	public Response getSchoolDetail(@Context SecurityContext sc, @PathParam("userId") String userId,
			@PathParam("schoolID") String schoolID) {

		SchoolModel schoolModel = userService.getSchoolModelDetailById(Long.valueOf(schoolID));

		return Response.ok().entity(schoolModel).build();
	}

	@RolesAllowed({ "administrator" })
	@GET
	@Path("getSchoolHolidayDetail/{holidayId}")
	public Response getSchoolHolidayDetail(@Context SecurityContext sc, @PathParam("holidayId") String holidayId) {

		SchoolHolidayModel holidayModel = userService.getSchoolHolidayModelDetailById(Long.valueOf(holidayId));

		return Response.ok().entity(holidayModel).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("getAllUsersAdmin/{userId}")
	@GET
	public Response getAllUsersAdmin(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<UserVM> userList = userService.getAllUsersAdmin(userId);
		return Response.ok().entity(userList).build();
	}

	@RolesAllowed({ "authenticated", "administrator" })
	@Path("sendmailForLatLongApproval/{userId}")
	@PUT
	public Response sendmailForLatLongApproval(@Context SecurityContext sc, @PathParam("userId") String userId,
			StudentRequest student) {
		/*
		 * ComplaintMessageRequest request;
		 * userService.broadcastMsgUsersByRouteId(request, userId);
		 */
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		userService.sendmailForLatLongApproval(userId, student, userMakingRequest);

		return Response.ok(200).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("getAllAdministrator/{userId}")
	@GET
	public Response getAllAdministrator(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<UserVM> userList = userService.getAllAdministrator();
		return Response.ok().entity(userList).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("getAllTeachers/{userId}")
	@GET
	public Response getAllTeachers(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<UserVM> userList = userService.getAllTeachers(userId);
		return Response.ok().entity(userList).build();
	}

	@RolesAllowed({ "superAdmin" })
	@Path("getUnMappedSchool/{userId}")
	@GET
	public Response getUnMappedSchool(@Context SecurityContext sc) {
		List<School> unMappedSchoolList = null;
		// userService.getUnMappedSchool();
		return Response.ok().entity(unMappedSchoolList).build();
	}

	@RolesAllowed({ "administrator" })
	@Path("addUpdateAssigment/{userId}")
	@PUT
	public Response addUpdateAssigment(@Context SecurityContext sc, @PathParam("userId") String userId,
			StudentAssignment req) {
		try {

			studentService.addUpdateAssigment(req, userId);

			return Response.ok().build();
		} catch (Exception ex) {
			return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
		}
	}
	@RolesAllowed({ "administrator" ,"gateKeeper"})
	@Path("sendParentsVisitorRequest/{userId}")
	@PUT
	public Response sendParentsVisitorRequest(@Context SecurityContext sc, @PathParam("userId") String userId,
			visitorModel visitorRequest) {
		try {
		ServiceResponse response=userService.saveVisitorRequest(userId, visitorRequest);
		return Response.ok().entity(response).build();
		} catch (Exception ex) {
			return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
		}
	}
	@RolesAllowed({ "administrator", "superAdmin" })
	@GET
	@Path("getUnassignedDevice/{userId}")
	public Response getUnassignedDevice(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<RouteVM> routeVM = new ArrayList<RouteVM>();
		String userRole = userService.getUserRole(userId).name();
		if (userRole.equalsIgnoreCase("superAdmin")) {
			routeVM = userService.getUnassignedDevice();

		} else if (userRole.equalsIgnoreCase("administrator")) {
			routeVM = userService.getUnassignedDevice(userId);
		} else {
			routeVM = userService.getUnassignedDevice();
		}
		return Response.ok().entity(routeVM).build();
	}

	@RolesAllowed({ "administrator" })
	@POST
	@Path("addSchoolTeacher")
	public Response addSchoolTeacher(@Context SecurityContext sc, ExternalUser teacher) {
		ExternalUser currentUser = (ExternalUser) sc.getUserPrincipal();
		List<School> schools = userService.getSchoolDetails(currentUser.getId());
		Long schoolId = 0L;
		if (schools != null && schools.size() > 0) {
			schoolId = schools.get(0).getId();
		}
		return Response.ok().entity(userService.addSchoolTeacher(schoolId, teacher)).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@GET
	@Path("getSchoolDetails/{userId}")
	public Response getSchoolDetails(@Context SecurityContext sc, @PathParam("userId") String userId) {
		ExternalUser currentUser = (ExternalUser) sc.getUserPrincipal();
		List<School> schools = userService.getSchoolDetails(userId);
		List<SchoolModel> schoolModels = new ArrayList<SchoolModel>();
		for (School school : schools) {
			schoolModels.add(new SchoolModel(school));
		}
		return Response.ok().entity(schoolModels).build();
	}

	@RolesAllowed({ "administrator" })
	@POST
	@Path("updateSchoolTeacher")
	public Response updateSchoolTeacher(@Context SecurityContext sc, ExternalUser teacher) {
		return Response.ok().entity(userService.updateSchoolTeacher(teacher)).build();
	}
	@RolesAllowed({ "administrator","gateKeeper" })
	@PUT
	@Path("sendOTPRequest/{userId}")
	public Response sendOTPRequest(@Context SecurityContext sc,@PathParam("userId") String userId,visitorModel visitorModel ) {
		
		
		return Response.ok().entity(userService.varifiedOTPRequest(userId, visitorModel)).build();
	}
	@RolesAllowed({ "administrator","gateKeeper" })
	@PUT
	@Path("resendOTPRequest/{userId}")
	public Response resendOTPRequest(@Context SecurityContext sc,@PathParam("userId") String userId,visitorModel visitorModel ) {
		System.out.println("resend");
		
		return Response.ok().entity(userService.sendOTPMessageToUser(visitorModel)).build();
	}

	@RolesAllowed({ "administrator" })
	@GET
	@Path("deleteSchoolTeacher/{teacherUUID}")
	public Response deleteSchoolTeacher(@Context SecurityContext sc, @PathParam("teacherUUID") String teacherUUID) {
		return Response.ok().entity(userService.deleteSchoolTeacher(teacherUUID)).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("getDeviceModelId/{userId}")
	@GET
	public Response getDeviceModelId(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<DeviceModel> deviceModelId = userService.getDeviceModelId(userId);
		return Response.ok().entity(deviceModelId).build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("getStudentAbsentRecord/{userId}")
	@GET
	public Response getStudentAbsentRecord(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<AbsentReasonModel> reasonList = userService.getStudentAbsentRecord(userId);

		return Response.ok().entity(reasonList).build();
	}
	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("getClassGroup/{userId}")
	@GET
	public Response getClassGroup(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<ClassGroupModel> classGroupList = userService.getclassGroupRecord(userId);

		return Response.ok().entity(classGroupList).build();
	}
	
}
