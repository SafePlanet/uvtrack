/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author ranjeet
 *
 */
@Entity
@Table(name="device_model_type")
public class DeviceModelType extends  AbstractPersistable<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5110065179737323855L;
	/**
	 * Device model e.g.Tk06A,UTX08A
	 */
	private String deviceModel;
	
	@OneToMany(mappedBy = "deviceModelType", targetEntity = Devices.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<Devices> devices;
	
	
	public List<Devices> getDevices() {
		return devices;
	}
	public void setDevices(List<Devices> devices) {
		this.devices = devices;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	
	public DeviceModelType(){
		super();
	}
	public DeviceModelType(Long id) {
		// TODO Auto-generated constructor stub
		setId(id);
	}
}
