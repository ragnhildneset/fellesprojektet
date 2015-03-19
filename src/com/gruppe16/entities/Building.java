package com.gruppe16.entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * The Class Building.
 * 
 * @author Gruppe 16
 */
public class Building {

	/** The name of the building. */
	private SimpleStringProperty name;
	
	/** The description. */
	private SimpleStringProperty description;
	
	/** The building ID. */
	private SimpleIntegerProperty id;


	/**
	 * Instantiates a new building.
	 *
	 * @param buildingID the building ID
	 * @param name the name of the building
	 * @param description the description
	 */
	public Building(int buildingID, String name, String description){
		this.name = new SimpleStringProperty(name);
		this.description = new SimpleStringProperty(description);
		this.id = new SimpleIntegerProperty(buildingID);
	}
	
	/**
	 * Gets the name of the building.
	 *
	 * @return the name
	 */
	public String getName() {
		return name.get();
	}
	
	
	/**
	 * Gets the building id.
	 *
	 * @return the id
	 */
	public int getID(){
		return id.get();
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription(){
		return description.get();
	}
	
}
