/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "school")
public class School extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -689628423689103683L;
	String name;
	@Enumerated(EnumType.STRING)
	String schoolType;
	String emailId;
	String phoneNumber1;
	String phoneNumber2;
	String phoneNumber3;
    String subType;
    Date serviceStartDate;
    Date serviceEndDate;
    String subAmount;
	
    String displayAddress;
        
	int voidInd;

	@OneToOne(mappedBy = "school", targetEntity = Address.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	private Address address;
	
	@OneToMany(mappedBy = "school", targetEntity = UserGroupSchool.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<UserGroupSchool> userGroupSchools;

	@OneToMany(mappedBy = "school", targetEntity = SchoolConfig.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<SchoolConfig> schoolConfigs = new ArrayList<SchoolConfig>();
	
	@OneToMany(mappedBy = "school", targetEntity = Route.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<Route> routes = new ArrayList<Route>();

	@OneToMany(mappedBy = "school", targetEntity = Student.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<Student> students = new ArrayList<Student>();

	@OneToMany(mappedBy = "school", targetEntity = SchoolHoliday.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<SchoolHoliday> holidays;// = new ArrayList<SchoolHoliday>();
	
//	@OneToOne(mappedBy = "school", targetEntity = UserGroupSchool.class, cascade = CascadeType.ALL)
//	private UserGroupSchool userGroupSchool;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name == null ? "" : name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the schoolType
	 */
	public String getSchoolType() {
		return schoolType == null ? "" : schoolType;
	}

	/**
	 * @param schoolType
	 *            the schoolType to set
	 */
	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId == null ? "" : emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the phoneNumber1
	 */
	public String getPhoneNumber1() {
		return phoneNumber1 == null ? "" : phoneNumber1;
	}

	/**
	 * @param phoneNumber1
	 *            the phoneNumber1 to set
	 */
	public void setPhoneNumber1(String phoneNumber1) {
		this.phoneNumber1 = phoneNumber1;
	}

	/**
	 * @return the phoneNumber2
	 */
	public String getPhoneNumber2() {
		return phoneNumber2 == null ? "" : phoneNumber2;
	}

	/**
	 * @param phoneNumber2
	 *            the phoneNumber2 to set
	 */
	public void setPhoneNumber2(String phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}

	/**
	 * @return the voidInd
	 */
	public int getVoidInd() {
		return voidInd;
	}

	/**
	 * @param voidInd
	 *            the voidInd to set
	 */
	public void setVoidInd(int voidInd) {
		this.voidInd = voidInd;
	}

    public String getPhoneNumber3() {
        return phoneNumber3 == null ? "" : phoneNumber3;
    }

    public void setPhoneNumber3(String phoneNumber3) {
        this.phoneNumber3 = phoneNumber3;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getDisplayAddress() {
        return displayAddress;
    }

    public void setDisplayAddress(String displayAddress) {
        this.displayAddress = displayAddress;
    }

	public List<SchoolConfig> getSchoolConfigs() {
		return schoolConfigs;
	}

	public void setSchoolConfigs(List<SchoolConfig> schoolConfigs) {
		this.schoolConfigs = schoolConfigs;
	}
	
	public List<SchoolHoliday> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<SchoolHoliday> holidays) {
		this.holidays = holidays;
	}

	public List<UserGroupSchool> getUserGroupSchools() {
		return userGroupSchools;
	}

	public void setUserGroupSchools(List<UserGroupSchool> userGroupSchools) {
		this.userGroupSchools = userGroupSchools;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getSubAmount() {
		return subAmount;
	}

	public void setSubAmount(String subAmount) {
		this.subAmount = subAmount;
	}

	public Date getServiceStartDate() {
		return serviceStartDate;
	}

	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}

	public Date getServiceEndDate() {
		return serviceEndDate;
	}

	public void setServiceEndDate(Date serviceEndDate) {
		this.serviceEndDate = serviceEndDate;
	}
	
	
}
