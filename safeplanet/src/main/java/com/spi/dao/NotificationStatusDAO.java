/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spi.domain.NotificationStatus;

public interface NotificationStatusDAO extends JpaRepository<NotificationStatus, Long> {

	//@Query("select s from NotificationStatus s")
	//List<NotificationStatus> getAllNotification();	
	
	@Query("select ns from NotificationStatus ns, RouteScheduleDetails rsd "
			+ "where ns.processDate = :processDate and ns.scheduleId = rsd.id and rsd.route.routeFleetDeviceXREF.fleet.id = :fleetId order by ns.startTime")
	List<NotificationStatus> findNotificationSummaryBySchedule(@Param("processDate") Date processDate, @Param("fleetId") Long fleetId);
}
