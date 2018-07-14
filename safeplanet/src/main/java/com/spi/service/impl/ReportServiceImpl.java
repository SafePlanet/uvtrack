package com.spi.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spi.VM.NotificationStatusVM;
import com.spi.VM.StudentNotificationStatusVM;
import com.spi.VM.WayPointVM;
import com.spi.config.ApplicationConfig;
import com.spi.config.SystemConstant;
import com.spi.dao.FleetDAO;
import com.spi.dao.NotificationStatusDAO;
import com.spi.dao.ReportDAO;
import com.spi.dao.RouteDAO;
import com.spi.dao.SchoolConfigDAO;
import com.spi.dao.StudentNotificationStatusDAO;
import com.spi.domain.Fleet;
import com.spi.domain.NotificationStatus;
import com.spi.domain.Route;
import com.spi.domain.RouteScheduleDetails;
import com.spi.domain.Student;
import com.spi.domain.WayPoint;
import com.spi.dto.LocationDTO;
import com.spi.service.BaseService;
import com.spi.service.ReportService;
import com.spi.service.RouteService;
import com.spi.service.UserService;
import com.spi.user.api.BusStopTimeReportModel;
import com.spi.user.api.FleetModel;
import com.spi.user.api.FleetReportModel;
import com.spi.user.api.HaltReportModel;
import com.spi.user.api.NotificationStatusModel;
import com.spi.user.api.OverSpeedingReportModel;
import com.spi.user.api.PositionReportModel;
import com.spi.user.api.PredictiveStopReportModel;
import com.spi.user.api.RouteModel;
import com.spi.user.api.SearchDTO;
import com.spi.user.api.TripSummaryReportModel;
import com.spi.util.DateUtil;
import com.spi.util.DistanceUtils;
import com.spi.util.SpiDateTime;
import com.spi.util.StringUtil;

enum VehicleStatus {
	standing, stopped, started, running,
}

enum StopageDuration {
	notStarted, started, ended, inProgress
}

enum RunningDuration {
	notStarted, started, inProgress, ended
}

@Service("reportService")
public class ReportServiceImpl extends BaseService implements ReportService {

	private static final Logger LOG = LoggerFactory.getLogger(ReportServiceImpl.class);

	private ApplicationConfig applicationConfig;

	@Autowired
	protected RouteService routeService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected RouteDAO routeDAO;

	@Autowired
	protected FleetDAO fleetDAO;
	
	@Autowired
	protected StudentNotificationStatusDAO studentnotificationDAO;

	@Autowired
	private SchoolConfigDAO schoolConfigDAO;

	@Autowired
	protected NotificationStatusDAO notificationStatusDAO;

	private static final double distanceFromStop = 0.025;
	private static final double predictDistancebtwnStops = 0.010;

	@Autowired
	ReportDAO reportDAO = null;

	public ReportServiceImpl(Validator validator) {
		super(validator);
	}

	@Autowired
	public ReportServiceImpl(Validator validator, ApplicationConfig applicationConfig) {
		this(validator);
		this.applicationConfig = applicationConfig;
	}

	public List<FleetModel> findAllFleetList(String userId) {
		List<FleetModel> fleetModels = new ArrayList<FleetModel>();
		List<Fleet> fleets = routeService.findAllFleetList(userId);
		for (Fleet fleet : fleets) {
			fleetModels.add((new FleetModel(fleet)));
		}
		return fleetModels;
	}

	public List<RouteModel> getAllRouteList(String userId) {
		List<RouteModel> routetModels = new ArrayList<RouteModel>();
		List<Route> routes = routeService.getAllRouteDetails(userId);
		for (Route route : routes) {
			routetModels.add((new RouteModel(route)));
		}
		return routetModels;
	}

	public List<FleetReportModel> getFleetSummary(String userId, SearchDTO searchDTO) {
		List<FleetReportModel> reportData = new ArrayList<FleetReportModel>();

		Long id;
		String vehicle;
		String haltDuration;
		String moveDuration;
		String startLocation = null;
		String endLocation = null;
		String totalMovement;
		String avgMoveSpeed;
		String maxSpeed;
		double endLocationLattitude = 0.0;
		double endLocationLongitude = 0.0;
//		setDatabaseSchema(searchDTO);
		Map<Long, String> fleetMap = reportDAO.getFleetAndDeviceForUser(userId);
		Iterator<Long> fleetMapItr = fleetMap.keySet().iterator();

		while (fleetMapItr.hasNext()) {
			id = fleetMapItr.next();
			vehicle = fleetMap.get(id);

			List<LocationDTO> locationsList = reportDAO.getPositionsForDevice(searchDTO, id);

			double speed = 0;
			double prevspeed = 0;
			double haltTime = 0;
			double moveTime = 0;
			double totalHaltTime = 0;
			int overSpeedEvents = 0;
			int longHaltEvents = 0;
			int idlingEvents = 0;

			LocationDTO prevLocation = null;
			double maxSpeedCalc = 0;
			double totalMovementCalc = 0;
			int counter = 0;
			for (LocationDTO location : locationsList) {
				if (counter == 0) {
					StringBuffer sb = new StringBuffer("http://maps.google.com/maps?q=");
					sb.append("n").append(location.getLatitude()).append(",e").append(location.getLongitude());
					startLocation = sb.toString();
					haltTime += DateUtil.getDateTimeDiff(searchDTO.getFromDate().getTimestampDate(), location.getFixTime());
				}

				speed = location.getSpeed();

				if (speed < searchDTO.getSpeedLimit() && prevspeed > searchDTO.getSpeedLimit()) {
					overSpeedEvents++;
				}

				if (speed == 0 && prevspeed == 0 && prevLocation != null) {
					haltTime += DateUtil.getDateTimeDiff(prevLocation.getFixTime(), location.getFixTime());
				} else if (speed > 0 && prevspeed == 0 && prevLocation != null) {
					haltTime += DateUtil.getDateTimeDiff(prevLocation.getFixTime(), location.getFixTime());
					if (haltTime > ((long) searchDTO.getHaltLimit() * 60 * 1000)) {
						String haltDurationDays = DateUtil.getFormattedStringTime((long) (haltTime));
						longHaltEvents++;
					}
					totalHaltTime += haltTime;
					haltTime = 0;
				}

				if (((speed == 0 && prevspeed > 0) || (speed > 0 && prevspeed > 0)) && prevLocation != null) {
					moveTime += DateUtil.getDateTimeDiff(prevLocation.getFixTime(), location.getFixTime());

					totalMovementCalc += DistanceUtils.distance(location.getLatitude(), location.getLongitude(), prevLocation.getLatitude(),
							prevLocation.getLongitude(), "K");
				}
				if (speed > maxSpeedCalc) {
					maxSpeedCalc = speed;
				}

				if (counter == (locationsList.size() - 1)) {
					StringBuffer sb = new StringBuffer("http://maps.google.com/maps?q=");
					sb.append("n").append(location.getLatitude()).append(",e").append(location.getLongitude());
					endLocation = sb.toString();

					if ((speed == 0 && prevspeed > 0) || (speed > 0 && prevspeed > 0)) {
						moveTime += DateUtil.getDateTimeDiff(location.getFixTime(), searchDTO.getToDate().getTimestampDate());
					} else {
						haltTime += DateUtil.getDateTimeDiff(location.getFixTime(), searchDTO.getToDate().getTimestampDate());
					}
					longHaltEvents++;
					totalHaltTime = totalHaltTime + haltTime;

					endLocationLattitude = location.getLatitude();
					endLocationLongitude = location.getLongitude();
				}

				counter++;

				prevspeed = speed;
				prevLocation = location;

			}

			haltDuration = DateUtil.getFormattedStringTime(totalHaltTime);
			moveDuration = DateUtil.getFormattedStringTime(moveTime);
			totalMovement = StringUtil.formatDecimal(totalMovementCalc);
			avgMoveSpeed = StringUtil.formatDecimal((moveTime > 0 ? (totalMovementCalc / DateUtil.getHoursFromMilliseconds(moveTime)) : 0));
			maxSpeed = StringUtil.formatDecimal(maxSpeedCalc) + "";

			FleetReportModel rm = new FleetReportModel(id, vehicle, haltDuration, moveDuration, startLocation, endLocation, totalMovement, avgMoveSpeed,
					maxSpeed, overSpeedEvents + "", longHaltEvents + "", idlingEvents + "", endLocationLattitude, endLocationLongitude);

			reportData.add(rm);
		}
		return reportData;
	}

