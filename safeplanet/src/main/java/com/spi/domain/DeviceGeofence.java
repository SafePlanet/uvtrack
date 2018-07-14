package com.spi.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "device_geofence")
public class DeviceGeofence extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 6607172100648330539L;

	public DeviceGeofence() {	}
	
	private int deviceid;
	
	private int geofenceid;

	public int getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}

	public int getGeofenceid() {
		return geofenceid;
	}

	public void setGeofenceid(int geofenceid) {
		this.geofenceid = geofenceid;
	}

}
