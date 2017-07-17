package com.tas.healthcheck.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tas.healthcheck.dao.ConnectionDao;
import com.tas.healthcheck.models.Connection;

public class ConnectionService {
	private static final Logger logger = LoggerFactory.getLogger(ConnectionService.class);
	
	@Autowired
	ConnectionDao connectionDao;

	public void removeAllByAppId(int appId) {
		connectionDao.removeAllByAppId(appId);
	}

	public void saveNewConnection(Connection conn) {
		connectionDao.saveConnection(conn);
	}

	public List<Connection> getAllByAppId(int appID) {
		return connectionDao.getListByAppId(appID);
	}
}
