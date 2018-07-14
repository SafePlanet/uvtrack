/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.VM;

import java.util.Date;

public class LocationVM {
	
	
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
    String protocol;
    
    public LocationVM() {
    	super();
	}
    
  
    
	public LocationVM(Date servertime, Date devicetime, Date fixtime,
			String valid, double latitude, double longitude, double altitude,
			double speed, int course, String address, String attributes,
			int gsmSignalStrength, int movingStatus, int gpsStatus,
			double externalBatteryVoltage, double internalBatteryVoltage,
			String protocol) {
		super();
		this.servertime = servertime;
		this.devicetime = devicetime;
		this.fixtime = fixtime;
		this.valid = valid;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.speed = speed;
		this.course = course;
		this.address = address;
		this.attributes = attributes;
		this.gsmSignalStrength = gsmSignalStrength;
		this.movingStatus = movingStatus;
		this.gpsStatus = gpsStatus;
		this.externalBatteryVoltage = externalBatteryVoltage;
		this.internalBatteryVoltage = internalBatteryVoltage;
		this.protocol = protocol;
	}


	public Date getServertime() {
		return servertime;
	}
	public void setServertime(Date servertime) {
		this.servertime = servertime;
	}
	public Date getDevicetime() {
		return devicetime;
	}
	public void setDevicetime(Date devicetime) {
		this.devicetime = devicetime;
	}
	public Date getFixtime() {
		return fixtime;
	}
	public void setFixtime(Date fixtime) {
		this.fixtime = fixtime;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
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
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
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
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
    
    
    
}
