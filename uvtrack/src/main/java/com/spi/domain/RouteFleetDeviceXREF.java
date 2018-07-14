/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.spi.model.BaseEntity;

@Entity
@Table(name = "route_fleet_device_xref")
public class RouteFleetDeviceXREF extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3605349146050887168L;

	@OneToOne
	@JoinColumn(name = "route_id")
	private Route route = null;

	@OneToOne
	@JoinColumn(name = "fleet_id")
	private Fleet fleet = null;

	@OneToOne
	@JoinColumn(name = "device_id")
	private Devices devices = null;

        public Route getRoute() {
            return route;
        }

        public void setRoute(Route route) {
            this.route = route;
        }

        public Fleet getFleet() {
            return fleet;
        }

        public void setFleet(Fleet fleet) {
            this.fleet = fleet;
        }

        public Devices getDevice() {
            return devices;
        }

        public void setDevice(Devices device) {
            this.devices = device;
        }



}
