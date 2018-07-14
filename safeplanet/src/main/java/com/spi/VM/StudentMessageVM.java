package com.spi.VM;

import java.util.Date;


public class StudentMessageVM {
	
	
	private Long student_id;
	private Date message_date;
	private String message_time;
	private String message;
	 
	public long getStudentId() {
		return student_id;
	}

	public void setStudentId(long student_id) {
		this.student_id = student_id;
	}
	
	
	public Date getmessage_date() {
		return message_date;
	}

	public void setmessage_date(Date message_date) {
		this.message_date = message_date;
	}
	
	public String getmessage(){
		return message;
	}
	public void setmessage(String message) {
		this.message = message;
	}
	
	public String getmessage_time(){
		return message_time;
	}
	public void setmessage_time(String message_time) {
		this.message_time = message_time;
	}
	

}
