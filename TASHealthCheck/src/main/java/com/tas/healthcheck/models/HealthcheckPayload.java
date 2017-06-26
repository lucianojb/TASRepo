package com.tas.healthcheck.models;

import java.util.Map;
import com.tas.healthcheck.models.Connection;

public class HealthcheckPayload {

	private Map<String, Connection> connections;
	private int resultValue;
	private Application app;
	private String errorMessage;
	
	public HealthcheckPayload(){
		super();
		this.connections = null;
		this.app = null;
		this.errorMessage = null;
	}
	
	public HealthcheckPayload(Application app, int resultValue, String errorMessage){
		super();
		this.app = app;
		this.resultValue = resultValue;
		this.errorMessage = errorMessage;
		this.connections = null;
	}
	
	public HealthcheckPayload(Application app, int resultValue){
		super();
		this.app = app;
		this.resultValue = resultValue;
		this.errorMessage = null;
		this.connections = null;
	}
	
	public HealthcheckPayload(Application app){
		super();
		this.app = app;
		this.errorMessage = null;
		this.connections = null;
	}

	public Map<String, Connection> getConnections() {
		return connections;
	}

	public void setConnections(Map<String, Connection> connections) {
		this.connections = connections;
	}

	public int getResultValue() {
		return resultValue;
	}

	public void setResultValue(int resultValue) {
		this.resultValue = resultValue;
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "HealthcheckPayload [connections=" + connections + ", resultValue=" + resultValue + ", app=" + app
				+ ", errorMessage=" + errorMessage + "]";
	}
}
