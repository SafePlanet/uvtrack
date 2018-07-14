package com.spi.user.api;

import java.util.List;

public class SchoolHolidaysModel {

	private List<SchoolHolidayModel> holidays;
	private SchoolModel defaultSchool;
	public List<SchoolHolidayModel> getHolidays() {
		return holidays;
	}
	public void setHolidays(List<SchoolHolidayModel> holidays) {
		this.holidays = holidays;
	}
	public SchoolModel getDefaultSchool() {
		return defaultSchool;
	}
	public void setDefaultSchool(SchoolModel school) {
		this.defaultSchool = school;
	}
}
