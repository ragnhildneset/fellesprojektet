package com.gruppe16.entities;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

import com.gruppe16.database.DBConnect;
import com.mysql.jdbc.ResultSet;

public class Appointment {
	int AppoinmentID;
	private String title, description;
	//private RoomReservation room;
	LocalDate date;
	LocalTime fromTime;
	LocalTime toTime;
	//private Employee host;
	//private Employee[] attendees;
	
	public Appointment(//Employee host, Employee[] attendees, RoomReservation room
			int AppointmentID, String title, String description, LocalDate date, LocalTime fromTime, LocalTime toTime) {
		//setHost(host);
		//setAttendees(attendees);
		//setRoomReservation(room);
		setTitle(title);
		setDescription(description);
	}
	
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
	
	public static void addNew(String title, String descr, LocalDate date, LocalTime toTime, LocalTime fromTime){
		String q = "INSERT INTO Appointment( Title, Description, Date, ToTime, FromTime) VALUES ( ?, ?, ?, ?, ? )";
		try {
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			s.setString(1, title);
			s.setString(2, descr);
			s.setDate(3, Date.valueOf(date.toString()));
			s.setTime(4, new java.sql.Time(toTime.getMillis()));
			s.setTime(5, java.sql.Time(fromTime.getHour(), fromTime.getMinute(), fromTime.getSecond())));
			s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initialize();
	}
	
	public static void main(String[] args) {
		addNew("meme", "ja", LocalDate.of(2012,1,2), LocalTime.of(19,45), LocalTime.of(18,45)); 
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
/*
	public Employee getHost() {
		return host;
	}

	public void setHost(Employee host) {
		this.host = host;
	}
	
	public Employee[] getAttendees() {
		return attendees;
	}

	public void setAttendees(Employee[] attendees) {
		this.attendees = attendees;
	}
	
	public RoomReservation getRoomReservation() {
		return room;
	}

	public void setRoomReservation(RoomReservation room) {
		this.room = room;
	}
	
*/
	public LocalTime getFromTime() {
		return fromTime;
	}
	public void setFromTime(LocalTime fromTime) {
		this.fromTime = fromTime;
	}
	
	public LocalTime getToTime() {
		return toTime;
	}
	public void setToTime(LocalTime toTime) {
		this.toTime = toTime;
	}
}


