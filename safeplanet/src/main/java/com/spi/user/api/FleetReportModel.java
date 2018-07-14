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
public class FleetReportModel implements Serializable{
    
    
	private static final long serialVersionUID = 8035024317024850511L;
	long id;
    String vehicle;
    String haltDuration;
    String moveDuration;
    String startLocation;
    String endLocation;
    String totalMovement;
    String avgMoveSpeed;
    String maxSpeed;
    String overSpeedEvents;
    String longHaltEvents;
    String idlingEvents;
    double lattitude;
	double longitude;

    public FleetReportModel() {
    }

    public FleetReportModel(long id, String vehicle, String haltDuration, String moveDuration, String startLocation, String endLocation, String totalMovement, String avgMoveSpeed, String maxSpeed, String overSpeedEvents, String longHaltEvents, String idlingEvents, double lattitue, double longitude) {
        this.id = id;
        this.vehicle = vehicle;
        this.haltDuration = haltDuration;
        this.moveDuration = moveDuration;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.totalMovement = totalMovement;
        this.avgMoveSpeed = avgMoveSpeed;
        this.maxSpeed = maxSpeed;
        this.overSpeedEvents = overSpeedEvents;
        this.longHaltEvents = longHaltEvents;
        this.idlingEvents = idlingEvents;
        this.lattitude = lattitue;
        this.longitude = longitude;
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

    public String getHaltDuration() {
        return haltDuration;
    }

    public void setHaltDuration(String haltDuration) {
        this.haltDuration = haltDuration;
    }

    public String getMoveDuration() {
        return moveDuration;
    }

    public void setMoveDuration(String moveDuration) {
        this.moveDuration = moveDuration;
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

    public String getTotalMovement() {
        return totalMovement;
    }

    public void setTotalMovement(String totalMovement) {
        this.totalMovement = totalMovement;
    }

    public String getAvgMoveSpeed() {
        return avgMoveSpeed;
    }

    public void setAvgMoveSpeed(String avgMoveSpeed) {
        this.avgMoveSpeed = avgMoveSpeed;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getOverSpeedEvents() {
        return overSpeedEvents;
    }

    public void setOverSpeedEvents(String overSpeedEvents) {
        this.overSpeedEvents = overSpeedEvents;
    }

    public String getLongHaltEvents() {
        return longHaltEvents;
    }

    public void setLongHaltEvents(String longHaltEvents) {
        this.longHaltEvents = longHaltEvents;
    }

    public String getIdlingEvents() {
        return idlingEvents;
    }

    public void setIdlingEvents(String idlingEvents) {
        this.idlingEvents = idlingEvents;
    }

	public double getLattitude() {
		return lattitude;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}    
}
