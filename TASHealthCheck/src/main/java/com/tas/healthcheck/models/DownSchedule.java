package com.tas.healthcheck.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="down_schedule")
public class DownSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="sched_id")
	private int schedID;
	
	@Column(name="start_date")
	@Future(message="Date must be in future")
	@DateTimeFormat(pattern = "MM/dd/yyyy hh:mm a")
	@NotNull
	private Date startDate;
	
	@Column(name="end_date")
	@Future(message="Date must be in future")
	@DateTimeFormat(pattern = "MM/dd/yyyy hh:mm a")
	@NotNull
	private Date endDate;

	@Column(name="app_id")
	@NotNull
	private int appID;
	
	public DownSchedule(){
		super();
	}
	
	public DownSchedule(int appID, Date startDate, Date endDate){
		super();
		this.appID = appID;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public int getSchedID() {
		return schedID;
	}

	public void setSchedID(int schedID) {
		this.schedID = schedID;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAppID() {
		return appID;
	}

	public void setAppID(int appID) {
		this.appID = appID;
	}

	@Override
	public String toString() {
		return "DownSchedule [schedID=" + schedID + ", startDate=" + startDate + ", endDate=" + endDate + ", appID="
				+ appID + "]";
	}
}
