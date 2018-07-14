package com.spi.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "geofences")
public class Geofence extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -2659673346531516399L;

	public Geofence() {	}
	
	private String name;
	
	private String description;
	
	private String area;
	
	private String type;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
//POLYGON((28.655554396043343 77.36763224386355, 28.655554396043343 77.36763224386355, 
//		28.654845933617068 77.36810699485918, 28.65524606248603 77.36882046245714, 
//		28.65599688841901 77.36831352495334, 
//		28.655632067712475 77.36758664631027, 28.655554396043343 77.36763224386355))
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	


}