	public List<HaltReportModel> getHaltSummary(String userId, SearchDTO searchDTO) {
		List<HaltReportModel> reportData = new ArrayList<HaltReportModel>();

		Long deviceId;
		String vehicleRegistrationNumber;
//		setDatabaseSchema(searchDTO);
		List<Fleet> fleet = null;
		if (searchDTO.getVehicleId() == 0) {
			fleet = fleetDAO.findFleetListByUser(userId);
		} else {
			fleet = fleetDAO.findFleetListById(searchDTO.getVehicleId());
		}
		for (Fleet ft : fleet) {
			Map<Long, String> fleetMap = reportDAO.getFleetAndDeviceForUserAndDevice(userId, ft.getId());
			Iterator<Long> fleetMapItr = fleetMap.keySet().iterator();

			if (fleetMapItr.hasNext()) {
				deviceId = fleetMapItr.next();
				vehicleRegistrationNumber = fleetMap.get(deviceId);
				reportData.addAll(getHaltRecords(searchDTO, deviceId, vehicleRegistrationNumber));
			}
		}
		return reportData;

	}

	private List<HaltReportModel> getHaltRecords(SearchDTO searchDTO, Long deviceId, String vehicleRegistrationNumber) {
		List<HaltReportModel> reportData = new ArrayList<HaltReportModel>();
		String startTime;
		String endTime;
		String haltDurationDays;

		List<LocationDTO> locationsList = reportDAO.getPositionsForDevice(searchDTO, deviceId);

		double speed = 0;
		double prevspeed = 0;
		LocationDTO prevLocation = null;

		String startLoc = "";
		Date startHaltTime = null;

		Date endHaltTime = null;
		int counter = 0;
		long haltDuration = 0;
		int maxLocations = locationsList.size();
		for (LocationDTO location : locationsList) {

			speed = location.getSpeed();

			if (speed == 0 && (prevspeed > 0 || prevLocation == null)) {
				//startLoc = location.getAddress(); 
				startLoc = null;
				if (!StringUtil.isValid(startLoc) && applicationConfig.isReverseGeocodeEnable()) {
					startLoc = reverseGeocodeAddres(location.getLatitude() + "", location.getLongitude() + "");
				} else if (startLoc == null) {
					startLoc = "http://maps.google.com/maps?q=" + location.getLatitude() + "," + location.getLongitude();
				}
				if (counter == 0) {
					startHaltTime = searchDTO.getFromDate().getTimestampDate();
				} else {
					startHaltTime = location.getFixTime();
				}
			} else if (prevspeed == 0 && speed > 0) {
				endHaltTime = location.getFixTime();
			}

			if (startHaltTime != null && endHaltTime != null && DateUtil.getDateTimeDiffInMinutes(startHaltTime, endHaltTime) >= searchDTO.getHaltLimit()) {
				haltDuration = (long) (endHaltTime.getTime() - startHaltTime.getTime());
				haltDurationDays = DateUtil.getFormattedStringTime(haltDuration);

				startTime = DateUtil.formatDate(startHaltTime, "dd/MM/yyyy hh:mm a z");
				endTime = DateUtil.formatDate(endHaltTime, "dd/MM/yyyy hh:mm a z");
				// https://www.google.com/maps?q=28.668424,77.375107
				// String latLong = startLoc.substring(startLoc.indexOf('='),
				// startLoc.length());
				double lat = Double.parseDouble(startLoc.substring(startLoc.indexOf('=') + 1, startLoc.indexOf(',')));
				double lon = Double.parseDouble(startLoc.substring(startLoc.indexOf(',') + 1, startLoc.length()));
				HaltReportModel rm = new HaltReportModel(deviceId, vehicleRegistrationNumber, startTime, endTime, haltDurationDays, startLoc, lat, lon,
						haltDuration);
				reportData.add(rm);
				haltDurationDays = null;
				startHaltTime = null;
				endHaltTime = null;
			}
			if ((maxLocations - 1) == counter
					&& DateUtil.getDateTimeDiffInMinutes(location.getFixTime(), searchDTO.getToDate().getTimestampDate()) >= searchDTO.getHaltLimit()) {
				haltDuration = (long) (searchDTO.getToDate().getTimestampDate().getTime() - location.getFixTime().getTime());
				haltDurationDays = DateUtil.getFormattedStringTime(haltDuration);
				startLoc = "http://maps.google.com/maps?q=" + location.getLatitude() + "," + location.getLongitude();
				startTime = DateUtil.formatDate(location.getFixTime(), "dd/MM/yyyy hh:mm a z");
				endTime = DateUtil.formatDate(searchDTO.getToDate().getTimestampDate(), "dd/MM/yyyy hh:mm a z");
				HaltReportModel rm = new HaltReportModel(deviceId, vehicleRegistrationNumber, startTime, endTime, haltDurationDays, startLoc,
						location.getLatitude(), location.getLongitude(), haltDuration);
				reportData.add(rm);
			}
			prevspeed = speed;
			prevLocation = location;
			counter++;
		}
		
		return reportData;
	}

