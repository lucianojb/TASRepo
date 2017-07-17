package com.tas.healthcheck.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tas.healthcheck.dao.HealthcheckPayloadDao;
import com.tas.healthcheck.models.HealthcheckPayload;

public class HealthcheckPayloadService {
	private static final Logger logger = LoggerFactory.getLogger(HealthcheckPayloadService.class);
	
	@Autowired
	HealthcheckPayloadDao healthcheckPayloadDao;

	public void removeAllByAppId(int appId) {
		healthcheckPayloadDao.removeAllByAppId(appId);
	}

	public void saveHealthCheckPayload(HealthcheckPayload saveP) {
		healthcheckPayloadDao.savePayload(saveP);
	}

	public HealthcheckPayload getByAppId(int appID) {
		return healthcheckPayloadDao.getOneByAppId(appID);
	}
}
