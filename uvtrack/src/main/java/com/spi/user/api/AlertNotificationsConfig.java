/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.spi.domain.AlertConfig;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlertNotificationsConfig {

	private int studentId;
    private Date pickStartTime;    
    private Date pickEndTime;
    
    private Date dropStartTime;
    private Date dropEndTime;
    
    private ArrayList<String> activeDays;
    
    private ArrayList<String> alertTypes;
    
    private String alertUpdateType;
    
    private int activeTime;

    public AlertNotificationsConfig() {}
    
    public AlertNotificationsConfig(AlertConfig alertConfig)
    {
        if(alertConfig!=null)
        {
            //this.pickStartTime = alertConfig.getPickStartTime()!=null?alertConfig.getPickStartTime():new java.util.Date();
            //this.pickEndTime = alertConfig.getPickEndTime()!=null?alertConfig.getPickEndTime():new java.util.Date();
            //this.dropStartTime = alertConfig.getDropStartTime()!=null?alertConfig.getDropStartTime():new java.util.Date();
            //this.dropEndTime = alertConfig.getDropEndTime()!=null?alertConfig.getDropEndTime():new java.util.Date();

//            if(alertConfig.getActiveDays()!=null && alertConfig.getActiveDays().size()>0) {
//                this.activeDays = alertConfig.getActiveDays();
//            } else {
//                this.activeDays = new ArrayList<String>();
//                this.activeDays.add("Select Days");
//            }
//            if(alertConfig.getAlertTypes()!=null && alertConfig.getAlertTypes().size()>0) {
//                this.alertTypes = alertConfig.getAlertTypes();
//            } else {
//                this.alertTypes = new ArrayList<String>();
//                this.alertTypes.add("Select Alert Types");
//            }

            this.activeTime = alertConfig.getActiveTime();
            
        }
        else
        {
            this.pickStartTime = new java.util.Date();
            this.pickEndTime = new java.util.Date();
            this.dropStartTime = new java.util.Date();
            this.dropEndTime = new java.util.Date();

            this.activeDays = new ArrayList<String>();
            this.activeDays.add("Select Days");
            
            this.alertTypes = new ArrayList<String>();
            this.alertTypes.add("Select Alert Types");

            this.activeTime = 0;
        }
    }
    
    public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public Date getPickStartTime() {
        return this.pickStartTime;
    }

    public void setPickStartTime(Date pickStartTime) {
        this.pickStartTime = pickStartTime;
    }

    public Date getPickEndTime() {
        return this.pickEndTime;
    }

    public void setPickEndTime(Date pickEndTime) {
        this.pickEndTime = pickEndTime;
    }

    public Date getDropStartTime() {
        return this.dropStartTime;
    }

    public void setDropStartTime(Date dropStartTime) {
        this.dropStartTime = dropStartTime;
    }

    public Date getDropEndTime() {
        return this.dropEndTime;
    }

    public void setDropEndTime(Date dropEndTime) {
        this.dropEndTime = dropEndTime;
    }

    public ArrayList<String> getActiveDays() {
        return this.activeDays;
    }

    public void setActiveDays(ArrayList<String> activeDays) {
        this.activeDays = activeDays;
    }

    public ArrayList<String> getAlertTypes() {
        return this.alertTypes;
    }

    public void setAlertTypes(ArrayList<String> alertTypes) {
        this.alertTypes = alertTypes;
    }

    public String getAlertUpdateType() {
        return this.alertUpdateType;
    }

    public void setAlertUpdateType(String alertUpdateType) {
        this.alertUpdateType = alertUpdateType;
    }

    public int getActiveTime() {
        return this.activeTime;
    }

    public void setActiveTime(int activeTime) {
        this.activeTime = activeTime;
    }
    
    
    
}
