package com.spi.user.api;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentAssignment {

	private List<Long> studentId;
	private long routeId;
	private String pickTime;
	private String dropTime;
	private Long wayPointId;
	private Double lattitude;
	private Double longitude;
	private String routeName;

	public long getRouteId() {
		return routeId;
	}

	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}

	public Long getWayPointId() {
		return wayPointId;
	}

	public void setWayPointId(Long wayPointId) {
		this.wayPointId = wayPointId;
	}

	public Double getLattitude() {
		return lattitude;
	}

	public void setLattitude(Double lattitude) {
		this.lattitude = lattitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public List<Long> getStudentId() {
		return studentId;
	}

	public void setStudentId(List<Long> studentId) {
		this.studentId = studentId;
	}

	public String getPickTime() {
		return pickTime;
	}

	public void setPickTime(String pickTime) {
		this.pickTime = pickTime;
	}

	public String getDropTime() {
		return dropTime;
	}

	public void setDropTime(String dropTime) {
		this.dropTime = dropTime;
	}

	@Override
	public String toString() {
		return "AlertConfigVM [studentId=" + studentId + ", routeId=" + routeId + ", routeName=" + routeName + ", lattitude=" + lattitude
				+ ", longitude=" + longitude + ", pickTime=" + pickTime + ", dropTime=" + dropTime + "]";
	}

}
