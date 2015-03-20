package com.gruppe16.entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.gruppe16.database.DBConnect;
import com.gruppe16.main.Login;

/**
 * The Entity Class Notif. (Notifications)
 * 
 * @author Gruppe 16
 */
public class Notif {

	/** Several self-explanatory variables. */
	public String title, description, toTime, fromTime, date, alarm, owner;
	
	/** Several self-explanatory variables. */
	public int appointmentID, status;
	
	/** Constants for status. WAITING = 0 */
	private final static int ACCEPTED = 1, DECLINED = 2;

	/**
	 * Instantiates a new notification.
	 *
	 * @param title the title
	 * @param description the description
	 * @param toTime the end time of the appointment
	 * @param fromTime the start time of the appointment
	 * @param date the date of the appointment
	 * @param alarm the alarm 
	 * @param owner the owner of the appointment, i.e the employee inviting
	 * @param appointmentID the appointment ID 
	 * @param status the status of the appointment
	 */
	public Notif(String title, String description, String toTime, String fromTime,
			String date, String alarm, String owner, int appointmentID, int status) {
		super();
		this.title = title;
		this.description = description;
		this.toTime = toTime;
		this.fromTime = fromTime;
		this.date = date;
		this.alarm = alarm;
		this.owner = owner;
		this.appointmentID = appointmentID;
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Notif [title=" + title + ", desc=" + description + ", to=" + toTime
				+ ", from=" + fromTime + ", date=" + date + ", alarm=" + alarm
				+ ", owner=" + owner + "]";
	}
	
	/**
	 * Accept an invitation.
	 *
	 * @return true, if successful
	 */
	public boolean accept(){
		try{
			String q = "update AppointmentAndEmployee\nset status = 1 where appid = " +appointmentID+" and employeeid="+Login.getCurrentUserID();
			PreparedStatement p = DBConnect.getConnection().prepareStatement(q);
			p.execute();
			status = ACCEPTED;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Decline an invitation.
	 *
	 * @return true, if successful
	 */
	public boolean decline(){
		try{
			String q = "update AppointmentAndEmployee\nset status = 2 where appid = " +appointmentID+" and employeeid="+Login.getCurrentUserID();
			PreparedStatement p = DBConnect.getConnection().prepareStatement(q);
			p.execute();
			status = DECLINED;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
		
}
