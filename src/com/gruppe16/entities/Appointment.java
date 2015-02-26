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
	
	int appoinmentID;
	private String title, description;
	//private RoomReservation room;
	LocalDate date;
	LocalTime fromTime;
	LocalTime toTime;
	int ownerid;
	LocalTime creationtime;
	//private Employee host;
	//private Employee[] attendees;
	

	
	public static void initialize(){
		String q = "SELECT appointmentID, title, description, appdate, totime, fromtime, ownerid, creationtime FROM Appointment;";
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				new Appointment(Integer.parseInt(rs.getString("appointmentID")), rs.getString("title"), rs.getString("description"),LocalDate.parse(rs.getString("appdate")), LocalTime.parse(rs.getString("totime")),  LocalTime.parse(rs.getString("fromtime")), Integer.parseInt(rs.getString("ownerid")), LocalTime.parse(rs.getString("creationtime")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Appointment(int appoinmentID, String title, String description,
			LocalDate date, LocalTime fromTime, LocalTime toTime, int ownerid, LocalTime creationtime) {
		Appointment.appointments.put(appoinmentID, this);
		this.appoinmentID = appoinmentID;
		this.title = title;
		this.description = description;
		this.date = date;
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.ownerid = ownerid;
		this.creationtime = creationtime;
	}

	public static void addNew(int AppointmentID, String title, String descr, LocalDate date, String toTime, String fromTime, int ownerid, String creationtime){
		String q = "INSERT INTO Appointment( appointmendID, title, description, appdate, totime, fromtime, ownerid, creationtime) VALUES ( ?, ?, ?, ?, ?, ?,?,? )";
		try {
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			s.setInt(1, AppointmentID);
			s.setString(2, title);
			s.setString(3, descr);
			s.setDate(4, Date.valueOf(date));
			s.setTime(5, Time.valueOf(toTime));
			s.setTime(6, Time.valueOf(fromTime));
			s.setInt(7, Login.getCurrentUserID());
			s.setTime(8,Time.valueOf(LocalTime.now()));
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
		return this.appoinmentID;
	}
	
	public static Appointment get(int id){
		return appointments.get(id);
	}
	
}


