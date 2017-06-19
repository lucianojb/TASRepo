package com.tas.healthcheck.dao;

import java.util.List;

import com.tas.healthcheck.models.DownSchedule;;

public interface DownScheduleDao {

	boolean saveDownSchedule(DownSchedule sched);

	List<DownSchedule> getAllDownSchedule();

	DownSchedule getDownScheduleById(int id);
	
	List<DownSchedule> getAllDownSchedulesByAppId(int id);

	boolean removeById(int id);

}
