/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.spi.VM.DeviceVM;
import com.spi.VM.*;
import com.spi.domain.Fleet;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class FleetModel {

	long id;
	String uuid;

	String regNumber;
	String driverName;
	String navigatorName;
	String fleetType; /* Bus Van */
	String fleetModel; /* TATA, ASHOK ETC */
	Date fleetMake;
	String ownerName;
	String navigatorMobile;
	private String conductor2Mobile;
	private String conductor2Name;
	String driverMobile;
	String ownerMobile;

	private DeviceVM device;
	private RouteVM routevm;
	
	public FleetModel() {
	}

	public FleetModel(String regNumber, String driverName, String navigatorName, String fleetType, String fleetModel, Date fleetMake, String ownerName, String navigatorMobile, String driverMobile,
			String ownerMobile, String conductor2Name, String conductor2Mobile) {
		this.regNumber = regNumber;
		this.driverName = driverName;
		this.navigatorName = navigatorName;
		this.fleetType = fleetType;
		this.fleetModel = fleetModel;
		this.fleetMake = fleetMake;
		this.ownerName = ownerName;
		this.navigatorMobile = navigatorMobile;
		this.driverMobile = driverMobile;
		this.ownerMobile = ownerMobile;
		this.conductor2Name = conductor2Name;
		this.conductor2Mobile = conductor2Mobile;
		this.device = new DeviceVM();
		this.routevm = new RouteVM();
	}

	public FleetModel(Fleet fleet) {

		this.id = fleet.getId();
		this.uuid = fleet.getUuid().toString();
		this.regNumber = fleet.getRegNumber();
		this.driverName = fleet.getDriverName();
		this.navigatorName = fleet.getNavigatorName();
		this.fleetType = fleet.getFleetType();
		this.fleetModel = fleet.getFleetModel();
		this.fleetMake = fleet.getFleetMake();
		this.ownerName = fleet.getOwnerName();
		this.navigatorMobile = fleet.getNavigatorMobile();
		this.driverMobile = fleet.getDriverMobile();
		this.ownerMobile = fleet.getOwnerMobile();
		this.conductor2Name = fleet.getConductor2Name();
		this.conductor2Mobile = fleet.getConductor2Mobile();
		
		this.device = new DeviceVM();
		this.routevm = new RouteVM();
		
		if(fleet.getRouteFleetDeviceXREF()!=null)
		{
			this.device.setServiceEndDate(fleet.getRouteFleetDeviceXREF().getDevice().getServiceEndDate());
			this.device.setId(fleet.getRouteFleetDeviceXREF().getDevice().getId());
		}
		
		if(fleet.getRouteFleetDeviceXREF().getRoute() != null) {
			this.routevm.setRouteId(fleet.getRouteFleetDeviceXREF().getRoute().getId());
			this.routevm.setRouteName(fleet.getRouteFleetDeviceXREF().getRoute().getRouteName());
			this.routevm.setRouteUuid(fleet.getRouteFleetDeviceXREF().getRoute().getUuid().toString());
		}
		
	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getNavigatorName() {
		return navigatorName;
	}

	public void setNavigatorName(String navigatorName) {
		this.navigatorName = navigatorName;
	}

	public String getFleetType() {
		return fleetType;
	}

	public void setFleetType(String fleetType) {
		this.fleetType = fleetType;
	}

	public String getFleetModel() {
		return fleetModel;
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
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getNavigatorMobile() {
		return navigatorMobile;
	}

	public void setNavigatorMobile(String navigatorMobile) {
		this.navigatorMobile = navigatorMobile;
	}

	public String getDriverMobile() {
		return driverMobile;
	}

	public void setDriverMobile(String driverMobile) {
		this.driverMobile = driverMobile;
	}

	public String getOwnerMobile() {
		return ownerMobile;
	}

	public void setOwnerMobile(String ownerMobile) {
		this.ownerMobile = ownerMobile;
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

	public String getConductor2Mobile() {
		return conductor2Mobile;
	}

	public void setConductor2Mobile(String conductor2Mobile) {
		this.conductor2Mobile = conductor2Mobile;
	}

	public String getConductor2Name() {
		return conductor2Name;
	}

	public void setConductor2Name(String conductor2Name) {
		this.conductor2Name = conductor2Name;
	}

	public DeviceVM getDevice() {
		return device;
	}
	
	public RouteVM getRoute() {
		return routevm;
	}
	
	public void setRoute(RouteVM routevm) {
		this.routevm = routevm;
	}
	
	public void setDevice(DeviceVM device) {
		this.device = device;
	}

}
