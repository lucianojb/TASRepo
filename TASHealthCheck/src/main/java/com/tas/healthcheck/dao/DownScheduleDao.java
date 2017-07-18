package com.tas.healthcheck.dao;

import java.util.List;

import com.tas.healthcheck.models.DownSchedule;;

public interface DownScheduleDao {

	DownSchedule saveDownSchedule(DownSchedule sched);

	List<DownSchedule> getAllDownSchedule();

	DownSchedule getDownScheduleById(int id);
	
	List<DownSchedule> getAllDownSchedulesByAppId(int id);

	boolean removeById(int id);

	void removeByAppId(int id);

}
