package com.gruppe16.entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.gruppe16.database.DBConnect;
import com.mysql.jdbc.ResultSet;

public class Room {
	String name;
	int capacity;
	
	private static HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
	
	
	public Room(int key, int capacity, String name, String descr, int buildingID ){
		setName(name);
		setCapacity(capacity);
		rooms.put(key, this);
		
	}
	
	public static void main(String[] args){
		System.out.println(getRooms());
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

