package com.spi.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "user_school")
public class UserSchool extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 6761371276074680978L;

	@OneToOne
	@JoinColumn(name = "school_id")
	private School school;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