	public List<OverSpeedingReportModel> getOverSpeedingSummary(String userId, SearchDTO searchDTO) {
		List<OverSpeedingReportModel> reportData = new ArrayList<OverSpeedingReportModel>();

		Long deviceId;
		String vehicleId;
		String startTime;
		String endTime;
		String overSpeedDurationMins;
		String overSpeedMovement;
//		setDatabaseSchema(searchDTO);
		List<Fleet> fleets = null;
		if (searchDTO.getVehicleId() == 0) {
			fleets = fleetDAO.findFleetListByUser(userId);
		} else {
			fleets = fleetDAO.findFleetListById(searchDTO.getVehicleId());
		}
		for (Fleet ft : fleets) {
			// Map<Long, String> fleetMap =
			// reportDAO.getFleetAndDeviceForUserAndDevice(userId, ft.getId());
			// Iterator<Long> fleetMapItr = fleetMap.keySet().iterator();

			int speedLimit = 0;
			if (searchDTO.getVehicleId() == 0) { // Default speed
				speedLimit = reportDAO.getSpeedForDevice(ft.getRouteFleetDeviceXREF().getDevice().getId()).getSpeedLimit();
			} else {
				speedLimit = searchDTO.getSpeedLimit();
			}
			searchDTO.setSpeedLimit(speedLimit);

			// while (fleetMapItr.hasNext()) {
			deviceId = ft.getRouteFleetDeviceXREF().getDevice().getId();
			vehicleId = ft.getRegNumber();

			List<LocationDTO> locationsList = reportDAO.getPositionsForDevice(searchDTO, deviceId);

			double speed = 0;
			double prevspeed = 0;
			LocationDTO prevLocation = null;
			double Hspeed = 0;
			String startLoc = "";
			String endLoc = "";
			Date startOverSpeedTime = null;
			Date endOverSpeedTime = null;

			double totalMovementCalc = 0;

			for (LocationDTO location : locationsList) {

				speed = location.getSpeed();

				if (speed > searchDTO.getSpeedLimit() && prevspeed < searchDTO.getSpeedLimit()) {
					startLoc = location.getAddress();
					if (!StringUtil.isValid(startLoc) && applicationConfig.isReverseGeocodeEnable()) {
						startLoc = reverseGeocodeAddres(location.getLatitude() + "", location.getLongitude() + "");
					} else if (startLoc == null) {
						startLoc = location.getLatitude() + "," + location.getLongitude();
					}

					startOverSpeedTime = location.getFixTime();
				} else if (prevspeed > searchDTO.getSpeedLimit() && speed > searchDTO.getSpeedLimit()) {
					totalMovementCalc += DistanceUtils.distance(location.getLatitude(), location.getLongitude(),
							prevLocation.getLatitude(), prevLocation.getLongitude(), "K");
					Hspeed = speed;
				} else if (prevspeed > searchDTO.getSpeedLimit() && speed < searchDTO.getSpeedLimit()) {
					endLoc = location.getAddress();
					if (!StringUtil.isValid(endLoc) && applicationConfig.isReverseGeocodeEnable()) {
						endLoc = reverseGeocodeAddres(location.getLatitude() + "", location.getLongitude() + "");
					} else if (endLoc == null) {
						endLoc = location.getLatitude() + "," + location.getLongitude();
					}
					Hspeed = prevspeed;
					endOverSpeedTime = location.getFixTime();
					totalMovementCalc += DistanceUtils.distance(location.getLatitude(), location.getLongitude(),
							prevLocation.getLatitude(), prevLocation.getLongitude(), "K");
				}

				if (startOverSpeedTime != null && endOverSpeedTime != null && prevspeed >= searchDTO.getSpeedLimit()) {

					overSpeedDurationMins = DateUtil
							.getFormattedStringTime(endOverSpeedTime.getTime() - startOverSpeedTime.getTime());
					overSpeedMovement = StringUtil.formatDecimal(totalMovementCalc);
					startTime = DateUtil.formatDate(startOverSpeedTime, "dd/MM/yyyy hh:mm a z");
					endTime = DateUtil.formatDate(endOverSpeedTime, "dd/MM/yyyy hh:mm a z");
					String path = getPathUrl(startLoc, endLoc);
					startLoc = "http://maps.google.com/maps?q=" + startLoc;
					endLoc = "http://maps.google.com/maps?q=" + endLoc;

					OverSpeedingReportModel rm = new OverSpeedingReportModel(deviceId, vehicleId, Hspeed, startTime,
							endTime, overSpeedDurationMins, overSpeedMovement, startLoc, endLoc, path, speedLimit);
					reportData.add(rm);
					totalMovementCalc = 0;
					startOverSpeedTime = null;
					endOverSpeedTime = null;

				}
				prevspeed = speed;
				prevLocation = location;
			}
			// }
		}
		return reportData;
	}

	public List<NotificationStatus> getMessageSummaryReport(String userId, SearchDTO searchDTO) {
		List<NotificationStatusModel> reportData = new ArrayList<NotificationStatusModel>();
		searchDTO.setToDate(new SpiDateTime(searchDTO.getFromDate()));
		RouteScheduleDetails routeScheduleDetails = routeDAO.findRouteScheduleDetailByPrimaryKey(searchDTO.getSchedule());
		if (routeScheduleDetails != null) {
			searchDTO.getFromDate().getTimestampDate().setHours(routeScheduleDetails.getStartTime().getHours());
			searchDTO.getFromDate().getTimestampDate().setMinutes(routeScheduleDetails.getStartTime().getMinutes());
		}
		List<WayPointVM> pointVMs = reportDAO.getWaypointsForVehicle(searchDTO.getVehicleId(), null);
		List<NotificationStatus> notificationStatuses = notificationStatusDAO.findAll();
		System.out.println(notificationStatuses);
		return notificationStatuses;
	}

