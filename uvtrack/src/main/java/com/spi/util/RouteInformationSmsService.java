package com.spi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RouteInformationSmsService {
	
	String prodDbURL = "jdbc:mysql://localhost:3306/spiprod?autoReconnect=true";
    String username = "root";
    String password = "password";
    Connection prodCon;
    
    private final static String smsLinkUserName = "http://103.16.101.52:8080/sendsms/bulksms?username=";
	private final static String smsLinkPassword = "&password=";
	private final static String smsLinkDestination = "&type=0&dlr=1&destination=";
	private final static String smsLinkSource = "&source=";
	private final static String smsLinkMessage = "&message=";

	private final static String smsUserName = "clik-spinv";
	private final static String smsUserPassword = "123456"; // Password need to be changed to 123456 when need to run this
	private final static String sourceId = "iTrack";
    
    final String routeQuery = "select distinct trim(u.first_name) name, u.mobile_number userMobile,  r.route_name rname, srx.time_pick ptime, srx.time_drop dtime, f.driver_name driverName, f.driver_mobile driverMobile " +
		"from user u, student s, student_route_xref srx, route r, route_fleet_device_xref rfdx, fleet f " +
		"where s.id = srx.student_id and u.id = s.user_id and r.id = srx.route_id and s.is_approved = 'Y' " +
		"and rfdx.route_id = r.id and f.id = rfdx.fleet_id " +
		"and s.school_id = ? order by r.id, u.first_name";
	
	public static void main(String[] args) {
		try {
			RouteInformationSmsService service = new RouteInformationSmsService();
		
			service.initializeConnection();
			
			List<RouteInfo> list = service.getRoutePickUpDropInformation("1000");
			for(RouteInfo info: list){
				service.sendMessage(info.getName(), info.getRouteName(), info.getPickTime(), info.getDropTime(), info.getUserMobile(), info);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	List<RouteInfo> getRoutePickUpDropInformation(String schoolId) throws SQLException{
		List<RouteInfo> list = new ArrayList<RouteInfo>(); 
		PreparedStatement routeQueryStmt = null ;
		
		routeQueryStmt = prodCon.prepareStatement(routeQuery);
		routeQueryStmt.setString(1, schoolId);
		ResultSet rs = routeQueryStmt.executeQuery();
		RouteInfo info = null;
		while(rs.next()) {
			info = new RouteInfo();
			info.setName(rs.getString("name"));
			info.setUserMobile(rs.getString("userMobile"));
			info.setRouteName(rs.getString("rname"));
			info.setPickTime(rs.getString("ptime"));
			info.setDropTime(rs.getString("dtime"));
			info.setDriveName(rs.getString("driverName"));
			info.setDriverMobile(rs.getString("driverMobile"));
			
			list.add(info);
		}
		System.out.println("Number of records copied ") ;
		routeQueryStmt.close();
		return list;
	}
	
	class RouteInfo{
		String name = null;
		String routeName = null;
		String userMobile = null;
		String pickTime = null;
		String dropTime = null;
		String driveName = null;
		String driverMobile = null;
		
		public String getDriveName() {
			return driveName;
		}
		public void setDriveName(String driveName) {
			this.driveName = driveName;
		}
		public String getDriverMobile() {
			return driverMobile;
		}
		public void setDriverMobile(String driverMobile) {
			this.driverMobile = driverMobile;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUserMobile() {
			return userMobile;
		}
		public void setUserMobile(String userMobile) {
			this.userMobile = userMobile;
		}
		public String getRouteName() {
			return routeName;
		}
		public void setRouteName(String routeName) {
			this.routeName = routeName;
		}
		public String getPickTime() {
			return pickTime;
		}
		public void setPickTime(String pickTime) {
			this.pickTime = pickTime;
		}
		public String getDropTime() {
			return dropTime;
		}
		public void setDropTime(String dropTime) {
			this.dropTime = dropTime;
		}
	}
	
	public String sendMessage(String name, String rName, String pTime, String dTime, String destination, RouteInfo info) {
		System.out.println("Sending message to " + destination + " rName ::" +rName + " pTime " + pTime + ":: dTime = " + dTime);
		String line = null;
		if (destination == null) {
			return null;
		}
//		String message = "Dear " + name + ", your kids pick time is " + pTime + " and drop time is " + dTime + ", Driver Name: " + info.driveName + ", Driver's Mobile: " +info.getDriverMobile();
		String message = "D/P Kindly Note that now onwards you will not get afternoon GPS messages of transport during examination. Kindly follow the timing given by the drivers- VBBPS";
		String linkToSendMessage = smsLinkUserName + smsUserName + smsLinkPassword + smsUserPassword + smsLinkDestination
				+ destination + smsLinkSource + sourceId + smsLinkMessage + message;
		try {
			// java.awt.Desktop.getDesktop().browse(java.net.URI.create(linkToSendMessage));
			URL url = new URL(linkToSendMessage.replace(" ", "%20"));
			InputStream is = url.openConnection().getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			line = reader.readLine();
			System.out.println(line);
			reader.close();
		} catch (IOException ioEx) {
			System.out.println("Problem in sending message ");
			line = "Exception";
		}
		return line;
	}
	
	void initializeConnection() throws SQLException{
		System.out.println("initializeConnection");
		prodCon = DriverManager.getConnection(prodDbURL, username, password);
		prodCon.setAutoCommit(false);
	}
}
