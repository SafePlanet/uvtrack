package com.spi.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "route_Schedule_Details")
public class RouteScheduleDetails extends AbstractPersistable<Long> {
	private static final long serialVersionUID = 5263144867458714737L;

	@ManyToOne
	@JoinColumn(name = "route_id")
	private Route route = null;

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	private String scheduleName = null;
	
	private char sjaFlag;

	@Temporal(TemporalType.TIME)
	private Date startTime = null;
	@Temporal(TemporalType.TIME)
	private Date endTime = null;

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
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

	public char getSjaFlag() {
		return sjaFlag;
	}

	public void setSjaFlag(char sjaFlag) {
		this.sjaFlag = sjaFlag;
	}


}
