package com.tas.healthcheck.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tas.healthcheck.dao.DownScheduleDao;
import com.tas.healthcheck.models.DownSchedule;

public class DownScheduleService {

	private static final Logger logger = LoggerFactory.getLogger(DownScheduleService.class);
	
	@Autowired
	DownScheduleDao downScheduleDao;
	
	public boolean saveSchedule(DownSchedule downSchedule) {
		logger.info("Saving schedule to dao");
		return downScheduleDao.saveDownSchedule(downSchedule);
	}

	public List<DownSchedule> getAllScheduledDownByAppId(int id) {
		return downScheduleDao.getAllDownSchedulesByAppId(id);
	}

	public DownSchedule getScheduleBySchedId(int id) {
		return downScheduleDao.getDownScheduleById(id);
	}

	public boolean removeScheduleById(int id) {
		return downScheduleDao.removeById(id);
	}

}