	public List<BusStopTimeReportModel> getBusStopTimeReport(String userId, SearchDTO searchDTO) {
		List<BusStopTimeReportModel> reportData = new ArrayList<BusStopTimeReportModel>();

		searchDTO.setToDate(new SpiDateTime(searchDTO.getFromDate()));
		RouteScheduleDetails routeScheduleDetails = routeDAO.findRouteScheduleDetailByPrimaryKey(searchDTO.getSchedule());

		if (routeScheduleDetails != null) {
			searchDTO.getFromDate().getTimestampDate().setHours(routeScheduleDetails.getStartTime().getHours());
			searchDTO.getFromDate().getTimestampDate().setMinutes(routeScheduleDetails.getStartTime().getMinutes());
			searchDTO.getToDate().getTimestampDate().setHours(routeScheduleDetails.getEndTime().getHours());
			searchDTO.getToDate().getTimestampDate().setMinutes(routeScheduleDetails.getEndTime().getMinutes());

		}
//		setDatabaseSchema(searchDTO);
		long deviceId;
		String vehicleId;
		long schoolId=reportDAO.getSchoolByUserIdForAdmin(userId);
		List<Student> students=routeDAO.findStudentsForVehicle((searchDTO.getVehicleId()));
		String schoolSession = (schoolConfigDAO.findBySchool(reportDAO.getSchoolByUserIdForAdmin(userId), SystemConstant.SCHOOL_CONFIG_SEASON_KEY).getValue());
		Map<Long, String> fleetMap = reportDAO.getFleetAndDeviceForUserAndDevice(userId, searchDTO.getVehicleId());
		Iterator<Long> fleetMapItr = fleetMap.keySet().iterator();

		while (fleetMapItr.hasNext()) {
			deviceId = fleetMapItr.next();
			vehicleId = fleetMap.get(deviceId);
			for(Student st:students){
			List<WayPointVM> pointVMs = reportDAO.getWaypointsForVehicle(searchDTO.getVehicleId(), st.getId());
			List<LocationDTO> locationsList = reportDAO.getPositionsForDevice(searchDTO, deviceId);

			for (WayPointVM pointVM : pointVMs) {
				LocationDTO reachLocation = null;
				LocationDTO moveAwayLocation = null;
				boolean flag = false;
				Double distance = null;
				for (LocationDTO location : locationsList) {
					// System.out.println(pointVM.getSequenceNumber() + " count
					// = "+ count + " loc =" + location.getId() + " " +
					// location.getFixTime());
					distance = DistanceUtils.distance(pointVM.getLatitude(), pointVM.getLongitude(), location.getLatitude(), location.getLongitude(), "K");
					if (!flag && distance.compareTo(distanceFromStop) <= 0) {
						flag = true;
						reachLocation = location;
					}
					if (flag && distance.compareTo(distanceFromStop) >= 0) {
						moveAwayLocation = location;
						break;
					}

				}

				BusStopTimeReportModel rm = null;
				String reachTime = null;
				String stopStayDurationMins = null;
				if (reachLocation != null && moveAwayLocation != null) {
					reachTime = DateUtil.formatDate(reachLocation.getFixTime(), "hh:mm:ss a");
					stopStayDurationMins = DateUtil.getFormattedStringTime(moveAwayLocation.getFixTime().getTime() - reachLocation.getFixTime().getTime());
				} else if (reachLocation == null && moveAwayLocation == null) {
					reachTime = "-";
					stopStayDurationMins = "-";
				}
				Date schedulePickUpTime = SystemConstant.SCHOOL_CONFIG_SESSION_VALUE_SUMMER.equals(schoolSession) ? pointVM.getPickTimeSummer()
						: pointVM.getWinterPickup();

				rm = new BusStopTimeReportModel(deviceId, vehicleId, reachTime, stopStayDurationMins, getMapUrl(pointVM), pointVM, schedulePickUpTime);

				reportData.add(rm);
			}}
		}
		Collections.sort(reportData);
		return reportData;
	}

	private List<HaltReportModel> getFilteredHaltRecords(SearchDTO searchDTO, Long deviceId, String vehicleRegistrationNumber, long sec) {
		List<HaltReportModel> haltReportModels = getHaltRecords(searchDTO, deviceId, vehicleRegistrationNumber);
		List<HaltReportModel> filteredModels = new ArrayList<HaltReportModel>();
		for (HaltReportModel haltReportModel : haltReportModels) {
			if ((haltReportModel.getHaltduration() / 1000) >= sec) {
				filteredModels.add(haltReportModel);
			}
		}
		return filteredModels;
	}

	public List<PredictiveStopReportModel> getBusStopPredictiveReport(String userId, SearchDTO searchDTO) {

		searchDTO.setToDate(new SpiDateTime(searchDTO.getFromDate()));
		RouteScheduleDetails routeScheduleDetails = routeDAO.findRouteScheduleDetailByPrimaryKey(searchDTO.getSchedule());

		if (routeScheduleDetails != null) {
			searchDTO.getFromDate().getTimestampDate().setHours(routeScheduleDetails.getStartTime().getHours());
			searchDTO.getFromDate().getTimestampDate().setMinutes(routeScheduleDetails.getStartTime().getMinutes());
			searchDTO.getToDate().getTimestampDate().setHours(routeScheduleDetails.getEndTime().getHours());
			searchDTO.getToDate().getTimestampDate().setMinutes(routeScheduleDetails.getEndTime().getMinutes());
		}

		long deviceId;
		String vehicleId;
		Map<Long, String> fleetMap = reportDAO.getFleetAndDeviceForUserAndDevice(userId, searchDTO.getVehicleId());
		Iterator<Long> fleetMapItr = fleetMap.keySet().iterator();
		HashMap<Integer, List<HaltReportModel>> haltDataByDate = new HashMap<Integer, List<HaltReportModel>>();
		if (fleetMapItr.hasNext()) {
			deviceId = fleetMapItr.next();
			vehicleId = fleetMap.get(deviceId);
			searchDTO.setHaltLimit(0);// Setting halt limit to 0 so as to get
										// all the stop locations.
			for (int i = 0; i < 10; i++) {
//				setDatabaseSchema(searchDTO);
				List<HaltReportModel> list = getFilteredHaltRecords(searchDTO, deviceId, vehicleId, 20);
				haltDataByDate.put(i, list);
				searchDTO.setFromDate(new SpiDateTime(DateUtil.addDaysToDate(searchDTO.getFromDate().getTimestampDate(), -1)));
				searchDTO.setToDate(new SpiDateTime(DateUtil.addDaysToDate(searchDTO.getToDate().getTimestampDate(), -1)));
			}
		}
		return compilePredictiveStopData(getCommonBusStops(haltDataByDate));
	}

	private List<PredictiveStopReportModel> compilePredictiveStopData(Map<Integer, Set<HaltReportModel>> predictiveStop) {
		Iterator<Integer> stopIterator = predictiveStop.keySet().iterator();
		Set<HaltReportModel> haltReportModels = null;
		PredictiveStopReportModel psrModel = null;
		double latitude = 0, longitude = 0;
		List<PredictiveStopReportModel> reportList = new ArrayList<PredictiveStopReportModel>();
		while (stopIterator.hasNext()) {
			int stopSequence = stopIterator.next();
			haltReportModels = predictiveStop.get(stopSequence);
			psrModel = new PredictiveStopReportModel();
			String stopTimes = "";
			psrModel.setStopSequence(stopSequence + 1);
			psrModel.setMatchingInstances(haltReportModels.size());
			double totalLatitude = 0, totalLongitude = 0;
			int count = 0;
			for (HaltReportModel haltReportModel : haltReportModels) {
				psrModel.setId(haltReportModel.getId());
				psrModel.setVehicleNumber(haltReportModel.getVehicle());

				stopTimes = (count == 0 ? "" : stopTimes + ", ") + haltReportModel.getStartTime();

				totalLatitude = totalLatitude + haltReportModel.getLatitude();
				totalLongitude = totalLongitude + haltReportModel.getLongitude();
				count++;
			}
			psrModel.setStopTimes(stopTimes);
			latitude = totalLatitude / count;
			longitude = totalLongitude / count;
			psrModel.setLocationUrl("https://www.google.com/maps?q=" + latitude + "," + longitude);
			reportList.add(psrModel);

		}
		return reportList;
	}

