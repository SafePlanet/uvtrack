/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.WayPoint;

public interface WayPointDAO extends JpaRepository<WayPoint, Long> {

	@Query("select w from WayPoint w where w.route.uuid=? order by sequenceNumber")
	List<WayPoint> findAllWayPointListforRoute(String routeUUID);

	@Query("select w from WayPoint w where w.uuid=?")
	WayPoint findByUUID(String uuid);

	@Query("select w from WayPoint w where id=?")
	WayPoint getWayPoint(long uuid);
	
	@Query("select w from WayPoint w where w.route.id=? order by w.description")
	List<WayPoint> findAllWayPointListforRoute(long routeId);
	
	@Query("select w from WayPoint w where id=?")
	List<WayPoint> getWayPointList(long waypointId);
	
}
