package com.tas.healthcheck.models;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tas.healthcheck.models.Connection;


@Entity
@Table(name="health_payload")
public class HealthcheckPayload {

	@Transient
	private Map<String, Connection> connections;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="health_id")
	private int healthId;
	
	@Column(name="app_id")
	private int appId;
	
	@Column(name="error_message")
	private String errorMessage;
	
	@Column(name="result_value")
	private int resultValue;
	
	@Transient
	private Application app;
	
	public HealthcheckPayload(){
		super();
		this.connections = null;
		this.app = null;
		this.errorMessage = null;
	}
	
	public HealthcheckPayload(int appId, String errorMessage, int resultValue) {
		super();
		this.appId = appId;
		this.errorMessage = errorMessage;
		this.resultValue = resultValue;
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

	public int getHealthId() {
		return healthId;
	}

	public void setHealthId(int healthId) {
		this.healthId = healthId;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	@Override
	public String toString() {
		return "HealthcheckPayload [healthId=" + healthId + ", appId=" + appId + ", connections=" + connections 
				+ ", resultValue=" + resultValue + ", app=" + app + ", errorMessage=" + errorMessage + "]";
	}
}
