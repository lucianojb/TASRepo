package com.tas.healthcheck.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tas.healthcheck.dao.TASApplicationDao;
import com.tas.healthcheck.models.Application;

public class TASApplicationService {

	@Autowired
	TASApplicationDao tasApplicationDao;
	
	public void saveApplication(Application application) {
		tasApplicationDao.saveApplication(application);
	}

	public List<Application> getAllApplications() {
		return tasApplicationDao.getAllApps();
	}

	public Application getApplicationById(int id) {
		return tasApplicationDao.getAppById(id);
	}

	public boolean removeApplicationById(int id) {
		return tasApplicationDao.removeById(id);
	}

}
