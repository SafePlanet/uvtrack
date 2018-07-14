/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.service;

import java.util.List;

import org.springframework.social.connect.Connection;

import com.spi.VM.RouteVM;
import com.spi.VM.StudentVM;
import com.spi.VM.UserVM;
import com.spi.api.ServiceResponse;
import com.spi.domain.AuthorizationToken;
import com.spi.domain.Role;
import com.spi.domain.Route;
import com.spi.domain.School;
import com.spi.domain.SchoolHoliday;
import com.spi.domain.State;
import com.spi.domain.Student;
import com.spi.domain.User;
import com.spi.domain.UserSchool;
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

/**
 * @author:
 *
 * 			Service to manage users
 */
public interface UserService {

	/**
	 * Create a new User with the given role
	 *
	 * @param request
	 * @param role
	 * @return AuthenticatedUserToken
	 */
	public AuthenticatedUserToken createUser(CreateUserRequest request, Role role);

	/**
	 * Create a Default User with a given role
	 *
	 * @param role
	 * @return AuthenticatedUserToken
	 */
	public AuthenticatedUserToken createUser(Role role);

	/**
	 * Login a User
	 *
	 * @param request
	 * @return AuthenticatedUserToken
	 */
	public AuthenticatedUserToken login(LoginRequest request);

	/**
	 * Log in a User using Connection details from an authorized request from
	 * the User's supported Social provider encapsulated in the
	 * {@link org.springframework.social.connect.Connection} parameter
	 *
	 * @param connection
	 *            containing the details of the authorized user account form the
	 *            Social provider
	 * @return the User account linked to the {@link com.spi.domain.SocialUser}
	 *         account
	 */
	public AuthenticatedUserToken socialLogin(Connection<?> connection);

	/**
	 * Get a User based on a unique identifier
	 *
	 * Identifiers supported are uuid, emailAddress
	 *
	 * @param userIdentifier
	 * @return User
	 */
	public ExternalUser getUser(ExternalUser requestingUser, String userIdentifier);

	/**
	 * Delete user, only authenticated user accounts can be deleted
	 *
	 * @param userMakingRequest
	 *            the user authorized to delete the user
	 * @param userId
	 *            the id of the user to delete
	 */
	public void deleteUser(ExternalUser userMakingRequest, String userId);

	/**
	 * Save User
	 *
	 * @param userId
	 * @param request
	 */
	public ExternalUser saveUser(String userId, UpdateUserRequest request);

	/**
	 * Create an AuthorizationToken for the User
	 *
	 * @return
	 */
	public AuthorizationToken createAuthorizationToken(User user);

	/**
	 * get Student Details for a User
	 *
	 * @return
	 */
	public List<Student> getStudentDetails(String userId);

	public List<SchoolModel> getSchoolsForDropDown();

	/**
	 * get holidays of schools of a user
	 *
	 * @return
	 */
	public SchoolHolidaysModel getSchoolHolidaysModelByUserId(String userUuId);

	public SchoolHolidaysModel getSchoolHolidaysModelByUserIdForParent(String userUuId);

	public SchoolHolidayModel getSchoolHolidayModelDetailById(Long id);

	public SchoolHolidayModel addUpdateSchoolHoliday(SchoolHolidayModel holiday);

	public Boolean deleteSchoolHoliday(Long holidayId);

	// school holiday master data

	public List<SchoolHolidayMasterDataModel> getSchoolHolidayMasterData(String query);

	public SchoolHolidayMasterDataModel getSchoolHolidayMasterDataById(Long id);

	public SchoolHolidayMasterDataModel addUpdateSchoolHolidayMasterData(SchoolHolidayMasterDataModel holidayMasterData);

	public Boolean deleteSchoolHolidayMasterData(Long holidayMasterDataId);

	/**
	 * Update User
	 *
	 * @param userId
	 * @param request
	 */
	public ExternalUser updateUser(String userId, UpdateUserRequest request);

	public ExternalUser updateUserNotificationSettings(String userId, String configType, AlertNotificationsConfig config);

	public List<AlertNotificationsConfig> getUserNotificationSettings(String userId);

	public void addComplaintMessage(String userId, String message);

	public List<AbsentReasonModel> getAbsentReason();

	public Role getUserRole(String userUUID);

	public List<UserVM> getAllUsersAdmin(String userUuid);

	public void deactivateUser(String userUUID, String action);

	public Route getUserRoute(long routeId);

	public List<School> getAllSchoolDetails();

	public List<School> getSchoolDetails(String userId);

	public SchoolHoliday getSchoolHolidayById(Long id);

	public School getSchoolDetailById(Long id);

	public SchoolModel getSchoolModelDetailById(Long id);

	public void addSchool(School school);
	
	public void updateSchool(School school);

	public void sendComplaintMessage(String userId, ComplaintMessageRequest complaintMessage, ExternalUser userMakingRequest);

	public void addUser(String lastName, String firstName, String houseNo, String address, String pinCode, String city, String state, String mobile,
			String emailAddress, String userUUID, String studentsList);

	public List<StudentVM> retrieveStudentDetails(String userId);

	public User getUserById(String userUUId);

	public ServiceResponse changePassword(String userId, ChangePasswordRequest request);

	public void sendWelcomeEmailToUser();

	public void sendWelcomeEmail(User user, boolean keepSchoolInCc);

	public void sendCustomEmail(User user, String emailSubject, String emailBody, boolean keepSchoolInCc);

	public SchoolHolidaysModel getAllSchoolHolidaysModel();

	public void sendmailForLatLongApproval(String userId, StudentRequest student, ExternalUser userMakingRequest);

	public User findAdminByDeviceId(Long deviceId);

	public List<UserVM> getAllAdministrator();

	public List<UserVM> getAllTeachers(String userId);

	public List<RouteVM> getUnassignedDevice();

	public List<RouteVM> getUnassignedDevice(String userUuid);

	public void addAdmin(String lastName, String firstName, String houseNo, String address, String pinCode, String city, State state, String mobile,
			String emailAddress, String userUUID, String selectedSchoolId);

	public ServiceResponse addSchoolTeacher(Long schoolId, ExternalUser teacher);
	
	public ServiceResponse saveVisitorRequest(String userId, visitorModel visitorModel);

	public ServiceResponse varifiedOTPRequest(String userId,visitorModel visitorModel); 
	
	public ServiceResponse updateSchoolTeacher(ExternalUser teacher);

	public ServiceResponse deleteSchoolTeacher(String teacherUUID);

	public UserSchool getSchoolbyUser(long userId);

	public void saveFCMToken(String fcmToken, String userId);

	public String readLogFile(String file);

	public List<DeviceModel> getDeviceModelId(String userId);

	boolean urlStatus();
	
	public UserSchool getSchoolbyUser(String userUuid);
	
	public List<AbsentReasonModel> getStudentAbsentRecord(String userId);
	
	public void addClassGroup(ClassGroupModel request, String userId);
	
	public ServiceResponse sendOTPMessageToUser(visitorModel visitorNumber);
	public List<ClassGroupModel> getclassGroupRecord(String userId);
	
}
