package com.spi.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "school_config")
public class SchoolConfig extends BaseEntity{

	private static final long serialVersionUID = -1983879917018236425L;

	@ManyToOne
	@JoinColumn(name = "school_id")
	School school;
	
	String configKey;
	String valueType;
	String value;
	
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public String getConfigKey() {
		return configKey;
	}
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
