/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.user.api;

import com.spi.domain.Route;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteModel {
    
    String uuid;
    String school;
    String routeName;
    String description;
    String geometry;
    Long routeId;

    public RouteModel() {
    }

    public RouteModel(String uuid, String schoolId, String routeName, String routeDescription, String geometry, Long routeId) {
        this.uuid = uuid;
        this.school = schoolId;
        this.routeName = routeName;
        this.description = routeDescription;
        this.geometry = geometry;
        this.routeId =routeId;
    }
    
    public RouteModel(Route route) {
        this.uuid = route.getUuid().toString();
        this.school = route.getSchool().getId().toString();
        this.routeName = route.getRouteName();
        this.description = route.getRouteDescription();
        this.geometry = route.getGeometry();
        this.routeId =route.getId();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

	public Long getRouteId() {
		return routeId;
	}

	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}
    
    
    
}
