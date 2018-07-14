package com.spi.VM;

import java.sql.Time;
import java.util.Date;

/**
 * 
 * CREATE TABLE `notification_status` ( `id` BIGINT(20) NOT NULL AUTO_INCREMENT, `schedule_id`
 * BIGINT(20) NOT NULL, `process_date` DATE NULL, `start_time` TIME NULL,
 * `end_time` TIME NULL, `total_way_point` INT NULL, `message_not_sent` INT
 * NULL, `student_ids` VARCHAR(512) NULL, PRIMARY KEY (`id`));
 *
 * ALTER TABLE `notification_status` ADD INDEX `ns_to_route_schedule_idx`
 * (`schedule_id` ASC); ALTER TABLE `notification_status` ADD CONSTRAINT
 * `ns_to_route_schedule` FOREIGN KEY (`schedule_id`) REFERENCES
 * `route_schedule_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
 *
 */
public class NotificationStatusVM {

	private long id;
	private long scheduleId;
	private String scheduleName;
	private Date processDate;
	private Time startTime;
	private Time endTime;
	private int totalWayPoint;
	private String wayPointNotTouched;
	private String wayPointDetails;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
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

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getWayPointDetails() {
		return wayPointDetails;
	}

	public void setWayPointDetails(String wayPointDetails) {
		this.wayPointDetails = wayPointDetails;
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
