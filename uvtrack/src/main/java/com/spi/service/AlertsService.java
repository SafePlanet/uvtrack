package com.spi.service;

import java.util.List;

import com.spi.domain.Alert;
import com.spi.domain.PickOrDropRequest;
import com.spi.user.api.PickUpRequestModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AlertsService {
	
	public Page<Alert> getLatestAlerts( Long userId, Pageable topTen);
	public Page<PickOrDropRequest> getpickAlerts(Long userId, Pageable topTen);
	public int getTotalAlerts( String userId);
	public List<Alert> getAllAlerts( Long userId);
	public List<PickUpRequestModel> getPickUpAlerts(Long userId);

}
