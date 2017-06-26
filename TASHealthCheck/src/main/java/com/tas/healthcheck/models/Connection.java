package com.tas.healthcheck.models;

public class Connection {
	private Boolean functional;
	private String details;
	
	public Connection(){
		super();
	}
	
	public Connection(Boolean functional, String details) {
		super();
		this.functional = functional;
		this.details = details;
	}
	@Override
	public String toString() {
		return "Connection [functional=" + functional + ", details=" + details + "]";
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
	
	
	
}
