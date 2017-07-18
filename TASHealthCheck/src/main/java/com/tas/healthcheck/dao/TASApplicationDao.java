package com.tas.healthcheck.dao;

import java.util.List;

import com.tas.healthcheck.models.Application;

public interface TASApplicationDao {

	Application saveApplication(Application app);

	List<Application> getAllApps();

	Application getAppById(int id);

	boolean removeById(int id);

}
