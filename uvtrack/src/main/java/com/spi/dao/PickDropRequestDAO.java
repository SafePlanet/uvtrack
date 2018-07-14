package com.spi.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.PickOrDropRequest;

public interface PickDropRequestDAO extends JpaRepository<PickOrDropRequest, Long> {

	@Query("select a from PickOrDropRequest a where student_id=? ")
	List<PickOrDropRequest> getStudentAbsentRecord(long studentId);

	@Query("select a from PickOrDropRequest a where a.uuid=? ")
	PickOrDropRequest getStudent(String uuid);

	@Query("SELECT p from PickOrDropRequest p where  school_id=? and pick_drop_status='U'")
	List<PickOrDropRequest> getPickUpAlerts(long schoolId);

	@Query("SELECT p from PickOrDropRequest p where  school_id=? and pick_drop_status='S'")
	List<PickOrDropRequest> getPickUpAlertsByUser(long schoolId);

	@Query("SELECT p from PickOrDropRequest p where pick_drop_status='U' and school_id=?")
	Page<PickOrDropRequest> getPickAlerts(long schoolId, Pageable topTen);

}
