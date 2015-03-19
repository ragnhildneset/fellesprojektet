package com.gruppe16.entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Room {
	
	private SimpleStringProperty name;
	private SimpleIntegerProperty capacity;
	private SimpleIntegerProperty roomid;
	private SimpleStringProperty description;
	private SimpleIntegerProperty buildingid;
	private SimpleStringProperty buildingname;
	
	
	public Room(int key, int capacity, String name, String descr, int buildingID, String buildingname ){

		this.name = new SimpleStringProperty(name);
		this.capacity = new SimpleIntegerProperty(capacity);
		this.roomid = new SimpleIntegerProperty(key);
		this.description = new SimpleStringProperty(descr);
		this.buildingid = new SimpleIntegerProperty(buildingID);
		this.buildingname = new SimpleStringProperty(buildingname);
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
	
}

