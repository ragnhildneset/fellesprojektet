package com.gruppe16.entities;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The Entity Class Appointment.
 * 
 * @author Gruppe 16
 */
public class Appointment {
	
	
	/** The appointment id. */
	private int appointmentID;
	
	/** The title and description. */
	private String title, description;
	//private RoomReservation room;
	/** The date. */
	private LocalDate date;
	
	/** The from time. */
	private LocalTime fromTime;
	
	/** The to time. */
	private LocalTime toTime;
	
	/** The owner's employee ID. */
	private int ownerid;

	/**
	 * Instantiates a new appointment object.
	 *
	 * @param appointmentID the appointment ID
	 * @param title the title
	 * @param description the description
	 * @param date the scheduled date of the appointment
	 * @param toTime the scheduled end time of the appointment
	 * @param fromTime the scheduled start time of the appointment
	 * @param ownerid the owner's employee ID
	 */
	public Appointment(int appointmentID, String title, String description,
			LocalDate date, LocalTime toTime, LocalTime fromTime, int ownerid) {
		this.appointmentID = appointmentID;
		this.title = title;
		this.description = description;
		this.date = date;
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.ownerid = ownerid;
	}
	
	/**
	 * Gets the title of the appointment.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the description of the appointment.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the scheduled start time of the appointment
	 *
	 * @return the scheduled start time
	 */
	public LocalTime getFromTime() {
		
		return fromTime;
	}
	
	/**
	 * Gets the scheduled end time of the appointment
	 *
	 * @return the scheduled end time
	 */
	public LocalTime getToTime() {
		return toTime;
	}
	
	/**
	 * Gets the appointment id.
	 *
	 * @return the appointment id
	 */
	public int getID(){
		return this.appointmentID;
	}
	
	/**
	 * Gets the owner's employee id.
	 *
	 * @return the owner's employee id
	 */
	public int getOwnerID(){
		return ownerid;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Appointment [appoinmentID=" + appointmentID + ", title=" + title
				+ "]";
	}

	/**
	 * Gets the scheduled date of the appointment
	 *
	 * @return the scheduled date
	 */
	public LocalDate getAppDate(){
		return date;
	}
	
}


