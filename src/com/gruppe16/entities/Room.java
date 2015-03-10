package com.gruppe16.entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import com.gruppe16.database.DBConnect;
import com.mysql.jdbc.ResultSet;

public class Room {
	
	SimpleStringProperty name;
	SimpleIntegerProperty capacity;
	SimpleIntegerProperty roomid;
	SimpleStringProperty description;
	SimpleIntegerProperty buildingid;
	SimpleStringProperty buildingname;
	
	
	private static HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
	
	
	public Room(int key, int capacity, String name, String descr, int buildingID, String buildingname ){

		this.name = new SimpleStringProperty(name);
		this.capacity = new SimpleIntegerProperty(capacity);
		this.roomid = new SimpleIntegerProperty(key);
		this.description = new SimpleStringProperty(descr);
		this.buildingid = new SimpleIntegerProperty(buildingID);
		this.buildingname = new SimpleStringProperty(buildingname);
		rooms.put(key, this);
	}
	
	public static void main(String[] args){
		System.out.println(getRooms());
	}
	
	public String getName() {
		return name.get();
	}
	
	public int getCapacity() {
		return capacity.get();
	}
	
	public int getID(){
		return roomid.get();
	}
	
	public String getDescription(){
		return description.get();
	}
	
	public int getBuildingID(){
		return buildingid.get();
	}
	
	public String getBuildingname(){
		return buildingname.get();
	}
	
	public static String[] getRooms(){
		String[] ls = new String[rooms.size()];
		int c = 0;
		for(Map.Entry<Integer, Room> e : rooms.entrySet()){
			ls[c] = e.getValue().getName();
			System.out.println(e.getValue().getName());
			c++;
		}
		return ls;
	}
}

