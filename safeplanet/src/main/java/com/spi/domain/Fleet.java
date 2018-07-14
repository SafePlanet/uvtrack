/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "fleet")
public class Fleet extends BaseEntity {

	private static final long serialVersionUID = -6579398300867227593L;

	private String regNumber;
	private String driverName;
	private String navigatorName;
	private String fleetType; /* Bus Van */
	private String fleetModel; /* TATA, ASHOK ETC */
	private Date fleetMake;
	private String ownerName;
	private String navigatorMobile;
	private String conductor2Mobile;
	private String conductor2Name;
	private String driverMobile;
	private String ownerMobile;
	private int voidInd;

	@OneToOne(mappedBy = "fleet", targetEntity = RouteFleetDeviceXREF.class, cascade = CascadeType.ALL)
	private RouteFleetDeviceXREF routeFleetDeviceXREF;

	@OneToOne
	@JoinColumn(name = "device_id")
	private Devices devices;

	public String getRegNumber() {
		return regNumber == null ? "" : regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	public String getDriverName() {
		return driverName == null ? "" : driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getNavigatorName() {
		return navigatorName == null ? "" : navigatorName;
	}

	public void setNavigatorName(String navigatorName) {
		this.navigatorName = navigatorName;
	}

	public String getFleetType() {
		return fleetType == null ? "" : fleetType;
	}

	public void setFleetType(String fleetType) {
		this.fleetType = fleetType;
	}

	public String getFleetModel() {
		return fleetModel == null ? "" : fleetModel;
	}

	public void setFleetModel(String fleetModel) {
		this.fleetModel = fleetModel;
	}

	public Date getFleetMake() {
		return fleetMake;
	}

	public void setFleetMake(Date fleetMake) {
		this.fleetMake = fleetMake;
	}

	public String getOwnerName() {
		return ownerName == null ? "" : ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getNavigatorMobile() {
		return navigatorMobile == null ? "" : navigatorMobile;
	}

	public void setNavigatorMobile(String navigatorMobile) {
		this.navigatorMobile = navigatorMobile;
	}

	public String getDriverMobile() {
		return driverMobile == null ? "" : driverMobile;
	}

	public void setDriverMobile(String driverMobile) {
		this.driverMobile = driverMobile;
	}

	public String getOwnerMobile() {
		return ownerMobile == null ? "" : ownerMobile;
	}

	public void setOwnerMobile(String ownerMobile) {
		this.ownerMobile = ownerMobile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getVoidInd() {
		return voidInd;
	}

	public void setVoidInd(int voidInd) {
		this.voidInd = voidInd;
	}

	public RouteFleetDeviceXREF getRouteFleetDeviceXREF() {
		return routeFleetDeviceXREF;
	}

	public void setRouteFleetDeviceXREF(RouteFleetDeviceXREF routeFleetDeviceXREF) {
		this.routeFleetDeviceXREF = routeFleetDeviceXREF;
	}

	public String getConductor2Mobile() {
		return conductor2Mobile == null ? "" : conductor2Mobile;
	}

	public void setConductor2Mobile(String conductor2Mobile) {
		this.conductor2Mobile = conductor2Mobile;
	}

	public String getConductor2Name() {
		return conductor2Name == null ? "" : conductor2Name;
	}

	public void setConductor2Name(String conductor2Name) {
		this.conductor2Name = conductor2Name;
	}

	public Devices getDevices() {
		return devices;
	}

	public void setDevices(Devices devices) {
		this.devices = devices;
	}

}
