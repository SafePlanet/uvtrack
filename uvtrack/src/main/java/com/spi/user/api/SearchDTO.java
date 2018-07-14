/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.spi.domain.Location;
import com.spi.dto.LocationDTO;
import com.spi.util.SpiDateTime;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchDTO {

    private SpiDateTime fromDate;
    private SpiDateTime toDate;
    private int speedLimit;
    private int schedule;
    private int haltLimit;
    private long vehicleId;
    private long studentId;
    private long schoolId;

    public SearchDTO() {
    }

    public SpiDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(SpiDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public SpiDateTime getToDate() {
        return toDate;
    }

    public void setToDate(SpiDateTime toDate) {
        this.toDate = toDate;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public int getHaltLimit() {
        return haltLimit;
    }

    public void setHaltLimit(int haltLimit) {
        this.haltLimit = haltLimit;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public boolean checkFleetLocation(Location location) {
        boolean flag = false;
        if (location == null || location.getDeviceTime() == null) {
            return false;
        }

        if (location.getDeviceTime().compareTo(this.getFromDate().getTimestampDate()) >= 0 && location.getDeviceTime().compareTo(this.getToDate().getTimestampDate()) <= 0) {
            flag = true;
        }

        return flag;
    }
    public boolean checkFleetLocation(LocationDTO location) {
        boolean flag = false;
        if (location == null || location.getFixTime() == null) {
            return false;
        }

        if (location.getFixTime().compareTo(this.getFromDate().getTimestampDate()) >= 0 && location.getFixTime().compareTo(this.getToDate().getTimestampDate()) <= 0) {
            flag = true;
        }

        return flag;
    }

	public int getSchedule() {
		return schedule;
	}

	public void setSchedule(int schedule) {
		this.schedule = schedule;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

}
