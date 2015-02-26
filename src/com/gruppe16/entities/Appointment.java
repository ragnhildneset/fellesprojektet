package com.gruppe16.entities;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import com.gruppe16.database.DBConnect;
import com.gruppe16.main.Login;
import com.mysql.jdbc.ResultSet;

public class Appointment {
	
	private static HashMap<Integer, Appointment> appointments = new HashMap<Integer, Appointment>(); 
	
	int AppoinmentID;
	private String title, description;
	//private RoomReservation room;
	LocalDate date;
	LocalTime fromTime;
	LocalTime toTime;
	//private Employee host;
	//private Employee[] attendees;
	

	
	public static void initialize(){
		String q = "SELECT AppointmentID, Title, Description, Date, ToTime, FromTime FROM Appointment;";
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				new Appointment(Integer.parseInt(rs.getString("AppointmentID")), rs.getString("Title"), rs.getString("Description"),LocalDate.parse(rs.getString("Date")), LocalTime.parse(rs.getString("ToTime")),  LocalTime.parse(rs.getString("FromTime")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Appointment(int appoinmentID, String title, String description,
			LocalDate date, LocalTime fromTime, LocalTime toTime) {
		Appointment.appointments.put(appoinmentID, this);
		AppoinmentID = appoinmentID;
		this.title = title;
		this.description = description;
		this.date = date;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

	public static void addNew(String title, String descr, LocalDate date, String toTime, String fromTime){
		String q = "INSERT INTO Appointment( Title, Description, Date, ToTime, FromTime, Owner_EID) VALUES ( ?, ?, ?, ?, ?, ? )";
		try {
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			s.setString(1, title);
			s.setString(2, descr);
			s.setDate(3, Date.valueOf(date));
			s.setTime(4, Time.valueOf(toTime));
			s.setTime(5, Time.valueOf(fromTime));
			s.setInt(6, Login.getCurrentUserID());
			s.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Added addpointment.");
		initialize();
	}
	
	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public LocalTime getFromTime() {
		return fromTime;
	}
	
	public LocalTime getToTime() {
		return toTime;
	}
	
	public int getID(){
		return this.AppoinmentID;
	}
	
	public static Appointment get(int id){
		return appointments.get(id);
	}
	
}


