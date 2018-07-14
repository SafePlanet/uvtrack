package com.spi.domain;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.spi.VM.StudentNotificationStatusVM;

@Entity
@Table(name="student_notification_status")
public class StudentNotificationStatus extends AbstractPersistable<Long> {
	
	private static final long serialVersionUID = -3993781188380684976L;
	
	//@Temporal(TemporalType.TIME)
	private Date messageDate;
	//@Temporal(TemporalType.TIME)
	private Time messageTime;
	private String message;
	
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	public StudentNotificationStatus() {
		
	}
	
	
	public StudentNotificationStatus(long id,int student_id,String message) {
		super();
		//this.id = id;
	//	this.setStudent_id(student_id);
		//this.setMessageDate(messageDate);
		this.setMessage(message);
	}
	
	public StudentNotificationStatus(StudentNotificationStatusVM StudentNotificationStatusVM) {
		super();
		
		this.setId(StudentNotificationStatusVM.getId());
		//this.setMessageDate(StudentNotificationStatusVM.getDate());
		this.setMessageTime(StudentNotificationStatusVM.getActualTime());
		this.setMessage(StudentNotificationStatusVM.getMessage());
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	

	public Date getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}

	public Time getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(Time message_time) {
		this.messageTime = message_time;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}
