package com.spi.user.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.spi.domain.SchoolHoliday;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolHolidayModel {

	long id;
	String uuid;
	private String description;
	private Date fromDate;
	private Date toDate;
	private String sjaFlag;
	private SchoolModel school;
	private boolean addToAllSchools;

	public SchoolHolidayModel(SchoolHoliday schoolHoliday) {
		if (schoolHoliday != null) {
			this.setId(schoolHoliday.getId());
			this.setDescription(schoolHoliday.getDescription());
			this.setFromDate(schoolHoliday.getFromDate());
			this.setSjaFlag(schoolHoliday.getSjaFlag());
			this.setToDate(schoolHoliday.getToDate());
			// this.setSchool(new SchoolModel(schoolHoliday.getSchool()));
		}
	}

	public SchoolHolidayModel() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getSjaFlag() {
		return sjaFlag;
	}

	public void setSjaFlag(String sjaFlag) {
		this.sjaFlag = sjaFlag;
	}

	public SchoolModel getSchool() {
		return school;
	}

	public void setSchool(SchoolModel school) {
		this.school = school;
	}

	public boolean getAddToAllSchools() {
		return addToAllSchools;
	}

	public void setAddToAllSchools(boolean addToAllSchools) {
		this.addToAllSchools = addToAllSchools;
	}

	
}
