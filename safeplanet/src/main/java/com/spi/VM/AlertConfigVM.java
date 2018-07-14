/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.VM;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlertConfigVM {

	private int studentId;

	private int activeTime;

	private String alerts;

	private String days;

	private Date pickTime;

	private Date dropTime;

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(int activeTime) {
		this.activeTime = activeTime;
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

	public Date getPickTime() {
		return pickTime;
	}

	public void setPickTime(Date pickTime) {
		this.pickTime = pickTime;
	}

	public Date getDropTime() {
		return dropTime;
	}

	public void setDropTime(Date dropTime) {
		this.dropTime = dropTime;
	}

	@Override
	public String toString() {
		return "AlertConfigVM [studentId=" + studentId + ", activeTime=" + activeTime + ", alerts=" + alerts + ", days=" + days + ", pickTime=" + pickTime + ", dropTime=" + dropTime + "]";
	}
}
