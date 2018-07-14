package com.spi.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spi.util.TrackoHasher;

@Entity
@Table(name = "users")
public class Users implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8754439261055325980L;

	@Id
	private long id;

	private int zoom;
	private String distanceunit = "km";
	private String speedunit = "kmh";
	private double latitude;
	private double longitude;
	private String name;
	private String email;
	private boolean readonly;
	private boolean admin;
	private String map = "osm";

	@Transient
	private String password;
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean twelvehourformat = true;
	private String hashedpassword;
	private String salt;
	@OneToMany(mappedBy = "user", targetEntity = UserGroupSchool.class, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<UserGroupSchool> userGroupSchools;

	public List<UserGroupSchool> getUserGroupSchools() {
		return userGroupSchools;
	}

	public void setUserGroupSchools(List<UserGroupSchool> userGroupSchools) {
		this.userGroupSchools = userGroupSchools;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean getAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

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

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public boolean isTwelvehourformat() {
		return twelvehourformat;
	}

	public void setTwelvehourformat(boolean twelvehourformat) {
		this.twelvehourformat = twelvehourformat;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		if (password != null && !password.isEmpty()) {
			TrackoHasher.HashingResult hashingResult = TrackoHasher.createHash(password);
			hashedpassword = hashingResult.getHash();
			salt = hashingResult.getSalt();
		}
	}

	@JsonIgnore
	public String getHashedpassword() {
		return hashedpassword;
	}

	public void setHashedpassword(String hashedPassword) {
		this.hashedpassword = hashedPassword;
	}

	@JsonIgnore
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public boolean isPasswordValid(String password) {
		return TrackoHasher.validatePassword(password, hashedpassword, salt);
	}

	public String getDistanceunit() {
		return distanceunit;
	}

	public void setDistanceunit(String distanceunit) {
		this.distanceunit = distanceunit;
	}

	public String getSpeedunit() {
		return speedunit;
	}

	public void setSpeedunit(String speedunit) {
		this.speedunit = speedunit;
	}

}
