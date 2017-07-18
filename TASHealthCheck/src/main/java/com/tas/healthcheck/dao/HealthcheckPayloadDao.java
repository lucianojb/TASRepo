package com.tas.healthcheck.dao;

import com.tas.healthcheck.models.HealthcheckPayload;

public interface HealthcheckPayloadDao {

	boolean removeAllByAppId(int appId);

	HealthcheckPayload savePayload(HealthcheckPayload saveP);

	HealthcheckPayload getOneByAppId(int appID);
}
