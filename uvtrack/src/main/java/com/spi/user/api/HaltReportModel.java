/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class HaltReportModel implements Serializable{
    
	private static final long serialVersionUID = 1474782767598404050L;
	long id;
    String vehicle;
    String startTime;
    String endTime;
    String haltDurationDays;
    String location;
    double latitude;
    double longitude;
    long haltduration = 0;

    public HaltReportModel() {
    }

    public HaltReportModel(long id, String vehicle, String startTime, String endTime, String haltDurationDays, String location, 
    		double latitude, double longitude, long haltduration) {
        this.id = id;
        this.vehicle = vehicle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.haltDurationDays = haltDurationDays;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.haltduration = haltduration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
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

    public String getHaltDurationDays() {
        return haltDurationDays;
    }

    public void setHaltDurationDays(String haltDurationDays) {
        this.haltDurationDays = haltDurationDays;
    }

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public long getHaltduration() {
		return haltduration;
	}

	public void setHaltduration(long haltduration) {
		this.haltduration = haltduration;
	}
}
