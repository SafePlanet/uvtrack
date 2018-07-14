package com.spi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spi.domain.Groups;

public interface GroupsDAO extends JpaRepository<Groups, Long> {


}
