package com.spi.dao;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spi.domain.RouteScheduleDetails;
public interface RouteScheduleDAO extends JpaRepository<RouteScheduleDetails, Long> {
	
	@Query("Select r From RouteScheduleDetails r")
	List<RouteScheduleDetails> getRouteScheduledetails();

}
