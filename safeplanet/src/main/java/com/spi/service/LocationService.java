/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.service;

import com.spi.domain.Fleet;
import com.spi.domain.Location;
import com.spi.user.api.FleetModel;
import java.util.Date;
import java.util.List;

public interface LocationService {

	public Location getFleetLocation(long routeId);
        
	public List<Fleet> getAllFleets();
        
	public List<Fleet> getSearchedFleets(Date fromDate, Date toDate);
        
	public List<Fleet> getSearchedFleetsForVehicle(Date fromDate, Date toDate, long vehicleId);
        
        public List<FleetModel> findAllFleetsList();

}
