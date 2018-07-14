package com.spi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.UserGroupSchool;

/**
 * 
 * @author ranjeet
 *
 */
public interface UserGroupSchoolDAO extends JpaRepository<UserGroupSchool, Long> {

	@Query("SELECT distinct ug from UserGroupSchool ug where ug.user.id=? and ug.groupid is not null")
	UserGroupSchool findUserGropuByUserid(Long userid);
	
	@Query("SELECT distinct ug from UserGroupSchool ug where ug.user.id=? and ug.groupid is not null")
	List<UserGroupSchool> findUserGroupsByUserid(Long userid);
}
