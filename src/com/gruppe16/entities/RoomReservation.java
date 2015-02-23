package com.gruppe16.entities;

import java.time.LocalDateTime;

public class RoomReservation {
	LocalDateTime fromTime;
	LocalDateTime toTime;
	Room room;
	
	RoomReservation(LocalDateTime fromTime, LocalDateTime toTime, Room room) {
		setFromTime(fromTime);
		setToTime(toTime);
		setRoom(room);
	}
	
	public LocalDateTime getFromTime() {
		return fromTime;
	}
	public void setFromTime(LocalDateTime fromTime) {
		this.fromTime = fromTime;
	}
	
	public LocalDateTime getToTime() {
		return toTime;
	}
	public void setToTime(LocalDateTime toTime) {
		this.toTime = toTime;
	}
	
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	
}
