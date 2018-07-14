package com.spi.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 
 * @author ranjeet
 *
 */
@Entity
@Table(name="device_command")
public class DeviceCommand extends  AbstractPersistable<Long>{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long device_id;
	String command_type;
	String command;
	
	public long getDevice_id() {
		return device_id;
	}
	public void setDevice_id(long device_id) {
		this.device_id = device_id;
	}
	public String getCommand_type() {
		return command_type;
	}
	public void setCommand_type(String command_type) {
		this.command_type = command_type;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}


}
