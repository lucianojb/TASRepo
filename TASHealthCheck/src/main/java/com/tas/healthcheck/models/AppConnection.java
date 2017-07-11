package com.tas.healthcheck.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "app_conns")
public class AppConnection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "conn_id")
	private int connID;

	@Column(name = "conn_name")
	@NotNull
	private String connName;

	@Column(name = "priority")
	@NotNull
	private Boolean priority;

	@Column(name = "app_id")
	@NotNull
	private int appID;

	public int getConnID() {
		return connID;
	}

	public void setConnID(int connID) {
		this.connID = connID;
	}

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public Boolean getPriority() {
		return priority;
	}

	public void setPriority(Boolean priority) {
		this.priority = priority;
	}

	public int getAppID() {
		return appID;
	}

	public void setAppID(int appID) {
		this.appID = appID;
	}

	public AppConnection(String connName, Boolean priority, int appID) {
		super();
		this.connName = connName;
		this.priority = priority;
		this.appID = appID;
	}
	
	public AppConnection(String connName, Boolean priority) {
		super();
		this.connName = connName;
		this.priority = priority;
	}
	
	public AppConnection(){
		super();
	}

	@Override
	public String toString() {
		return "AppConnection [connID=" + connID + ", connName=" + connName + ", priority=" + priority + ", appID="
				+ appID + "]";
	}
}