	private Map<Integer, Set<HaltReportModel>> getCommonBusStops(HashMap<Integer, List<HaltReportModel>> haltDataByDate) {
		Map<Integer, List<HaltReportModel>> haltDataByDateDup = (HashMap<Integer, List<HaltReportModel>>) haltDataByDate.clone();
		Map<Integer, Set<HaltReportModel>> predictiveStop = new HashMap<Integer, Set<HaltReportModel>>();
		int count = 0;
		Set<HaltReportModel> list = null;
		double distance = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = i + 1; j < 10; j++) {
				List<HaltReportModel> haltReportModels = haltDataByDate.get(i);
				for (HaltReportModel haltReportModel : haltReportModels) {
					List<HaltReportModel> haltReportModelsDup = haltDataByDateDup.get(j);
					for (HaltReportModel haltReportModelDup : haltReportModelsDup) {
						distance = DistanceUtils.distance(haltReportModel.getLatitude(), haltReportModel.getLongitude(), haltReportModelDup.getLatitude(),
								haltReportModelDup.getLongitude(), "K");
						if (distance <= predictDistancebtwnStops) {
							if (i == 0 && j == 1) {
								list = new HashSet<HaltReportModel>();
								list.add(haltReportModel);
								list.add(haltReportModelDup);
								predictiveStop.put(count++, list);
							} else {
								if (!addUpdatePredictiveBusStop(predictiveStop, haltReportModelDup)) {
									list = new HashSet<HaltReportModel>();
									list.add(haltReportModel);
									list.add(haltReportModelDup);
									predictiveStop.put(count++, list);
								}
							}
						}
					}
				}
			}
		}
		return predictiveStop;
	}

	private boolean addUpdatePredictiveBusStop(Map<Integer, Set<HaltReportModel>> predictiveStop, HaltReportModel haltReportModel) {
		Iterator<Integer> iterator = predictiveStop.keySet().iterator();
		Set<HaltReportModel> haltReportModels;
		double distance;
		boolean isMatchFound = false;
		while (iterator.hasNext()) {
			haltReportModels = predictiveStop.get(iterator.next());
			isMatchFound = false;
			for (HaltReportModel reportModel : haltReportModels) {
				distance = DistanceUtils.distance(haltReportModel.getLatitude(), haltReportModel.getLongitude(), reportModel.getLatitude(),
						reportModel.getLongitude(), "K");
				if (distance <= predictDistancebtwnStops) {
					isMatchFound = true;
					break;
				}
			}
			if (isMatchFound) {
				haltReportModels.add(haltReportModel);
				break;
			}
		}
		return isMatchFound;
	}

	public List<BusStopTimeReportModel> getStudentStopTimeReport(String userId, SearchDTO searchDTO) {
		List<BusStopTimeReportModel> reportData = new ArrayList<BusStopTimeReportModel>();

		long deviceId;
		String vehicleId;
//		setDatabaseSchema(searchDTO);
		Map<Long, String> fleetMap = reportDAO.getFleetAndDeviceForUserAndDevice(userId, searchDTO.getVehicleId());
		Iterator<Long> fleetMapItr = fleetMap.keySet().iterator();
		searchDTO.setToDate(new SpiDateTime(DateUtils.addDays(searchDTO.getFromDate().getTimestampDate(), 1)));

		while (fleetMapItr.hasNext()) {
			deviceId = fleetMapItr.next();
			vehicleId = fleetMap.get(deviceId);
			List<WayPointVM> pointVMs = reportDAO.getWaypointsForVehicle(searchDTO.getVehicleId(), searchDTO.getStudentId());
			List<LocationDTO> locationsList = reportDAO.getPositionsForDevice(searchDTO, deviceId);

			for (WayPointVM pointVM : pointVMs) {
				LocationDTO reachLocation = null;
				LocationDTO moveAwayLocation = null;
				boolean flag = false;
				Double distance = null;
				for (LocationDTO location : locationsList) {
					// System.out.println(pointVM.getSequenceNumber() + " count
					// = "+ count + " loc =" + location.getId() + " " +
					// location.getFixTime());
					distance = DistanceUtils.distance(pointVM.getLatitude(), pointVM.getLongitude(), location.getLatitude(), location.getLongitude(), "K");
					if (!flag && distance.compareTo(distanceFromStop) <= 0) {
						flag = true;
						reachLocation = location;
					}
					if (flag && distance.compareTo(distanceFromStop) >= 0) {
						moveAwayLocation = location;
						break;
					}
				}

				String reachTime = null;
				String stopStayDurationMins = null;
				if (reachLocation != null && moveAwayLocation != null) {
					reachTime = DateUtil.formatDate(reachLocation.getFixTime(), "hh:mm:ss a");
					stopStayDurationMins = DateUtil.getFormattedStringTime(moveAwayLocation.getFixTime().getTime() - reachLocation.getFixTime().getTime());
				} else if (reachLocation == null && moveAwayLocation == null) {
					reachTime = "-";
					stopStayDurationMins = "-";
				}
				reportData.add(new BusStopTimeReportModel(deviceId, vehicleId, reachTime, stopStayDurationMins, getMapUrl(pointVM), pointVM, null));
			}
		}
		return reportData;
	}

	public Map<String, Object> getPositionSummary(HttpServletRequest httpRequest, String userId, SearchDTO searchDTO) {
		List<PositionReportModel> reportData = new ArrayList<PositionReportModel>();

		Map<String, Object> reportDataMap = new HashMap<String, Object>();
//		setDatabaseSchema(searchDTO);
		Map<Long, String> fleetMap = reportDAO.getFleetAndDeviceForUserAndDevice(userId, searchDTO.getVehicleId());

		Long deviceId;
		double totalLat = 0;
		double totalLong = 0;
		double maxLat = 0;
		double maxLong = 0;
		double minLat = 2000;
		double minLong = 2000;
		LOG.debug("Search Filters FromDate:: " + searchDTO.getFromDate() + "  speed::" + searchDTO.getSpeedLimit());

		Iterator<Long> fleetMapItr = fleetMap.keySet().iterator();

		while (fleetMapItr.hasNext()) {
			int locSize = 0;
			deviceId = fleetMapItr.next();
			String vehicleRegNumber = fleetMap.get(deviceId);

			List<LocationDTO> locationsList = reportDAO.getPositionsForDevice(searchDTO, deviceId);

			LocationDTO location = null;

			double startLat = 0;
			double startLng = 0;

			double endLat = 0;
			double endLng = 0;

			locSize = locationsList.size();
			ArrayList<LinkedHashMap<String, Double>> paths = new ArrayList<LinkedHashMap<String, Double>>();

			for (int i = 0; i < locSize; i++) {
				location = locationsList.get(i);
				LinkedHashMap<String, Double> latLongList = new LinkedHashMap<String, Double>();

				if (i == 0) {
					startLat = location.getLatitude();
					startLng = location.getLongitude();
				}
				if (i == locSize - 1) {
					endLat = location.getLatitude();
					endLng = location.getLongitude();
				}
				if (maxLat < location.getLatitude())
					maxLat = location.getLatitude();
				if (maxLong < location.getLongitude())
					maxLong = location.getLongitude();
				if (minLat > location.getLatitude())
					minLat = location.getLatitude();
				if (minLong > location.getLongitude())
					minLong = location.getLongitude();

				latLongList.put("lat", location.getLatitude());
				latLongList.put("lng", location.getLongitude());
				totalLat = totalLat + location.getLatitude();
				totalLong = totalLong + location.getLongitude();
				paths.add(latLongList);

				PositionReportModel reportModel = new PositionReportModel(deviceId, vehicleRegNumber, location.getFixTime(), location.getLongitude(),
						location.getLatitude(), location.getSpeed(), location.getCourse());
				reportData.add(reportModel);
			}

			double avgLat = totalLat / locSize;
			double avgLong = totalLong / locSize;

			double latDiff = (maxLat - avgLat) > (avgLat - minLat) ? (maxLat - avgLat) : (avgLat - minLat);
			double longDiff = (maxLong - avgLong) > (avgLong - minLong) ? (maxLong - avgLong) : (avgLong - minLong);
			int zoom = (int) Math.round((latDiff > longDiff ? latDiff : longDiff) * 250);

			reportDataMap.put("positionReportList", reportData);
			reportDataMap.put("positionReportPath", paths);

			reportDataMap.put("startLat", startLat);
			reportDataMap.put("startLng", startLng);
			reportDataMap.put("endLat", endLat);
			reportDataMap.put("endLng", endLng);
			reportDataMap.put("cLatLong", avgLat + "," + avgLong);
			reportDataMap.put("zoom", zoom);
		}

		httpRequest.getSession().setAttribute("POSITION_REPORT_DATA", reportData);
		return reportDataMap;
	}

	public Map<String, Object> getTripSummary(HttpServletRequest httpRequest, String userId, SearchDTO searchDTO) {
		LOG.debug("Inside getTripSummary");
		List<TripSummaryReportModel> reportData = new ArrayList<TripSummaryReportModel>();

		Map<String, Object> reportDataMap = new HashMap<String, Object>();
//		setDatabaseSchema(searchDTO);
		Map<Long, String> fleetMap = reportDAO.getFleetAndDeviceForUserAndDevice(userId, searchDTO.getVehicleId());

		Long deviceId;
		String point = null;
		String tripLegDuration = null;
		String timeFrom = null;
		String timeTo = null;
		String vehicleLocation = null;
		String aggrHaltDur = null;
		String aggrMoveDur = null;

		DecimalFormat pointFormatter = new DecimalFormat("0.0");

		DecimalFormat pointFormatter2 = new DecimalFormat("0");

		Iterator<Long> fleetMapItr = fleetMap.keySet().iterator();

		while (fleetMapItr.hasNext()) {
			deviceId = fleetMapItr.next();

			List<LocationDTO> locationsList = reportDAO.getPositionsForDevice(searchDTO, deviceId);

			double currentSpeed = 0;
			double prevspeed = 0;

			LocationDTO currentRow = null;
			LocationDTO previousRow = null;

			Date haltStartTime = null;
			Date haltEndTime = null;
			Date runStartTime = null;
			Date insertedRunStartTime = null;
			Date runEndTime = null;

			long aggrHaltTime = 0;
			long aggrMoveTime = 0;
			long aggrDistanceCalc = 0;
			long runningDurationLenght = 0;
			long stoppageDurationLenght = 0;
			String ignition = "Key On";

			double startLat = 0;
			double startLng = 0;
			double endLat = 0;
			double endLng = 0;

			float uiRowCounter = 0.0f;

			StopageDuration stopageDuration = StopageDuration.started;
			RunningDuration runningDuration = RunningDuration.notStarted;

			int locSize = locationsList.size();
			List<LinkedHashMap<String, Double>> paths = new ArrayList<LinkedHashMap<String, Double>>();

			TripSummaryReportModel runningStatusStartRow = null;
			TripSummaryReportModel runningStatusEndRow = null;
			TripSummaryReportModel removedRunningStatusEndRow = null;

			Boolean runningEndLegInserted = false;
			Boolean runningEndLegRemoved = false;

			for (int rowCount = 0; rowCount < locSize; rowCount++) {

				if (rowCount == 5) {
					LOG.debug("DEBUG");
				}

				LOG.debug("reading row-->" + rowCount);
				currentRow = locationsList.get(rowCount);
				if (rowCount > 0) {
					previousRow = locationsList.get(rowCount - 1);
				}

				if (currentRow.getAttributes().contains("\"ignition\":false")) {
					ignition = "Key Off";
				} else if (currentRow.getAttributes().contains("\"ignition\":true")) {
					ignition = "Key On";
				}

				currentSpeed = currentRow.getSpeed();

				if (currentSpeed != 0 && prevspeed == 0) {
					LOG.debug("currentSpeed != 0 && prevspeed == 0");
					LOG.debug("stoppage duration ended");
					stopageDuration = StopageDuration.ended;
					LOG.debug("capturing run start time");
					runStartTime = currentRow.getFixTime();
					LOG.debug("run start time-->" + runStartTime);
					LOG.debug("running duration started\n\n\n");
					runningDuration = RunningDuration.started;
				}

				if (currentSpeed == 0 && prevspeed != 0) {
					LOG.debug("currentSpeed == 0&& prevspeed != 0");
					LOG.debug("stoppage duration started");
					stopageDuration = StopageDuration.started;
					LOG.debug("capturing halt start time");
					haltStartTime = currentRow.getFixTime();
					LOG.debug("halt start time-->" + haltStartTime);
					LOG.debug("running duration ended\n\n\n");
					runningDuration = RunningDuration.ended;

				}

				if (prevspeed == 0 && currentSpeed == 0) {
					runningDuration = RunningDuration.notStarted;
					stopageDuration = StopageDuration.inProgress;
				}
				if (prevspeed != 0 && currentSpeed != 0) {
					stopageDuration = StopageDuration.notStarted;
					runningDuration = RunningDuration.inProgress;
				}

				if (rowCount == 0) {
					LOG.debug("first record--->starting position");
					stopageDuration = StopageDuration.started;
					runningDuration = RunningDuration.notStarted;
					haltStartTime = currentRow.getFixTime();
					LOG.debug("halt start time-->" + haltStartTime);
					startLat = currentRow.getLatitude();
					startLng = currentRow.getLongitude();
				}

				if (stopageDuration == StopageDuration.ended && runningDuration == RunningDuration.started) {

					LOG.debug("run duration started and stoppage duration ended");
					LOG.debug("capturing halt end time-->");
					haltEndTime = currentRow.getFixTime();

					LOG.debug("halt start time-->" + haltStartTime);
					LOG.debug("halt end time-->" + haltEndTime);

					stoppageDurationLenght = (long) haltEndTime.getTime() - haltStartTime.getTime();

					LOG.debug("stoppage Duration Lenght-->" + stoppageDurationLenght);

					if (stoppageDurationLenght > applicationConfig.getReportDurationIgnore()) {

						if (runningEndLegRemoved == true && runningEndLegInserted == false) {
							LOG.debug("\n\n");
							LOG.debug("inserting removed run duration end row");
							reportData.add(removedRunningStatusEndRow);
							runningEndLegRemoved = false;
						}

						LOG.debug("condition passed..creating stop duration row");
						LOG.debug("vehicleStatus set to started");

						uiRowCounter++;
						point = pointFormatter2.format(uiRowCounter);
						LOG.debug("point-->" + point);

						timeFrom = DateUtil.formatDate(haltStartTime, "dd/MM/yyyy hh:mm a z");
						timeTo = DateUtil.formatDate(haltEndTime, "dd/MM/yyyy hh:mm a z");

						tripLegDuration = DateUtil.getFormattedStringTimeWithSec((long) (haltEndTime.getTime() - haltStartTime.getTime()));

						LOG.debug("tripLegDuration-->" + tripLegDuration);
						aggrHaltTime += (long) (haltEndTime.getTime() - haltStartTime.getTime());
						aggrHaltDur = DateUtil.getFormattedStringTime(aggrHaltTime);

						LOG.debug("inserting stop duration row\n\n\n");
						TripSummaryReportModel stoppedDurationRow = new TripSummaryReportModel(deviceId, point, tripLegDuration, timeFrom, timeTo,
								vehicleLocation, "Long Halt, Idling," + ignition, StringUtil.formatDecimal(prevspeed) + " km/h",
								StringUtil.formatDecimal(aggrDistanceCalc) + " kms", aggrHaltDur, aggrMoveDur);
						reportData.add(stoppedDurationRow);

						LOG.debug("preparing run duration start row");

						uiRowCounter = (float) Math.floor(uiRowCounter);
						uiRowCounter = uiRowCounter + 1.1f;
						point = pointFormatter.format(uiRowCounter);
						LOG.debug("point-->" + point);

						LOG.debug("run start time-->" + runStartTime);

						insertedRunStartTime = runStartTime;

						timeFrom = DateUtil.formatDate(runStartTime, "dd/MM/yyyy hh:mm a z");
						timeTo = "";
						tripLegDuration = "0 sec";
						LOG.debug("tripLegDuration hard coded-->" + tripLegDuration + "\n\n\n");

						startLat = currentRow.getLatitude();
						startLng = currentRow.getLongitude();
						vehicleLocation = currentRow.getAddress();

						LOG.debug("--->inserting run duration start row\n\n\n");
						runningStatusStartRow = new TripSummaryReportModel(deviceId, point, tripLegDuration, timeFrom, timeTo, vehicleLocation, ignition,
								StringUtil.formatDecimal(currentSpeed) + " km/h", StringUtil.formatDecimal(aggrDistanceCalc) + " kms", aggrHaltDur,
								aggrMoveDur);
						reportData.add(runningStatusStartRow);
					} else {

						LOG.debug("condition failed..ignoring stop duration\n\n\n");
						if (rowCount > 0 && runningEndLegInserted == true) {
							LOG.debug("removing run duration end row");
							removedRunningStatusEndRow = reportData.remove(reportData.size() - 1);
							uiRowCounter = uiRowCounter - 0.1f;
							runningEndLegRemoved = true;
							runningEndLegInserted = false;
						}
					}
					LOG.debug("\n\n");
				}

				if (stopageDuration == StopageDuration.started && runningDuration == RunningDuration.ended) {

					LOG.debug("stoppage duration started and run duration ended");

					if (rowCount > 0) {
						runEndTime = previousRow.getFixTime();
					}
					LOG.debug("run end time-->" + runEndTime);

					LOG.debug("run start time-->" + runStartTime);
					LOG.debug("run end time-->" + runEndTime);

					if (null != runStartTime && null != runEndTime) {
						runningDurationLenght = (long) runEndTime.getTime() - runStartTime.getTime();
						LOG.debug("running Duration Lenght-->" + runningDurationLenght);
					} else {
						runningDurationLenght = 0;
					}

					if (runningDurationLenght > applicationConfig.getReportDurationIgnore()) {

						LOG.debug("condition passed..creating run duration end row");

						uiRowCounter = uiRowCounter + 0.1f;
						point = pointFormatter.format(uiRowCounter);
						LOG.debug("point-->" + point);

						timeFrom = "";
						timeTo = DateUtil.formatDate(runEndTime, "dd/MM/yyyy hh:mm a z");

						if (null != insertedRunStartTime) {
							tripLegDuration = DateUtil.getFormattedStringTimeWithSec((long) (runEndTime.getTime() - insertedRunStartTime.getTime()));
							LOG.debug("tripLegDuration-->" + tripLegDuration);

							aggrMoveTime += (long) (runEndTime.getTime() - insertedRunStartTime.getTime());
						} else {
							tripLegDuration = DateUtil.getFormattedStringTimeWithSec((long) (runEndTime.getTime() - runStartTime.getTime()));
							LOG.debug("tripLegDuration-->" + tripLegDuration);

							aggrMoveTime += (long) (runEndTime.getTime() - runStartTime.getTime());
						}

						aggrMoveDur = DateUtil.getFormattedStringTime(aggrMoveTime);

						endLat = currentRow.getLatitude();
						endLng = currentRow.getLongitude();
						vehicleLocation = currentRow.getAddress();
						vehicleLocation = currentRow.getAddress();
						if (!StringUtil.isValid(vehicleLocation) && applicationConfig.isReverseGeocodeEnable()) {
							vehicleLocation = reverseGeocodeAddres(currentRow.getLatitude() + "", currentRow.getLongitude() + "");
						}

						aggrDistanceCalc += DistanceUtils.distance(endLat, endLng, startLat, startLng, "K");

						LOG.debug("inserting run duration end row\n\n\n");

						runningStatusEndRow = new TripSummaryReportModel(deviceId, point, tripLegDuration, timeFrom, timeTo, vehicleLocation, ignition,
								StringUtil.formatDecimal(prevspeed) + " km/h", StringUtil.formatDecimal(aggrDistanceCalc) + " kms", aggrHaltDur, aggrMoveDur);
						reportData.add(runningStatusEndRow);
						runningEndLegInserted = true;

					} else {
						LOG.debug("condition failed..ignoring run duration\n\n\n");
					}

					LOG.debug("\n\n");

					startLat = currentRow.getLatitude();
					startLng = currentRow.getLongitude();
				}
				prevspeed = currentSpeed;
				previousRow = currentRow;
			}

			reportDataMap.put("tripSummaryReportList", reportData);
			reportDataMap.put("tripSummaryPath", paths);

			reportDataMap.put("startLat", startLat);
			reportDataMap.put("startLng", startLng);
			reportDataMap.put("endLat", endLat);
			reportDataMap.put("endLng", endLng);

		}

		httpRequest.getSession().setAttribute("TRIP_REPORT_DATA", reportData);
		return reportDataMap;
	}

	public String reverseGeocodeAddres(String lat, String lon) {
		URL url;
		String serviceURL = "http://services.gisgraphy.com/reversegeocoding/search?format=json&lat=" + lat + "&lng=" + lon;
		String address = "";
		try {
			url = new URL(serviceURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String input;
			String response = "";
			while ((input = br.readLine()) != null) {
				response += input;
			}
			br.close();
			JSONObject jsonRespRouteDistance = new JSONObject(response).getJSONArray("result").getJSONObject(0);
			address = jsonRespRouteDistance.getString("formatedFull").toString();

		} catch (Exception e) {
			LOG.error("Error in reverseGeocodeAddres", e);
		}
		return address;
	}

	private String getPathUrl(String startLoc, String endLoc) {
		String path = "http://maps.google.com/maps?saddr=" + startLoc + "&daddr=" + endLoc;
		return path;
	}

	private String getMapUrl(LocationDTO location) {
		return "http://maps.google.com/maps?q=" + location.getLatitude() + "," + location.getLongitude();
	}

	private String getMapUrl(WayPointVM pointVM) {
		return "http://maps.google.com/maps?q=" + pointVM.getLatitude() + "," + pointVM.getLongitude();
	}

//	private void setDatabaseSchema(SearchDTO searchDTO) {
//		Date fromDate = DateUtil.getDateOnly(searchDTO.getFromDate().getTimestampDate());
//		Date today = DateUtil.getDateOnly(new Date());
//		if (fromDate != null && fromDate.equals(today)) {
//			reportDAO.setDatabaseSchema(SystemConstant.SCHOOL_CONFIG_DB_PRODUCTION);
//		} else {
//			reportDAO.setDatabaseSchema(SystemConstant.SCHOOL_CONFIG_DB_BACKUP);
//		}
//	}

	public List<NotificationStatusVM> getMessageSummaryReport(SearchDTO searchDTO) {

		List<NotificationStatus> notification = getAllNotificationDetails(searchDTO);
		List<NotificationStatusVM> notificationModel = new ArrayList<NotificationStatusVM>();
		for (NotificationStatus notificationStatus : notification) {
			Map<Long, String> studentNamesByWayPoint = getStudentNamesByWayPoint(notificationStatus.getScheduleId());
			NotificationStatusVM model = new NotificationStatusVM();
			model.setStartTime(notificationStatus.getStartTime());
			model.setProcessDate(notificationStatus.getProcessDate());
			model.setEndTime(notificationStatus.getEndTime());
			model.setTotalWayPoint(notificationStatus.getTotalWayPoint());
			model.setWayPointNotTouched(notificationStatus.getWayPointNotTouched());
			if(notificationStatus.getWayPointIds() != null){
				List<String> wayPoints = Arrays.asList(notificationStatus.getWayPointIds().replace(" ", "").split(","));
				Collections.sort(wayPoints);
				StringBuffer wayPointDetails = new StringBuffer();
				String studentName = null;
				for (String wayPoint : wayPoints) {
					if (wayPoint == null || wayPoint.equals(""))
						continue;
					wayPointDetails = wayPointDetails.append(wayPoint + "(");
					studentName = studentNamesByWayPoint.get(Long.valueOf(wayPoint));
					wayPointDetails.append(studentName == null ? "" : studentName);
					wayPointDetails.append("), \n");
				}
				model.setWayPointDetails(wayPointDetails.toString());
			}
			notificationModel.add(model);
		}
		return notificationModel;
	}

	private List<NotificationStatus> getAllNotificationDetails(SearchDTO searchDTO) {
		List<NotificationStatus> notificationStatuses = notificationStatusDAO.findNotificationSummaryBySchedule(searchDTO.getFromDate().getTimestampDate(),
				searchDTO.getVehicleId());
		System.out.println(notificationStatuses);
		return notificationStatuses;
	}

	private Map<Long, String> getStudentNamesByWayPoint(int scheduleId) {
		Map<Long, String> studentNamesByWayPoint = new HashMap<Long, String>();
		RouteScheduleDetails routeScheduleDetails = routeDAO.findRouteScheduleDetailByPrimaryKey(scheduleId);
		List<String> studentNameList = null;
		List<WayPoint> wayPointList = routeScheduleDetails.getRoute().getWaypoints();
		for (WayPoint wayPoint : wayPointList) {
			studentNameList = new ArrayList<String>();
			for (Student student : wayPoint.getStudents()) {
				studentNameList.add(student.getFirstName() + " " + student.getLastName());
			}
			studentNamesByWayPoint.put(wayPoint.getId(), studentNameList.stream().collect(Collectors.joining(", ")));
		}
		return studentNamesByWayPoint;

	}

	@Override
	public List<StudentNotificationStatusVM> getStudentNotification(SearchDTO searchDTO, String userUuid) {
		RouteScheduleDetails routeScheduleDetails = routeDAO.findRouteScheduleDetailByPrimaryKey(searchDTO.getSchedule());
		Long schoolId = userService.getSchoolbyUser(userUuid).getSchool().getId();
		boolean isPickSchedule = routeScheduleDetails.getScheduleName().toLowerCase().contains("pick");
		List<StudentNotificationStatusVM> studentModel = reportDAO.getStudentNotification(searchDTO.getFromDate().getTimestampDate(), searchDTO.getSchedule(),
				isPickSchedule, schoolId);
		System.out.println(studentModel);
		return studentModel;
	}

}
