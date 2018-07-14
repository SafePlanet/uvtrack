/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.stereotype.Component;

import com.spi.VM.DeviceVM;
import com.spi.VM.FleetVM;
import com.spi.VM.GeofenceVM;
import com.spi.VM.RouteVM;
import com.spi.VM.StudentVM;
import com.spi.VM.WayPointVM;
import com.spi.config.ApplicationConfig;
import com.spi.domain.Fleet;
import com.spi.domain.Location;
import com.spi.domain.LocationModel;
import com.spi.domain.Route;
import com.spi.domain.School;
import com.spi.domain.WayPoint;
import com.spi.gateway.EmailServicesGateway;
import com.spi.model.FileRead;
import com.spi.model.RoutePath;
import com.spi.model.UrlStatus;
import com.spi.service.RouteService;
import com.spi.service.UserService;
import com.spi.service.VerificationTokenService;
import com.spi.service.impl.RouteServiceImpl;
import com.spi.user.api.DeviceModel;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.FleetModel;
import com.spi.user.api.FleetXRefModel;
import com.spi.user.api.RouteModel;
import com.spi.user.api.RouteScheduleModel;
import com.spi.user.api.SchoolModel;
import com.spi.user.api.SendSmsModel;
import com.spi.user.api.WayPointModel;
import com.sun.jersey.api.view.Viewable;

import io.swagger.annotations.Api;

@Path("/route")
@Component
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
@Api
public class RouteResource {

	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	protected RouteService routeService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected VerificationTokenService verificationTokenService;

	@Autowired
	protected EmailServicesGateway emailServicesGateway;

	@Context
	protected UriInfo uriInfo;

	@Autowired
	ApplicationConfig config;

	Logger LOG = LoggerFactory.getLogger(RouteResource.class);
	
	public RouteResource(){}

	@Autowired
	public RouteResource(ConnectionFactoryLocator connectionFactoryLocator) {
		this.connectionFactoryLocator = connectionFactoryLocator;
	}

