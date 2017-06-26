package com.tas.healthcheck.models;

import java.util.Map;

public class HealthcheckPayload {

	private Map<String, Boolean> connections;
	private int resultValue;
	private Application app;
	private String errorMessage;
	private Map<String, String> details;
	
	public HealthcheckPayload(){
		super();
		this.connections = null;
		this.app = null;
		this.errorMessage = null;
	}
	
	public HealthcheckPayload(Application app, int resultValue, Map<String, Boolean> connections, Map<String, String> details, String errorMessage){
		super();
		this.connections = connections;
		this.details = details;
		this.resultValue = resultValue;
		this.app = app;
		this.errorMessage = errorMessage;
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

	public Map<String, Boolean> getConnections() {
		return connections;
	}

	public void setConnections(Map<String, Boolean> connections) {
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
	
	public Map<String, String> getDetails(){
		return details;
	}
	
	public void setDetails(Map<String, String> details){
		this.details = details;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "HealthcheckPayload [connections=" + connections + ", details=" + details + ", resultValue=" + resultValue + ", app=" + app
				+ ", errorMessage=" + errorMessage + "]";
	}
}
