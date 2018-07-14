package com.spi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spi.domain.Users;

public interface UsersDAO extends JpaRepository<Users, Long> {

	Users findByEmail(String email);

}
