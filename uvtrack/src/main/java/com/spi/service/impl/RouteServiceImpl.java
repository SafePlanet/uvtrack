/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.net.ssl.HttpsURLConnection;
import javax.validation.Validator;
import javax.ws.rs.core.SecurityContext;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spi.VM.DeviceVM;
import com.spi.VM.FleetVM;
import com.spi.VM.GeofenceVM;
import com.spi.VM.RouteVM;
import com.spi.VM.StudentVM;
import com.spi.VM.WayPointVM;
import com.spi.config.ApplicationConfig;
import com.spi.dao.AlertsDAO;
import com.spi.dao.DeviceDAO;
import com.spi.dao.DeviceModelTypeDAO;
import com.spi.dao.FleetDAO;
import com.spi.dao.FleetXrefDAO;
import com.spi.dao.GeofenceDAO;
import com.spi.dao.LocationDao;
import com.spi.dao.ReportDAO;
import com.spi.dao.RouteDAO;
import com.spi.dao.RouteFleetDeviceXrefDAO;
import com.spi.dao.RouteScheduleDAO;
import com.spi.dao.SchoolConfigDAO;
import com.spi.dao.SchoolDAO;
import com.spi.dao.StudentsDAO;
import com.spi.dao.UserDAO;
import com.spi.dao.UserDeviceDAO;
import com.spi.dao.UserMessageDAO;
import com.spi.dao.UserSchoolDAO;
import com.spi.dao.WayPointDAO;
import com.spi.domain.Alert;
import com.spi.domain.DeviceModelType;
import com.spi.domain.Devices;
import com.spi.domain.Fleet;
import com.spi.domain.Location;
import com.spi.domain.Route;
import com.spi.domain.RouteFleetDeviceXREF;
import com.spi.domain.RouteScheduleDetails;
import com.spi.domain.School;
import com.spi.domain.Student;
import com.spi.domain.User;
import com.spi.domain.UserDevice;
import com.spi.domain.UserGroupSchool;
import com.spi.domain.UserMessage;
import com.spi.domain.UserSchool;
import com.spi.domain.WayPoint;
import com.spi.model.RoutePath;
import com.spi.service.BaseService;
import com.spi.service.CommonService;
import com.spi.service.RouteService;
import com.spi.service.UserService;
import com.spi.user.api.DeviceModel;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.FleetModel;
import com.spi.user.api.FleetXRefModel;
import com.spi.user.api.RouteModel;
import com.spi.user.api.RouteScheduleModel;
import com.spi.user.api.SchoolModel;
import com.spi.user.api.SendSmsModel;
import com.spi.user.api.WayPointModel;
import com.spi.util.DateUtil;
import com.spi.util.DistanceUtils;
import com.spi.util.FcmMessageService;
import com.spi.util.SmsPrimaryService;
import com.spi.util.StringUtil;

@Service("resourceService")
@Transactional
public class RouteServiceImpl extends BaseService implements RouteService {

	private static final Logger LOG = LoggerFactory.getLogger(RouteServiceImpl.class);

	private RouteDAO routeRepository;

	private UserDAO userRepository;

	private FleetDAO fleetRepository;

	private DeviceDAO deviceRepository;
	
	@Autowired
	JpaTransactionManager jpManager;
	
	@Autowired
	private UserDeviceDAO userDeviceDAO;
	
	@Autowired
	private ReportDAO reportDAO;
	
	@Autowired
	private UserSchoolDAO userschoolDAO;
	
	private SchoolDAO schoolRepository;
	
	@Autowired
	private ApplicationConfig config;
	
    @Autowired
	private DeviceModelTypeDAO deviceModelTypeRepository;
	
	@Autowired
	private RouteFleetDeviceXrefDAO routeFleetDeviceDAO;
	
	@Autowired
	private AlertsDAO alertsRepository;;

	private WayPointDAO wayPointRepository;

	private FleetXrefDAO fleetXrefRepository;
	
	@Autowired
	private GeofenceDAO geofenceRepository;
	
	@Autowired
	private LocationDao locationDao;
	
	@Autowired
	private StudentsDAO studentRepository;
	
	@Autowired
	private SchoolConfigDAO schoolConfigDAO;
	
	@Autowired
	private UserMessageDAO userMessageRepository;

	private ApplicationConfig applicationConfig;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected RouteScheduleDAO routeScheduleRepository;

	@Autowired
	protected CommonService commonService;

	String matrixURL = "https://maps.googleapis.com/maps/api/distancematrix/json?key=AIzaSyB0w0tD41WTssddua5uvJWmtxAnBT2N7zw";
	
	private static Date googleApiExpDate = new Date("01/01/2000");

	public RouteServiceImpl(Validator validator) {
		super(validator);
	}

	@Autowired
	public void setRouteRepository(RouteDAO routeRepository) {
		this.routeRepository = routeRepository;
	}

	@Autowired
	public void setWayPointRepository(WayPointDAO wayPointRepository) {
		this.wayPointRepository = wayPointRepository;
	}

	@Autowired
	public void setSchoolRepository(SchoolDAO schoolRepository) {
		this.schoolRepository = schoolRepository;
	}

