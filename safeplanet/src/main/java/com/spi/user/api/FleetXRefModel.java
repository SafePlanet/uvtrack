/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import com.spi.domain.RouteFleetDeviceXREF;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class FleetXRefModel {
    
    String uuid;
    String deviceID;
    String deviceName;
    String routeID;
    String routeName;
    String fleetID;
    long id;
 

    public FleetXRefModel() {
    }

    public FleetXRefModel(String uuid, String deviceID, String routeID, String fleetID,long id, String deviceName, String routeName) {
        this.uuid = uuid;
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.routeID = routeID;
        this.routeName = routeName;
        this.fleetID = fleetID;
       this.id=id;
    }

    public FleetXRefModel(RouteFleetDeviceXREF fleetXref) {
        this.uuid = fleetXref.getIdentifier().toString();
        this.deviceID = fleetXref.getDevice().getId().toString();
        this.deviceName = fleetXref.getDevice().getName();
        this.routeID = fleetXref.getRoute().getId().toString();
        this.routeName = fleetXref.getRoute().getRouteName();
        this.fleetID = fleetXref.getFleet().getId().toString();
        this.id=fleetXref.getId();
       
    }
    
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getRouteID() {
		return routeID;
	}

	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}

	public String getFleetID() {
		return fleetID;
	}

	public void setFleetID(String fleetID) {
		this.fleetID = fleetID;
	}

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getRouteName() {
            return routeName;
        }

        public void setRouteName(String routeName) {
            this.routeName = routeName;
        }
    
        
}
