package com.spi.dto;

import java.util.Date;

public class LocationDTO {

	String protocol;

	Integer id;
	Integer device_id;
	Integer version;
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getDevice_id() {
		return device_id;
	}

	public void setDevice_id(Integer device_id) {
		this.device_id = device_id;
	}

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
		return (double) Math.round(speed * 100) / 100;
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

	private static final long serialVersionUID = 6738786435753029134L;

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

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public double getVehicleSpeed() {
		return speed;
	}

	public void setVehicleSpeed(double vehicleSpeed) {
		this.speed = vehicleSpeed;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}
