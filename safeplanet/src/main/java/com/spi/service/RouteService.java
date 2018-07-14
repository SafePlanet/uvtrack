/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.service;

import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.core.SecurityContext;

import org.springframework.transaction.annotation.Transactional;

import com.spi.VM.DeviceVM;
import com.spi.VM.FleetVM;
import com.spi.VM.GeofenceVM;
import com.spi.VM.RouteVM;
import com.spi.VM.StudentVM;
import com.spi.VM.WayPointVM;
import com.spi.domain.DeviceModelType;
import com.spi.domain.Devices;
import com.spi.domain.Fleet;
import com.spi.domain.Location;
import com.spi.domain.Route;
import com.spi.domain.RouteFleetDeviceXREF;
import com.spi.domain.School;
import com.spi.domain.WayPoint;
import com.spi.model.RoutePath;
import com.spi.user.api.DeviceModel;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.FleetModel;
import com.spi.user.api.FleetXRefModel;
import com.spi.user.api.RouteModel;
import com.spi.user.api.RouteScheduleModel;
import com.spi.user.api.SchoolModel;
import com.spi.user.api.SendSmsModel;
import com.spi.user.api.WayPointModel;

@Transactional
public interface RouteService {

	public Route getRouteDetails(ExternalUser requestingUser, String studentId);

	public RouteModel getRouteDetails(String routeId);

//	public List<Route> getAllRouteDetailsForStudents(ExternalUser requestingUser);

	public Devices getCurrentDeviceLocation(long routeId);

	public List<Devices> getCurrentDeviceLocationForRoutes(List<Long> routeId);
	
	public Devices findDeviceByFleetId(long fleetId);
	
	public Fleet findFleetByRouteId(long routeId);

	public List<Route> findAllRouteList(String userId);

	public List<Route> findRoutesBySchoolId(Long schoolId);
	
	public List<Route> findAllRouteListByUserId(String userId);

	public List<School> findAllSchoolList();

	public void addRoute(String routeName, String description, String geometry, String userUuid, String routeUuid ,Fleet fleet);

	public List<WayPoint> findAllWayPointList(String routeId);

	public List<Fleet> findAllFleetList(String userUuid);

	public Fleet findFleetById(long fleetId);

	public Devices findByDeviceId(long deviceId);
	
	public List<Route> getAllRouteDetails(String userId);

	public DeviceModel findByDeviceIdModel(long deviceId);

	public Route findByRouteId(long routeId);

	public RouteFleetDeviceXREF findByRFDXrefId(long xRefId);

	public FleetModel findFleetModelById(long fleetId);

	public void addUpdateFleet(Fleet fleet ,Devices device);

	public void addUpdateDevice(Devices device,String uuid, Long schoolId);

	public void addUpdateRouteFleetDeviceXref(RouteFleetDeviceXREF xRef ,Fleet fleet ,Route route);

	public List<Devices> findAllDeviceList(String userUuid);

	public List<Devices> findAllDeviceListNotMapped();

	public FleetXRefModel getBusRouteDetails(String xRefID);

	public List<WayPointModel> findAllWayPointListforRoute(String routeUUID);

	public void addWayPoint(String name, String lattitude, String longitude, String sequenceNumber, String altitude, String wayPointUUID,
			String routeUUID, String picktime, String droptime);

	public WayPointModel getWayPointDetails(String wayPointId);
	public WayPointModel getWayPointDetails(long wayPointId);
	public List<Devices> getCurrentDeviceLocations(String userId);
	
	public String getGMapMatrixResponse(HttpsURLConnection con);
	
	public List<RoutePath> getDeviceCurrentLocation(SecurityContext sc, String userId);
	
	public List<RoutePath> getAllRoute(SecurityContext sc, String userId);
	
	public int sendMessage(SendSmsModel model, String userId);
	
	public int sendMessageForStudent(SendSmsModel model, long studentId);
	
	public List getDeviceCurrentLocationForAdmin(SecurityContext sc, String userId);

	public Map<String, DeviceModelType> getAllDeviceModelType();
	
	public List<DeviceModel> getAllDevices(String userId);

	public List<WayPoint> getStudentCurrentLocation(SecurityContext sc,
			String userId);

	public Location getBusCurrentLocation(String userId, String vehicleId);
	
	public List<RouteVM> getScheduleByRoute(String uuid);
	
	public List<RouteVM> getScheduleByFleet(long fleetId);
	
	public DeviceVM getSpeedByFleet(long fleetId);
	
	public List<FleetVM> findAllFleetListNotMapped();
	
	public List<FleetVM> findAllFleetListNotMapped(String userId);
	
	public List<WayPointVM> findAllWayPointListforRoute(long routeId);
	
	public List<WayPointVM> getScheduleByWayPoint(long waypointId);
	
	public List<RouteFleetDeviceXREF> findAllFleetListByUser(String userUuid);
	
	public GeofenceVM getGeofenceByVehicle(long vehicleId);

	public List<RouteScheduleModel> getRouteScheduleDetails();
	public List<SchoolModel> getSchoolList();
	
	public void addRouteSchedule(RouteScheduleModel request);
	
	public List<StudentVM> getStudentByRoute(String routeUUID);
}