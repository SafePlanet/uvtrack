/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.spi.domain.StudentNotificationStatus;
import com.spi.user.api.studentNotificationStatusModel;

public interface StudentNotificationStatusDAO extends JpaRepository<StudentNotificationStatus, Long> {
	@Query("select s from StudentNotificationStatus s")
	List<StudentNotificationStatus> getStudentNotification();	
}
