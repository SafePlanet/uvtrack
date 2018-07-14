/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.Geofence;

public interface GeofenceDAO extends JpaRepository<Geofence, Long> {
    
	@Query("select g from Geofence g, DeviceGeofence df, RouteFleetDeviceXREF rfdx where g.id = df.geofenceid and df.deviceid = rfdx.devices.id and g.type = 'RBon' and rfdx.fleet.id = ?")
	public Geofence getRouteGeoFenceByFleet(long vehicleId);
}
