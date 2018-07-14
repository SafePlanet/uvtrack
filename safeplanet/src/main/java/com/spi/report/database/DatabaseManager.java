package com.spi.report.database;

import java.sql.Connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.spi.config.SystemConstant;

@Component
public class DatabaseManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseManager.class);
	
	@Autowired
	@Qualifier("database")
	private BasicDataSource dataSource;
	
	@Autowired
	@Qualifier("backupDatabase")
	private BasicDataSource backupDatabase;
	
	public Connection getConnection(String databaseSchema){
		Connection connection = null;
		try {
			if(SystemConstant.SCHOOL_CONFIG_DB_PRODUCTION.equals(databaseSchema)){
				connection = this.dataSource.getConnection();
			}else{
				connection = this.backupDatabase.getConnection();
			}
			
		} catch (Throwable e) {
			LOG.error("Error in getting connection for schema " + databaseSchema, e);
		}
		return connection;
	}
	

}