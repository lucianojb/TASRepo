package com.tas.healthcheck.models;

public class Connection {
	private Boolean functional;
	private String details;
	private boolean expected;
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
	@Override
	public String toString() {
		return "Connection [functional=" + functional + ", details=" + details + ", expected=" + expected + ", priority=" + priority + "]";
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
}
