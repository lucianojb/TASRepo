package com.tas.healthcheck.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tas.healthcheck.dao.AppConnectionDao;
import com.tas.healthcheck.models.AppConnection;

public class AppConnectionService {

private static final Logger logger = LoggerFactory.getLogger(AppConnectionService.class);
	
	@Autowired
	AppConnectionDao appConnectionDao;
	
	public AppConnection saveAppConnection(AppConnection conn){
		return appConnectionDao.saveAppConnection(conn);
	}

	public void removeApplicationConnections(int appId) {
		appConnectionDao.removeAllByAppId(appId);
	}

	public List<AppConnection> getConnectionsByAppId(int appId) {
		return appConnectionDao.getAllAppConnectionByAppId(appId);
	}
	
}
