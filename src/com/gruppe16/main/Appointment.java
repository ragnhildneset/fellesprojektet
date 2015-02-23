package com.gruppe16.main;

import java.time.LocalDateTime;

public class Appointment {
	private LocalDateTime date;
	private LocalDateTime fromTime, toTime;
	public LocalDateTime getDate() {
		return date;
	}
	

	public void setDate(LocalDateTime date) {
		this.date = date;
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

	private String title, description;


}
