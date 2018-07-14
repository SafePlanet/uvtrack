package com.spi.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "absent")
public class Absent extends BaseEntity {

	private static final long serialVersionUID = -1417416063318717977L;

	private String message;
	private char presentFlag;
	
	@ManyToOne
	@JoinColumn(name = "absent_reason_id")
	private AbsentReason absentReason;
	
	@Temporal(TemporalType.DATE)
	private Date fromDate;
	
	@Temporal(TemporalType.DATE)
	private Date toDate;

	@ManyToOne
	@JoinColumn(name = "student_id")
	Student student;

	public Absent( String message, char presentFlag) {
		super();
		this.message = message;
		this.presentFlag = presentFlag;
	}

	public Absent() {
		this(UUID.randomUUID());
	}

	public Absent(UUID guid) {
		super(guid);
	}

	public char getPresentFlag() {
		return presentFlag;
	}

	public void setPresentFlag(char presentFlag) {
		this.presentFlag = presentFlag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public AbsentReason getAbsentReason() {
		return absentReason;
	}

	public void setAbsentReason(AbsentReason absentReason) {
		this.absentReason = absentReason;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}
