/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "devices")
public class Devices extends BaseEntity {
	/**
	 * 
	 */
	public static final String STATUS_OFFLINE = "offline";
	public static final String STATUS_ONLINE = "online";
	
	private static final long serialVersionUID = -7313309884794080018L;
	private String deviceType; /* Mobile, GPS */
	@ManyToOne
	@JoinColumn(name = "device_model_id")
	private DeviceModelType deviceModelType;

	@OneToMany(mappedBy = "devices", targetEntity = UserDevice.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<UserDevice> userDevice;

	private Date manfDate;
	private Date puchaseDate;
	private Date serviceEndDate;
	private Date warrantyDate;
	private String protocol;
	private Integer frqUpdateTime; /* in seconds */
	private Integer frqUpdateDist;
	private String frqMode;
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean voidInd;
	private String name;
	private long uniqueid;
	private String status = "online";
	private Date lastupdate;
	private Long groupid;
	private long positionid;
	/**
	 * mobile to map with device
	 */
	private Long mobile;

	@OneToMany(mappedBy = "devices", targetEntity = Location.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.TRUE)
	@javax.persistence.OrderBy("fixtime DESC")
	private List<Location> locations = new ArrayList<Location>();

	@OneToOne(mappedBy = "devices", targetEntity = RouteFleetDeviceXREF.class, cascade = CascadeType.ALL)
	RouteFleetDeviceXREF routeFleetDeviceXREF;

	public Devices() {
		super();
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public DeviceModelType getDeviceModelType() {
		return deviceModelType;
	}

	public void setDeviceModelType(DeviceModelType deviceModelType) {
		this.deviceModelType = deviceModelType;
	}

	public Date getManfDate() {
		return manfDate;
	}

	public void setManfDate(Date manfDate) {
		this.manfDate = manfDate;
	}

	public Date getPuchaseDate() {
		return puchaseDate;
	}

	public void setPuchaseDate(Date puchaseDate) {
		this.puchaseDate = puchaseDate;
	}

	public Date getWarrantyDate() {
		return warrantyDate;
	}

	public void setWarrantyDate(Date warrantyDate) {
		this.warrantyDate = warrantyDate;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Integer getFrqUpdateTime() {
		return frqUpdateTime;
	}

	public void setFrqUpdateTime(Integer frqUpdateTime) {
		this.frqUpdateTime = frqUpdateTime;
	}

	public Integer getFrqUpdateDist() {
		return frqUpdateDist;
	}

	public void setFrqUpdateDist(Integer frqUpdateDist) {
		this.frqUpdateDist = frqUpdateDist;
	}

	public String getFrqMode() {
		return frqMode;
	}

	public void setFrqMode(String frqMode) {
		this.frqMode = frqMode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean getVoidInd() {
		return voidInd;
	}

	public void setVoidInd(boolean voidInd) {
		this.voidInd = voidInd;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public RouteFleetDeviceXREF getRouteFleetDeviceXREF() {
		return routeFleetDeviceXREF;
	}

	public void setRouteFleetDeviceXREF(
			RouteFleetDeviceXREF routeFleetDeviceXREF) {
		this.routeFleetDeviceXREF = routeFleetDeviceXREF;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(long uniqueid) {
		this.uniqueid = uniqueid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastUpdate() {
		if (lastupdate != null) {
			return new Date(lastupdate.getTime());
		} else {
			return null;
		}
	}

	public void setLastUpdate(Date lastUpdate) {
		if (lastUpdate != null) {
			this.lastupdate = new Date(lastUpdate.getTime());
		} else {
			this.lastupdate = null;
		}
	}

	public long getPositionId() {
		return positionid;
	}

	public void setPositionId(long positionId) {
		this.positionid = positionId;
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public Long getGroupId() {
		return groupid;
	}

	public void setGroupId(Long groupId) {
		this.groupid = groupId;
	}

	public List<UserDevice> getUserDevice() {
		return userDevice;
	}

	public void setUserDevice(List<UserDevice> userDevice) {
		this.userDevice = userDevice;
	}

	public Date getServiceEndDate() {
		return serviceEndDate;
	}

	public void setServiceEndDate(Date serviceEndDate) {
		this.serviceEndDate = serviceEndDate;
	}

}
