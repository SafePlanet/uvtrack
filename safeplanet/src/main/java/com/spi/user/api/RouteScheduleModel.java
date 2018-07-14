package com.spi.user.api;

import java.util.Date;

public class RouteScheduleModel {

	private String routeUuid = null;
	private long scheduleId ;
	private String scheduleName;
	private char scheduleFlag;
	private long schoolId;
	private String SchoolName;
	private String routeName;
	private Date startTime;
	private Date endTime;
	private long routeId;
	private boolean sendToAll = false;
	
	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public char getScheduleFlag() {
		return scheduleFlag;
	}

	public void setScheduleFlag(char scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return SchoolName;
	}

	public void setSchoolName(String schoolName) {
		SchoolName = schoolName;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public long getRouteId() {
		return routeId;
	}

	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}

    public String getRouteUuid() {
		return routeUuid;
	}

	public void setRouteUuid(String routeUuid) {
		this.routeUuid = routeUuid;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public boolean isSendToAll() {
		return sendToAll;
	}

	public void setSendToAll(boolean sendToAll) {
		this.sendToAll = sendToAll;
	}

}
