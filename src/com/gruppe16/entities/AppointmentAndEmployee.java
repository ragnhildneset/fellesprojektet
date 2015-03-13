package com.gruppe16.entities;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class AppointmentAndEmployee {
	

	SimpleIntegerProperty appid;
	SimpleIntegerProperty employeeid;
	SimpleIntegerProperty status;
	SimpleIntegerProperty alarm;
	SimpleStringProperty color;
	
	private static HashMap<Integer, HashMap<Integer, AppointmentAndEmployee>> AppointmentAndEmployee = new HashMap<Integer, HashMap<Integer, AppointmentAndEmployee>>();
	private static HashMap<Integer, AppointmentAndEmployee> AppAndEmp = new HashMap<Integer, AppointmentAndEmployee>();
	
	public static AppointmentAndEmployee getAppointmentAndEmployee(int appid, int employeeid){
		return AppointmentAndEmployee.get(appid).get(employeeid);
	}
	
	
	public AppointmentAndEmployee(int appid, int employeeid, int status, int alarm, String color) {
		AppAndEmp.put(employeeid, this);
		AppointmentAndEmployee.put(appid, AppAndEmp);
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
