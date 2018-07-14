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
public class TripSummaryReportModel implements Serializable{
  
	private static final long serialVersionUID = 8452239839849541495L;
	long id;
    String point;
    String tripLegDuration;
    String timeFrom;
    String timeTo;
    String vehicleLocation;
    String status;
    String speed;
    String aggrDist;
    String aggrHaltDur;
    String aggrMoveDur;

    public TripSummaryReportModel() {
    }

    public TripSummaryReportModel(long id, String point, String tripLegDuration, String timeFrom, String timeTo, String vehicleLocation, String status, String speed, String aggrDist, String aggrHaltDur, String aggrMoveDur) {
        this.id = id;
        this.point = point;
        this.tripLegDuration = tripLegDuration;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.vehicleLocation = vehicleLocation;
        this.status = status;
        this.speed = speed;
        this.aggrDist = aggrDist;
        this.aggrHaltDur = aggrHaltDur;
        this.aggrMoveDur = aggrMoveDur;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTripLegDuration() {
        return tripLegDuration;
    }

    public void setTripLegDuration(String tripLegDuration) {
        this.tripLegDuration = tripLegDuration;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getVehicleLocation() {
        return vehicleLocation;
    }

    public void setVehicleLocation(String vehicleLocation) {
        this.vehicleLocation = vehicleLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getAggrDist() {
        return aggrDist;
    }

    public void setAggrDist(String aggrDist) {
        this.aggrDist = aggrDist;
    }

    public String getAggrHaltDur() {
        return aggrHaltDur;
    }

    public void setAggrHaltDur(String aggrHaltDur) {
        this.aggrHaltDur = aggrHaltDur;
    }

    public String getAggrMoveDur() {
        return aggrMoveDur;
    }

    public void setAggrMoveDur(String aggrMoveDur) {
        this.aggrMoveDur = aggrMoveDur;
    }
    
    
}
