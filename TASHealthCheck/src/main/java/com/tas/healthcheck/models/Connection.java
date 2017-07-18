package com.tas.healthcheck.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="connections")
public class Connection {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="conn_id")
	private int connId;
	
	@Column(name="conn_name")
	private String connName;
	
	@Column(name="details")
	private String details;
	
	@Column(name="app_id")
	private int appId;
	
	@Column(name="functional")
	private Boolean functional;
	
	@Column(name="expected")
	private boolean expected;
	
	@Column(name="priority")
	private boolean priority;

	
	public Connection(){
		super();
	}
	
	public Connection(Boolean functional, String details, boolean expected, boolean priority) {
		super();
		this.functional = functional;
		this.details = details;
		this.expected = expected;
		this.priority = priority;
	}
	
	public Connection(String connName, String details, int appId, Boolean functional, boolean expected, boolean priority) {
		super();
		this.connName = connName;
		this.details = details;
		this.appId = appId;
		this.functional = functional;
		this.details = details;
		this.expected = expected;
		this.priority = priority;
	}
	@Override
	public String toString() {
		return "Connection [connId=" + connId + ", connName=" + connName + ", appId=" + appId +
				", functional=" + functional + ", details=" + details + ", expected=" + expected + ", priority=" + priority + "]";
	}
	public Boolean getFunctional() {
		return functional;
	}
	public void setFunctional(Boolean functional) {
		this.functional = functional;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public boolean getExpected() {
		return expected;
	}
	public void setExpected(boolean expected) {
		this.expected = expected;
	}
	
	public boolean getPriority() {
		return priority;
	}
	
	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	public int getConnId() {
		return connId;
	}

	public void setConnId(int connId) {
		this.connId = connId;
	}

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}
}
