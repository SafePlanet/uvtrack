package com.spi.user.api;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class PickUpRequestModel {
	@NotBlank(message = "{student.firstname.required}")
	private long studentId;
	private char presentFlag;
	private String message;
	private String className;
	private String firstName;
	private String lastName;
	private String userName;
	private String userLName;
	private String pickTime;
	private String Status;
	private char pickOrDrop ;
	private char requestAction;
	Date pickUpTime;
	private String uuid;
	private long schoolId;
	
	public long getStudentId() {
		
		return studentId;
	}
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public Date getPickUpTime() {
		return pickUpTime;
	}
	public void setPickUpTime(Date pickUpTime) {
		this.pickUpTime = pickUpTime;
	}
	public char getPresentFlag() {
		return presentFlag;
	}
	public void setPresentFlag(char presentFlag) {
		this.presentFlag = presentFlag;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPickTime() {
		return pickTime;
	}
	public void setPickTime(String pickTime) {
		this.pickTime = pickTime;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public long getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public char getPickOrDrop() {
		return pickOrDrop;
	}
	public void setPickOrDrop(char pickOrDrop) {
		this.pickOrDrop = pickOrDrop;
	}
	public char getRequestAction() {
		return requestAction;
	}
	public void setRequestAction(char requestAction) {
		this.requestAction = requestAction;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lasttName) {
		this.lastName = lasttName;
	}
	public String getUserLName() {
		return userLName;
	}
	public void setUserLName(String userLName) {
		this.userLName = userLName;
	}
	
	
}
