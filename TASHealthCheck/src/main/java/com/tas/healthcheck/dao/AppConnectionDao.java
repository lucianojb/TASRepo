package com.tas.healthcheck.dao;

import java.util.List;

import com.tas.healthcheck.models.AppConnection;

public interface AppConnectionDao {

	AppConnection saveAppConnection(AppConnection conn);

	List<AppConnection> getAllAppConnection();

	AppConnection getAppConnectionById(int connId);

	List<AppConnection> getAllAppConnectionByAppId(int appId);

	boolean removeById(int connId);

	boolean removeAllByAppId(int appId);

}
