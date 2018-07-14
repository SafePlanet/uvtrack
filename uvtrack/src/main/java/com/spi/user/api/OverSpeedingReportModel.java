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
public class OverSpeedingReportModel implements Serializable{
    
	private static final long serialVersionUID = -4694298107559607880L;
	long id;
    String vehicle;
    double speed;
    String startTime;
    String endTime;
    String overSpeedDurationMins;
    String overSpeedMovement;
    String startLocation;
    String endLocation;
    String path;
    int speedLimit;

    public OverSpeedingReportModel(long id, String vehicle, double speed, String startTime, String endTime, 
    		String overSpeedDurationMins, String overSpeedMovement, String startLocation, String endLocation, 
    		String path, int speedLimit) {
        this.id = id;
        this.vehicle = vehicle;
        this.speed = speed;
        this.speedLimit = speedLimit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.overSpeedDurationMins = overSpeedDurationMins;
        this.overSpeedMovement = overSpeedMovement;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.path = path;
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

    public String getOverSpeedDurationMins() {
        return overSpeedDurationMins;
    }

    public void setOverSpeedDurationMins(String overSpeedDurationMins) {
        this.overSpeedDurationMins = overSpeedDurationMins;
    }

    public String getOverSpeedMovement() {
        return overSpeedMovement;
    }

    public void setOverSpeedMovement(String overSpeedMovement) {
        this.overSpeedMovement = overSpeedMovement;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }
    
    public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}
    
}
