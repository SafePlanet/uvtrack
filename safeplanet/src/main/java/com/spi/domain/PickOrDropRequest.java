
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "pick_drop_request")
public class PickOrDropRequest extends BaseEntity {

	private static final long serialVersionUID = 5464200389956142390L;

	private String reason;
	private char pickOrDrop;
	private char pickDropStatus;
	private long studentId;
	private long schoolId;
	private Date requestDate;

	public String getPickUpReason() {
		return reason;
	}

	public void setPickUpReason(String pickUpReason) {
		this.reason = pickUpReason;
	}

	public char getPickOrDrop() {
		return pickOrDrop;
	}

	public void setPickOrDrop(char pickOrDrop) {
		this.pickOrDrop = pickOrDrop;
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

	public Date getPickUpDate() {
		return requestDate;
	}

	public void setPickUpDate(Date pickUpDate) {
		this.requestDate = pickUpDate;
	}

	public char getPickDropStatus() {
		return pickDropStatus;
	}

	public void setPickDropStatus(char pickDropStatus) {
		this.pickDropStatus = pickDropStatus;
	}

}
