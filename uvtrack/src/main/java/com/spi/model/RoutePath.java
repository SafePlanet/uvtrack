package com.spi.model;

public class RoutePath {

	private String message;
	private String weight = "8";

	private String[] latlongs;

	private String[][] currentLatlongs;

	private String kmlfile;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String[] getLatlongs() {
		return latlongs;
	}

	public void setLatlongs(String[] latlongs) {
		this.latlongs = latlongs;
	}

	public String[][] getCurrentLatlongs() {
		return currentLatlongs;
	}

	public void setCurrentLatlongs(String[][] currentLatlongs) {
		this.currentLatlongs = currentLatlongs;
	}

	public String getKmlfile() {
		return kmlfile;
	}

	public void setKmlfile(String kmlfile) {
		this.kmlfile = kmlfile;
	}

}
