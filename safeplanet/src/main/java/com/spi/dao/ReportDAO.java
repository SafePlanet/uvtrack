package com.spi.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spi.VM.Attributes;
import com.spi.VM.DeviceVM;
import com.spi.VM.StudentNotificationStatusVM;
import com.spi.VM.WayPointVM;
import com.spi.config.SystemConstant;
import com.spi.dto.LocationDTO;
import com.spi.report.database.DatabaseManager;
import com.spi.user.api.SearchDTO;

@Component
public class ReportDAO {

	private static final Logger LOG = LoggerFactory.getLogger(ReportDAO.class);

	private String databaseSchema = SystemConstant.SCHOOL_CONFIG_DB_BACKUP;

	private DatabaseManager databaseManager;

	public ReportDAO() {
	}

	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	@Autowired
	public void setDatabaseManager(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	public String getDatabaseSchema() {
		return databaseSchema;
	}

	public void setDatabaseSchema(String databaseSchema) {
		this.databaseSchema = databaseSchema;
	}

	public Map<Long, String> getFleetAndDeviceForUser(String userId) {
		Connection conn = databaseManager.getConnection(SystemConstant.SCHOOL_CONFIG_DB_PRODUCTION);
		Map<Long, String> fleetMap = new HashMap<Long, String>();
		ResultSet rs = null;
		try {

			String sql = "select d.id, f.reg_number from " + " user u, " + "user_device ud, devices d, route_fleet_device_xref rfdx, fleet f "
					+ " where u.id = ud.userid and ud.deviceid = d.id and rfdx.device_id = d.id and rfdx.fleet_id = f.id and u.uuid = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			LOG.debug(ps.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				Long deviceId = rs.getLong(1);
				String number = rs.getString(2);
				fleetMap.put(deviceId, number);
			}
		} catch (SQLException e) {
			LOG.error("Error in getPositionsForDevice ", e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					LOG.error("Error in getPositionsForDevice ", e1);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					LOG.error("Error in getPositionsForDevice ", e);
				}
		}
		return fleetMap;
	}

	public List<LocationDTO> getPositionsForDevice(SearchDTO searchDTO, long id) {
		LOG.debug("Entering getPositionsForDevice ");

		Connection conn = databaseManager.getConnection(SystemConstant.SCHOOL_CONFIG_DB_PRODUCTION);
		List<LocationDTO> locationsList = new ArrayList<LocationDTO>();
		ResultSet rs = null;
		try {

			String sql = "select p.id, p.protocol, p.deviceid , p.fixtime, p.valid, p.latitude ,p.longitude, p.altitude, "
					+ " p.version, p.speed, p.address, p.attributes, p.moving_status,p.course"
					+ " from " + SystemConstant.SCHOOL_CONFIG_DB_PRODUCTION + ".positions p "
					+ " where p.deviceid = ? "
					+ " and  p.fixtime > ? and  p.fixtime < ? "
					+ " UNION"
					+ " select p.id, p.protocol, p.deviceid , p.fixtime, p.valid, p.latitude ,p.longitude, p.altitude, "
					+ " p.version, p.speed, p.address, p.attributes, p.moving_status,p.course"
					+ " from " + SystemConstant.SCHOOL_CONFIG_DB_BACKUP + ".positions p "
					+ " where p.deviceid = ? "
					+ " and  p.fixtime > ? and  p.fixtime < ?"
					+ " order by fixtime asc";

			java.sql.PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			ps.setTimestamp(2, new Timestamp(searchDTO.getFromDate().getTimestampDate().getTime()));
			ps.setTimestamp(3, new Timestamp(searchDTO.getToDate().getTimestampDate().getTime()));
			ps.setLong(4, id);
			ps.setTimestamp(5, new Timestamp(searchDTO.getFromDate().getTimestampDate().getTime()));
			ps.setTimestamp(6, new Timestamp(searchDTO.getToDate().getTimestampDate().getTime()));
			LOG.debug(ps.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				LocationDTO loc = new LocationDTO();
				loc.setId(rs.getInt(1));
				loc.setProtocol(rs.getString(2));
				loc.setDevice_id(rs.getInt(3));
				loc.setFixTime(rs.getTimestamp(4));
				loc.setValid(rs.getString(5));
				loc.setLatitude(rs.getDouble(6));
				loc.setLongitude(rs.getDouble(7));
				loc.setAltitude(rs.getDouble(8));
				loc.setVersion(rs.getInt(9));
				double speed = rs.getDouble(10) * SystemConstant.SPEED_KMH_MULTIPLIER;
				loc.setSpeed(speed);
				loc.setAddress(rs.getString(11));
				loc.setAttributes(rs.getString(12));
				loc.setMovingStatus(rs.getInt(13));
				loc.setCourse(rs.getInt(14));
				locationsList.add(loc);

			}
		} catch (SQLException e) {
			LOG.error("Error in getPositionsForDevice ", e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					LOG.error("Error in getPositionsForDevice ", e1);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					LOG.error("Error in getPositionsForDevice ", e);
				}
		}
		return locationsList;
	}

