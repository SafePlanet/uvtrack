/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import com.spi.domain.School;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author Gungun&Goldy
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolModel {
    
    private long id;
    private String uuid;
    
    private String name;
    @Enumerated(EnumType.STRING)
    private String schoolType;
    private String emailId;
    private String phoneNumber1;
    private String phoneNumber2;
    private String phoneNumber3;
    private String subType;
    private String subAmount;
    private String displayAddress;
    Date serviceStartDate;
    Date serviceEndDate;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pinCode;

    public SchoolModel() {
    }

    public SchoolModel(long id, String uuid, String name, String schoolType, String emailId, String phoneNumber1, String phoneNumber2, String phoneNumber3, String displayAddress, String addressLine1, String addressLine2, String city, String state, String pinCode) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.schoolType = schoolType;
        this.emailId = emailId;
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
        this.phoneNumber3 = phoneNumber3;
        this.displayAddress = displayAddress;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
    }

    
    
    public SchoolModel(School school) {
        this.id = school.getId();
        this.uuid = school.getUuid().toString();
        this.name = school.getName();
        this.schoolType = school.getSchoolType();
        this.emailId = school.getEmailId();
        this.phoneNumber1 = school.getPhoneNumber1();
        this.phoneNumber2 = school.getPhoneNumber2();
        this.phoneNumber3 = school.getPhoneNumber3();
        this.subType=school.getSubType();
        this.subAmount=school.getSubAmount();
        this.serviceStartDate=school.getServiceStartDate();
        this.serviceEndDate=school.getServiceEndDate();
        this.displayAddress = school.getDisplayAddress();
        if(school.getAddress()!=null)
        {
            this.addressLine1 = school.getAddress().getAddressLine1();
            this.addressLine2 = school.getAddress().getAddressLine2();
            this.city = school.getAddress().getCity();
            this.state = school.getAddress().getState();
            this.pinCode = school.getAddress().getPinCode();
        }
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getPhoneNumber3() {
        return phoneNumber3;
    }

    public void setPhoneNumber3(String phoneNumber3) {
        this.phoneNumber3 = phoneNumber3;
    }

    public String getDisplayAddress() {
        return displayAddress;
    }

    public void setDisplayAddress(String displayAddress) {
        this.displayAddress = displayAddress;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getSubAmount() {
		return subAmount;
	}

	public void setSubAmount(String subAmount) {
		this.subAmount = subAmount;
	}

	public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

	public Date getServiceStartDate() {
		return serviceStartDate;
	}

	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}

	public Date getServiceEndDate() {
		return serviceEndDate;
	}

	public void setServiceEndDate(Date serviceEndDate) {
		this.serviceEndDate = serviceEndDate;
	}
    
    
    
}
