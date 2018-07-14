package com.spi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.City;
import com.spi.domain.State;

public interface StatesDAO extends JpaRepository<State, Long> {

	@Query("select s from State s")
	List<State> getStates();
	
	@Query("select c from City c ,State s where c.state.id=s.id and s.id=?")
	List<City> getCity(long stateId);
	
	@Query("select s from State s Where s.id=?")
	State getStateById(long stateId);
}
