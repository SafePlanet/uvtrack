/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.VM;

//import java.time.temporal.Temporal;
import java.util.*;

public class StudentVM {

	private Long studentId;
	private Long userId;
	private String studentUUID;
	private String firstName;
	private String lastName;
	private String email_address;
	private String mobile_number;
	private String studentClass;
	private long regId;
	private String section;
	private String absentMessage;
	private long schoolId;
	private char presentFlag;
	private char isApproved;
	private long absentid;
	private Date absentFromDate;
	private Date absentToDate;
	private String absentReason;
	private String routeName;
	private String parentName;
	private String schoolName;
	private Object identifier;
	private Date pickTimeSummer;
	private Date winterPickup;
	private Date drop;
	private long routeId;
	private long wayPointId;
	private Double lattitude;
	private Double longitude;
	private String address_line1;
	private String address_line2;
	private String city;
	private String State;
	private String country;
	private String pinCode;

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

	/**
	 * @return the schoolId
	 */
	public long getSchoolId() {
		return schoolId;
	}

	/**
	 * @param schoolId
	 *            the schoolId to set
	 */
	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getStudentUUID() {
		return studentUUID;
	}

	public void setStudentUUID(String studentUUID) {
		this.studentUUID = studentUUID;
	}

	public char getPresentFlag() {
		return presentFlag;
	}

	public void setPresentFlag(char presentFlag) {
		this.presentFlag = presentFlag;
	}

	public String getAbsentMessage() {
		return absentMessage;
	}

	public void setAbsentMessage(String absentMessage) {
		this.absentMessage = absentMessage;
	}

	public char getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(char isApproved) {
		this.isApproved = isApproved;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Object getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Object identifier) {
		this.identifier = identifier;
	}

	public Date getAbsentToDate() {
		return absentToDate;
	}

	public void setAbsentToDate(Date absentToDate) {
		this.absentToDate = absentToDate;
	}

	public Date getAbsentFromDate() {
		return absentFromDate;
	}

	public void setAbsentFromDate(Date absentFromDate) {
		this.absentFromDate = absentFromDate;
	}

	public long getAbsentid() {
		return absentid;
	}

	public void setAbsentid(long absentid) {
		this.absentid = absentid;
	}

	public Date getPickTimeSummer() {
		return pickTimeSummer;
	}

	public void setPickTimeSummer(Date pickTimeSummer) {
		this.pickTimeSummer = pickTimeSummer;
	}

	public Date getWinterPickup() {
		return winterPickup;
	}

	public void setWinterPickup(Date winterPickup) {
		this.winterPickup = winterPickup;
	}

	public Date getDrop() {
		return drop;
	}

	public void setDrop(Date drop) {
		this.drop = drop;
	}

	public long getRouteId() {
		return routeId;
	}

	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}

	public long getWayPointId() {
		return wayPointId;
	}

	public void setWayPointId(long wayPointId) {
		this.wayPointId = wayPointId;
	}

	public Double getLattitude() {
		return lattitude;
	}

	public void setLattitude(Double lattitude) {
		this.lattitude = lattitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;

	}

	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public String getAddress_line1() {
		return address_line1;
	}

	public void setAddress_line1(String address_line1) {
		this.address_line1 = address_line1;
	}

	public String getAddress_line2() {
		return address_line2;
	}

	public void setAddress_line2(String address_line2) {
		this.address_line2 = address_line2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getAbsentReason() {
		return absentReason;
	}

	public void setAbsentReason(String absentReason) {
		this.absentReason = absentReason;
	}
	
	public String toString(){
		return " :: studentId = " + studentId + " Name = " + getFirstName() + " " + getLastName() + 
				" Admission id = " + getRegId() + " userId = " + getUserId() + " wayPointId = " + getWayPointId(); 
	}

}
