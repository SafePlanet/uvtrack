package com.spi.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spi.domain.Student;
import com.spi.domain.User;
import com.spi.domain.WayPoint;

public interface StudentsDAO extends JpaRepository<Student, Long> {

	@Query("select s from Student s where s.uuid = ?")
	Student findByUuid(String uuid);

	@Query("select s from Student s, User u where u.uuid = ? and u.routeId= s.wayPoint.route.id and s.isApproved='Y'")
	List<Student> getPresentStudents(String userId);

	@Query("select s from Student s, IN(s.absent) a, User u, UserSchool ug where u.uuid = :userId and ug.user.id = u.id and ug.school.id = s.school.id and a.presentFlag = :presentFlag and a.fromDate<=:date and a.toDate >=:date and s.isApproved='Y'")
	List<Student> getTodaysPresentStudents(@Param("userId") String userId, @Param("presentFlag") char presentFlag, @Param("date") Date date);

	@Query("select s from Student s, Absent a where s.id = :studentId and s.id = a.student.id and a.presentFlag = :presentFlag and a.fromDate<=:date and a.toDate >=:date and s.isApproved='Y'")
	List<Student> isStudentAbsentOnDate(@Param("studentId") String studentId, @Param("presentFlag") char presentFlag, @Param("date") Date date);

	@Query("select s from Student s where s.wayPoint.route.id=? and s.isApproved='Y' order by s.wayPoint.sequenceNumber")
	List<Student> getUsersAllRouteStudents(long routeId);

	List<Student> findByUser(User user);

	@Query("select s from Student s where s.user.uuid = ? and s.isApproved='Y'")
	List<Student> getStudentDetails(String userId);

	@Query("select s from Student s, UserSchool ugs, User u where s.school.id = ugs.school.id and ugs.user.id = u.id and u.role = 'administrator' and u.uuid=? Order by s.wayPoint.route.routeName ,s.firstName, s.lastName ")
	List<Student> getAllStudentDetailsAdmin(String userUuid);
	
	@Query("select s from Student s, UserSchool ugs, User u where s.school.id = ugs.school.id and ugs.user.id = u.id and u.role = 'administrator' and s.isApproved = 'Y' and s.user is null and u.uuid=? Order by s.wayPoint.route.routeName ,s.firstName, s.lastName ")
	List<Student> getStudentUnassignedWithParent(String userUuid);

	@Query("select wp from WayPoint wp where wp.id=?")
	WayPoint findWayPoint(long routeId);
	
	@Query("select s from Student s, RouteFleetDeviceXREF rfdx where s.wayPoint.route.id = rfdx.route.id and rfdx.fleet.id = ? order by s.firstName")
	List<Student> getStudentDetailsByFleet(long fleetId);
	
	@Query("select s from Student s , WayPoint w where s.wayPoint.id=w.id and w.id=? ")
	List<Student> getStudentsByWayPoint(long waypointId);
	
	@Query("select s from Student s where s.school.id = ? ")
	List<Student> getActiveStudentsBySchool(long schoolId);
	
	@Query("select s from Student s ,User u where s.user.id=u.id and u.uuid=? ")
	List<Student> getStudentByUserUUID(String  uuid);
	
	@Query("select s from Student s ,WayPoint w ,Route r where s.wayPoint.id=w.id and w.route.id=r.id and r.uuid=? and s.isApproved='Y' ")
	List<Student> getStudentByRouteId(String routeUUID);
	
	@Query("select s from Student s ,UserSchool us ,User u where us.user.id=u.id and us.school.id=s.school.id and u.uuid=? and s.studentClass=? and s.isApproved='Y' ")
	List<Student> getStudentListByClass(String userId ,String className);
}