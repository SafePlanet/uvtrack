/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.spi.domain.Devices;
import com.spi.domain.LocationModel;
import com.spi.domain.UserSchool;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceModel {

	long id;
	String uuid;

	// String deviceSerialNo;
	long uniqueid;
	String deviceType;
	long deviceModelId;
	Date ManfDate;
	Date serviceEndDate;
	Date puchaseDate;
	Date warrantyDate;
	String protocol;
	Integer frqUpdateTime;
	Integer frqUpdateDist;
	String frqMode;
	long schoolId;
	private String status;
	private LocationModel lastLocation;
	private String name;
	private Long mobileNumber;
	private String fleetType = "";
	Date lastUpdate;

	public DeviceModel() {
	}

	public DeviceModel(long id, String uuid, long uniqueId, String deviceType, long deviceModelId, Date ManfDate, Date puchaseDate, Date warrantyDate,
			String protocol, Integer frqUpdateTime, Integer frqUpdateDist, String frqMode, String fleetType, Date lastUpdate) {
		this.id = id;
		this.uuid = uuid;
		this.uniqueid = uniqueId;
		this.deviceType = deviceType;
		this.deviceModelId = deviceModelId;
		this.ManfDate = ManfDate;
		this.puchaseDate = puchaseDate;
		this.warrantyDate = warrantyDate;
		this.protocol = protocol;
		this.frqUpdateTime = frqUpdateTime;
		this.frqUpdateDist = frqUpdateDist;
		this.frqMode = frqMode;
		this.fleetType = fleetType;
		this.lastUpdate = lastUpdate;
	}

	public Date getServiceEndDate() {
		return serviceEndDate;
	}

	public void setServiceEndDate(Date serviceEndDate) {
		this.serviceEndDate = serviceEndDate;
	}

	public DeviceModel(Devices device,long schoolId) {
		this.id = device.getId();
		this.uuid = device.getUuid().toString();
		this.uniqueid = device.getUniqueid();
		this.deviceType = device.getDeviceType();
		this.deviceModelId = device.getDeviceModelType() != null ? device.getDeviceModelType().getId() : 0L;
		this.ManfDate = device.getManfDate();
		this.puchaseDate = device.getPuchaseDate();
		this.warrantyDate = device.getWarrantyDate();
		this.protocol = device.getProtocol();
		this.schoolId=schoolId;
		this.frqUpdateTime = device.getFrqUpdateTime();
		this.frqUpdateDist = device.getFrqUpdateDist();
		this.frqMode = device.getFrqMode();
		if (device.getRouteFleetDeviceXREF() != null) {
			this.fleetType = device.getRouteFleetDeviceXREF().getFleet().getFleetType();
		} else {
			this.fleetType = "";
		}
		this.lastUpdate = device.getLastUpdate();
		try {
			if (lastUpdate == null || ((new Date()).getTime() - lastUpdate.getTime()) > (frqUpdateTime * 3000)) {
				this.status = Devices.STATUS_OFFLINE;
			} else {
				this.status = Devices.STATUS_ONLINE;
			}
		} catch (Exception ex) {
		}
		this.setName(device.getName());
		this.setMobileNumber(device.getMobile());
		if (device.getLocations() != null && device.getLocations().size() > 0) {
			this.setLastLocation(new LocationModel(device.getLocations().get(0)));
		} else {
			this.setLastLocation(new LocationModel());
		}

		this.serviceEndDate = device.getServiceEndDate();
	}
	public DeviceModel(Devices device) {
		this.id = device.getId();
		this.uuid = device.getUuid().toString();
		this.uniqueid = device.getUniqueid();
		this.deviceType = device.getDeviceType();
		this.deviceModelId = device.getDeviceModelType() != null ? device.getDeviceModelType().getId() : 0L;
		this.ManfDate = device.getManfDate();
		this.puchaseDate = device.getPuchaseDate();
		this.warrantyDate = device.getWarrantyDate();
		this.protocol = device.getProtocol();
		this.frqUpdateTime = device.getFrqUpdateTime();
		this.frqUpdateDist = device.getFrqUpdateDist();
		this.frqMode = device.getFrqMode();
		if (device.getRouteFleetDeviceXREF() != null) {
			this.fleetType = device.getRouteFleetDeviceXREF().getFleet().getFleetType();
		} else {
			this.fleetType = "";
		}
		this.lastUpdate = device.getLastUpdate();
		try {
			if (lastUpdate == null || ((new Date()).getTime() - lastUpdate.getTime()) > (frqUpdateTime * 3000)) {
				this.status = Devices.STATUS_OFFLINE;
			} else {
				this.status = Devices.STATUS_ONLINE;
			}
		} catch (Exception ex) {
		}
		this.setName(device.getName());
		this.setMobileNumber(device.getMobile());
		if (device.getLocations() != null && device.getLocations().size() > 0) {
			this.setLastLocation(new LocationModel(device.getLocations().get(0)));
		} else {
			this.setLastLocation(new LocationModel());
		}

		this.serviceEndDate = device.getServiceEndDate();
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

	public long getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(long uniqueid) {
		this.uniqueid = uniqueid;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public long getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(long deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public Date getManfDate() {
		return ManfDate;
	}

	public void setManfDate(Date ManfDate) {
		this.ManfDate = ManfDate;
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

	public LocationModel getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(LocationModel lastLocation) {
		this.lastLocation = lastLocation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getFleetType() {
		return fleetType;
	}

	public void setFleetType(String fleetType) {
		this.fleetType = fleetType;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}
	
}