	@Autowired
	public void setUserRepository(UserDAO userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public RouteServiceImpl(Validator validator, ApplicationConfig applicationConfig) {
		this(validator);
		this.applicationConfig = applicationConfig;
	}

	@Autowired
	public void setFleetRepository(FleetDAO fleetRepository) {
		this.fleetRepository = fleetRepository;
	}

	@Autowired
	public void setDeviceRepository(DeviceDAO deviceRepository) {
		this.deviceRepository = deviceRepository;
	}

	@Autowired
	public void setXrefRepository(FleetXrefDAO fleetXrefRepository) {
		this.fleetXrefRepository = fleetXrefRepository;
	}

	@Override
	public Route getRouteDetails(ExternalUser requestingUser, String studentId) {
		// LOG.debug("INSIDE getRouteDetails");
		Route route = null;
		return route;
	}

	@Override
	public Devices getCurrentDeviceLocation(long routeId) {
		Devices device = null;
		device = routeRepository.findDeviceforRoute(routeId);
		return device;
	}

	@Override
	public List<Devices> getCurrentDeviceLocationForRoutes(List<Long> routeId) {
		List<Devices> device = new ArrayList<Devices>();

		if (routeId != null && routeId.size() > 0)
			device = routeRepository.findAllDeviceforRouteList(routeId);

		return device;
	}

//	@Override
//	public List<Route> getAllRouteDetailsForStudents(ExternalUser requestingUser) {
//
//		List<Route> routeList = new ArrayList<Route>();
//		LOG.debug("Requesting User UUID:: " + requestingUser.getId());
//		User user = userRepository.findByUuid(requestingUser.getId());
//		List<Student> studentList = studentRepository.findByUser(user);
//		LOG.debug("studentList:: " + studentList);
//		List<String> studentUUIDs = new ArrayList<String>();
//		if (studentList != null && studentList.size() > 0) {
//			for (int k = 0; k < studentList.size(); k++) {
//				studentUUIDs.add(studentList.get(k).getUuid().toString());
//			}
//			routeList = routeRepository.findByStudentRouteXREFStudentIn(studentUUIDs);
//		}
//		return routeList;
//	}
	@Override
	public List<RouteFleetDeviceXREF> findAllFleetListByUser(String userUuid) {
		List<RouteFleetDeviceXREF> fleets = null;
		try {
			fleets = fleetRepository.findFleetListByUserUUID(userUuid);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fleets;
	}

	@Override
	public List<Route> findAllRouteList(String userId) {
		User user=null;
		if(userId!=null && userId.length()>0){
			user=userRepository.findByUuid(userId);
		}
		if(user.getId()==1037){
			return routeRepository.findAll();
			
		}
		return routeRepository.findAllRouteList(userId);
	}

	@Override
	public List<Route> findRoutesBySchoolId(Long schoolId) {
		return routeRepository.findRoutesBySchoolId(schoolId);
	}
	
	@Override
	public List<Route> findAllRouteListByUserId(String userId) {
		return routeRepository.findAllRouteList(userId);
	}

	@Override
	public RouteModel getRouteDetails(String routeId) {

		Route route = routeRepository.findByUUID(routeId);
		RouteModel rm = new RouteModel(route);
		return rm;
	}

	@Override
	public WayPointModel getWayPointDetails(String wayPointId) {

		WayPoint wayPoint = wayPointRepository.findByUUID(wayPointId);
		WayPointModel wpModel = new WayPointModel(wayPoint);
		return wpModel;
	}
	@Override
	public WayPointModel getWayPointDetails(long wayPointId) {

		WayPoint wayPoint = wayPointRepository.getWayPoint(wayPointId);
		WayPointModel wpModel = new WayPointModel(wayPoint);
		return wpModel;
	}

	@Override
	public FleetXRefModel getBusRouteDetails(String xRefID) {

		RouteFleetDeviceXREF xRef = fleetXrefRepository.findByFleetID(Long.valueOf(xRefID));
		FleetXRefModel rm = null;
		if (xRef != null) {
			rm = new FleetXRefModel(xRef);
		}
		return rm;
	}

	@Override
	public List<School> findAllSchoolList() {
		return routeRepository.findAllSchoolList();
	}

	@Override
	public void addRoute(String routeName, String description, String geometry, String userUuid, String uuid ,Fleet fleet) {
		Route route = null;
		
		if (uuid != null && uuid.length() > 0) {
			route = routeRepository.findByUUID(uuid);
		} else {
			route = new Route();
		}

		route.setRouteName(routeName);
		route.setRouteDescription(description);
		route.setGeometry(geometry);
		School school = schoolRepository.getSchoolBySchoolAdminId(userUuid);
		route.setSchool(school);
		school.getRoutes().add(route);
		routeRepository.save(route);
		schoolRepository.save(school);
		
      RouteFleetDeviceXREF rfdx=null;
		
		if(fleet.getId()!=null){
			rfdx=routeFleetDeviceDAO.findFleetConfiguration(fleet.getId());
		rfdx.setFleet(rfdx.getFleet());	
		rfdx.setRoute(route);
		routeFleetDeviceDAO.save(rfdx);
			
		}
	}

	@Override
	public List<WayPoint> findAllWayPointList(String routeId) {
		return routeRepository.findAllWayPointList(Long.valueOf(routeId));
	}

	@Override
	public List<Fleet> findAllFleetList(String userUuid) {
		List<Fleet> fleets = null;
		try {
			fleets = fleetRepository.findFleetListByUser(userUuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fleets;
	}

	@Override
	public Fleet findFleetById(long fleetId) {
		return fleetRepository.findFleetById(fleetId);
	}

	@Override
	public Devices findByDeviceId(long deviceId) {
		return deviceRepository.findById(deviceId);
	}

	@Override
	public DeviceModel findByDeviceIdModel(long deviceId) {
		UserSchool us=userschoolDAO.getSchoolByUserDevice(deviceId);
		return new DeviceModel(deviceRepository.findById(deviceId),us.getSchool().getId());
	}

	@Override
	public Route findByRouteId(long routeId) {
		return routeRepository.findRouteByPK(routeId);
	}

	@Override
	public RouteFleetDeviceXREF findByRFDXrefId(long xRefId) {
		return fleetXrefRepository.findById(xRefId);
	}

	@Override
	public void addUpdateRouteFleetDeviceXref(RouteFleetDeviceXREF xRef ,Fleet fleet ,Route route ) {
		RouteFleetDeviceXREF secondaryXref=null;
		if(route.getId()!=null){
			secondaryXref=routeFleetDeviceDAO.findFleetByRouteConfiguration(route.getId());
			Fleet fleets=secondaryXref.getFleet();
			Route routee=xRef.getRoute();
			Devices device=secondaryXref.getDevice();
			
			secondaryXref.setFleet(fleets);
			secondaryXref.setRoute(routee);
			secondaryXref.setDevice(device);
			fleetXrefRepository.save(secondaryXref);
			
		}
		
		
		xRef.setFleet(fleet);
		xRef.setRoute(route);
		xRef.setDevice(xRef.getDevice());
		fleetXrefRepository.save(xRef);
	}

	@Override
	public FleetModel findFleetModelById(long fleetId) {
		return new FleetModel(fleetRepository.findFleetById(fleetId));
	}

	@Override
	public List<Devices> findAllDeviceList(String userUuid) {
		return deviceRepository.findAllDeviceList(userUuid);
	}

	@Override
	public List<Devices> findAllDeviceListNotMapped() {
		return deviceRepository.findAllDeviceListNotMapped();
	}
	@Override
	public Devices findDeviceByFleetId(long fleetId){
		RouteFleetDeviceXREF rfd =routeFleetDeviceDAO.findFleetConfiguration(fleetId);
		Devices device= rfd.getDevice();
		return device;
	}
	@Override
	public Fleet findFleetByRouteId(long routeId){
		RouteFleetDeviceXREF rfd =routeFleetDeviceDAO.findFleetByRouteConfiguration(routeId);
		Fleet fleet=rfd.getFleet();
		return fleet;
	}

	@Override
	public void addUpdateFleet(Fleet fleet ,Devices device) {
		
		fleetRepository.save(fleet);
		RouteFleetDeviceXREF routefleetdevice=null;
		if (fleet.getId() != null && device.getId() != null ) {
			routefleetdevice = routeFleetDeviceDAO.findByDeviceIdAndFleetId(fleet.getId(),device.getId());
		}if(routefleetdevice == null){
			RouteFleetDeviceXREF rfdx= new RouteFleetDeviceXREF();
			rfdx.setDevice(device);
			rfdx.setFleet(fleet);
			routeFleetDeviceDAO.save(rfdx);
			
		}
	}
	
	private void addUpdateUserDevice(final User user,final Devices device) {
		UserDevice userDevice = null;
		if (user.getId() != null && device.getId()!=null ) {
			userDevice = userDeviceDAO.findByDeviceIdAndUserId(user.getId(), device.getId());
		}
		if(userDevice==null){
			userDevice=new UserDevice();
			userDevice.setDevices(device);
			userDevice.setUser(user);
		}
		userDeviceDAO.save(userDevice);
	}

	@Override
	@RolesAllowed({ "superAdmin", "administrator" })
	public void addUpdateDevice(Devices device,final String uuid, Long schoolId) {
			//set admin first
			User user=userRepository.findByUuid(uuid);
			User schoolAdmin= null;
			  /*Calendar cal = Calendar.getInstance();
			  cal.setTime(device.getPuchaseDate());
			  cal.add(device.getPuchaseDate(), 3);*/
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date myDate =device.getPuchaseDate();
			myDate = DateUtil.addDaysToDate(myDate, 90);
			if(device.getServiceEndDate()==null){
			device.setServiceEndDate(myDate);
			}else{
				device.setServiceEndDate(device.getServiceEndDate());
			}
			if(schoolId != null) schoolAdmin = schoolRepository.findSchoolAdminBySchool(schoolId);
			//school group id and same need to update here.
			if(user.getRole().equals(com.spi.domain.Role.administrator)){
				schoolAdmin = user;
				schoolId = user.getUserSchool().getSchool().getId();
			} else if(user.getRole().equals(com.spi.domain.Role.superAdmin)){
				schoolAdmin = schoolRepository.findSchoolAdminBySchool(schoolId);
			}
			addUpdateDeviceForSchoolAdmin(device, schoolAdmin);
			
	}

	private void addUpdateDeviceForSchoolAdmin(Devices device, User schoolUserAdmin) {
		for(UserGroupSchool groupSchool : schoolUserAdmin.getUserGroupSchools()){
			if(groupSchool.getGroupid() != null){
				device.setGroupId(groupSchool.getGroupid());
				break;
			}
		}
		
		//first save the device record and then update user device mapping table
		deviceRepository.save(device);
		addUpdateUserDevice(schoolUserAdmin,device);
		//set super admin 
		User userSupAdmin=userRepository.findSuperAdmin();
		addUpdateUserDevice(userSupAdmin,device);
	}

	@Override
	public List<WayPointModel> findAllWayPointListforRoute(String routeUUID) {
		List<WayPoint> wayPoints = wayPointRepository.findAllWayPointListforRoute(routeUUID);
		List<WayPointModel> wayPointModels = new ArrayList<WayPointModel>();
		for(WayPoint wayPoint : wayPoints) {
			wayPointModels.add(new WayPointModel(wayPoint));
		}
		return wayPointModels;
	}
	@Override
	public List<WayPointVM> findAllWayPointListforRoute(long routeId) {
		List<WayPoint> wayPoints = wayPointRepository.findAllWayPointListforRoute(routeId);
		List<WayPointVM> wayPointVMs = new ArrayList<WayPointVM>();
		for (WayPoint wayPoint : wayPoints) {
			WayPointVM wpvm=  new WayPointVM();
			wpvm.setWaypointId(wayPoint.getId());
			wpvm.setDescription(wayPoint.getDescription());
			wayPointVMs.add(wpvm);
		}
		return wayPointVMs;
	}

	@Override
	public void addWayPoint(String name, String lattitude, String longitude, String sequenceNumber, String altitude, String uuid,
			String routeUUID, String picktime, String droptime) {
		WayPoint wayPoint = null;
		if (uuid != null && uuid.length() > 0) {
			wayPoint = wayPointRepository.findByUUID(uuid);
		} else {
			wayPoint = new WayPoint();
		}

		wayPoint.setName(name);
		wayPoint.setRoute(routeRepository.findByUUID(routeUUID));
		wayPoint.setLattitude(Double.parseDouble(lattitude));
		wayPoint.setLongitude(Double.parseDouble(longitude));
		wayPoint.setSequenceNumber(Integer.parseInt(sequenceNumber));
		
		Date pickDate =new Date( Date.parse(picktime));
		wayPoint.setTimePick(pickDate);

		Date dropDate =new Date( Date.parse(droptime));
		wayPoint.setTimeDrop(dropDate);
		wayPointRepository.save(wayPoint);
	}
	
	public List<Devices> getCurrentDeviceLocations(String userId) {
		List<Student> studentList = userService.getStudentDetails(userId);
		List<Long> routeIdList = new ArrayList<Long>();

		List routeWaypointsDeviceList = new ArrayList();
		Map routeWaypointsMap = new HashMap();

		for (int k = 0; k < studentList.size(); k++) {
			if (studentList.get(k).getWayPoint() != null && studentList.get(k).getWayPoint().getRoute() != null)
				routeIdList.add(studentList.get(k).getWayPoint().getRoute().getId());
			routeWaypointsMap.put(studentList.get(k).getWayPoint().getRoute().getId(), studentList.get(k).getWayPoint());
		}

		List<Devices> deviceList = getCurrentDeviceLocationForRoutes(routeIdList);

		routeWaypointsDeviceList.add(deviceList);
		routeWaypointsDeviceList.add(routeWaypointsMap);

		return routeWaypointsDeviceList;
	}
	
	public List getDeviceCurrentLocation(SecurityContext sc, String userId){
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		// Route route = routeService.getRouteDetails(userMakingRequest,
		// studentId);
		List<RoutePath> routeDetailsList = new ArrayList<RoutePath>();
		List routeWaypointsDeviceList = null;
		
		routeWaypointsDeviceList = getCurrentDeviceLocations(userId);
		List<Devices> deviceList = (List<Devices>) routeWaypointsDeviceList.get(0);
		Map routeWaypointsMap = (HashMap) routeWaypointsDeviceList.get(1);
		int deviceLen = deviceList.size();

		String[][] currentLatLongs = new String[deviceLen][3];
		RoutePath currentPath = new RoutePath();

		URL url;
		String matrixParam = "";
		for (int k = 0; k < deviceLen; k++) {
			matrixParam = "";
			Location location = (locationDao.findByPrimaryId(deviceList.get(k).getPositionId()));
			if(location == null) continue;
			currentLatLongs[k][0] = location.getLattitude() + "";
			currentLatLongs[k][1] = location.getLongitude() + "";
			WayPoint stWayPoint = (WayPoint) routeWaypointsMap.get(deviceList.get(k).getRouteFleetDeviceXREF().getRoute().getId());
			
//			vehicleSpeed = deviceList.get(k).getLocations().get(0).getVehicleSpeed();
			// Once Exception is received for any Date then there is no need to execute this code
			if(!googleApiExpDate.equals(DateUtil.getDateOnly(new Date()))){
				
				try {
					matrixParam += "&origins=" + currentLatLongs[k][0] + "," + currentLatLongs[k][1];
					matrixParam += "&destinations=" + stWayPoint.getLattitude() + "," + stWayPoint.getLongitude();
					url = new URL(matrixURL + matrixParam);
					HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
	
					String matrixResponse = getGMapMatrixResponse(con);
	
					JSONObject jsonRespRouteDistance = new JSONObject(matrixResponse).getJSONArray("rows").getJSONObject(0)
							.getJSONArray("elements").getJSONObject(0);
	
					String distance = jsonRespRouteDistance.getJSONObject("distance").get("text").toString();
					String time = jsonRespRouteDistance.getJSONObject("duration").get("text").toString();
					currentLatLongs[k][2] = "<b>ETA:</b> " + time + " (" + distance + ")";
	
				} catch (Exception e) {
					LOG.error("Error in getDeviceCurrentLocation", e);
					googleApiExpDate = DateUtil.getDateOnly(new Date());
				}
			} else {

				double distance = DistanceUtils.distance(Double.parseDouble(currentLatLongs[k][0]), Double.parseDouble(currentLatLongs[k][1]), stWayPoint.getLattitude(),
						stWayPoint.getLongitude(), "K") * 1.4;
				distance = Math.round(distance * 100D) / 100D;
				double time = Math.round(distance/(20)*60 * 100D) / 100D;
				currentLatLongs[k][2] = "<b>ETA:</b> " + time + " minutes (" + distance + " km)";

			}
		}
		currentPath.setCurrentLatlongs(currentLatLongs);
		routeDetailsList.add(currentPath);
		return routeDetailsList;
	}
	
	public List getDeviceCurrentLocationForAdmin(SecurityContext sc, String userId){
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		// Route route = routeService.getRouteDetails(userMakingRequest,
		// studentId);
		List<RoutePath> routeDetailsList = new ArrayList<RoutePath>();
		List routeWaypointsDeviceList = null;
		
		routeWaypointsDeviceList = getCurrentDeviceLocationsForAdmin(userMakingRequest, userId);
		List<Devices> deviceList = (List<Devices>) routeWaypointsDeviceList.get(0);

		int deviceLen = deviceList.size();

		String[][] currentLatLongs = new String[deviceLen][3];
		RoutePath currentPath = new RoutePath();

		URL url;
		String matrixParam = "";
		for (int k = 0; k < deviceLen; k++) {
			matrixParam = "";
			currentLatLongs[k][0] = deviceList.get(k).getLocations().get(0).getLattitude() + "";
			currentLatLongs[k][1] = deviceList.get(k).getLocations().get(0).getLongitude() + "";
//			vehicleSpeed = deviceList.get(k).getLocations().get(0).getVehicleSpeed();
			try {
//				matrixParam += "&origins=" + currentLatLongs[k][0] + "," + currentLatLongs[k][1];
//				matrixParam += "&destinations=" + stWayPoint.getLattitude() + "," + stWayPoint.getLongitude();
//				url = new URL(matrixURL + matrixParam);
//				HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//
//				String matrixResponse = getGMapMatrixResponse(con);
//
//				JSONObject jsonRespRouteDistance = new JSONObject(matrixResponse).getJSONArray("rows").getJSONObject(0)
//						.getJSONArray("elements").getJSONObject(0);
//
//				String distance = jsonRespRouteDistance.getJSONObject("distance").get("text").toString();
//				String time = jsonRespRouteDistance.getJSONObject("duration").get("text").toString();
				currentLatLongs[k][2] = "<b>ETA:</b> " + " (" + deviceList.get(k).getName() + ")";

			} catch (Exception e) {
				LOG.error("Error in getDeviceCurrentLocation", e);
			}
		}
		currentPath.setCurrentLatlongs(currentLatLongs);
		routeDetailsList.add(currentPath);
		return routeDetailsList;
	}
	
	public List<Devices> getCurrentDeviceLocationsForAdmin(ExternalUser userMakingRequest, String userId) {
		List<Long> routeIdList = new ArrayList<Long>();

		List routeWaypointsDeviceList = new ArrayList();
		List<Route> routes = findAllRouteList(userId);
		for(Route route :routes){
			routeIdList.add(route.getId());
		}
//		routeWaypointsMap.put(studentList.get(k).getStudentRouteXREF().getRoute().getId(), studentList.get(k).getStudentRouteXREF().getWayPoint());
		List<Devices> deviceList = getCurrentDeviceLocationForRoutes(routeIdList);

		routeWaypointsDeviceList.add(deviceList);

		return routeWaypointsDeviceList;
	}
	
	
	
	public String getGMapMatrixResponse(HttpsURLConnection con) {
		String response = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String input;
			while ((input = br.readLine()) != null) {
				response += input;
			}
			br.close();

		} catch (IOException e) {
			LOG.error("Error in getGMapMatrixResponse", e);
		}
		return response;
	}
	
	public List<RoutePath> getAllRoute(SecurityContext sc, String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		
		List<RoutePath> routeDetailsList = new ArrayList<RoutePath>();

		List routeWaypointsDeviceList = getCurrentDeviceLocations(userId);
		List<Devices> deviceList = (ArrayList<Devices>) routeWaypointsDeviceList.get(0);
		Map routeWaypointsMap = (HashMap) routeWaypointsDeviceList.get(1);

		int deviceLen = deviceList.size();

		String[][] currentLatLongs = new String[deviceLen][3];
		RoutePath currentPath = new RoutePath();

		double vehicleSpeed = 0.0d;

		URL url;
		String matrixParam = "";

		for (int k = 0; k < deviceLen; k++) {
			matrixParam = "";
			Location location = (locationDao.findByPrimaryId(deviceList.get(k).getPositionId()));
			currentLatLongs[k][0] = location.getLattitude() + "";
			currentLatLongs[k][1] = location.getLongitude() + "";

			vehicleSpeed = location.getSpeed();
			WayPoint stWayPoint = (WayPoint) routeWaypointsMap
					.get(deviceList.get(k).getRouteFleetDeviceXREF().getRoute().getId());
			try {
				matrixParam += "&origins=" + currentLatLongs[k][0] + "," + currentLatLongs[k][1];
				matrixParam += "&destinations=" + stWayPoint.getLattitude() + "," + stWayPoint.getLongitude();
				url = new URL(matrixURL + matrixParam);
				HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

				String matrixResponse = getGMapMatrixResponse(con);

				JSONObject jsonRespRouteDistance = new JSONObject(matrixResponse).getJSONArray("rows").getJSONObject(0)
						.getJSONArray("elements").getJSONObject(0);

				String distance = jsonRespRouteDistance.getJSONObject("distance").get("text").toString();
				String time = jsonRespRouteDistance.getJSONObject("duration").get("text").toString();
				currentLatLongs[k][2] = "<b>ETA:</b> " + time + " (" + distance + ")";

			} catch (Exception e) {
				LOG.error("Error in getAllRoute", e);
			}
		}
		currentPath.setCurrentLatlongs(currentLatLongs);
		routeDetailsList.add(currentPath);

		User user = userRepository.findByUuid(userId);
		
		for (Student student : user.getStudent()) {
			RoutePath path = new RoutePath();
			Route route = student.getWayPoint().getRoute();
			
			WayPoint wayPoints = student.getWayPoint();

			path.setMessage(route.getRouteName() + ", " + student.getFirstName() + " " + student.getLastName());
			String[] latLongs = new String[3];
			for (int k = 0; k < 1; k++) {
				latLongs[0] = wayPoints.getLattitude() + "";
				latLongs[1] = wayPoints.getLongitude() + "";
				latLongs[2] = wayPoints.getName() + "";

			}
			path.setLatlongs(latLongs);
			if (StringUtil.isValid(route.getGeometry())) {
				path.setKmlfile(route.getGeometry());
			} else {
				path.setKmlfile("");
			}
			routeDetailsList.add(path);
		}
		return routeDetailsList;
	}
	public int sendMessageForStudent(SendSmsModel model ,long studentId){
		if(!validateSmsModel(model)){
			return 0;
		}
		LOG.info("Sending messages SendSmsModel = {}", model.toString());
		boolean sendSms =  model.isSendSms();
		boolean sendAppMessage = model.isSendAppMessage();
		Student student=studentRepository.findOne(studentId);
		User user=student.getUser();
		int count = 0;
		int total = 0;
		String output = null;
		if (  student.getIsApproved() == 'Y' 
				&& (student.getUser() != null && student.getUser().getIsEnable() == 1)) {
		
			if(model.getMessageTemplateId().equals("1000")){ // Welcome Template
				model.setMessage("Dear " +student.getUser().getFirstName() + ", your kid's pick time is " + student.getWayPoint().getTimePick() 
						+ " and drop time is " + student.getWayPoint().getTimeDrop() + ", Driver: " 
						+ student.getWayPoint().getRoute().getRouteFleetDeviceXREF().getFleet().getDriverName() 
						+ "(" + student.getWayPoint().getRoute().getRouteFleetDeviceXREF().getFleet().getDriverMobile() + ")");
				
			}
			
			if(sendSms){
				output = SmsPrimaryService.getInstance(config).sendMessage(student.getUser().getMobileNumber(), model.getMessage());
			}
			
			if(sendAppMessage){
				String fcmtoken = student.getUser().getFcmtoken();
				FcmMessageService.getInstance(config).sendMessage(fcmtoken, model.getMessage());
			}
			
			updateAlert(model.getMessage(), student.getId(), student.getUser().getId());
			if(output != null && output.contains(":")) count++;
			total++;
		}
	return count ;
	}
	
	
	public int sendMessage(SendSmsModel model, String userId) {
		if(!validateSmsModel(model)){
			return 0;
		}
		LOG.info("Sending messages SendSmsModel = {}", model.toString());
		boolean sendSms =  model.isSendSms();
		boolean sendAppMessage = model.isSendAppMessage();
		User user = userRepository.findByUuid(userId);
		RouteScheduleDetails routeScheduleDetails = routeRepository.findRouteScheduleDetailByPrimaryKey(model.getScheduleId());
		Date startTime = new Date();
		Date endTime = new Date();
		if(routeScheduleDetails != null){
			startTime.setHours(routeScheduleDetails.getStartTime().getHours());
			startTime.setMinutes(routeScheduleDetails.getStartTime().getMinutes());
			startTime.setSeconds(0);
			startTime = DateUtil.getTimeOnly(startTime);
			endTime.setHours(routeScheduleDetails.getEndTime().getHours());
			endTime.setMinutes(routeScheduleDetails.getEndTime().getMinutes());
			endTime.setSeconds(0);
			endTime = DateUtil.getTimeOnly(endTime);
		}
		boolean isPickTimeSlot = false;
		List<Student> students = commonService.getStudentForSendingMessage(model.isSendToAll(), model.getRouteUuid(), userId);
		
		int count = 0;
		int total = 0;
		String output = null;
		for (Student student : students) {
			Date pickTime = student.getWayPoint().getTimePick();
			Date dropTime = student.getWayPoint().getTimeDrop();
			if ((model.isSendToAll() || isPickTimeSlot || ((startTime.before(pickTime) && endTime.after(pickTime))
					|| (startTime.before(dropTime) && endTime.after(dropTime)))) && student.getIsApproved() == 'Y' 
					&& (student.getUser() != null && student.getUser().getIsEnable() == 1)) {
			
				if(model.getMessageTemplateId().equals("1000")){ // Welcome Template
					model.setMessage("Dear " +student.getUser().getFirstName() + ", your kid's pick time is " + student.getWayPoint().getTimePick() 
							+ " and drop time is " + student.getWayPoint().getTimeDrop() + ", Driver: " 
							+ student.getWayPoint().getRoute().getRouteFleetDeviceXREF().getFleet().getDriverName() 
							+ "(" + student.getWayPoint().getRoute().getRouteFleetDeviceXREF().getFleet().getDriverMobile() + ")");
					
				}
				
				if(sendSms){
					output = SmsPrimaryService.getInstance(config).sendMessage(student.getUser().getMobileNumber(), model.getMessage());
				}
				
				if(sendAppMessage){
					String fcmtoken = student.getUser().getFcmtoken();
					FcmMessageService.getInstance(config).sendMessage(fcmtoken, model.getMessage());
				}
				
				updateAlert(model.getMessage(), student.getId(), student.getUser().getId());
				if(output != null && output.contains(":")) count++;
				total++;
				System.out.println(total);
			}
		}
		
		//Sending SMS to School Administrator
		total++; count++;
		if(sendSms) {
			LOG.info("Sending SMS message to School Admin {}", user.getFirstName());
			SmsPrimaryService.getInstance(config).sendMessage(user.getMobileNumber(), count + ":" + model.getMessage());
		}
		if(sendAppMessage) {
			LOG.info("Sending FCM message to School Admin {}", user.getFirstName());
			String fcmtoken = user.getFcmtoken();
			FcmMessageService.getInstance(config).sendMessage(fcmtoken, model.getMessage());
		}
		
		UserMessage message = new UserMessage();
		message.setMessageCount(total);
		message.setTimeCreated(new Date());
		message.setUserid(user.getId());
		message.setMessage(count + " out of " + total + ": " + model.getMessage());
		userMessageRepository.save(message);
		
		return count;
	}
	
	private void updateAlert(String message, long studentId, long userId){
		Alert alert = new Alert();
		alert.setAddText(message);
		alert.setStudentId(studentId);
		alert.setUserId(userId);
		alertsRepository.save(alert);
	}
	
	private boolean validateSmsModel(SendSmsModel model){
		boolean result = true;
		if(model.isSendToAll()) return true;
		if("1001".equals(model.getMessageTemplateId()) && (model.getMessage() == null || model.getMessage().equals("") || model.getMessage().equals("undefined"))){
			result = false;
		} else if(model.getRouteUuid() == null || model.getRouteUuid().equals("") || model.getRouteUuid().equals("undefined")){
			result = false;
//		} else if(!"1001".equals(model.getMessageTemplateId()) ){
//			result = false;
		} 
		
		return result;
	}

	@Override
	public Map<String, DeviceModelType> getAllDeviceModelType() {
		List<DeviceModelType>  list=null;
		Map<String, DeviceModelType>  deviceMap = new HashMap<String, DeviceModelType>();
		list= deviceModelTypeRepository.findAllDeviceModelTypeList();
		for(DeviceModelType deviceModelType : list){
			deviceMap.put(deviceModelType.getId().toString(), deviceModelType);
		}
		return deviceMap;
	}
	
	/*public List<DeviceModel> getAllDevices(String userId) {
		List<Devices> devices = findAllDeviceList(userId);

		List<DeviceModel> devicesModels = new ArrayList<DeviceModel>();
		for (Devices device : devices) {
			DeviceModel deviceModel = new DeviceModel(device);
			Location location = jpManager.getEntityManagerFactory().createEntityManager().createNamedQuery("findPreviousToCurrentPostion",Location.class).setParameter(1, device.getPositionId()).getSingleResult();
			LocationModel previoudLocationModel = new LocationModel(location);
			deviceModel.addLastLocation(previoudLocationModel);
			Location currentLocation = locationDao.findByPrimaryId(device.getPositionId());
			deviceModel.addLastLocation(new LocationModel(currentLocation));
			devicesModels.add(deviceModel);
		}
		return devicesModels;
	}*/
	
	public List<DeviceModel> getAllDevices(String userId) {
		List<Devices> devices = findAllDeviceList(userId);

		List<DeviceModel> devicesModel = new ArrayList<DeviceModel>();
		for (Devices device : devices) {
			devicesModel.add(new DeviceModel(device));
		}
		return devicesModel;
	}
	
	/*public List<RoutePath> getStudentCurrentLocation(SecurityContext sc, String userId) {
		List<Student> studentList = userService.getStudentDetails(userId);
		List<Long> routeIdList = new ArrayList<Long>();

		List routeWaypointsDeviceList = new ArrayList();
		Map routeWaypointsMap = new HashMap();

		for (int k = 0; k < studentList.size(); k++) {
			if (studentList.get(k).getStudentRouteXREF() != null && studentList.get(k).getStudentRouteXREF().getRoute() != null)
				routeIdList.add(studentList.get(k).getStudentRouteXREF().getRoute().getId());
			routeWaypointsMap.put(studentList.get(k).getStudentRouteXREF().getRoute().getId(), studentList.get(k)
					.getStudentRouteXREF().getWayPoint());
		}

		List<Devices> deviceList = getCurrentDeviceLocationForRoutes(routeIdList);

		routeWaypointsDeviceList.add(deviceList);
		routeWaypointsDeviceList.add(routeWaypointsMap);

		return routeWaypointsDeviceList;
	}*/

	
	public List<WayPoint> getStudentCurrentLocation(SecurityContext sc, String userId) {
		List<Student> studentList = userService.getStudentDetails(userId);
		//List<Long> routeIdList = new ArrayList<Long>();

		//List routeWaypointsDeviceList = new ArrayList();
		//Map routeWaypointsMap = new HashMap();
		double lat;
		double lng;
		List<WayPoint> routeDetailsList = new ArrayList<WayPoint>();
		
		for (int k = 0; k < studentList.size(); k++) {
			
			
			if (studentList.get(k).getWayPoint() != null && studentList.get(k).getWayPoint().getRoute() != null){
				WayPoint wp=new WayPoint();
				//routeIdList.add(studentList.get(k).getStudentRouteXREF().getRoute().getId());
			//routeWaypointsMap.put(studentList.get(k).getStudentRouteXREF().getRoute().getId(), studentList.get(k).getStudentRouteXREF().getWayPoint());
				wp.setLattitude(studentList.get(k).getWayPoint().getLattitude());
			
				wp.setLongitude(studentList.get(k).getWayPoint().getLongitude());
			
		
			routeDetailsList.add(wp);
			}
			
		}
		
		

		//List<Devices> deviceList = getCurrentDeviceLocationForRoutes(routeIdList);

		//routeWaypointsDeviceList.add(deviceList);
		//routeWaypointsDeviceList.add(routeWaypointsMap);

		return routeDetailsList;
	}
	
	@Override
	public List<Route> getAllRouteDetails(String userId) {
		List<Route> routes = null;
		try {
			routes = routeRepository.getAllRouteDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return routes;
	}

	@Override
	public Location getBusCurrentLocation(String userId, String vehicleId) {
		
		long vehicleid=Long.parseLong(vehicleId);
		
		Location position = null;
		
		RouteFleetDeviceXREF fleet=fleetRepository.findRouteFleetDeviceById(vehicleid);
		
		try {
			
			position = locationDao.findByPrimaryId(fleet.getDevice().getPositionId());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return position;
	}
	
	public List<RouteVM> getScheduleByRoute(String routeUuid) {
		RouteFleetDeviceXREF deviceXREF = fleetRepository.findRouteFleetDevicebyRouteUuid(routeUuid);
		if(deviceXREF != null){
			return getScheduleByFleet(deviceXREF.getFleet().getId());
		} 
		return null;
	}
	
	public List<RouteVM> getScheduleByFleet(long fleetId) {
		List<RouteScheduleDetails> routeList = routeRepository.findrouteTime(fleetId);
		return transformToRouteVM(routeList); 
	}

	public DeviceVM getSpeedByFleet(long fleetId) {
		RouteFleetDeviceXREF rfdx = routeFleetDeviceDAO.findDeviceByFleetConfiguration(fleetId);
		DeviceVM deviceVM = null;
		if(rfdx == null) {
			deviceVM = new DeviceVM();
			deviceVM.setSpeedLimit(60);
		} else {
			deviceVM = reportDAO.getSpeedForDevice(rfdx.getDevice().getId());
		}
		return deviceVM;
	}
	
	private List<RouteVM> transformToRouteVM(List<RouteScheduleDetails> routeList) {
		List<RouteVM> routevm = new ArrayList<RouteVM>();
		for (RouteScheduleDetails route : routeList) {
			RouteVM svm = new RouteVM();
			svm.setStartTime(route.getStartTime().toString().substring(0, 5));
			svm.setEndTime(route.getEndTime().toString().substring(0, 5));
			svm.setRouteId(route.getId());
			routevm.add(svm);
		}
		return routevm;
	}
	@Override
	public List<FleetVM>findAllFleetListNotMapped() {
		LOG.info("inside getUnassignedSchool");

		List<FleetVM> fleetVMList = new ArrayList<FleetVM>();

		List<Fleet> schools = fleetRepository.findAllFleetListNotMapped();
		for (Fleet device : schools) {

			FleetVM svm = new FleetVM();
			svm.setFleetModel(device.getFleetModel());
			svm.setRegNumber(device.getRegNumber());
			svm.setFleetId(device.getId());
		    fleetVMList.add(svm);	
		}
		return fleetVMList;
	}
	@Override
	public List<FleetVM>findAllFleetListNotMapped(String userId){
		List<FleetVM> fleet= new ArrayList<FleetVM>();
		List<Fleet> fleets=fleetRepository.findAllFleetListNotMapped(userId);
		for(Fleet device : fleets){
			FleetVM vm = new FleetVM();
			vm.setRegNumber(device.getRegNumber());
			vm.setFleetModel(device.getFleetModel());
			vm.setFleetId(device.getId());
			fleet.add(vm);
		}
		
		
		return fleet;
	}
	@Override
	public List<WayPointVM> getScheduleByWayPoint(long waypointId) {
		List<WayPoint> wayPoints = wayPointRepository.getWayPointList(waypointId);
		List<WayPointVM> wpvm= new ArrayList<WayPointVM>();
		for(WayPoint wp : wayPoints){
			WayPointVM wpvmm = new WayPointVM();
			wpvmm.setTimePick(wp.getTimePick().toString().substring(0, 5));
			wpvmm.setTimeDrop(wp.getTimeDrop().toString().substring(0, 5));
			wpvm.add(wpvmm);
		}
		
		return wpvm;
	}
	
	public GeofenceVM getGeofenceByVehicle(long vehicleId){
		
		return new GeofenceVM(geofenceRepository.getRouteGeoFenceByFleet(vehicleId));		
	}
	public List<RouteScheduleModel> getRouteScheduleDetails(){
		List<RouteScheduleModel> rsm= new ArrayList<>();
		List<RouteScheduleDetails> rsd=routeScheduleRepository.findAll();
		for(RouteScheduleDetails rd : rsd){
			RouteScheduleModel routeModel= new RouteScheduleModel();
			routeModel.setScheduleId(rd.getId());
			routeModel.setScheduleName(rd.getScheduleName());
			routeModel.setScheduleFlag(rd.getSjaFlag());
			routeModel.setStartTime(rd.getStartTime());
			routeModel.setEndTime(rd.getEndTime());
			routeModel.setRouteId(rd.getRoute().getId());
			routeModel.setSchoolName(rd.getRoute().getSchool().getName());
			routeModel.setSchoolId(rd.getRoute().getSchool().getId());
			routeModel.setRouteName(rd.getRoute().getRouteName());
			
			rsm.add(routeModel);
			
		}
		return rsm;
	}
	@Override
	public List<SchoolModel> getSchoolList(){
		List<SchoolModel> sm= new ArrayList<>();
		List<School> school=schoolRepository.findAll();
		
		for(School s: school){
			SchoolModel smodel= new SchoolModel();
			smodel.setName(s.getName());
			smodel.setId(s.getId());
			sm.add(smodel);
		}
		return sm;
	}
	public void addRouteSchedule(RouteScheduleModel request){
		RouteScheduleDetails rsd=null;
		if(request.getScheduleId()>0){
		rsd=routeScheduleRepository.findOne(request.getScheduleId());	
		}else{
			rsd= new RouteScheduleDetails();
		}
		Route route=routeRepository.findOne(request.getRouteId());
		rsd.setSjaFlag(request.getScheduleFlag());
		rsd.setRoute(route);
		rsd.setScheduleName(request.getScheduleName());
		rsd.setStartTime(request.getStartTime());
		rsd.setEndTime(request.getEndTime());
		routeScheduleRepository.save(rsd);
		
	}
	
	public List<StudentVM> getStudentByRoute(String routeUUID){
		List<StudentVM> studentvm= new ArrayList<>();
		List<Student> student=studentRepository.getStudentByRouteId(routeUUID);
		for(Student st :student){
			StudentVM studentmodel=new StudentVM();
			studentmodel.setFirstName(st.getFirstName());
			studentmodel.setLastName(st.getLastName());
			studentmodel.setStudentId(st.getId());
		  studentvm.add(studentmodel);

		}
		return studentvm;
	}
	
	
}
