package com.tas.healthcheck.dao;

import java.util.List;

import com.tas.healthcheck.models.Connection;

public interface ConnectionDao {

	Connection saveConnection(Connection conn);

	boolean removeAllByAppId(int appId);

	List<Connection> getListByAppId(int appID);

}
