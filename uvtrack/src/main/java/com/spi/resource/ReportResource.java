/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, Jun 2016
 */
package com.spi.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spi.VM.NotificationStatusVM;
import com.spi.VM.StudentNotificationStatusVM;
import com.spi.VM.StudentVM;
import com.spi.config.ApplicationConfig;
import com.spi.service.ReportService;
import com.spi.service.UserService;
import com.spi.user.api.BusStopTimeReportModel;
import com.spi.user.api.FleetModel;
import com.spi.user.api.FleetReportModel;
import com.spi.user.api.HaltReportModel;
import com.spi.user.api.OverSpeedingReportModel;
import com.spi.user.api.PositionReportModel;
import com.spi.user.api.PredictiveStopReportModel;
import com.spi.user.api.RouteModel;
import com.spi.user.api.SearchDTO;
import com.spi.user.api.TripSummaryReportModel;

/**
 */
@Path("/reports")
@Component
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ReportResource {

	Logger LOG = LoggerFactory.getLogger(ReportResource.class);

	@Autowired
	protected ReportService reportService;

	@Autowired
	protected UserService userService;

	@Autowired
	ApplicationConfig config;

	public ReportResource() {
	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("getAllVehicleList/{userId}")
	@GET
	public Response getAllVehicleList(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId) {

		List<FleetModel> fleetList = reportService.findAllFleetList(userId);

		return Response.ok().entity(fleetList).build();

	}

	@RolesAllowed({ "administrator" })
	@Path("getAllRouteList/{userId}")
	@GET
	public Response getAllRouteList(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId) {

		List<RouteModel> fleetList = reportService.getAllRouteList(userId);

		return Response.ok().entity(fleetList).build();

	}

	@RolesAllowed({ "administrator", "superAdmin", "transporter" })
	@Path("getFleetSummary/{userId}")
	@PUT
	public Response getFleetSummary(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId,
			SearchDTO searchDTO) {

		if (searchDTO.getSpeedLimit() == -1 || searchDTO.getHaltLimit() == -1)
			return Response.ok().entity(new ArrayList<FleetReportModel>()).build();

		List<FleetReportModel> reportData = reportService.getFleetSummary(userId, searchDTO);

		httpRequest.getSession().setAttribute("FLEET_REPORT_DATA", reportData);

		return Response.ok().entity(reportData).build();
	}

	// ============================================================//
	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("getMessageSummaryReport/{userId}")
	@PUT
	public Response getMessageSummaryReport(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId,
			SearchDTO searchDTO) {
		searchDTO.getFromDate().getTimestampDate().setHours(0);
		searchDTO.getFromDate().getTimestampDate().setMinutes(0);
		List<NotificationStatusVM> notificationModel = reportService.getMessageSummaryReport(searchDTO);

		return Response.ok().entity(notificationModel).build();

	}

	@RolesAllowed({ "administrator", "superAdmin" })
	@Path("getStudentMessageSummaryReport/{userId}")
	@PUT
	public Response getStudentMessageSummaryReport(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId,
			SearchDTO searchDTO) {

		List<StudentNotificationStatusVM> students = reportService.getStudentNotification(searchDTO, userId);
		httpRequest.getSession().setAttribute("Message_Report_Data", students);
		return Response.ok().entity(students).build();
	}

	// ===========================================================//

	@RolesAllowed({ "administrator", "transporter" })
	@Path("getHaltSummary/{userId}")
	@PUT
	public Response getHaltSummary(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId,
			SearchDTO searchDTO) {

		if (searchDTO.getVehicleId() == -1 || searchDTO.getHaltLimit() == -1)
			return Response.ok().entity(new ArrayList<HaltReportModel>()).build();

		List<HaltReportModel> reportData = reportService.getHaltSummary(userId, searchDTO);

		httpRequest.getSession().setAttribute("HALT_REPORT_DATA", reportData);

		return Response.ok().entity(reportData).build();
	}

	@RolesAllowed({ "administrator", "transporter" })
	@Path("getOverSpeedingSummary/{userId}")
	@PUT
	public Response getOverSpeedingSummary(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId,
			SearchDTO searchDTO) {

		if (searchDTO.getVehicleId() == -1)
			return Response.ok().entity(new ArrayList<OverSpeedingReportModel>()).build();

		List<OverSpeedingReportModel> reportData = reportService.getOverSpeedingSummary(userId, searchDTO);

		httpRequest.getSession().setAttribute("OVERSPEED_REPORT_DATA", reportData);

		return Response.ok().entity(reportData).build();
	}

	@RolesAllowed({ "administrator", "transporter" })
	@Path("getBusStopTimeReport/{userId}")
	@PUT
	public Response getBusStopTimeReport(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId,
			SearchDTO searchDTO) {
		LOG.info("Running getBusStopTimeReport for vehicle " + searchDTO.getVehicleId());

		if (searchDTO.getVehicleId() == -1 || searchDTO.getSchedule() == -1)
			return Response.ok().entity(new ArrayList<BusStopTimeReportModel>()).build();

		List<BusStopTimeReportModel> reportData = reportService.getBusStopTimeReport(userId, searchDTO);

		httpRequest.getSession().setAttribute("BUS_STOP_TIME_REPORT_DATA", reportData);
		return Response.ok().entity(reportData).build();
	}

	@RolesAllowed({ "administrator", "transporter" })
	@Path("getBusStopPredictiveReport/{userId}")
	@PUT
	public Response getBusStopPredictiveReport(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId,
			SearchDTO searchDTO) {
		LOG.info("Running getBusStopTimeReport for vehicle " + searchDTO.getVehicleId());

		if (searchDTO.getVehicleId() == -1 || searchDTO.getSchedule() == -1)
			return Response.ok().entity(new ArrayList<BusStopTimeReportModel>()).build();

		List<PredictiveStopReportModel> reportData = reportService.getBusStopPredictiveReport(userId, searchDTO);

		httpRequest.getSession().setAttribute("BUS_STOP_TIME_REPORT_DATA", reportData);
		return Response.ok().entity(reportData).build();
	}

	@RolesAllowed({ "administrator", "transporter" })
	@Path("getStudentStopTimeReport/{userId}")
	@PUT
	public Response getStudentStopTimeReport(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId,
			SearchDTO searchDTO) {
		LOG.info("Running getStudentStopTimeReport for vehicle " + searchDTO.getVehicleId());

		if (searchDTO.getVehicleId() == -1 || searchDTO.getSchedule() == -1)
			return Response.ok().entity(new ArrayList<BusStopTimeReportModel>()).build();

		List<BusStopTimeReportModel> reportData = reportService.getStudentStopTimeReport(userId, searchDTO);

		httpRequest.getSession().setAttribute("STUDENT_STOP_REPORT_DATA", reportData);
		return Response.ok().entity(reportData).build();
	}

	@RolesAllowed({ "administrator", "transporter" })
	@Path("getPositionSummary/{userId}")
	@PUT
	public Response getPositionSummary(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId,
			SearchDTO searchDTO) {
		LOG.debug("Entering getPositionSummary");

		if (searchDTO.getVehicleId() == -1 || searchDTO.getVehicleId() <= 0)
			return Response.ok().entity(new ArrayList<PositionReportModel>()).build();

		Map<String, Object> reportDataMap = reportService.getPositionSummary(httpRequest, userId, searchDTO);

		return Response.ok().entity(reportDataMap).build();
	}

	@RolesAllowed({ "administrator", "transporter" })
	@Path("getTripSummary/{userId}")
	@PUT
	public Response getTripSummary(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId,
			SearchDTO searchDTO) {

		if (searchDTO.getVehicleId() == -1 || searchDTO.getVehicleId() <= 0)
			return Response.ok().entity(new ArrayList<TripSummaryReportModel>()).build();

		Map<String, Object> reportDataMap = reportService.getTripSummary(httpRequest, userId, searchDTO);

		return Response.ok().entity(reportDataMap).build();
	}

	@GET
	@RolesAllowed({ "administrator", "transporter" })
	@Path("/downloadReport/{userId}/{reportName}")
	public Response downloadReport(@Context SecurityContext sc, @Context HttpServletRequest httpRequest, @PathParam("userId") String userId,
			@PathParam("reportName") String reportName) {

		String fileName = "";

		if ("fleetReport".equals(reportName)) {
			fileName = "fleetSummaryReport_" + userId + ".xls";

			List<FleetReportModel> reportData = (List<FleetReportModel>) httpRequest.getSession().getAttribute("FLEET_REPORT_DATA");

			if (reportData == null || reportData.size() <= 0) {
				return Response.ok(Response.status(500)).build();
			}
			exportFleetReport(reportData, config.getReportDirectory() + fileName);

		} else if ("haltReport".equals(reportName)) {
			fileName = "haltSummaryReport_" + userId + ".xls";

			List<HaltReportModel> reportData = (List<HaltReportModel>) httpRequest.getSession().getAttribute("HALT_REPORT_DATA");

			if (reportData == null || reportData.size() <= 0) {
				return Response.ok(Response.status(500)).build();
			}
			exportHaltReport(reportData, config.getReportDirectory() + fileName);

		} else if ("overSpeedingReport".equals(reportName)) {
			fileName = "overSpeedingReport_" + userId + ".xls";

			List<OverSpeedingReportModel> reportData = (List<OverSpeedingReportModel>) httpRequest.getSession().getAttribute("OVERSPEED_REPORT_DATA");

			if (reportData == null || reportData.size() <= 0) {
				return Response.ok(Response.status(500)).build();
			}
			exportOverSpeedingReport(reportData, config.getReportDirectory() + fileName);

		} else if ("busStopTimeReport".equals(reportName)) {
			fileName = "busStopTimeReport_" + userId + ".xls";

			List<BusStopTimeReportModel> reportData = (List<BusStopTimeReportModel>) httpRequest.getSession().getAttribute("BUS_STOP_TIME_REPORT_DATA");

			if (reportData == null || reportData.size() <= 0) {
				return Response.ok(Response.status(500)).build();
			}
			exportBusStopTimeReport(reportData, config.getReportDirectory() + fileName);

		} else if ("tripReport".equals(reportName)) {
			fileName = "tripSummaryReport_" + userId + ".xls";

			List<TripSummaryReportModel> reportData = (List<TripSummaryReportModel>) httpRequest.getSession().getAttribute("TRIP_REPORT_DATA");

			if (reportData == null || reportData.size() <= 0) {
				return Response.ok(Response.status(500)).build();
			}
			exportTripSummaryReport(reportData, config.getReportDirectory() + fileName);
		} else if ("positionReport".equals(reportName)) {
			fileName = "positionSummaryReport_" + userId + ".xls";

			List<PositionReportModel> reportData = (List<PositionReportModel>) httpRequest.getSession().getAttribute("POSITION_REPORT_DATA");

			if (reportData == null || reportData.size() <= 0) {
				return Response.ok(Response.status(500)).build();
			}
			exportPositionSummaryReport(reportData, config.getReportDirectory() + fileName);
		} else if ("studentStopTimeReport".equals(reportName)) {
			fileName = "studentStopTimeReport" + userId + ".xls";

			List<PositionReportModel> reportData = (List<PositionReportModel>) httpRequest.getSession().getAttribute("STUDENT_STOP_REPORT_DATA");

			if (reportData == null || reportData.size() <= 0) {
				return Response.ok(Response.status(500)).build();
			}
			exportPositionSummaryReport(reportData, config.getReportDirectory() + fileName);
		} else if ("studentReport".equals(reportName)) {
			fileName = "studentReport_" + userId + ".xls";

			List<StudentVM> studentVMList = (List<StudentVM>) httpRequest.getSession().getAttribute("STUDENT_REPORT_DATA");

			if (studentVMList == null || studentVMList.size() <= 0) {
				return Response.ok(Response.status(500)).build();
			}
			String routeName = httpRequest.getHeader("route");
			exportStudentSummaryReport(studentVMList, config.getReportDirectory() + fileName, routeName);
		} else if ("studentReportAllData".equals(reportName)) {
			fileName = "studentReport_" + userId + ".xls";

			List<StudentVM> studentVMList = (List<StudentVM>) httpRequest.getSession().getAttribute("STUDENT_REPORT_DATA");

			if (studentVMList == null || studentVMList.size() <= 0) {
				return Response.ok(Response.status(500)).build();
			}
			String routeName = httpRequest.getHeader("route");
			exportAllStudentSummaryReport(studentVMList, config.getReportDirectory() + fileName, routeName);
		}else if ("studentMessageReport".equals(reportName)) {
			fileName = "messageSummaryReport_" + userId + ".xls";
			System.out.println("Entering");

			List<StudentNotificationStatusVM> studentMessageReport = (List<StudentNotificationStatusVM>) httpRequest.getSession().getAttribute("Message_Report_Data");

			if (studentMessageReport == null || studentMessageReport.size() <= 0) {
				return Response.ok(Response.status(500)).build();
			}
			String routeName = httpRequest.getHeader("route");
			exportMessageSummaryReport(studentMessageReport, config.getReportDirectory() + fileName);
		}

		return Response.ok(200).build();

	}

	public void exportStudentSummaryReport(List<StudentVM> reportData, String fileName, String routeName) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet sheet = workbook.createSheet("Report");
		// This data needs to be written (Object[])
		Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
		data.put(1, new Object[] { "Student Name", "Parent Name", "Admission No.", "Class", "Section", "Summer Pickup Time", "Winter Pickup Time", "Drop Time",
				"Route" });
		int i = 2;
		for (StudentVM frm : reportData) {

			// if(routeName.equalsIgnoreCase("All Routes"))
			// {
			// data.put(i,
			// new Object[] { frm.getFirstName() + frm.getLastName(),
			// frm.getParentName(), Long.toString(frm.getRegId()),
			// frm.getStudentClass(), frm.getSection(), frm.getPickup(),
			// frm.getDrop(), frm.getRouteName()
			// });
			// i++;
			// }

			// else if(frm.getRouteName().equalsIgnoreCase(routeName))
			// {

			data.put(i, new Object[] { frm.getFirstName() + frm.getLastName(), frm.getParentName(), Long.toString(frm.getRegId()), frm.getStudentClass(),
					frm.getSection(), frm.getPickTimeSummer(), frm.getWinterPickup(), frm.getDrop(), frm.getRouteName() });
			i++;
			// }
		}
		// Iterate over data and write to sheet
		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for (Integer key : keyset) {
			HSSFRow row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				HSSFCell cell = row.createCell(cellnum++);
				if (obj != null)
					cell.setCellValue((String) obj.toString());
			}
		}

		try {
			File file = new File(fileName);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			LOG.error("Error in exportStudentReport", e);
		}
	}

	public void exportAllStudentSummaryReport(List<StudentVM> reportData, String fileName, String routeName) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet sheet = workbook.createSheet("Report");
		// This data needs to be written (Object[])
		Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
		data.put(1,
				new Object[] { "Student first Name", "Student last name", "Parent Name", "Admission No.", "Section", "Class", "Email", "mobile no",
						"Address_line1", "Address_line2", "City", "Pin_code", "Country", "Summer Pickup", "Winter PickUp", "Drop", "Route name", "Lattitude",
						"Longitude" });
		int i = 2;
		for (StudentVM frm : reportData) {

			// if(routeName.equalsIgnoreCase("All Routes"))
			// {
			// data.put(i,
			// new Object[] { frm.getFirstName() + frm.getLastName(),
			// frm.getParentName(), Long.toString(frm.getRegId()),
			// frm.getStudentClass(), frm.getSection(), frm.getPickup(),
			// frm.getDrop(), frm.getRouteName()
			// });
			// i++;
			// }

			// else if(frm.getRouteName().equalsIgnoreCase(routeName))
			// {

			data.put(i,
					new Object[] { frm.getFirstName(), frm.getLastName(), frm.getParentName(), Long.toString(frm.getRegId()), frm.getSection(),
							frm.getStudentClass(), frm.getEmail_address(), frm.getMobile_number(), frm.getAddress_line1(), frm.getAddress_line2(),
							frm.getCity(), frm.getPinCode(), frm.getCountry(), frm.getPickTimeSummer(), frm.getWinterPickup(), frm.getDrop(),
							frm.getRouteName(), frm.getLattitude(), frm.getLongitude() });
			i++;
			// }
		}
		// Iterate over data and write to sheet
		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for (Integer key : keyset) {
			HSSFRow row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				HSSFCell cell = row.createCell(cellnum++);
				if (obj != null)
					cell.setCellValue((String) obj.toString());
			}
		}

		try {
			File file = new File(fileName);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			LOG.error("Error in exportStudentReport", e);
		}
	}
	public void exportMessageSummaryReport(List<StudentNotificationStatusVM> reportData, String fileName) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet sheet = workbook.createSheet("Report");
		// This data needs to be written (Object[])
		Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
		data.put(1, new Object[] { "Name", "Bus StopId", "Date", "Expected Time", "Actual Time", "Time Variance",
				"Message" });
		int i = 2;
		for (StudentNotificationStatusVM notif : reportData) {
			data.put(i,
					new Object[] { notif.getName(),String.valueOf(notif.getWayPointId()),notif.getDate().toString(),notif.getExpectedTime().toString(),notif.getActualTime().toString() ,String.valueOf(notif.getTimeVariance()),notif.getMessage() });
			i++;
		}
		// Iterate over data and write to sheet
		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for (Integer key : keyset) {
			HSSFRow row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				HSSFCell cell = row.createCell(cellnum++);
				if (obj != null)
					cell.setCellValue((String) obj);
			}
		}

		try {
			File file = new File(fileName);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			LOG.error("Error in exportMessageReport", e);
		}
	}

	public void exportFleetReport(List<FleetReportModel> reportData, String fileName) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet sheet = workbook.createSheet("Report");
		// This data needs to be written (Object[])
		Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
		data.put(1, new Object[] { "Vehicle", "Halt Duration", "Move Duration", "Start Location - End Location", "Total Movement", "Avg. Move Speed",
				"Max Speed", "Over Speeding Events", "Long Halt Events", "Idling Events" });
		int i = 2;
		for (FleetReportModel frm : reportData) {
			data.put(i,
					new Object[] { frm.getVehicle(), frm.getHaltDuration(), frm.getMoveDuration(), frm.getStartLocation() + " - " + frm.getEndLocation(),
							frm.getTotalMovement(), frm.getAvgMoveSpeed(), frm.getMaxSpeed(), frm.getOverSpeedEvents(), frm.getLongHaltEvents(),
							frm.getIdlingEvents() });
			i++;
		}
		// Iterate over data and write to sheet
		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for (Integer key : keyset) {
			HSSFRow row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				HSSFCell cell = row.createCell(cellnum++);
				if (obj != null)
					cell.setCellValue((String) obj);
			}
		}

		try {
			File file = new File(fileName);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			LOG.error("Error in exportFleetReport", e);
		}
	}

	public void exportHaltReport(List<HaltReportModel> reportData, String fileName) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet sheet = workbook.createSheet("Report");
		// This data needs to be written (Object[])
		Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
		data.put(1, new Object[] { "Vehicle", "Start Time", "End Time", "Halt Duration", "Location" });
		int i = 2;
		for (HaltReportModel frm : reportData) {
			data.put(i, new Object[] { frm.getVehicle(), frm.getStartTime(), frm.getEndTime(), frm.getHaltDurationDays(), frm.getLocation() });
			i++;
		}
		// Iterate over data and write to sheet
		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for (Integer key : keyset) {
			HSSFRow row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				HSSFCell cell = row.createCell(cellnum++);
				if (obj != null)
					cell.setCellValue((String) obj);
			}
		}

		try {
			File file = new File(fileName);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exportOverSpeedingReport(List<OverSpeedingReportModel> reportData, String fileName) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet sheet = workbook.createSheet("Report");
		// This data needs to be written (Object[])
		Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
		data.put(1, new Object[] { "Vehicle", "Start Time", "End Time","Speed Limit","Speed", "Over Speeding Dur. (Mins)", "Over Speeding Movement (km)", "Start Location",
				"End Location", "Path" });
		int i = 2;
		for (OverSpeedingReportModel frm : reportData) {
			data.put(i, new Object[] { frm.getVehicle(), frm.getStartTime(), frm.getEndTime(),String.valueOf(frm.getSpeedLimit()), String.valueOf(frm.getSpeed()), frm.getOverSpeedDurationMins(), frm.getOverSpeedMovement(),
					frm.getStartLocation(), frm.getEndLocation(), frm.getPath() });
			i++;
		}
		// Iterate over data and write to sheet
		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for (Integer key : keyset) {
			HSSFRow row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				HSSFCell cell = row.createCell(cellnum++);
				if (obj != null)
					cell.setCellValue((String) obj);
			}
		}

		try {
			File file = new File(fileName);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exportBusStopTimeReport(List<BusStopTimeReportModel> reportData, String fileName) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet sheet = workbook.createSheet("Report");
		// This data needs to be written (Object[])
		Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
		data.put(1, new Object[] { "Stop No.", "Stop Name", "Stop Description", "Bus reached at", "Stopover time", "Location Link" });
		int i = 2;
		for (BusStopTimeReportModel frm : reportData) {
			data.put(i, new Object[] { frm.getWayPointSequence(), frm.getWayPointName(), frm.getWayPointDesc(), frm.getReachTime(), frm.getStayDuration(),
					frm.getMapUrl() });
			i++;
		}
		// Iterate over data and write to sheet
		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for (Integer key : keyset) {
			HSSFRow row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				HSSFCell cell = row.createCell(cellnum++);
				if (obj != null)
					cell.setCellValue((String) obj);
			}
		}

		try {
			File file = new File(fileName);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exportPositionSummaryReport(List<PositionReportModel> reportData, String fileName) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet sheet = workbook.createSheet("Report");
		// This data needs to be written (Object[])
		Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
		data.put(1, new Object[] { "Vehicle Reg. Number", "Time", "Longitude", "Latitude", "Speed(kmph)", "Location" });
		int i = 2;
		for (PositionReportModel frm : reportData) {
			data.put(i, new Object[] { frm.getVehicleRegNumber(), frm.getFixTime(), frm.getLongitude(), frm.getLatitude(), frm.getSpeed(), frm.getLocation() });
			i++;
		}
		// Iterate over data and write to sheet
		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for (Integer key : keyset) {
			HSSFRow row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				HSSFCell cell = row.createCell(cellnum++);
				if (obj instanceof Double)
					obj = String.valueOf(obj);
				if (obj != null)
					cell.setCellValue((String) obj);
			}
		}

		try {
			File file = new File(fileName);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			LOG.error("Error in exportPositionReport", e);
		}
	}

	public void exportTripSummaryReport(List<TripSummaryReportModel> reportData, String fileName) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet sheet = workbook.createSheet("Report");
		// This data needs to be written (Object[])
		Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
		data.put(1, new Object[] { "Point", "Trip Leg Duration", "Time", "Location", "Status", "Speed(kmph)", "Aggr. Dist (kms)",
				"Aggr. Halt Dur.(days hrs min)", "Aggr. Move Dur.(days hrs min)" });
		int i = 2;
		for (TripSummaryReportModel frm : reportData) {
			data.put(i, new Object[] { frm.getPoint(), frm.getTripLegDuration(), frm.getTimeFrom() + " - " + frm.getTimeTo(), frm.getVehicleLocation(),
					frm.getStatus(), frm.getSpeed(), frm.getAggrDist(), frm.getAggrHaltDur(), frm.getAggrMoveDur() });
			i++;
		}
		// Iterate over data and write to sheet
		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for (Integer key : keyset) {
			HSSFRow row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				HSSFCell cell = row.createCell(cellnum++);
				if (obj != null)
					cell.setCellValue((String) obj);
			}
		}

		try {
			File file = new File(fileName);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			LOG.error("Error in exportTripSummaryReport", e);
		}
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

}
