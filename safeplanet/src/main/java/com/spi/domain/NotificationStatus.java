package com.spi.domain;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.spi.VM.NotificationStatusVM;

@Entity
@Table(name = "notification_status")
public class NotificationStatus extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -3993781100380684976L;

	// @Temporal(TemporalType.TIME)
	private Date processDate;
	// @Temporal(TemporalType.TIME)
	private Time startTime;
	// @Temporal(TemporalType.TIME)
	private Time endTime;
	private int totalWayPoint;
	private String wayPointNotTouched;
	private String wayPointIds;
	private int scheduleId;

	public NotificationStatus() {
	}

	public NotificationStatus(Date processDate, Time startTime, Time endTime, int scheduleId, int totalWayPoint, String wayPointNotTouched, String wayPointIds) {
		super();
		this.setProcessDate(processDate);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setScheduleId(scheduleId);
		this.setTotalWayPoint(totalWayPoint);
		this.setWayPointNotTouched(wayPointNotTouched);
		this.setWayPointIds(wayPointIds);
	}

	public NotificationStatus(NotificationStatusVM NotificationStatusVM) {

		// this.id= (int) NotificationStatusVM.getId();
		this.setScheduleId((int) NotificationStatusVM.getScheduleId());
		this.setProcessDate(NotificationStatusVM.getProcessDate());
		this.setStartTime(NotificationStatusVM.getStartTime());
		this.setEndTime(NotificationStatusVM.getEndTime());
		this.setTotalWayPoint(NotificationStatusVM.getTotalWayPoint());
		this.setWayPointNotTouched(NotificationStatusVM.getWayPointNotTouched());
		this.setWayPointIds(NotificationStatusVM.getWayPointDetails());

	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public String getWayPointIds() {
		return wayPointIds;
	}

	public void setWayPointIds(String wayPointIds) {
		this.wayPointIds = wayPointIds;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public int getTotalWayPoint() {
		return totalWayPoint;
	}

	public void setTotalWayPoint(int totalWayPoint) {
		this.totalWayPoint = totalWayPoint;
	}

	public String getWayPointNotTouched() {
		return wayPointNotTouched;
	}

	public void setWayPointNotTouched(String wayPointNotTouched) {
		this.wayPointNotTouched = wayPointNotTouched;
	}

}
