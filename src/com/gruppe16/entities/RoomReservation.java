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
	public static void main(String[] args){
		findRoom(LocalDate.of(2015,3,3), LocalTime.of(19,0), LocalTime.of(21,0), 10);
	}
	
	
	public static void findRoom(LocalDate appdate, LocalTime fromtime, LocalTime totime, int capacity){
		String dateString = "" + appdate.getYear() + "-" + appdate.getMonthValue() + "-" + appdate.getDayOfMonth();
		String totimeString = "" + totime.getHour() + ":" + totime.getMinute() + ":" + totime.getSecond();
		String fromtimeString = "" + fromtime.getHour() + ":" + fromtime.getMinute() + ":" + fromtime.getSecond();
		ArrayList<Room> available = new ArrayList<Room>();
		String q = "select D.roomNumber, D.BuildingID\n"+
				"from Room as D\n"+
				"where (D.roomNumber, D.BuildingID) not in (\n"+
				"select R.roomNumber, R.BuildingID\n"+
				"from Room as R, RoomReservation as E, Appointment as A\n"+
				"where  E.appid = A.appointmentID\n"+
				"and E.BuildingID = R.BuildingID\n"+
				"and E.roomid = R.roomNumber\n"+
				"and A.appdate = '"+dateString+"'\n"+
				"and ('"+fromtimeString+"' between A.fromtime and A.totime or '"+totimeString+"' between A.fromtime and A.totime)\n"+
				") and D.capacity >= "+capacity+";";
		System.out.println(q);
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){

				available.add(new Room(rs.getInt("roomNumber"), rs.getInt("capacity"), rs.getString("name"), rs.getString("descr"), rs.getInt("buildingID"), rs.getString("buildingName")));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(Tuple booking:available){
			System.out.println("");
			System.out.print(" Room: " + booking.a + " Building: "+ booking.b);
		}
		
	}
	
}


	

