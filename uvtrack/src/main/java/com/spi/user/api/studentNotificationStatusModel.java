/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import com.spi.domain.School;
import com.spi.domain.StudentNotificationStatus;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author Gungun&Goldy
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class studentNotificationStatusModel {
    
	private long id;
	private long studentId;
	private Date date = null;
	private Time time = null;
	private String message;
	
    public studentNotificationStatusModel() {
    }

    public studentNotificationStatusModel(long id, long studentId, Date date, Time time, String message) {
        this.setId(id);
       this.setStudentId(studentId);
       this.setDate(date);
       this.setTime(time);
       this.setMessage(message);
    }

    
    
    public studentNotificationStatusModel(StudentNotificationStatus sns) {
       this.setId(sns.getId());
    //   this.setStudentId(sns.getStudent_id());
       this.setDate((Date) sns.getMessageDate());
       this.setTime(sns.getMessageTime());
       this.setMessage(sns.getMessage());
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
       
    
}
