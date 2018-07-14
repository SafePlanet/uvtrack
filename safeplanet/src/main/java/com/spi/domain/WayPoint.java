/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.spi.VM.WayPointVM;
import com.spi.model.BaseEntity;

@Entity
@Table(name = "way_point")
public class WayPoint extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3993781199380684976L;

	private int sequenceNumber;
	private String name;
	private String description;
	private double lattitude;
	private double longitude;
	private double altitude;

	@Temporal(TemporalType.TIME)
	private Date timeDrop;

	@Temporal(TemporalType.TIME)
	private Date timePick;

	@Temporal(TemporalType.TIME)
	private Date timePickWinter;

	@OneToMany(mappedBy = "wayPoint", targetEntity = Student.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<Student> students = new ArrayList<Student>();

	@ManyToOne
	@JoinColumn(name = "route_id")
	Route route;

	public WayPoint() {
		super();
	}

	public WayPoint(UUID guid) {
		super(guid);
	}

	public WayPoint(int sequenceNumber, String name, double lattitude, double longitude, double altitude,
			Route route, String description) {
		super();
		this.sequenceNumber = sequenceNumber;
		this.name = name;
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.route = route;
		this.description = description;
	}

	public WayPoint(WayPointVM wayPointVM, Route route) {
		this.route = route;
		this.sequenceNumber = Integer.parseInt(wayPointVM.getSequenceNumber());
		this.name = wayPointVM.getName();
		this.lattitude = wayPointVM.getLatitude();
		this.longitude = wayPointVM.getLongitude();
		this.altitude = wayPointVM.getAltitude();
		
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

	public Date getTimePickWinter() {
		return timePickWinter;
	}

	public void setTimePickWinter(Date timePickWinter) {
		this.timePickWinter = timePickWinter;
	}

	/**
	 * @return the routeId
	 */
	public Route getRoute() {
		return route;
	}
	
	public void setRoute(Route route) {
		this.route = route;
	}

	/**
	 * @return the sequenceNumber
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber
	 *            the sequenceNumber to set
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return the lattitude
	 */
	public double getLattitude() {
		return lattitude;
	}

	/**
	 * @param lattitude
	 *            the lattitude to set
	 */
	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the altitude
	 */
	public double getAltitude() {
		return altitude;
	}

	/**
	 * @param altitude
	 *            the altitude to set
	 */
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

}