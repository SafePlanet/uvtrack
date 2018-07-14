/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.VM;

import java.util.Date;

public class WayPointVM {

	long routeId;
	String sequenceNumber;
	String name;
	String description;
	String studentName;
	private Date pickTimeSummer;
	private Date winterPickup;
	private Date drop;
	double latitude;
	double longitude;
	double altitude;
	long waypointId;
	private String timePick;
	private String timeDrop;

	/**
	 * @return the routeId
	 */
	public long getRouteId() {
		return routeId;
	}

	/**
	 * @param routeId
	 *            the routeId to set
	 */
	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}

	public WayPointVM(long routeId, String sequenceNumber, String name,
			double lattitude, double longitude, double altitude) {
		super();
		this.routeId = routeId;
		this.sequenceNumber = sequenceNumber;
		this.name = name;
		this.latitude = lattitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}

	public WayPointVM() {
		super();
	}

	/**
	 * @return the sequenceNumber
	 */
	public String getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber
	 *            the sequenceNumber to set
	 */
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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

	/**
	 * @return the lattitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param lattitude
	 *            the lattitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
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

	public long getWaypointId() {
		return waypointId;
	}

	public void setWaypointId(long waypointId) {
		this.waypointId = waypointId;
	}

	public String getTimePick() {
		return timePick;
	}

	public void setTimePick(String timePick) {
		this.timePick = timePick;
	}

	public String getTimeDrop() {
		return timeDrop;
	}

	public void setTimeDrop(String timeDrop) {
		this.timeDrop = timeDrop;
	}
	
	

}
