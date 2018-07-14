/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.VM;


public class RouteVM {
	String routeName; 
	String routeDescription;
	int schoolId;
	int voidInd;
	String geometry;
	Long routeId;
	String uuid;
	private String scheduleName;
	private String name;
	private long deviceId;

	private String startTime = null;
	private String endTime = null;
	/**
	 * @return the routeName
	 */
	public String getRouteName() {
		return routeName;
	}

	/**
	 * @param routeName
	 *            the routeName to set
	 */
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	/**
	 * @return the routeDescription
	 */
	public String getRouteDescription() {
		return routeDescription;
	}

	/**
	 * @param routeDescription
	 *            the routeDescription to set
	 */
	public void setRouteDescription(String routeDescription) {
		this.routeDescription = routeDescription;
	}

	/**
	 * @return the schoolId
	 */
	public int getSchoolId() {
		return schoolId;
	}

	/**
	 * @param schoolId
	 *            the schoolId to set
	 */
	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}

	/**
	 * @return the voidInd
	 */
	public int getVoidInd() {
		return voidInd;
	}

	/**
	 * @param voidInd
	 *            the voidInd to set
	 */
	public void setVoidInd(int voidInd) {
		this.voidInd = voidInd;
	}

	/**
	 * @return the geometry
	 */
	public String getGeometry() {
		return geometry;
	}

	/**
	 * @param geometry
	 *            the geometry to set
	 */
	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}

	public Long getRouteId() {
		return routeId;
	}

	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}
	
	public String getRouteUuId() {
		return uuid;
	}

	public void setRouteUuid(String Uuid) {
		this.uuid = Uuid;
	}
	
	
	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	
}
