package com.gruppe16.entities;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import com.gruppe16.database.DBConnect;
import com.mysql.jdbc.ResultSet;
import com.gruppe16.main.RoomPicker;
import com.gruppe16.util.Tuple;

public class RoomReservation {
	
	SimpleObjectProperty<LocalDate> date;
	SimpleObjectProperty<LocalTime> fromTime;
	SimpleObjectProperty<LocalTime> toTime;
	SimpleIntegerProperty capacity;
	
	public RoomReservation(LocalDate date, LocalTime fromTime, LocalTime toTime, int capacity){

		this.date = new SimpleObjectProperty<LocalDate>(date);
		this.fromTime = new SimpleObjectProperty<LocalTime> (fromTime);
		this.toTime = new SimpleObjectProperty<LocalTime> (toTime);
		this.capacity = new SimpleIntegerProperty (capacity);
	
	}
	
	public static void addNew(int appid, int roomid){
		String q = "INSERT INTO RoomReservation(appid, roomid) VALUES (?, ?)";
		try {
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			s.setInt(1, appid);
			s.setInt(2, roomid);
			s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void findRoom(Date appdate, Time fromtime, Time totime, int capacity){
		//String dateString = "" + appdate.getYear() + "-" + appdate.getMonth() + "-" + appdate.getDayOfMonth();
		//String totimeString = "" + totime.getHour() + ":" + totime.getMinute() + ":" + totime.getSecond();
		//String fromtimeString = "" + fromtime.getHour() + ":" + fromtime.getMinute() + ":" + fromtime.getSecond();
		ArrayList<Tuple> available = new ArrayList<Tuple>();
		String q = "SELECT Room.roomNumber, Room.buildingID, Room.capacity FROM Room JOIN Building ON(Room.buildingID = Building.buildingID) "
				+ "WHERE Room.roomNumber NOT IN (SELECT RR.roomid FROM Appointment AS A JOIN RoomReservation AS RR ON(A.appointmentID = RR.appid)"
				+ " WHERE A.appdate = "+appdate+" AND A.fromtime <="+fromtime+" AND A.totime >= "+totime+");";
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			s.setDate(1, appdate);
			s.setTime(2, totime);
			s.setTime(3, fromtime);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				available.add(new Tuple<Integer>(rs.getInt("roomNumber"), rs.getInt("buildingID")));
			}new RoomPicker(available);

		} catch (Exception e) {
			e.printStackTrace();
		}
		for(Tuple booking:available){
			System.out.print("" + booking.a + booking.b);
		}
		
	}
}
	

