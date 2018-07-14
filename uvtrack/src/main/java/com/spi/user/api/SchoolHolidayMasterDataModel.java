package com.spi.user.api;

import java.util.Date;

import com.spi.domain.SchoolHolidayMasterData;

public class SchoolHolidayMasterDataModel {
	
	private long id;
	private String description;
	private Date fromDate;
	private Date toDate;
	
	public SchoolHolidayMasterDataModel()
	{
		
	}
	
	public SchoolHolidayMasterDataModel(SchoolHolidayMasterData masterData)
	{
		setId(masterData.getId());
		setDescription(masterData.getDescription());
		setFromDate(masterData.getFromDate());
		setToDate(masterData.getToDate());
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
