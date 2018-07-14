
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
@Table(name = "class_Group")
public class ClassGroup extends BaseEntity {

	private static final long serialVersionUID = 5464200389956142390L;

	public String className;
	public String groupName;
	public long schoolId;
	
	public long getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	

}
