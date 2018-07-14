/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */

package com.spi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.Location;

public interface LocationDao extends JpaRepository<Location, Long> {

	@Query("select l from Location l where l.devices.id = ?")
	Location findFleetLocation(long deviceID);
	
	@Query("select l from Location l where l.id = ?")
	Location findByPrimaryId(long id);

}
