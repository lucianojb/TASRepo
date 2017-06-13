package com.tas.healthcheck.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name="tas_app")
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="app_id")
	private int appID;
	
	@Column(name="app_name")
	@NotEmpty
	private String appName;
	
	@Column(name="url")
	@URL(message="Not a valid url (assure url contains http:// or https:// in front)")
	@NotEmpty
	private String url;
	
	@Column(name="components")
	private String components;
	
	@Column(name="version_num")
	private String versionNum;
	
	public Application(){
		super();
	}
	
	public Application(String appName, String url, String components, String versionNum){
		super();
		this.appName = appName;
		this.url = url;
		this.components = components;
		this.versionNum = versionNum;
	}

	@Override
	public String toString() {
		return "Application [appName=" + appName + ", url=" + url + ", components=" + components + ", versionNum="
				+ versionNum + "]";
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

	public String getComponents() {
		return components;
	}

	public void setComponents(String components) {
		this.components = components;
	}

	public String getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}
}
