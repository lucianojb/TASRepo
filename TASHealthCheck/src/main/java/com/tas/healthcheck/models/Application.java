package com.tas.healthcheck.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="tas_app")
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="app_id")
	private int appID;
	
	@Column(name="app_name")
	@NotEmpty(message = "Please enter an application name")
	private String appName;
	
	@Column(name="url")
	@URL(message="Not a valid URL (URL must contain http:// or https://)")
	@NotEmpty(message = "Please enter a valid URL")
	private String url;
	
	@Column(name="version_num")
	private String versionNum;
	
	@Column(name="active")
	private boolean activeState;
	
	@Column(name="uptimestart")
	@DateTimeFormat(pattern = "MM/dd/yy HH:mm")
	private Date upTime;
	
	public Application(){
		super();
	}
	
	public Application(String appName, String url, String versionNum, boolean activeState){
		super();
		this.appName = appName;
		this.url = url;
		this.versionNum = versionNum;
		this.activeState = activeState;
	}

	@Override
	public String toString() {
		return "Application [appName=" + appName + ", url=" + url + ", versionNum="
				+ versionNum + ", activeState=" + activeState + "]";
	}

	public int getAppID() {
		return appID;
	}

	public void setAppID(int appID) {
		this.appID = appID;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}

	public boolean isActiveState() {
		return activeState;
	}

	public void setActiveState(boolean activeState) {
		this.activeState = activeState;
	}
	
	public void setupTime(Date ut){
		this.upTime = ut;
	}
	
	public Date getupTime(){
		return this.upTime;
	}
}
