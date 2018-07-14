package com.spi.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.spi.VM.NotificationStatusVM;
import com.spi.VM.StudentNotificationStatusVM;
import com.spi.user.api.BusStopTimeReportModel;
import com.spi.user.api.FleetModel;
import com.spi.user.api.FleetReportModel;
import com.spi.user.api.HaltReportModel;
import com.spi.user.api.OverSpeedingReportModel;
import com.spi.user.api.PredictiveStopReportModel;
import com.spi.user.api.RouteModel;
import com.spi.user.api.SearchDTO;


public interface ReportService {
	
	public List<FleetModel> findAllFleetList(String userId);
	
	public List<FleetReportModel> getFleetSummary(String userId, SearchDTO searchDTO);
	
	public List<HaltReportModel> getHaltSummary(String userId, SearchDTO searchDTO);
	
	public List<OverSpeedingReportModel> getOverSpeedingSummary(String userId, SearchDTO searchDTO);
	
	public List<BusStopTimeReportModel> getBusStopTimeReport(String userId, SearchDTO searchDTO);
	
	public List<BusStopTimeReportModel> getStudentStopTimeReport(String userId, SearchDTO searchDTO);
	
	public Map<String, Object> getPositionSummary(HttpServletRequest httpRequest, String userId, SearchDTO searchDTO);
	
	public Map<String, Object> getTripSummary(HttpServletRequest httpRequest, String userId, SearchDTO searchDTO);
	
	public List<PredictiveStopReportModel> getBusStopPredictiveReport(String userId, SearchDTO searchDTO);
	
	public List<RouteModel> getAllRouteList(String userId);
	
	public List<NotificationStatusVM> getMessageSummaryReport(SearchDTO searchDTO);
	
	public List<StudentNotificationStatusVM> getStudentNotification(SearchDTO searchDTO, String userId);
}
