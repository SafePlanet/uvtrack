/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.spi.domain.WayPoint;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentRouteModel {
    
    long studentId;
    long routeId;
    long wayPointId;
    long schoolId;

    public StudentRouteModel() {
    }

    public StudentRouteModel(long studentId, long routeId, long wayPointId, long schoolId) {
        this.studentId = studentId;
        this.routeId = routeId;
        this.wayPointId = wayPointId;
        this.schoolId = schoolId;
    }
    
    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public long getWayPointId() {
        return wayPointId;
    }

    public void setWayPointId(long wayPointId) {
        this.wayPointId = wayPointId;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }
    
    
    
    
}
