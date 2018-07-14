package com.spi.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.spi.VM.StateCityVM;
import com.spi.domain.State;

@Transactional
public interface StatesService {

	public List<StateCityVM> getStatesList();  
	public List<StateCityVM> getCityList(long stateId);
    public State getStateById(long stateId);
	
}
