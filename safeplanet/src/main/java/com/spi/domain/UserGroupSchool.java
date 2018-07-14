package com.spi.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_group")
public class UserGroupSchool implements Serializable {

	private static final long serialVersionUID = 2063980350924233222L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private Long groupid;

	@ManyToOne
	@JoinColumn(name = "schoolid")
	School school;

	
	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;

	@ManyToOne
	@JoinColumn(name = "userid")
	Users users;

	public Long getGroupid() {
		return groupid;
	}

	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}

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

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
