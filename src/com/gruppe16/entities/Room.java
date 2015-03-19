package com.gruppe16.entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// TODO: Auto-generated Javadoc
/**
 * The Class Room.
 * 
 * @author Gruppe 16
 */
public class Room {
	
	/** The name of the room. */
	private SimpleStringProperty name;
	
	/** The room's capacity. Indicates how many the room can hold.*/
	private SimpleIntegerProperty capacity;
	
	/** The room ID. */
	private SimpleIntegerProperty roomID;
	
	/** The description. */
	private SimpleStringProperty description;
	
	/** The building ID. */
	private SimpleIntegerProperty buildingID;
	
	/** The building Name. */
	private SimpleStringProperty buildingName;
	
	
	/**
	 * Instantiates a new room.
	 *
	 * @param id the room ID
	 * @param capacity the capacity of the room
	 * @param name the name of the room
	 * @param description the description of the room
	 * @param buildingID the building ID of the building this room belongs to
	 * @param buildingName the building name of the building this room belongs to
	 */
	public Room(int id, int capacity, String name, String description, int buildingID, String buildingName ){

		this.name = new SimpleStringProperty(name);
		this.capacity = new SimpleIntegerProperty(capacity);
		this.roomID = new SimpleIntegerProperty(id);
		this.description = new SimpleStringProperty(description);
		this.buildingID = new SimpleIntegerProperty(buildingID);
		this.buildingName = new SimpleStringProperty(buildingName);
	}
	
	/**
	 * Gets the room's name.
	 *
	 * @return the room name
	 */
	public String getName() {
		return name.get();
	}
	
	/**
	 * Gets the room's capacity.
	 *
	 * @return the room's capacity
	 */
	public int getCapacity() {
		return capacity.get();
	}
	
	/**
	 * Gets the room ID.
	 *
	 * @return the room ID
	 */
	public int getID(){
		return roomID.get();
	}
	
	/**
	 * Gets the description of the room.
	 *
	 * @return the description of the room
	 */
	public String getDescription(){
		return description.get();
	}
	
	/**
	 * Gets the building ID.
	 *
	 * @return the building ID
	 */
	public int getBuildingID(){
		return buildingID.get();
	}
	
	/**
	 * Gets the building's Name
	 *
	 * @return the building's Name
	 */
	public String getBuildingName(){
		return buildingName.get();
	}
	
}

