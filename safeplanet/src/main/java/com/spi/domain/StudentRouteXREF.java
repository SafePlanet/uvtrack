/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "student_route_xref")
public class StudentRouteXREF extends BaseEntity {

	private static final long serialVersionUID = 2884823601002996721L;

	public StudentRouteXREF() {
		super();
	}

	public StudentRouteXREF(UUID guid) {
		super(guid);
	}

	@OneToOne
	@JoinColumn(name = "student_id")
	private Student student = null;

	@OneToOne
	@JoinColumn(name = "route_id")
	private Route route = null;

	private String pickTime = null;

	private String dropTime = null;
	
	@Temporal(TemporalType.TIME)
	private Date timeDrop;

	@Temporal(TemporalType.TIME)
	private Date timePick;
	
	@Temporal(TemporalType.TIME)
	private Date timePickWinter;
	
	@Temporal(TemporalType.TIME)
	private Date pickTimew;	

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public String getDropTime() {
		return dropTime;
	}

	public void setDropTime(String dropTime) {
		this.dropTime = dropTime;
	}

	public String getPickTime() {
		return pickTime;
	}

	public void setPickTime(String pickTime) {
		this.pickTime = pickTime;
	}

	public Date getTimeDrop() {
		return timeDrop;
	}

	public void setTimeDrop(Date timeDrop) {
		this.timeDrop = timeDrop;
	}

	public Date getTimePick() {
		return timePick;
	}

	public void setTimePick(Date timePick) {
		this.timePick = timePick;
	}

	@Override
	public String toString() {
		return "StudentRouteXREF [student=" + student.getFirstName() + " " +student.getLastName() + ", route=" + route + ", pickTime=" + pickTime + ", dropTime=" + dropTime + ", timeDrop=" + timeDrop + ", timePick="
				+ timePick + "]";
	}

	public Date getPickTimew() {
		return pickTimew;
	}

	public void setPickTimew(Date pickTimew) {
		this.pickTimew = pickTimew;
	}

	public Date getTimePickWinter() {
		return timePickWinter;
	}

	public void setTimePickWinter(Date timePickWinter) {
		this.timePickWinter = timePickWinter;
	}

}
