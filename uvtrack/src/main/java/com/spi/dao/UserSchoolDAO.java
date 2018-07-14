package com.spi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.UserSchool;

public interface UserSchoolDAO extends JpaRepository<UserSchool, Long> {
	@Query("Select us from UserSchool us where us.user.id=?")
	UserSchool getSchoolByUser(long userId);
	
	@Query("Select us from UserSchool us where us.user.uuid=?")
	UserSchool getSchoolByUser(String userUuid);
	
	@Query("Select us from UserSchool us ,UserDevice ud where us.user.id=ud.user.id and ud.devices.id=?")
	UserSchool getSchoolByUserDevice(long deviceId);

}
