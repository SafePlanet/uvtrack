package com.spi.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spi.domain.SchoolHoliday;

public interface SchoolHolidaysDAO extends JpaRepository<SchoolHoliday, Long> {
	@Query("select s from SchoolHoliday s where school_id=?")
	List<SchoolHoliday> getSchoolHolidays(long schoolId);
	
	@Query("select s from SchoolHoliday s where id=?")
	SchoolHoliday getSchoolHolidayById(Long id);
	
	@Query("select sh from SchoolHoliday sh where sh.school.id = :id and sh.toDate >=:fromDate and sh.toDate <=:toDate")
	List<SchoolHoliday> getSchoolHolidaysForParent( @Param("id")Long id, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	
}
