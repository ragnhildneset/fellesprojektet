package com.gruppe16.entities;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class AppointmentAndEmployee {
	

	private SimpleIntegerProperty appid;
	private SimpleIntegerProperty employeeid;
	private SimpleIntegerProperty status;
	private SimpleIntegerProperty alarm;
	private SimpleStringProperty color;

	public AppointmentAndEmployee(int appid, int employeeid, int status, int alarm, String color) {
		this.appid = new SimpleIntegerProperty(appid);
		this.employeeid = new SimpleIntegerProperty(employeeid);
		this.status = new SimpleIntegerProperty(status);
		this.alarm = new SimpleIntegerProperty(alarm);
		this.color = new SimpleStringProperty(color);
	}
	
	public int getAppid() {
		return appid.get();
	}

	public int getEmployeeid() {
		return employeeid.get();
	}

	public int getStatus() {
		return status.get();
	}

	public int getAlarm() {
		return alarm.get();
	}

	public String getColor() {
		return color.get();
	}	
	
	public void setStatus(int i) {
		this.status = new SimpleIntegerProperty(i);
	}
	
}
