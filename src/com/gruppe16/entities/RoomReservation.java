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
	public static void main(String[] args){
		LocalDate d = LocalDate.of(2015,3,2);
		Date sqlD = java.sql.Date.valueOf(d);
		findRoom(LocalDate.of(2015,3,2), LocalTime.of(9,0), LocalTime.of(14,0), 10);
	}
	
	
	public static void findRoom(LocalDate appdate, LocalTime fromtime, LocalTime totime, int capacity){
		String dateString = "" + appdate.getYear() + "-" + appdate.getMonth() + "-" + appdate.getDayOfMonth();
		String totimeString = "" + totime.getHour() + ":" + totime.getMinute() + ":" + totime.getSecond();
		String fromtimeString = "" + fromtime.getHour() + ":" + fromtime.getMinute() + ":" + fromtime.getSecond();
		ArrayList<Tuple> available = new ArrayList<Tuple>();
		String q = "SELECT Room.roomNumber, Room.buildingID, Room.capacity FROM Room JOIN Building ON(Room.buildingID = Building.buildingID) "
				+ "WHERE Room.roomNumber NOT IN (SELECT RR.roomid FROM Appointment AS A JOIN RoomReservation AS RR ON(A.appointmentID = RR.appid)"
				+ " WHERE Room.capacity < " + capacity + " OR (A.appdate = '"+ dateString +"' AND A.fromtime <= '"+ fromtimeString+"' AND A.totime >= '" +totimeString+ "'));";
		System.out.println(q);
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				available.add(new Tuple<Integer>(rs.getInt("roomNumber"), rs.getInt("buildingID")));
				
			}//sende videre til roompicker?
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(Tuple booking:available){
			System.out.println("");
			System.out.print(" Room: " + booking.a + " Building: "+ booking.b);
		}
		
	}
}
	

