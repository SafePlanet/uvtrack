/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.spi.domain.Student;
import com.spi.domain.User;
import com.spi.domain.VerificationToken;

/**
 *
 * @version 1.0
 * @author:
 * 
 */
public class WelcomeEmailModel implements Serializable {

	private static final long serialVersionUID = 8075622043221781018L;

	private String emailAddress;
	private List<Student> students = new ArrayList<Student>();
	private String schoolName = null;
	private String schoolContact = null;
	private String schoolAddress = null;
	private String userName = null;

	public WelcomeEmailModel(User user, VerificationToken token, String hostNameUrl) {
		this.emailAddress = user.getEmailAddress();
	}

	public WelcomeEmailModel(String userName, String emailId, String schoolName,
			String schoolContact, String schoolAddress) {
		this.emailAddress = emailId;
		this.userName = userName;
		this.schoolContact = schoolContact;
		this.schoolName = schoolName;
		this.schoolAddress = schoolAddress;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public String getSchoolContact() {
		return schoolContact;
	}

	public String getSchoolAddress() {
		return schoolAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public void setSchoolContact(String schoolContact) {
		this.schoolContact = schoolContact;
	}

	public void setSchoolAddress(String schoolAddress) {
		this.schoolAddress = schoolAddress;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

}
