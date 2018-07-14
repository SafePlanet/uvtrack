/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */

package com.spi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.RouteFleetDeviceXREF;

public interface FleetXrefDAO extends JpaRepository<RouteFleetDeviceXREF, Long> {

	@Query("select xref from RouteFleetDeviceXREF xref where xref.fleet.id=?")
	RouteFleetDeviceXREF findByFleetID(long xRefID);

	@Query("select xref from RouteFleetDeviceXREF xref where xref.id=?")
	RouteFleetDeviceXREF findById(long xRefID);

	@Query("select xref from RouteFleetDeviceXREF xref")
	List<RouteFleetDeviceXREF> findAllXref();

}
