package com.gruppe16.entities;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Building {

	SimpleStringProperty name;
	SimpleStringProperty description;
	SimpleIntegerProperty buildingid;
	
	private static HashMap<Integer, Building> buildings = new HashMap<Integer, Building>();
	
	
	public Building(int key, String name, String descr){

		this.name = new SimpleStringProperty(name);
		this.description = new SimpleStringProperty(descr);
		this.buildingid = new SimpleIntegerProperty(key);
		buildings.put(key, this);
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
	
	public static String[] getBuildings(){
		String[] ls = new String[buildings.size()];
		int c = 0;
		for(Map.Entry<Integer, Building> e : buildings.entrySet()){
			ls[c] = e.getValue().getName();
			c++;
		}
		return ls;
	}
}
