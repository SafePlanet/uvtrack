package com.spi.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "school_holidays")
public class SchoolHoliday extends BaseEntity {

	private static final long serialVersionUID = -689628523689103688L;

	private String description;
	@Temporal(TemporalType.DATE)
	private Date fromDate;
	@Temporal(TemporalType.DATE)
	private Date toDate;
	// private Date time_created;
	private String sjaFlag;

	@ManyToOne
	@JoinColumn(name = "schoolId")
	private School school;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SchoolHoliday() {
		this(UUID.randomUUID());
	}

	public SchoolHoliday(UUID guid) {
		super(guid);
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}
}
