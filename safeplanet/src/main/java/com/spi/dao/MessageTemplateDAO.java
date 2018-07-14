/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.MessageTemplate;

public interface MessageTemplateDAO extends JpaRepository<MessageTemplate, Long>{
    
	@Query("select mt from MessageTemplate mt where mt.userInitiated = 'Y'")
	List<MessageTemplate> findUserInitiatedTemplates();
}
