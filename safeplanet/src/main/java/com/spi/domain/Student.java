/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.spi.VM.StudentVM;
import com.spi.model.BaseEntity;
import com.spi.user.api.StudentRequest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OrderBy;

@Entity
@Table(name = "student")
public class Student extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1417416063318717977L;

	public Student(String firstName, String lastName, String studentClass, long regId, String section, long schholId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.studentClass = studentClass;
		this.regId = regId;
		this.section = section;

	}

	public Student(StudentVM studentVM, User user, School school) {
		super();
		this.user = user;
		this.firstName = studentVM.getFirstName();
		this.lastName = studentVM.getLastName();
		this.studentClass = studentVM.getStudentClass();
		this.regId = studentVM.getRegId();
		this.section = studentVM.getSection();
		this.school = school;
		this.isApproved = studentVM.getIsApproved();

	}

	public Student(StudentRequest studentRequest, User user, School school) {
		super();
		this.user = user;
		this.firstName = studentRequest.getFirstName();
		this.lastName = studentRequest.getLastName();
		this.studentClass = studentRequest.getStudentClass();
		this.regId = studentRequest.getRegId();
		this.section = studentRequest.getSection();
		this.school = school;
//		this.isApproved = studentRequest.getIsApproved();
		this.isApproved = 'Y';

	}

	public Student() {
		this(UUID.randomUUID());
	}

	public Student(UUID guid) {
		super(guid);
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;

	@ManyToOne
	@JoinColumn(name = "school_id")
	School school;
	
	@ManyToOne
	@JoinColumn(name = "way_point_id")
	@NotFound(action=NotFoundAction.IGNORE)
	WayPoint wayPoint;

	@OneToOne(mappedBy = "student", targetEntity = AlertConfig.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	AlertConfig alertConfig;
	
	@OneToMany(mappedBy = "student", targetEntity = Absent.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause = "date desc")
	private List<Absent> absent = new ArrayList<Absent>();

	private String firstName;
	private String lastName;
	private String studentClass;
	private long regId;
	private String section;

	@Column(name = "is_approved", columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N'")
	private char isApproved;

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the studentClass
	 */
	public String getStudentClass() {
		return studentClass;
	}

	/**
	 * @param studentClass
	 *            the studentClass to set
	 */
	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}

	/**
	 * @return the regId
	 */
	public long getRegId() {
		return regId;
	}

	/**
	 * @param regId
	 *            the regId to set
	 */
	public void setRegId(long regId) {
		this.regId = regId;
	}

	/**
	 * @return the section
	 */
	public String getSection() {
		return section;
	}

	/**
	 * @param section
	 *            the section to set
	 */
	public void setSection(String section) {
		this.section = section;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public List<Absent> getAbsent() {
		return absent;
	}

	public void setAbsent(List<Absent> absent) {
		this.absent = absent;
	}

	public char getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(char isApproved) {
		this.isApproved = isApproved;
	}

	public AlertConfig getAlertConfig() {
		return alertConfig;
	}

	public void setAlertConfig(AlertConfig alertConfig) {
		this.alertConfig = alertConfig;
	}
	public WayPoint getWayPoint() {
		return wayPoint;
	}

	public void setWayPoint(WayPoint wayPoint) {
		this.wayPoint = wayPoint;
	}
	

	@Override
	public String toString() {
		return "Student [user=" + user + ", school=" + school + ", alertConfig=" + alertConfig + ", wayPoint=" + wayPoint + ", absent=" + absent + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", studentClass=" + studentClass + ", regId=" + regId + ", section=" + section + ", isApproved=" + isApproved + "]";
	}

}