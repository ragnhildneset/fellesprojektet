package com.gruppe16.entities;

import java.time.LocalDateTime;

public class Appointment {
	private String title, description;
	private RoomReservation room;
	private Employee host;
	private Employee[] attendees;
	
	public Appointment(Employee host, Employee[] attendees, String title, String description, RoomReservation room) {
		setHost(host);
		setAttendees(attendees);
		setRoomReservation(room);
		setTitle(title);
		setDescription(description);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Employee getHost() {
		return host;
	}

	public void setHost(Employee host) {
		this.host = host;
	}
	
	public Employee[] getAttendees() {
		return attendees;
	}

	public void setAttendees(Employee[] attendees) {
		this.attendees = attendees;
	}
	
	public RoomReservation getRoomReservation() {
		return room;
	}

	public void setRoomReservation(RoomReservation room) {
		this.room = room;
	}
}


