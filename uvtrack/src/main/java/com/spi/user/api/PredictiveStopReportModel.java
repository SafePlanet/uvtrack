/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Manish Kaushik
 *
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictiveStopReportModel implements Serializable, Comparable<PredictiveStopReportModel> {

	private static final long serialVersionUID = 7318112311255315210L;

	long id;
	String vehicleNumber;
	int stopSequence = 0;
	String stopTimes = null;
	int matchingInstances = 0;
	String locationUrl = null;

	public PredictiveStopReportModel() {
	}

	public PredictiveStopReportModel(long id, String vehicleNumber, int stopSequence, String stopTimes, int matchingInstances) {
		this.id = id;
		this.vehicleNumber = vehicleNumber;
		this.stopSequence = stopSequence;
		this.stopTimes = stopTimes;
		this.matchingInstances = matchingInstances;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public int getStopSequence() {
		return stopSequence;
	}

	public void setStopSequence(int stopSequence) {
		this.stopSequence = stopSequence;
	}

	public int getMatchingInstances() {
		return matchingInstances;
	}

	public void setMatchingInstances(int matchingInstances) {
		this.matchingInstances = matchingInstances;
	}

	public String getLocationUrl() {
		return locationUrl;
	}

	public void setLocationUrl(String locationUrl) {
		this.locationUrl = locationUrl;
	}

	public String getStopTimes() {
		return stopTimes;
	}

	public void setStopTimes(String stopTimes) {
		this.stopTimes = stopTimes;
	}

	@Override
	public int compareTo(PredictiveStopReportModel o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
