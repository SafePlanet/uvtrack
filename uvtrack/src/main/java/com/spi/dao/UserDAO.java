/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */

package com.spi.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.Student;
import com.spi.domain.User;

/**
 *
 * @version 1.0
 * @author:
 * 
 */
public interface UserDAO extends JpaRepository<User, Long> {

	User findByEmailAddress(String emailAddress);

	@Query("select u from User u where uuid = ?")
	User findByUuid(String uuid);

	@Query("select u from User u where u in (select user from AuthorizationToken where lastUpdated < ?)")
	List<User> findByExpiredSession(Date lastUpdated);

	@Query("select u from User u where u = (select user from AuthorizationToken where token = ?)")
	User findBySession(String token);

	@Query("select s from Student s, User u where u.uuid = ? and u.id= s.user.id and s.isApproved='Y'")
	List<Student> getStudentDetails(String userId);

	@Query("select u from User u where routeId = ?")
	List<User> findAllByRouteId(long routeId);

	@Query("select u from User u order by u.firstName, u.lastName")
	List<User> findAllUser();

	@Query("select distinct u from Student s, UserSchool ugs, User uadmin, User u where s.school.id = ugs.school.id and ugs.user.id = uadmin.id and s.user.id = u.id and uadmin.uuid=? order by s.wayPoint.route.routeName, u.firstName, u.lastName")
	List<User> findUsersForAdmin(String uuid);

	@Query("select u from User u where u.role = 'administrator' order by u.firstName, u.lastName")
	List<User> findUsersForSuperAdmin();
	
	@Query("select teacher from User teacher where teacher.role = 'teacher' and teacher.userSchool.school.id=? order by teacher.firstName, teacher.lastName")
	List<User> findTeachers(Long schoolId);
	
	@Query("select distinct u from Student s, UserSchool ug, User u where s.id = ? and s.school.id = ug.school.id and u.id = ug.user.id and u.role = 'administrator'")
	User findAdminByStudentId(long studentId);
	
	@Query("select distinct u from User u where u.role = 'superAdmin' ")
	User findSuperAdmin();
	
	@Query("select u from Devices d, UserDevice ud, User u where d.id = ud.devices.id and ud.user.id = u.id and d.id = ? and d.voidInd='n' ")
	User findAdminByDeviceId(long deviceId);

}
