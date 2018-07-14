package com.spi.domain;

public class LocationModel {

	private long id;
	private double lattitude;
	private double longitude;
	private double speed;
	private int direction;

	public LocationModel(Location location) {
		this.id = location.getId();
		this.lattitude = location.getLattitude();
		this.longitude = location.getLongitude();
		this.speed = Math.round(location.getSpeed()*100.0)/100.0;
		this.direction = location.getCourse();
	}
	
	public LocationModel() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public double getLattitude() {
		return lattitude;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

}
