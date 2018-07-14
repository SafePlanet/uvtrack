
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
@Table(name = "visitor_request")
public class VisitorRequest extends BaseEntity {

	private static final long serialVersionUID = 5464200389956142390L;

	private String purpose;
	private String visitorName;
	private String visitorNumber;
	private String otpNumber;
	private String token;
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getVisitorName() {
		return visitorName;
	}
	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}
	public String getVisitorNumber() {
		return visitorNumber;
	}
	public void setVisitorNumber(String visitorNumber) {
		this.visitorNumber = visitorNumber;
	}
	public String getOtpNumber() {
		return otpNumber;
	}
	public void setOtpNumber(String otpNumber) {
		this.otpNumber = otpNumber;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
