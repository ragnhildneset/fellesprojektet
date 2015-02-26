package com.gruppe16.entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import com.gruppe16.database.DBConnect;

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
	
	public Room[] findRoom(LocalDate appdate, LocalTime fromtime, LocalTime totime, int capacity){
		
		
	}
}
	