	// @RolesAllowed({ "authenticated" })
	@RolesAllowed({ "authenticated", "teacher", "administrator", "superAdmin" })
	@POST
	@Path("getroute/{userId}")
	public Response getRouteDetails(@Context SecurityContext sc, String studentId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		Route route = routeService.getRouteDetails(userMakingRequest, studentId);
		return Response.ok().entity(route).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" ,"superAdmin"})
	@GET
	@Path("getUser/{loginId}/{userId}")
	public Response getUser(@Context SecurityContext sc, @PathParam("loginId") String loginId, @PathParam("userId") String userId) {

		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		ExternalUser user = userService.getUser(userMakingRequest, userId);
		return Response.ok().entity(user).build();
	}
	
	@RolesAllowed({ "authenticated", "teacher" })
	@GET
	@Path("getAllRoute/{userId}")
	public Response getAllRoute(@Context SecurityContext sc, @PathParam("userId") String userId, String studentId) {
		List<RoutePath> routeDetailsList = routeService.getAllRoute(sc, userId);

		return Response.ok(new Viewable("/route", routeDetailsList)).build();
	}

	@RolesAllowed({ "authenticated", "teacher", "administrator", "superAdmin" })
	@GET
	@Path("getDeviceCurrentLocation/{userId}")
	public Response getDeviceCurrentLocation(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<RoutePath> routeDetailsList = routeService.getDeviceCurrentLocation(sc, userId);

		return Response.ok(new Viewable("/currentLocation", routeDetailsList)).build();
	}

	@RolesAllowed({ "administrator", "superAdmin", "superAdmin" })
	@GET
	@Path("getModifyRouteDetail/{userId}/{routeId}")
	public Response getModifyRouteDetail(@Context SecurityContext sc, @PathParam("userId") String userId,
			@PathParam("routeId") String routeId) {

		RouteModel route = routeService.getRouteDetails(routeId);
		return Response.ok().entity(route).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@GET
	@Path("getModifyWayPointDetail/{userId}/{wayPointId}")
	public Response getModifyWayPointDetail(@Context SecurityContext sc, @PathParam("userId") String userId,
			@PathParam("wayPointId") long wayPointId) {

		WayPointModel wayPoint = routeService.getWayPointDetails(wayPointId);
		return Response.ok().entity(wayPoint).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@GET
	@Path("getRouteWayPoints/{userId}/{routeId}")
	public Response getRouteWayPoints(@Context SecurityContext sc, @PathParam("userId") String userId,
			@PathParam("routeId") String routeId) {

		List<WayPoint> wayPoints = routeService.findAllWayPointList(routeId);

		HashMap wayPointMap = new HashMap();

		for (WayPoint wp : wayPoints) {
			wayPointMap.put(wp.getId(), wp.getName());
		}

		return Response.ok().entity(wayPointMap).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@GET
	@Path("getModifyBusRouteDetail/{userId}/{xRef}")
	public Response getModifyBusRouteDetail(@Context SecurityContext sc, @PathParam("userId") String userId,
			@PathParam("xRef") String xRefId) {

		FleetXRefModel xRef = routeService.getBusRouteDetails(xRefId);

		return Response.ok().entity(xRef).build();
	}

	@RolesAllowed({ "administrator", "superAdmin" ,"transporter" })
	@GET
	@Path("getFleetDetail/{userId}/{fleetID}")
	public Response getFleetDetail(@Context SecurityContext sc, @PathParam("userId") String userId,
			@PathParam("fleetID") String fleetID) {

		FleetModel fleetModel = routeService.findFleetModelById(Long.valueOf(fleetID));

		return Response.ok().entity(fleetModel).build();
	}

	@RolesAllowed({ "administrator", "superAdmin","superAdmin" })
	@GET
	@Path("getDeviceDetail/{userId}/{deviceId}")
	public Response getDeviceDetail(@Context SecurityContext sc, @PathParam("userId") String userId,
			@PathParam("deviceId") String deviceId) {

		DeviceModel deviceModel = routeService.findByDeviceIdModel(Long.valueOf(deviceId));

		return Response.ok().entity(deviceModel).build();
	}
	
	@RolesAllowed({ "administrator", "superAdmin" , "transporter"})
	@GET
	@Path("findAllRouteList/{userId}")
	public Response findAllRouteList(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<Route> routeList = routeService.findAllRouteList(userId);
		List<RouteModel> routeModels = new ArrayList<RouteModel>();
		for(Route route : routeList){
			routeModels.add(new RouteModel(route));
		}
		return Response.ok().entity(routeModels).build();
	}
	
	@RolesAllowed({ "administrator", "superAdmin" ,"teacher", "transporter"})
	@GET
	@Path("findAllRoutesBySchoolId")
	public Response findAllRoutesBySchoolId(@Context SecurityContext sc) {
		ExternalUser currentUser = (ExternalUser) sc.getUserPrincipal();
		List<School> schools = userService.getSchoolDetails(currentUser.getId());
		Long schoolId = 0L;
		if(schools!=null && schools.size()>0) {
			schoolId= schools.get(0).getId();
		}
		List<Route> routeList = routeService.findRoutesBySchoolId(schoolId);
		List<RouteModel> routeModels = new ArrayList<RouteModel>();
		for(Route route : routeList){
			routeModels.add(new RouteModel(route));
		}
		return Response.ok().entity(routeModels).build();
	}
	
	@RolesAllowed({ "administrator", "superAdmin" })
	@PUT
	@Path("sendMessage/{userId}")
	public Response sendMessage(@Context SecurityContext sc, @PathParam("userId") String userId, SendSmsModel model) {
		int count=0;
		System.out.println(model.getStudentId());
		System.out.println(model.isSendToAll());
		if(model.getStudentId()==1 || model.isSendToAll()==true){
			System.out.println("true");
		 count = routeService.sendMessage(model, userId);
	}else{
		count=routeService.sendMessageForStudent(model, model.getStudentId());
	}
		return Response.ok().entity(count).build();}
	
	@RolesAllowed({"administrator", "superAdmin" })
	@GET
	@Path("getAdminDeviceCurrentLocation/{userId}")
	public Response getAdminDeviceCurrentLocation(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<RoutePath> routeDetailsList = routeService.getDeviceCurrentLocationForAdmin(sc, userId);

		return Response.ok(new Viewable("/currentLocation", routeDetailsList)).build();
	}
	
	@RolesAllowed({ "administrator", "superAdmin" , "transporter","superAdmin"})
	@GET
	@Path("findAllFleetList/{userId}")
	public Response findAllFleetList(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<Fleet> fleetList = routeService.findAllFleetList(userId);
		List<FleetModel> fleetModels = new ArrayList<FleetModel>();
		for(Fleet fleet : fleetList){
			fleetModels.add(new FleetModel(fleet));
		}
		return Response.ok().entity(fleetModels).build();
	}

	@RolesAllowed({"administrator", "superAdmin","authenticated" })
	@GET
	@Path("getAllDevices/{userId}")
	public Response getAllDevices(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<DeviceModel> devicesModel = routeService.getAllDevices(userId);
		return Response.ok().entity(devicesModel).build();
	}
	
	////=========================Student_status_notification================================//
	/*@RolesAllowed({"administrator", "superAdmin","authenticated" })
	@GET
	@Path("getStudentNotification")
	public Response getStudentNotification() {
		List<StudentNotificationStatus> students = userService.getStudentNotification();
		List<StudentNotificationStatusVM> studentModel = new ArrayList<StudentNotificationStatusVM>();
		
		for (StudentNotificationStatus studentNotificationStatus : students) {
			StudentNotificationStatusVM model = new StudentNotificationStatusVM();
			model.setStudentId(studentNotificationStatus.getStudent().getId());
	        model.setMessage(studentNotificationStatus.getMessage());
	        model.setTime(studentNotificationStatus.getMessageTime());
			model.setName(studentNotificationStatus.getStudent().getFirstName()+" "+studentNotificationStatus.getStudent().getLastName());
			studentModel.add(model);
		}
		
		return Response.ok().entity(studentModel).build();
	}*/

	

	
	@RolesAllowed({"superAdmin" })
	@GET
	@Path("readLogFile")
	public Response readLogFile(String file) {
		FileRead f= new FileRead();
		f.setFileData(userService.readLogFile(file));
	   return Response.ok(f).build();
	}
	
	@RolesAllowed({"administrator", "superAdmin","authenticated" })
	@GET
	@Path("urlStatus")
	public Response urlStatus(String status) {
		UrlStatus urlStatus= new UrlStatus();
		urlStatus.setStatus(userService.urlStatus());
	   return Response.ok(urlStatus).build();
	}
	
	
	@RolesAllowed({"administrator", "superAdmin","authenticated" })
	@GET
	@Path("startedService")
	public Response startedService() {
		//StartedService service= new StartedService();
		
	   return Response.ok().build();
	}
	
	//=================================End============================================//
	@RolesAllowed({ "authenticated", "teacher", "administrator", "superAdmin" })
	@GET
	@Path("getStudentCurrentLocation/{userId}")
	public Response getStudentCurrentLocation(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<WayPoint> routeDetailsList = routeService.getStudentCurrentLocation(sc, userId);

		return Response.ok(routeDetailsList).build();
	}
	
	@RolesAllowed({"administrator"})
	@GET
	@Path("getBusCurrentLocation/{userId}/{vehicleId}")
	public Response getBusCurrentLocation(@Context SecurityContext sc, @PathParam("userId") String userId,
			@PathParam("vehicleId") String vehicleId) {
		
		Location location = routeService.getBusCurrentLocation(userId, vehicleId);

		List<LocationModel> locModels = new ArrayList<LocationModel>();
		if(location != null){
			locModels.add(new LocationModel(location));
		}
		
		return Response.ok().entity(locModels).build();
	}
	
	@RolesAllowed({ "administrator", "teacher","superAdmin" })
	@Path("getScheduleByFleet/{userId}/{fleetId}")
	@GET
	public Response getScheduleByFleet(@Context SecurityContext sc, @PathParam("userId") String userId, @PathParam("fleetId") long fleetId) {
		List<RouteVM> routeList = routeService.getScheduleByFleet(fleetId);
		return Response.ok().entity(routeList).build();
	}
	@RolesAllowed({ "administrator", "teacher","superAdmin" })
	@Path("getSpeedByFleet/{userId}/{fleetId}")
	@GET
	public Response getSpeedByFleet(@Context SecurityContext sc, @PathParam("userId") String userId, @PathParam("fleetId") long fleetId) {
		DeviceVM deviceVM = routeService.getSpeedByFleet(fleetId);
		
		return Response.ok().entity(deviceVM).build();
	}
	
	@RolesAllowed({ "administrator", "teacher" })
	@Path("getScheduleByRoute/{userId}/{routeId}")
	@GET
	public Response getScheduleByRoute(@Context SecurityContext sc, @PathParam("userId") String userId, @PathParam("routeId") String uuid) {
		
		List<RouteVM> routeList = routeService.getScheduleByRoute(uuid);
		return Response.ok().entity(routeList).build();
	}
	@RolesAllowed({ "administrator", "superAdmin" })
	@GET
	@Path("getUnassignedFleet/{userId}")
	public Response getUnassignedFleet(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<FleetVM> fleetVM= new ArrayList<FleetVM>();
		String userRole = userService.getUserRole(userId).name();
		if(userRole.equalsIgnoreCase("superAdmin")){
		 fleetVM = routeService.findAllFleetListNotMapped();
		
	} else if(userRole.equalsIgnoreCase("administrator")){
		fleetVM = routeService.findAllFleetListNotMapped(userId);
	} else{
		fleetVM = routeService.findAllFleetListNotMapped();
	}
		return Response.ok().entity(fleetVM).build();
	}
	
	@RolesAllowed({ "administrator", "superAdmin" })
	@GET
	@Path("findAllWayPointListforRoute/{userId}/{routeId}")
	public Response findAllWayPointListforRoute(@Context SecurityContext sc, @PathParam("userId") String userId, @PathParam("routeId") long routeId) {
		
	List<WayPointVM> wp  = routeService.findAllWayPointListforRoute(routeId);
	return Response.ok().entity(wp).build();
	
	}
	
	@RolesAllowed({ "administrator", "superAdmin" })
	@GET
	@Path("findAllWayPointsByRouteUUID/{routeId}")
	public Response findAllWayPointsByRouteUUID(@Context SecurityContext sc, @PathParam("routeId") String routeId) {
		
	List<WayPointModel> wp  = routeService.findAllWayPointListforRoute(routeId);
	return Response.ok().entity(wp).build();
	
	}
	
	@RolesAllowed({ "administrator", "teacher" })
	@Path("getScheduleByWayPoint/{userId}/{id}")
	@GET
	public Response getScheduleByWayPoint(@Context SecurityContext sc, @PathParam("userId") String userId, @PathParam("id") long waypointId) {
		
		List<WayPointVM> wayPointDetails = routeService.getScheduleByWayPoint(waypointId);
		return Response.ok().entity(wayPointDetails).build();
	}
	
	@RolesAllowed({ "administrator" })
	@Path("getGeofenceByVehicle/{vehicleId}")
	@GET
	public Response getGeofenceByVehicle(@Context SecurityContext sc, @PathParam("vehicleId") long vehicleId) {
		GeofenceVM geofence = routeService.getGeofenceByVehicle(vehicleId);
		return Response.ok().entity(geofence).build();
	}
	@RolesAllowed({ "superAdmin" })
	@Path("getRouteScheduleDetails/{vehicleId}")
	@GET
	public Response getRouteScheduleDetails(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<RouteScheduleModel> rsm=routeService.getRouteScheduleDetails();
		return Response.ok().entity( rsm).build();
	}
	@RolesAllowed({ "superAdmin" })
	@Path("getSchoolList/{userId}")
	@GET
	public Response getSchoolList(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<SchoolModel> rsm=routeService.getSchoolList();
		return Response.ok().entity( rsm).build();
	}
	@RolesAllowed({ "superAdmin" })
	@Path("getRouteBySchool/{schoolId}")
	@GET
	public Response getRouteBySchool(@Context SecurityContext sc, @PathParam("schoolId") long schoolId) {
		List<Route> rsm=routeService.findRoutesBySchoolId(schoolId);
		List<RouteModel> rm= new ArrayList<>();
		for(Route r :rsm){
			RouteModel rmodel=new RouteModel(r);
			rm.add(rmodel);
		}
		
		return Response.ok().entity( rm).build();
	}
	@RolesAllowed({ "superAdmin" })
	@Path("addRouteSchedule/{userId}")
	@PUT
	public Response addRouteSchedule(@Context SecurityContext sc, @PathParam("userId") String userId ,RouteScheduleModel request) {
		
		routeService.addRouteSchedule(request);
		
		return Response.ok().build();
	}
	@RolesAllowed({ "administrator"})
	@Path("getStudentByRoute/{userId}/{routeId}")
	@GET
	public Response getStudentByRoute(@Context SecurityContext sc, @PathParam("userId") String userId ,@PathParam("routeId") String routeUUID ) {
		
	List<StudentVM> student=routeService.getStudentByRoute(routeUUID);
		
		return Response.ok().entity(student).build();
	}
}