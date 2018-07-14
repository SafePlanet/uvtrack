package com.spi.VM;

import java.util.Date;
import java.sql.Time;

// CREATE TABLE `student_notification_status` (
//		  `id` INT NOT NULL AUTO_INCREMENT,
//		  `student_id` BIGINT(20) NOT NULL,
//		  `message_date` DATE NOT NULL
//		  `message_time` TIME NOT NULL,
//		  `message` VARCHAR(512) NOT NULL,
//		  PRIMARY KEY (`id`),
//		  INDEX `stud_fk_idx` (`student_id` ASC),
//		  CONSTRAINT `stud_fk`
//		    FOREIGN KEY (`student_id`)
//		    REFERENCES `student` (`id`)
//		    ON DELETE NO ACTION
//		    ON UPDATE NO ACTION);

public class StudentNotificationStatusVM {

	private long id;
	private long studentId;
	private Date date = null;
	private Time actualTime = null;
	private Time expectedTime = null;
	private String message;
	private String name;
	private String timeVariance;
	private long wayPointId;

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimeVariance() {
		return timeVariance;
	}

	public void setTimeVariance(String timeVariance) {
		this.timeVariance = timeVariance;
	}

	public Time getActualTime() {
		return actualTime;
	}

	public void setActualTime(Time actualTime) {
		this.actualTime = actualTime;
	}

	public Time getExpectedTime() {
		return expectedTime;
	}

	public void setExpectedTime(Time expectedTime) {
		this.expectedTime = expectedTime;
	}

	public long getWayPointId() {
		return wayPointId;
	}

	public void setWayPointId(long wayPointId) {
		this.wayPointId = wayPointId;
	}

}
