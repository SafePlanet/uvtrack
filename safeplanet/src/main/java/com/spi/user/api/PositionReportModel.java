/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.spi.util.DateUtil;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionReportModel implements Serializable{
    
	private static final long serialVersionUID = -3565681612522135020L;
	
	long id;
    String vehicleRegNumber;
    String fixTime;
    Double longitude;
    Double latitude;
    Double speed;
    String location;
    int course;

    public PositionReportModel() {
    }
    
    public PositionReportModel(long id, String vehicleRegNumber, Date fixTime, Double longitude, Double latitude, Double speed,int course) {
        this.id = id;
        this.vehicleRegNumber = vehicleRegNumber;
        this.fixTime = DateUtil.formatDate(fixTime, "dd/MM/yyyy hh:mm:ss a z");
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        if(longitude != null && latitude != null){
			location = "http://maps.google.com/maps?q=" + latitude + "," + longitude;
		}
        this.course = course;
    }

	public int getCourse() {
		return course;
	}

	public void setCourse(int course) {
		this.course = course;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVehicleRegNumber() {
		return vehicleRegNumber;
	}

	public void setVehicleRegNumber(String vehicleRegNumber) {
		this.vehicleRegNumber = vehicleRegNumber;
	}

	public String getFixTime() {
		return fixTime;
	}

	public void setFixTime(String fixTime) {
		this.fixTime = fixTime;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
