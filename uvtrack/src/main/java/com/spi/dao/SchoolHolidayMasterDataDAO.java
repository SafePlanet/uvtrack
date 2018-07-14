package com.spi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.SchoolHolidayMasterData;

public interface SchoolHolidayMasterDataDAO extends JpaRepository<SchoolHolidayMasterData, Long> {
	@Query("select s from SchoolHolidayMasterData s")
	List<SchoolHolidayMasterData> getSchoolHolidaysMasterData();
	
	@Query("select s from SchoolHolidayMasterData s where id=?")
	SchoolHolidayMasterData getSchoolHolidayMasterDataById(Long id);
}
