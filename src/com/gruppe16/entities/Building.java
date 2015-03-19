package com.gruppe16.entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Building {

	private SimpleStringProperty name;
	private SimpleStringProperty description;
	private SimpleIntegerProperty buildingid;


	public Building(int key, String name, String descr){
		this.name = new SimpleStringProperty(name);
		this.description = new SimpleStringProperty(descr);
		this.buildingid = new SimpleIntegerProperty(key);
	}
	
	public String getName() {
		return name.get();
	}
	
	
	public int getID(){
		return buildingid.get();
	}
	
	public String getDescription(){
		return description.get();
	}
	
}
