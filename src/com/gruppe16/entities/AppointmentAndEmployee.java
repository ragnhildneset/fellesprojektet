package com.gruppe16.entities;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


/**
 * The Relation Class AppointmentAndEmployee.
 * 
 * @author Gruppe 16
 */
public class AppointmentAndEmployee {
	

	/** The appointment ID. */
	private SimpleIntegerProperty appointmentID;
	
	/** The employee ID. */
	private SimpleIntegerProperty employeeID;
	
	/** The status. */
	private SimpleIntegerProperty status;
	
	/** The alarm. */
	private SimpleIntegerProperty alarm;
	
	/** The color. */
	private SimpleStringProperty color;

	/**
	 * Instantiates a new appointment and employee.
	 *
	 * @param appointmentID the appointment ID
	 * @param employeeID the employee ID
	 * @param status the status, where 0 is pending, 1 is accepted and 2 is declined
	 * @param alarm the alarm, where 0 turns off alarm, and 1 turns on alarm
	 * @param color the color, as a string
	 */
	public AppointmentAndEmployee(int appointmentID, int employeeID, int status, int alarm, String color) {
		this.appointmentID = new SimpleIntegerProperty(appointmentID);
		this.employeeID = new SimpleIntegerProperty(employeeID);
		this.status = new SimpleIntegerProperty(status);
		this.alarm = new SimpleIntegerProperty(alarm);
		this.color = new SimpleStringProperty(color);
	}
	
	/**
	 * Gets the appointment ID
	 *
	 * @return the appointment ID
	 */
	public int getAppointmentID() {
		return appointmentID.get();
	}

	/**
	 * Gets the employee ID.
	 *
	 * @return the employee ID
	 */
	public int getEmployeeID() {
		return employeeID.get();
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status.get();
	}

	/**
	 * Gets the alarm.
	 *
	 * @return the alarm
	 */
	public int getAlarm() {
		return alarm.get();
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public String getColor() {
		return color.get();
	}	
	
}
