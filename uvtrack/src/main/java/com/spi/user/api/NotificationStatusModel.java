package com.spi.user.api;

import java.sql.Date;
import java.sql.Time;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.spi.VM.NotificationStatusVM;

/**
 *
 * @author Gungun&Goldy
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationStatusModel {
	private Date processDate = null;
	private Time startTime = null;
	private Time endTime = null;
	private int totalMsgToSent;
	private String messageNotSent;
	private String wayPointIds;

	private int scheduleId;

	// @OneToMany(mappedBy = "notification_status", targetEntity =WayPoint.class
	// , cascade = CascadeType.ALL)
	// @LazyCollection(LazyCollectionOption.TRUE)
	// private List<WayPoint> wayPoint = new ArrayList<WayPoint>();

	public NotificationStatusModel() {
	}

	public NotificationStatusModel(Time startTime, Time endTime, int scheduleId, int totalMsgToSent, String messageNotSent, String wayPointIds) {
		super();
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setScheduleId(scheduleId);
		this.setTotalMsgToSent(totalMsgToSent);
		this.setMessageNotSent(messageNotSent);
		this.setWayPointIds(wayPointIds);

	}

	public NotificationStatusModel(NotificationStatusVM NotificationStatusVM) {

		this.setScheduleId((int) NotificationStatusVM.getScheduleId());
		// this.setProcessDate(NotificationStatusVM.getProcessDate());
		this.setStartTime(NotificationStatusVM.getStartTime());
		this.setEndTime(NotificationStatusVM.getEndTime());
		this.setTotalMsgToSent(NotificationStatusVM.getTotalWayPoint());
		this.setMessageNotSent(NotificationStatusVM.getWayPointNotTouched());
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

	public int getTotalMsgToSent() {
		return totalMsgToSent;
	}

	public void setTotalMsgToSent(int totalMsgToSent) {
		this.totalMsgToSent = totalMsgToSent;
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

	public String getMessageNotSent() {
		return messageNotSent;
	}

	public void setMessageNotSent(String messageNotSent) {
		this.messageNotSent = messageNotSent;
	}

}
