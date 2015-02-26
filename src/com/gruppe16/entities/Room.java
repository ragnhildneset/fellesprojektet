package com.gruppe16.entities;

public class Room {
	String name;
	int capacity;
	
	Room(String name, int capacity){
		setName(name);
		setCapacity(capacity);
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

