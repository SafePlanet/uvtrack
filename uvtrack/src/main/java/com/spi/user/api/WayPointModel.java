/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.spi.domain.Student;
import com.spi.domain.WayPoint;
import com.spi.util.StringUtil;


@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class WayPointModel {
    
    private long id;
	private int sequenceNumber;
    private String name;
    private double lattitude;
    private double longitude;
    private double altitude;
    private String uuid;
    private long wayPointId;
    private Date timeDrop;
    private Date timePick;
	private String studentNames;

    public WayPointModel() {
    }

    public WayPointModel(int sequenceNumber, String name, double lattitude, double longitude, double altitude, String uuid) {
        this.sequenceNumber = sequenceNumber;
        this.name = name;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.uuid = uuid;
    }
    public WayPointModel(WayPoint wayPoint) {
    	this.id = wayPoint.getId();
        this.sequenceNumber = wayPoint.getSequenceNumber();
        this.name = wayPoint.getName();
        this.lattitude = wayPoint.getLattitude();
        this.longitude = wayPoint.getLongitude();
        this.altitude = wayPoint.getAltitude();
        this.uuid = wayPoint.getUuid().toString();
        this.wayPointId=wayPoint.getId();
        this.timePick = wayPoint.getTimePick();
        this.timeDrop = wayPoint.getTimeDrop();
        List<String> studentNameList = new ArrayList<String>();
        for(Student student : wayPoint.getStudents()){
        	studentNameList.add(student.getFirstName() + " " + student.getLastName());
        }
        
        this.studentNames = StringUtil.getCommaSerperatedValues(studentNameList);
    }
    
    public long getId() {
		return id;
	}

	public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public long getWayPointId() {
		return wayPointId;
	}

	public String getStudentNames() {
		return studentNames;
	}
	
	public Date getTimeDrop() {
		return timeDrop;
	}

	public Date getTimePick() {
		return timePick;
	}
    
}
