package com.spi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.RouteFleetDeviceXREF;
import com.spi.domain.UserDevice;
import com.spi.service.impl.RouteServiceImpl;;

public interface RouteFleetDeviceXrefDAO extends JpaRepository<RouteFleetDeviceXREF, Long> {

	
	
	@Query("select sc from RouteFleetDeviceXREF sc where sc.fleet.id = ? ")
	RouteFleetDeviceXREF findFleetConfiguration(long fleetId);
	
	@Query("select ud from RouteFleetDeviceXREF ud where ud.fleet.id=? and ud.devices.id=?")
	RouteFleetDeviceXREF findByDeviceIdAndFleetId(long fleetId,long deviceId);
	
	@Query("select sc from RouteFleetDeviceXREF sc where sc.route.id = ? ")
	RouteFleetDeviceXREF findFleetByRouteConfiguration(long routeId);
	
	@Query("select sc from RouteFleetDeviceXREF sc where sc.fleet.id = ? ")
	RouteFleetDeviceXREF findDeviceByFleetConfiguration(long fleetId);
    
}

