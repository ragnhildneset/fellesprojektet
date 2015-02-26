package com.gruppe16.entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import com.gruppe16.database.DBConnect;
import com.mysql.jdbc.ResultSet;

public class RoomReservation {
	

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
	
	public void findRoom(LocalDate appdate, LocalTime fromtime, LocalTime totime, int capacity){
		ArrayList<String> booked = new ArrayList<String>();
		String q = "SELECT roomid FROM RoomReservation JOIN Appointment ON(RoomReservation.appid = Appointment.appid);";
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				booked.add(rs.getString("roomid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(String booking:booked){
			System.out.print(booking);
		}
		
	}
}
	

