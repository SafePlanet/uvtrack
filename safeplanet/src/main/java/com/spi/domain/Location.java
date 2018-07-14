/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "positions")

@NamedNativeQueries({
		@NamedNativeQuery(
				name = "findPreviousToCurrentPostion", 
				query = "select p2.* from positions p2, positions p1 where p1.id = ? and p2.deviceid = p1.deviceid and p1.id>p2.id order by 1 desc LIMIT 1", 
				resultClass = Location.class)})
public class Location extends BaseEntity {

	String protocol;
	@ManyToOne
	@JoinColumn(name = "deviceid")
	Devices devices;

	Date servertime;
	Date devicetime;
	Date fixtime;
	String valid;
	double latitude;
	double longitude;
	double altitude;
	double speed;
	int course;
	String address;
	String attributes;

	int gsmSignalStrength;
	int movingStatus;
	int gpsStatus;
	double externalBatteryVoltage;
	double internalBatteryVoltage;

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Date getFixTime() {
		return fixtime;
	}

	public void setFixTime(Date fixTime) {
		this.fixtime = fixTime;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public Date getServerTime() {
		return servertime;
	}

	public void setServerTime(Date serverTime) {
		this.servertime = serverTime;
	}

	public int getCourse() {
		return course;
	}

	public void setCourse(int course) {
		this.course = course;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	private static final long serialVersionUID = 6738786435753029134L;

	public double getLattitude() {
		return latitude;
	}

	public void setLattitude(double lattitude) {
		this.latitude = lattitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public Date getDeviceTime() {
		return devicetime;
	}

	public void setDeviceTime(Date deviceTime) {
		this.devicetime = deviceTime;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public int getGsmSignalStrength() {
		return gsmSignalStrength;
	}

	public void setGsmSignalStrength(int gsmSignalStrength) {
		this.gsmSignalStrength = gsmSignalStrength;
	}

	public int getMovingStatus() {
		return movingStatus;
	}

	public void setMovingStatus(int movingStatus) {
		this.movingStatus = movingStatus;
	}

	public int getGpsStatus() {
		return gpsStatus;
	}

	public void setGpsStatus(int gpsStatus) {
		this.gpsStatus = gpsStatus;
	}

	public Date getLogDate() {
		return servertime;
	}

	public void setLogDate(Date logDate) {
		this.servertime = logDate;
	}

	public double getExternalBatteryVoltage() {
		return externalBatteryVoltage;
	}

	public void setExternalBatteryVoltage(double externalBatteryVoltage) {
		this.externalBatteryVoltage = externalBatteryVoltage;
	}

	public double getInternalBatteryVoltage() {
		return internalBatteryVoltage;
	}

	public void setInternalBatteryVoltage(double internalBatteryVoltage) {
		this.internalBatteryVoltage = internalBatteryVoltage;
	}

	public Devices getDevice() {
		return devices;
	}

	public void setDevice(Devices device) {
		this.devices = device;
	}

}
