/**
 * 
 */
package com.spi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spi.domain.UserDevice;

/**
 * @author ranjeet
 *
 */
public interface UserDeviceDAO extends JpaRepository<UserDevice, Long>{
	@Query("select ud from UserDevice ud where ud.user.id=? and ud.devices.id=?")
	UserDevice findByDeviceIdAndUserId(Long userid,Long deviceid);
}
