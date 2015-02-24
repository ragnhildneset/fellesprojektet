package com.gruppe16.entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.application.Application;

import com.gruppe16.database.DBConnect;
import com.mysql.jdbc.ResultSet;

public class Room{
	int key;
	String name;
	int capacity;
	
	Room(String name, int capacity){
		setName(name);
		setCapacity(capacity);
	}
	
	public static void main(String[] args){
		addNew("Kjemirom", 8);
	}
	
	
	
	public static void initialize(){
		String q = "SELECT * FROM Room;";
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				new Room(Integer.parseInt(rs.getString("RoomID")), rs.getString("RoomName"), Integer.parseInt(rs.getString("Capacity")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addNew(String name, int capacity){
		String q = "INSERT INTO Room(RoomName, Capacity) VALUES ( ?, ?)";
		try {
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			s.setString(1, name);
			s.setInt(2, capacity);
			s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		initialize();
	}
	
	
	public Room(int key, String Name, int capacity) {
		this.key = key;
		this.name = Name;
		this.capacity = capacity;
	}
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}

