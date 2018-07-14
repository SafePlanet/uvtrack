/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */

package com.spi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spi.domain.AlertConfig;

public interface AlertsConfigDAO extends JpaRepository<AlertConfig, Long> {


}
