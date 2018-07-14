/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.domain;

import java.util.List;

import com.spi.model.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;



@Entity
@Table(name = "absent_reason")
public class AbsentReason extends BaseEntity {
    
	@OneToMany(mappedBy = "absentReason", targetEntity = Absent.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Absent> absent;
	
	private static final long serialVersionUID = -5209911800396660417L;
	String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

	public List<Absent> getAbsent() {
		return absent;
	}

	public void setAbsent(List<Absent> absent) {
		this.absent = absent;
	}

        
}
