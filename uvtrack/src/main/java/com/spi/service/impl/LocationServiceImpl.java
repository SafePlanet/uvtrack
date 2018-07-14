/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.service.impl;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spi.config.ApplicationConfig;
import com.spi.dao.FleetDAO;
import com.spi.dao.LocationDao;
import com.spi.dao.RouteDAO;
import com.spi.domain.Devices;
import com.spi.domain.Fleet;
import com.spi.domain.Location;
import com.spi.service.BaseService;
import com.spi.service.LocationService;
import com.spi.user.api.FleetModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("locationService")
public class LocationServiceImpl extends BaseService implements LocationService {

	LocationDao locationDao;
        
	FleetDAO fleetDAO;

	RouteDAO routeDao;

	private ApplicationConfig applicationConfig;

	@Autowired
	public void setLocationRepository(LocationDao locationRepository) {
		this.locationDao = locationRepository;
	}

	@Autowired
	public void setRouteRepository(RouteDAO routeDao) {
		this.routeDao = routeDao;
	}
        
	@Autowired
	public void setFleetRepository(FleetDAO fleetDAO) {
		this.fleetDAO = fleetDAO;
	}

	public LocationServiceImpl(Validator validator) {
		super(validator);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	public LocationServiceImpl(Validator validator,
			ApplicationConfig applicationConfig) {
		this(validator);
		this.applicationConfig = applicationConfig;
	}

	public Location getFleetLocation(long routeId) {
		Devices device = routeDao.findDeviceforRoute(routeId);
		Location location = locationDao.findFleetLocation(device.getId());
		return location;
	}
        
        public List<Fleet> getAllFleets(){
            
            return fleetDAO.findAllFleetList();
            
        }
        
        public List<Fleet> getSearchedFleets(Date fromDate, Date toDate) {
            
            return fleetDAO.getSearchedFleets(fromDate, toDate);
            
        }
        public List<Fleet> getSearchedFleetsForVehicle(Date fromDate, Date toDate, long vehicleId) {
            
            return fleetDAO.getSearchedFleetsForVehicle(fromDate, toDate, vehicleId);
            
        }
        
        @Override
        public List<FleetModel> findAllFleetsList()
        {
            List<Fleet> fleetList = fleetDAO.findAllFleetList();
            
            List<FleetModel> fleetModelList = new ArrayList<FleetModel>();
            
            for(Fleet fleet : fleetList)
            {
                fleetModelList.add(new FleetModel(fleet));
            }
            
            return fleetModelList;
        }

}
