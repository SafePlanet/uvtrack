package com.spi.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "user_device")
public class UserDevice  extends AbstractPersistable<Long>{
	
	private static final long serialVersionUID = -8673200728666128647L;
	
	
	
	@ManyToOne
	@JoinColumn(name="deviceid",referencedColumnName="id")
	private Devices devices=null;
	
	@ManyToOne
	@JoinColumn(name="userid",referencedColumnName="id")
	private User user=null;

	public UserDevice() {
		super();
	}
	
	public Devices getDevices() {
		return devices;
	}

	public void setDevices(Devices devices) {
		this.devices = devices;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
