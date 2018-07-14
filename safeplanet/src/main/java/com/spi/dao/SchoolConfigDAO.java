/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.SchoolConfig;

public interface SchoolConfigDAO extends JpaRepository<SchoolConfig, Long> {

	@Query("select sc from SchoolConfig sc where sc.school.id = ? and sc.configKey = ?")
	SchoolConfig findBySchool(long schoolId, String configKey);
	
	@Query("select sc from SchoolConfig sc where sc.school.id = ? and sc.configKey =?")
	SchoolConfig findSchoolConfiguration(long schoolId, String configKey);
    
}
