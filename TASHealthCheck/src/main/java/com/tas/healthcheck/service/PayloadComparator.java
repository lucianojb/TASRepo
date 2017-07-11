package com.tas.healthcheck.service;

import java.util.Comparator;

import com.tas.healthcheck.models.HealthcheckPayload;

public class PayloadComparator implements Comparator<HealthcheckPayload>{
	@Override
	public int compare(HealthcheckPayload p1,  HealthcheckPayload p2) {
	    if (p1.getResultValue() > p2.getResultValue()) {
	        return -1;
	    } else if (p1.getResultValue() < p2.getResultValue()) {
	        return 1;
	    }
	    return 0;
	}
}