	public Map<Long, String> getFleetAndDeviceForUserAndDevice(String userId, long vehicleId) {
		Connection conn = databaseManager.getConnection(SystemConstant.SCHOOL_CONFIG_DB_PRODUCTION);
		Map<Long, String> fleetMap = new HashMap<Long, String>();
		ResultSet rs = null;
		try {

			String sql = "select d.id, f.reg_number from " + " user u, " + "user_device ud, devices d, route_fleet_device_xref rfdx, fleet f "
					+ " where u.id = ud.userid and ud.deviceid = d.id and rfdx.device_id = d.id and rfdx.fleet_id = f.id "
					+ " and u.uuid = ? and f.id = ? order by 2";
			java.sql.PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setLong(2, vehicleId);
			rs = ps.executeQuery();
			while (rs.next()) {
				fleetMap.put(rs.getLong(1), rs.getString(2));
			}
		} catch (SQLException e) {
			LOG.error("Error in getPositionsForDevice ", e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					LOG.error("Error in getPositionsForDevice ", e1);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					LOG.error("Error in getPositionsForDevice ", e);
				}
		}
		return fleetMap;
	}

	public Long getSchoolByUserIdForAdmin(String userId) {
		Connection conn = databaseManager.getConnection(SystemConstant.SCHOOL_CONFIG_DB_PRODUCTION);
		ResultSet rs = null;
		Long schooId = null;
		try {

			String sql = "select distinct schoolid from user_group ug, user u where u.uuid = ? and ug.userid = u.id and schoolid is not null";
			java.sql.PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				schooId = rs.getLong(1);
				break;
			}
		} catch (SQLException e) {
			LOG.error("Error in getPositionsForDevice ", e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					LOG.error("Error in getPositionsForDevice ", e1);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					LOG.error("Error in getPositionsForDevice ", e);
				}
		}
		return schooId;
	}

	public List<WayPointVM> getWaypointsForVehicle(long vehicleId, Long studentId) {
		Connection conn = databaseManager.getConnection(SystemConstant.SCHOOL_CONFIG_DB_PRODUCTION);
		List<WayPointVM> pointVMs = new ArrayList<WayPointVM>();
		WayPointVM pointVM = null;
		ResultSet rs = null;
		try {

			String sql = getWaypointsSql(studentId != null);
			java.sql.PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, vehicleId);
			if (studentId != null)
				ps.setLong(2, studentId);
			rs = ps.executeQuery();
			while (rs.next()) {
				pointVM = new WayPointVM();
				pointVM.setSequenceNumber(rs.getString(1));
				pointVM.setName(rs.getString(2));
				pointVM.setDescription(rs.getString(3));
				pointVM.setLatitude(rs.getDouble(4));
				pointVM.setLongitude(rs.getDouble(5));
				pointVM.setPickTimeSummer(rs.getTime("time_pick"));
				pointVM.setWinterPickup(rs.getTime("time_pick_winter"));
				pointVM.setDrop(rs.getTime("time_drop"));
				pointVM.setStudentName(rs.getString("studentName"));
				pointVMs.add(pointVM);
			}
		} catch (SQLException e) {
			LOG.error("Error in getWaypointsForVehicle ", e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					LOG.error("Error in getWaypointsForVehicle ", e1);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					LOG.error("Error in getWaypointsForVehicle ", e);
				}
		}
		return pointVMs;
	}

	private String getWaypointsSql(boolean isStudent) {
		String SQL ="select wp.sequence_number, wp.name, wp.description, wp.lattitude, wp.longitude, wp.time_pick, wp.time_pick_winter, wp.time_drop, "
				+ "group_concat(DISTINCT concat(s.first_name, ' ', s.last_name) ORDER BY s.first_name, ' ', s.last_name DESC SEPARATOR ', ') studentName "
				+ "from way_point wp, route_fleet_device_xref rfdx, student s " + "where wp.route_id = rfdx.route_id "
				+ "and wp.id = s.way_point_id "  + "and rfdx.fleet_id = ? " + (isStudent ? "and s.id= ? " : "")
				+ "group by wp.sequence_number, wp.time_drop " + "order by wp.sequence_number";
		return SQL;
	}

	public List<StudentNotificationStatusVM> getStudentNotification(Date messageDate, int scheduleId, boolean isPickSchedule, Long schoolId) {
		List<StudentNotificationStatusVM> studentNotificationStatusVMs = new ArrayList<StudentNotificationStatusVM>();
		Connection conn = databaseManager.getConnection(SystemConstant.SCHOOL_CONFIG_DB_PRODUCTION);
		StudentNotificationStatusVM sns = new StudentNotificationStatusVM();
		ResultSet rs = null;
		try {

			String sql = getStudentNotificationQuery(isPickSchedule);
			java.sql.PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDate(1, new java.sql.Date(messageDate.getTime()));
			ps.setInt(2, scheduleId);
			ps.setLong(3, schoolId);
			rs = ps.executeQuery();
			while (rs.next()) {
				sns = new StudentNotificationStatusVM();
				sns.setId(rs.getLong(1));
				sns.setName(rs.getString(2));
				sns.setDate(rs.getDate(3));
				sns.setActualTime(rs.getTime(4));
				sns.setExpectedTime(rs.getTime(5));
				sns.setTimeVariance(rs.getString(6));
				sns.setMessage(rs.getString(7));
				sns.setWayPointId(rs.getLong(8));
				studentNotificationStatusVMs.add(sns);
			}
		} catch (SQLException e) {
			LOG.error("Error in getStudentNotification ", e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					LOG.error("Error in getStudentNotification ", e1);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					LOG.error("Error in getStudentNotification ", e);
				}
		}

		return studentNotificationStatusVMs;
	}

	private String getStudentNotificationQuery(boolean isPickSchedule) {
		String pickSelectClause = "sns.id, concat(s.first_name, ' ', s.last_name), sns.message_date, sns.message_time, wp.time_pick , concat(timediff(wp.time_pick, sns.message_time), ' '), sns.message, wp.id way_point ";
		String dropSelectClause = "sns.id, concat(s.first_name, ' ', s.last_name), sns.message_date, sns.message_time, wp.time_drop , concat(timediff(wp.time_drop, sns.message_time), ' '), sns.message, wp.id way_point ";
		String pickTimeCondition = "wp.time_pick > rsd.start_time and wp.time_pick < rsd.end_time";
		String dropTimeCondition = "wp.time_drop > rsd.start_time and wp.time_drop < rsd.end_time";
		String sql = "SELECT " + (isPickSchedule ? pickSelectClause : dropSelectClause)
				+ " FROM student_notification_status sns, student s, way_point wp, route_schedule_details rsd "
				+ " where sns.student_id = s.id and s.way_point_id = wp.id and sns.message_time > rsd.start_time and sns.message_time < rsd.end_time and rsd.route_id = wp.route_id and "
				+ "sns.message_date = ? and " + (isPickSchedule ? pickTimeCondition : dropTimeCondition) + " and rsd.id = ? and s.school_id = ? order by 5 ";
		return sql;
	}
	public DeviceVM getSpeedForDevice(long id) {
		LOG.debug("Entering getSpeedForDevice ");

		Connection conn = databaseManager.getConnection(SystemConstant.SCHOOL_CONFIG_DB_PRODUCTION);
		DeviceVM deivcevm = null;
		ResultSet rs = null;
		try {

			String sql = "select attributes from devices d where d.id=?";

			java.sql.PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			
			rs = ps.executeQuery();
			BigDecimal speedInKnots;
			while (rs.next()) {
		      deivcevm = new DeviceVM();
		      Attributes attributes = getJsonToJava(rs.getString(1));
		      speedInKnots = attributes.getSpeedLimit();
              deivcevm.setSpeedLimit(speedInKnots.multiply(new BigDecimal(1.852)).setScale(0, 0).intValue());
			}
		} catch (SQLException e) {
			LOG.error("Error in getSpeedForDevice ", e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					LOG.error("Error in getSpeedForDevice ", e1);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					LOG.error("Error in getSpeedForDevice ", e);
				}
		}
		return deivcevm;
	}

	public Attributes getJsonToJava(String attributeJson) {
		if(attributeJson == null) return new Attributes();
		Attributes attributes = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			attributes = mapper.readValue(attributeJson, Attributes.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attributes;
	}
}
