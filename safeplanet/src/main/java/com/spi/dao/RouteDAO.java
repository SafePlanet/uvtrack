/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */

package com.spi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spi.domain.Devices;
import com.spi.domain.Location;
import com.spi.domain.Route;
import com.spi.domain.RouteScheduleDetails;
import com.spi.domain.School;
import com.spi.domain.Student;
import com.spi.domain.WayPoint;

public interface RouteDAO extends JpaRepository<Route, Long> {

	@Query("select d from Devices d, RouteFleetDeviceXREF rfdxref where rfdxref.route.id = ? and rfdxref.devices.id = d.id")
	Devices findDeviceforRoute(long routeId);

	@Query("select d from Devices d, RouteFleetDeviceXREF rfdxref where rfdxref.route.id in :routeIdList and rfdxref.devices.id = d.id")
	List<Devices> findAllDeviceforRouteList(
			@Param("routeIdList") List<Long> routeIdList);

	@Query("select distinct r from Route r, User u, UserSchool ugs "
			+ "where ugs.user.id = u.id and ugs.school.id = r.school.id "
			+ "and u.uuid = ? and r.voidInd = 0 order by r.routeName")
	List<Route> findAllRouteList(String userId);

	@Query("select distinct r from Route r "
			+ "where r.school.id = ?"
			+ " and r.voidInd = 0 order by r.routeName")
	List<Route> findRoutesBySchoolId(Long schoolId);
	
	@Query("select r from Route r where r not in (select route from RouteFleetDeviceXREF)")
	List<Route> findAllRouteListNotMapped();

	@Query("select r from Route r where r.uuid=?")
	Route findByUUID(String routeUUId);

	/*
	 * @Query("select r from Route r where r.id=?") Route findByUUID(Long
	 * routeUUId);
	 */

	@Query("select r from Route r where r.id=?")
	Route findRouteByPK(long id);

	@Query("select sc from School sc")
	List<School> findAllSchoolList();

	@Query("select sc from School sc where sc.id=?")
	School findSchoolById(Long id);

	@Query("select wp from WayPoint wp where wp.route.id=?")
	List<WayPoint> findAllWayPointList(long routeId);

	@Query("select distinct s from Route r, Student s where r.id = s.wayPoint.route.id and "
			+ "r.uuid = ? and s.isApproved = 'Y'")
	List<Student> findStudentsForRoute(String routeUuid);

	@Query("select r from Route r ")
	List<Route> getAllRouteDetails();
	
	@Query("select l from Location l where l.devices.id=?  order by servertime asc limit 1")
	List<Location> getPostionByDeviceId(long fleetId);
	
	@Query("select rsch from RouteScheduleDetails rsch, RouteFleetDeviceXREF rfdx "
			+ "where rsch.route.id=rfdx.route.id and rfdx.fleet.id = ?")
	List<RouteScheduleDetails> findrouteTime(long fleetId);

	@Query("select rsch from RouteScheduleDetails rsch where rsch.id= ?")
	RouteScheduleDetails findRouteScheduleDetailByPrimaryKey(long primaryId);
	
	@Query("select distinct s from Route r, Student s ,RouteFleetDeviceXREF rfdx "+" where r.id = s.wayPoint.route.id and r.id = rfdx.route.id and rfdx.fleet.id=? and s.isApproved = 'Y'")
	List<Student> findStudentsForVehicle(long fleetId);

}
