package com.tas.healthcheck.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.tas.healthcheck.dao.TASApplicationDao;
import com.tas.healthcheck.models.Application;

public class TASApplicationService {

	@Autowired
	TASApplicationDao tasApplicationDao;
	
	public void saveApplication(Application application) {
		tasApplicationDao.saveApplication(application);
	}

}
