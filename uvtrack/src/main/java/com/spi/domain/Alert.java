/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "alert")
public class Alert extends AbstractPersistable<Long> {

	/**
	 * 
	 */
	private long userId;
	private long studentId;
	private Date alertDateTime;
	private String addText;
	private String emailAddress;

	@OneToOne(mappedBy = "alert", targetEntity = AlertType.class, cascade = CascadeType.ALL)
	private AlertType alertType;

	private static final long serialVersionUID = -3464704269743212311L;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	

	public Date getAlertDateTime() {
		return alertDateTime;
	}

	public void setAlertDateTime(Date alertDateTime) {
		this.alertDateTime = alertDateTime;
	}

	public String getAddText() {
		return addText;
	}

	public void setAddText(String addText) {
		this.addText = addText;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	
	public String getEmail() {
	    return emailAddress; 
		
	}

	public void setEmail(String emailAddress) {
		this.emailAddress = emailAddress;
		
	}

	

}
