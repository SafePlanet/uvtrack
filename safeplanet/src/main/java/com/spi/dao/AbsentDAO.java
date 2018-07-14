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

import com.spi.domain.Absent;

public interface AbsentDAO extends JpaRepository<Absent, Long> {
    
    @Query("select a from Absent a where a.student.id = :studentId and (a.fromDate<=:fromDate and a.toDate >=:toDate) ")
    Absent getPresenceDetailsByDate(@Param("studentId") Long studentId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
    
    @Query("select a from Absent a where a.student.id=? order by a.fromDate desc , a.toDate desc ")
    List<Absent> getStudentAbsentRecord(long studentId);
}
