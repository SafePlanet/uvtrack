package com.spi.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.Student;
import com.spi.domain.VisitorRequest;

public interface VisitorRequestDAO extends JpaRepository<VisitorRequest, Long> {

	@Query("select v from VisitorRequest v where v.otpNumber = ? and v.visitorNumber=?")
	VisitorRequest getVisitor(String otpNumber,String visitorNumber);
	
	@Query("select v from VisitorRequest v where v.uuid = ?")
	VisitorRequest getVisitorById(String uuid);

}
