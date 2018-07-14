/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import com.spi.domain.AbsentReason;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;



@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbsentReasonModel {
    
    long id;
    String reason;
    String firstName;
    String lastName;
    String fromDate;
    String toDate;

    public AbsentReasonModel() {
    }

    public AbsentReasonModel(AbsentReason absentReason) {
        this.id = absentReason.getId();
        this.reason = absentReason.getReason();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
   
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
    
}
