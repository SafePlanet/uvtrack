/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */

package com.spi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.DeviceModelType;

public interface DeviceModelTypeDAO extends JpaRepository<DeviceModelType, Long> {

	@Query("select dmt from DeviceModelType dmt")
	List<DeviceModelType> findAllDeviceModelTypeList();
}
