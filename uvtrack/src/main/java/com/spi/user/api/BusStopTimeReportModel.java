/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.user.api;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.spi.VM.WayPointVM;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusStopTimeReportModel implements Serializable, Comparable<BusStopTimeReportModel>{
    
	private static final long serialVersionUID = -3434204540890174237L;
	long id;
    String vehicle;
    String wayPointSequence;
    String wayPointName;
    String wayPointDesc;
    String reachTime;
    String stayDuration;
    String mapUrl;
    String studentNames;
    private Date pickTime;
	private Date drop;

    public BusStopTimeReportModel(long id, String vehicle,  
    		String reachTime, String stayDuration, String mapUrl, WayPointVM pointVM, Date schedulePickUpTime) {
        this.id = id;
        this.vehicle = vehicle;
        this.wayPointSequence = pointVM.getSequenceNumber();
        this.wayPointName = pointVM.getName();
        this.wayPointDesc = pointVM.getDescription();
        this.reachTime = reachTime;
        this.stayDuration = stayDuration;
        this.mapUrl = mapUrl;
        this.studentNames = pointVM.getStudentName();
        this.pickTime = schedulePickUpTime;
        this.drop = pointVM.getDrop();
        
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

	public String getWayPointSequence() {
		return wayPointSequence;
	}

	public void setWayPointSequence(String wayPointSequence) {
		this.wayPointSequence = wayPointSequence;
	}

	public String getWayPointName() {
		return wayPointName;
	}

	public void setWayPointName(String wayPointName) {
		this.wayPointName = wayPointName;
	}

	public String getWayPointDesc() {
		return wayPointDesc;
	}

	public void setWayPointDesc(String wayPointDesc) {
		this.wayPointDesc = wayPointDesc;
	}

	public String getReachTime() {
		return reachTime;
	}

	public void setReachTime(String reachTime) {
		this.reachTime = reachTime;
	}

	public String getStayDuration() {
		return stayDuration;
	}

	public void setStayDuration(String stayDuration) {
		this.stayDuration = stayDuration;
	}

	public String getMapUrl() {
		return mapUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}

	public String getStudentNames() {
		return studentNames;
	}

	public void setStudentNames(String studentNames) {
		this.studentNames = studentNames;
	}

	public Date getDrop() {
		return drop;
	}

	public void setDrop(Date drop) {
		this.drop = drop;
	}

	public Date getPickTime() {
		return pickTime;
	}

	public void setPickTime(Date pickTime) {
		this.pickTime = pickTime;
	}

	@Override
	public int compareTo(BusStopTimeReportModel anotherModel) {
		if(this.reachTime != null && anotherModel.getReachTime() != null){
			return this.reachTime.compareTo(anotherModel.getReachTime());
		}

		return 0;
	}


}
