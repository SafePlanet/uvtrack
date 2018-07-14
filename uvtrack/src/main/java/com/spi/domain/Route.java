/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.spi.VM.RouteVM;
import com.spi.model.BaseEntity;

@Entity
@Table(name = "route")
public class Route extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4236727822774518010L;
	String routeName;
	String routeDescription;
	int voidInd;
	String geometry;

	public Route(String routeName, String routeDescription, int voidInd,
			String geometry) {
		this();
		this.routeName = routeName;
		this.geometry = routeDescription;
		this.routeDescription = routeDescription;
	}

	public Route(RouteVM routeVM, School school) {
		this();
		this.school = school;
		this.routeName = routeVM.getRouteName();
		this.geometry = routeVM.getGeometry();
		this.routeDescription = routeVM.getRouteDescription();

	}

	public Route() {
		// TODO Auto-generated constructor stub
		super();
	}

	@ManyToOne
	@JoinColumn(name = "school_id")
	School school;

	@OneToMany(mappedBy = "route", targetEntity = WayPoint.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<WayPoint> waypoints = new ArrayList<WayPoint>();

	@OneToOne(mappedBy = "route", targetEntity = RouteFleetDeviceXREF.class, cascade = CascadeType.ALL)
	RouteFleetDeviceXREF routeFleetDeviceXREF;
	
	@OneToMany(mappedBy = "route", targetEntity = RouteScheduleDetails.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	List<RouteScheduleDetails> routeScheduleDetails;

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
		return geometry == null ? "" : geometry;
	}

	/**
	 * @param geometry
	 *            the geometry to set
	 */
	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public List<WayPoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<WayPoint> waypoints) {
		this.waypoints = waypoints;
	}

	public RouteFleetDeviceXREF getRouteFleetDeviceXREF() {
		return routeFleetDeviceXREF;
	}

	public void setRouteFleetDeviceXREF(RouteFleetDeviceXREF routeFleetDeviceXREF) {
		this.routeFleetDeviceXREF = routeFleetDeviceXREF;
	}
	
	

}
