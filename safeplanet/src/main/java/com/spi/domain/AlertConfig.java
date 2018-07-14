/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "alertconfig")
public class AlertConfig extends BaseEntity {

	private static final long serialVersionUID = 927936135058330166L;

	@Temporal(TemporalType.TIME)
	private Date pickStartTime;

	@Temporal(TemporalType.TIME)
	private Date dropStartTime;

	@Temporal(TemporalType.TIME)
	private Date pickEndTime;

	@Temporal(TemporalType.TIME)
	private Date dropEndTime;

	@Column(columnDefinition = "int DEFAULT 1")
	int activeTime;

	@Column(name = "alerts", nullable = true)
	private String alerts;

	@Column(name = "days", nullable = true)
	private String days;

	@OneToOne
	@JoinColumn(name = "student_id")
	Student student;

	public int getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(int activeTime) {
		this.activeTime = activeTime;
	}

	public Date getPickStartTime() {
		return pickStartTime;
	}

	public void setPickStartTime(Date pickStartTime) {
		this.pickStartTime = pickStartTime;
	}

	public Date getPickEndTime() {
		return pickEndTime;
	}

	public void setPickEndTime(Date pickEndTime) {
		this.pickEndTime = pickEndTime;
	}

	public Date getDropStartTime() {
		return dropStartTime;
	}

	public void setDropStartTime(Date dropStartTime) {
		this.dropStartTime = dropStartTime;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getAlerts() {
		return alerts;
	}

	public void setAlerts(String alerts) {
		this.alerts = alerts;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	@Override
	public String toString() {
		return "AlertConfig [pickStartTime=" + pickStartTime + ", dropStartTime=" + dropStartTime + ", pickEndTime=" + pickEndTime + ", dropEndTime=" + dropEndTime + ", activeTime=" + activeTime
				+ ", alerts=" + alerts + ", days=" + days + ", student=" + student.getFirstName() + "" +student.getLastName() + "]";
	}

	public Date getDropEndTime() {
		return dropEndTime;
	}

	public void setDropEndTime(Date dropEndTime) {
		this.dropEndTime = dropEndTime;
	}

}
