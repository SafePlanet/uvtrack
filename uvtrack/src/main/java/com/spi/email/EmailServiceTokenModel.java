/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.spi.domain.Fleet;
import com.spi.domain.Route;
import com.spi.domain.Student;
import com.spi.domain.User;
import com.spi.domain.VerificationToken;
import com.spi.user.api.ExternalUser;

/**
 *
 * @version 1.0
 * @author:
 * 
 */
public class EmailServiceTokenModel implements Serializable {

	private static final long serialVersionUID = -6706997308126703420L;
	private String toEmailAddress = null;
	private String ccEmailAddress = null;
	private String bccEmailAddress = null;
	private String token;
	private VerificationToken.VerificationTokenType tokenType;
	private String hostNameUrl;
	private List<Student> students = new ArrayList<Student>();
	private String schoolName = null;
	private String schoolContact = null;
	private String schoolAddress = null;
	private String schoolSession = null;
	private String wayPointLink = null;
	private String schoolEmailAddress ;
	private String emailSubject = null;
	private String emailBody = null;
	private String busNumber = null;
	private String driverName = null;
	private String driverNumber = null;
	private String conductorName = null;
	private String conductorNumber = null;
	private String attendantName = null;
	private String attendantNumber = null;
    
	private String lostPassword;
	
	private String userName;

	private String complaintMessage;

	public EmailServiceTokenModel(User user, VerificationToken token, String hostNameUrl) {
		this.toEmailAddress = user.getEmailAddress();
		this.token = token.getToken();
		this.tokenType = token.getTokenType();
		this.hostNameUrl = hostNameUrl;
		this.userName = user.getFirstName() + " " + user.getLastName();
		
	}
	public EmailServiceTokenModel(User user, VerificationToken token, String hostNameUrl ,String lostPassword ,Route route) {
		this.toEmailAddress = user.getEmailAddress();
		this.token = token.getToken();
		this.tokenType = token.getTokenType();
		this.hostNameUrl = hostNameUrl;
		this.userName = user.getFirstName() + " " + user.getLastName();
		this.lostPassword=lostPassword;
		this.schoolName=route.getSchool().getName();
		
	}
	// For Custom Email
	public EmailServiceTokenModel(VerificationToken token, String userName, String emailAddress, String schoolName,
			String schoolContact, String schoolAddress, String schoolEmailAddress, String emailSubject, String emailBody) {
		this.token = token.getToken();
		this.tokenType = token.getTokenType();
		this.toEmailAddress = emailAddress;
		this.userName = userName;
		this.schoolContact = schoolContact;
		this.schoolName = schoolName;
		this.schoolAddress = schoolAddress;
		this.schoolEmailAddress = schoolEmailAddress;
		this.emailSubject = emailSubject;
		this.emailBody = emailBody;
	}

	// For Welcome Email
	public EmailServiceTokenModel(VerificationToken token, String userName, List<Student> students, String emailAddress, String schoolName,
			String schoolContact, String schoolAddress, String schoolEmailAddress, String wayPointLink, Fleet fleet) {
		this.token = token.getToken();
		this.tokenType = token.getTokenType();
		this.toEmailAddress = emailAddress;
		this.students = students;
		this.userName = userName;
		this.schoolContact = schoolContact;
		this.schoolName = schoolName;
		this.schoolAddress = schoolAddress;
		this.schoolEmailAddress = schoolEmailAddress;
		this.wayPointLink = wayPointLink;
		this.busNumber = fleet.getRegNumber();
		this.driverName = fleet.getDriverName();
		this.driverNumber = fleet.getDriverMobile();
		this.conductorName = fleet.getNavigatorName();
		this.conductorNumber = fleet.getNavigatorMobile();
		this.attendantName = fleet.getConductor2Name();
		this.attendantNumber = fleet.getConductor2Mobile();
	}

	public EmailServiceTokenModel(ExternalUser user, VerificationToken token, String hostNameUrl, String userName, String complaintMessage,
			String emailId, String schoolEmailAddress ,List<Student> student) {
		this.toEmailAddress = emailId;
		this.ccEmailAddress = user.getEmailAddress();
		this.token = token.getToken();
		this.tokenType = token.getTokenType();
		this.hostNameUrl = hostNameUrl;
		this.userName = userName;
		this.students=student;
		this.schoolEmailAddress = schoolEmailAddress;
		this.complaintMessage = complaintMessage;
	}

	public String getEncodedToken() {
		return new String(Base64.encodeBase64(token.getBytes()));
	}

	public String getToken() {
		return token;
	}

	public VerificationToken.VerificationTokenType getTokenType() {
		return tokenType;
	}

	public String getHostNameUrl() {
		return hostNameUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getComplaintMessage() {
		return complaintMessage;
	}

	public void setComplaintMessage(String complaintMessage) {
		this.complaintMessage = complaintMessage;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSchoolContact() {
		return schoolContact;
	}

	public void setSchoolContact(String schoolContact) {
		this.schoolContact = schoolContact;
	}

	public String getSchoolAddress() {
		return schoolAddress;
	}

	public void setSchoolAddress(String schoolAddress) {
		this.schoolAddress = schoolAddress;
	}

	public String getWayPointLink() {
		return wayPointLink;
	}

	public void setWayPointLink(String wayPointLink) {
		this.wayPointLink = wayPointLink;
	}

	public String getSchoolSession() {
		return schoolSession;
	}

	public void setSchoolSession(String schoolSession) {
		this.schoolSession = schoolSession;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getSchoolEmailAddress() {
		return schoolEmailAddress;
	}
	public String getBusNumber() {
		return busNumber;
	}
	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverNumber() {
		return driverNumber;
	}
	public void setDriverNumber(String driverNumber) {
		this.driverNumber = driverNumber;
	}
	public String getConductorName() {
		return conductorName;
	}
	public void setConductorName(String conductorName) {
		this.conductorName = conductorName;
	}
	public String getConductorNumber() {
		return conductorNumber;
	}
	public void setConductorNumber(String conductorNumber) {
		this.conductorNumber = conductorNumber;
	}
	public String getAttendantName() {
		return attendantName;
	}
	public void setAttendantName(String attendantName) {
		this.attendantName = attendantName;
	}
	public String getAttendantNumber() {
		return attendantNumber;
	}
	public void setAttendantNumber(String attendantNumber) {
		this.attendantNumber = attendantNumber;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setTokenType(VerificationToken.VerificationTokenType tokenType) {
		this.tokenType = tokenType;
	}
	public void setHostNameUrl(String hostNameUrl) {
		this.hostNameUrl = hostNameUrl;
	}
	public void setSchoolEmailAddress(String schoolEmailAddress) {
		this.schoolEmailAddress = schoolEmailAddress;
	}
	public String getToEmailAddress() {
		return toEmailAddress;
	}
	public String getCcEmailAddress() {
		return ccEmailAddress;
	}
	public String getBccEmailAddress() {
		return bccEmailAddress;
	}public String getLostPassword() {
		return lostPassword;
	}
	public void setLostPassword(String lostPassword) {
		this.lostPassword = lostPassword;
	}
	

}
