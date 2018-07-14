/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */

package com.spi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.Fleet;
import com.spi.domain.RouteFleetDeviceXREF;

import java.util.Date;

public interface FleetDAO extends JpaRepository<Fleet, Long> {

	@Query("select f from Fleet f")
	List<Fleet> findAllFleetList();

	@Query("select distinct f from Fleet f, User u, UserDevice ud, RouteFleetDeviceXREF rfdx where u.uuid = ? and u.id = ud.user.id and rfdx.fleet.id = f.id and rfdx.devices.id = ud.devices.id")
	List<Fleet> findFleetListByUser(String userUuid);
	
	@Query("select distinct f from Fleet f, User u, UserDevice ud, RouteFleetDeviceXREF rfdx where u.uuid = ? and u.id = ud.user.id and rfdx.fleet.id = f.id and rfdx.devices.id = ud.devices.id")
	List<Fleet> findFleetListByUserAnd(String userUuid);

	@Query("select f from Fleet f where f.uuid=?")
	Fleet findByUUID(String routeUUId);

	@Query("select f from Fleet f where f.id=?")
	Fleet findFleetById(long id);
	
	@Query("select f from Fleet f where f.id=?")
	List<Fleet> findFleetListById(long id);
	
	@Query("select f from Fleet f where f.routeFleetDeviceXREF.route.id=?")
	Fleet findFleetByRouteId(long id);

	@Query("select distinct f from Fleet f JOIN f.routeFleetDeviceXREF.devices.locations locs where locs.fixtime BETWEEN ? AND ? ")
	List<Fleet> getSearchedFleets(Date fromDate, Date toDate);

	@Query("select distinct f from Fleet f JOIN f.routeFleetDeviceXREF.devices.locations locs where locs.fixtime BETWEEN ? AND ? AND f.id=?")
	List<Fleet> getSearchedFleetsForVehicle(Date fromDate, Date toDate, long vehicleId);
	
	@Query("select f from RouteFleetDeviceXREF f where f.fleet.id=?")
	RouteFleetDeviceXREF findRouteFleetDeviceById(long id);

	@Query("select rxref from RouteFleetDeviceXREF rxref where rxref.route.uuid = ?")
	RouteFleetDeviceXREF findRouteFleetDevicebyRouteUuid(String routeUuid);
	
	@Query("select f from Fleet f where f  in (select fleet from RouteFleetDeviceXREF where route.id=null )")
    List<Fleet> findAllFleetListNotMapped();
	
	@Query("select distinct f from Fleet f, User u, UserDevice ud, RouteFleetDeviceXREF rfdx where u.uuid = ? and u.id = ud.user.id and rfdx.fleet.id = f.id and rfdx.devices.id = ud.devices.id and  f  in (select fleet from RouteFleetDeviceXREF where route.id=null )")
    List<Fleet> findAllFleetListNotMapped(String userId);
	@Query("select distinct rfdx from RouteFleetDeviceXREF rfdx, User u, UserDevice ud,Fleet f  where u.uuid = ? and u.id = ud.user.id and rfdx.fleet.id = f.id and rfdx.devices.id = ud.devices.id")
	List<RouteFleetDeviceXREF> findFleetListByUserUUID(String userUuid);
}
