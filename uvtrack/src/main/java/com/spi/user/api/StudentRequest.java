package com.spi.user.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import com.spi.exception.ValidationException;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentRequest {

	// @Digits(integer = 12, fraction = 0, message="{student.regid.mustnumeric}")
	// @Range(min = 1L, message="{student.regid.invalid}")
	// @Pattern(regexp="^(?:(?:\\-{1})?\\d+(?:\\.{1}\\d+)?)$", message="{student.regid.mustnumeric}")
	// @NotBlank(message="{student.regid.invalid}")
	// @Pattern(regexp="[\\d]{12}", message="{student.regid.invalid}")
	private String regId;
	
	@NotBlank(message = "{student.firstname.required}")
	private String firstName;

	@NotBlank(message = "{student.lastname.required}")
	private String lastName;

	private String studentClass;
	private String section;
	private String message;

	private long schholId;

	private long studentId;
	private long absentReasonId;
	
	private char presentFlag;
	private char isApproved;
	private Date absentFromDate;
	private Date absentToDate;
	
	private Long routeId; 
	
	private Date pickTimeSummer;
	
	private Date winterPickup;
	private Date drop;
	private Date pickUp;
	
	private Long wayPointId; 
	private Double lattitude;
	private Double longitude;
	
	private String routeName;

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public void setPickTimeSummer(Date pickTimeSummer) {
		this.pickTimeSummer = pickTimeSummer;
	}

	public StudentRequest() {

	}

	public String getFirstName() {
		if ("undefined".equals(this.firstName)) {
			throw new ValidationException("First Name is not valid.", "firstName");
		}
		return firstName;
	}

	public void setFirstName(String firstName) {

		if ("undefined".equals(this.firstName)) {
			throw new ValidationException("First Name is not valid.", "firstName");
		}
		this.firstName = firstName;
	}

	public String getLastName() {
		if ("undefined".equals(this.lastName)) {
			throw new ValidationException("Last Name is not valid.", "firstName");
		}
		return lastName;
	}

	public void setLastName(String lastName) {
		if ("undefined".equals(this.lastName)) {
			throw new ValidationException("Last Name is not valid.", "firstName");
		}
		this.lastName = lastName;
	}

	public String getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}

	public long getRegId() {
		try {

			return Long.parseLong(this.regId);
		} catch (Exception e) {
			e.printStackTrace();

			throw new ValidationException("Admission Id is not valid, it should be numeric only.", "regId");
		}

	}

	public void setRegId(String regId) {

		try {
			// Long.parseLong(regId);
			this.regId = regId;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException("Admission Id is not valid, it should be numeric only.", "regId");
		}

	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public long getSchholId() {
		return schholId;
	}

	public void setSchholId(long schholId) {
		this.schholId = schholId;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public char getPresentFlag() {
		return presentFlag;
	}

	public void setPresentFlag(char presentFlag) {
		this.presentFlag = presentFlag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getAbsentReasonId() {
		return absentReasonId;
	}

	public void setAbsentReasonId(long absentReasonId) {
		this.absentReasonId = absentReasonId;
	}

	public char getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(char isApproved) {
		this.isApproved = isApproved;
	}

	public Long getRouteId() {
		return routeId;
	}

	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}

	public Date getPickTimeSummer() {
		return pickTimeSummer;
	}

	public void setPickTime(Date pickTimeSummer) {
		//pickTimeSummer.setSeconds(0);
		this.pickTimeSummer = pickTimeSummer;
	}

	public Date getDrop() {
		return drop;
	}

	public void setDrop(Date drop) {
		//drop.setSeconds(0);
		this.drop = drop;
	}

	public Date getAbsentFromDate() {
		return absentFromDate;
	}

	public void setAbsentFromDate(Date absentFromDate) {
		this.absentFromDate = absentFromDate;
	}

	public Date getAbsentToDate() {
		return absentToDate;
	}

	public void setAbsentToDate(Date absentToDate) {
		this.absentToDate = absentToDate;
	}

	public Date getWinterPickup() {
		return winterPickup;
	}

	public void setWinterPickup(Date winterPickup) {
		//winterPickup.setSeconds(0);
		this.winterPickup = winterPickup;
	}
	public Long getWayPointId() {
		return wayPointId;
	}

	public void setWayPointId(Long wayPointId) {
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
	public Date getPickUp() {
		return pickUp;
	}

	public void setPickUp(Date pickUp) {
		this.pickUp = pickUp;
	}

	
}
