/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.VM;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.spi.domain.Geofence;

public class GeofenceVM {

	private String name;

	private String description;

	private String area;

	private List<LatLong> latLongs;
	
	public GeofenceVM(Geofence geofence) {
		this.name = geofence.getName();
		this.description = geofence.getDescription();
		this.area = geofence.getArea();
		setLatLongList(area);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
		setLatLongList(area);
	}

	private List<LatLong> setLatLongList(String area) {
		this.latLongs = new ArrayList<GeofenceVM.LatLong>();
		if(area.startsWith("POLYGON")){
			area = area.substring(9, area.length()-2);
			area = area.replaceAll(", ", ",");
		}
//		area = area.replaceAll("POLYGON((", "").replaceAll("))", "");
		String[] latLongString = StringUtils.split(area, ',');
		LatLong latLong = null;
		for (int i = 0; i < latLongString.length; i++) {
			latLong = new LatLong();
			latLong.setLatitude(Double.parseDouble(latLongString[i].substring(0, latLongString[i].indexOf(" "))));
			latLong.setLongitude(Double.parseDouble(latLongString[i].substring(latLongString[i].indexOf(" ") + 1, latLongString[i].length())));
			latLongs.add(latLong);
		}
		return latLongs;
	}

	public List<LatLong> getLatLongList() {
		return this.latLongs;
	}

	class LatLong {
		private double latitude;
		private double longitude;

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

	}

}
