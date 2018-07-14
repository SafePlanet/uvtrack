package com.spi.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.ClassGroup;
import com.spi.domain.School;
public interface ClassGroupDAO extends JpaRepository<ClassGroup, Long> {
	@Query("select cg from ClassGroup cg where cg.schoolId=? ")
	List<ClassGroup> getClassGroupBySchoolId(long schoolId);
	
	@Query("select cg from ClassGroup cg where cg.uuid=? ")
	ClassGroup getClassGroupByUUID(String uuid);
}
