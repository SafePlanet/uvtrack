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
import org.springframework.data.repository.query.Param;

import com.spi.domain.Devices;

public interface DeviceDAO extends JpaRepository<Devices, Long> {

	@Query("select d from Devices d, UserDevice ud, User u where d.id = ud.devices.id and ud.user.id = u.id and u.uuid = ? and d.voidInd='n' order by d.name")
	List<Devices> findAllDeviceList(String userUuid);

	@Query("select d from Devices d where d.uuid=? and  d.voidInd='n' ")
	Devices findByUUID(String deviceUUId);

	@Query("select d from Devices d where d.id=? and  d.voidInd='n' ")
	Devices findById(long id);

	@Query("select d from Devices d where d not in (select devices from RouteFleetDeviceXREF)")
	List<Devices> findAllDeviceListNotMapped();
	
	@Query("select d from Devices d, UserDevice ud, User u where d.id = ud.devices.id and ud.user.id = u.id and u.uuid = ? and  d not in (select devices from RouteFleetDeviceXREF)")
	List<Devices> findAllDeviceListNotMapped(String userUuid);

}
