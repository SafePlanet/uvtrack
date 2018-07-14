/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.School;
import com.spi.domain.User;

public interface SchoolDAO extends JpaRepository<School, Long> {

	@Query("select s from School s order by s.name")
	List<School> getAllSchool();

	@Query("select s from School s where id=?")
	School getSchoolDetailById(Long id);
	
	@Query("select s from School s where uuid=?")
	School getSchoolDetailByUUID(String uuid);

	@Query("select s from School s, UserSchool ugs, User u where s.id = ugs.school.id and ugs.user.id = u.id and u.uuid=? order by s.name")
	List<School> getSchoolBySchoolAdminUserId(String uuid);
	
	@Query("select distinct sch from School sch, Student st, User u where sch.id = st.school.id and u.id = st.user.id and u.uuid=? order by sch.name")
	List<School> getSchoolByParentUserId(String uuid);
	
	@Query("select u from School s, UserSchool ugs, User u where s.id = ugs.school.id and ugs.user.id = u.id and u.role='administrator' and s.id = ?")
	User findSchoolAdminBySchool(long schoolId);
	
	@Query("select s from School s  where s not in (select school from UserSchool) order by s.name")
	List<School> getUnassignedSchool();
	
	@Query("select s from School s, UserSchool ugs, User u where s.id = ugs.school.id and ugs.user.id = u.id and u.role='administrator' and u.uuid =? order by s.name")
	School getSchoolBySchoolAdminId(String userUuid);
}
