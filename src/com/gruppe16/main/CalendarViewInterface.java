package com.gruppe16.main;

import java.util.Collection;
import java.util.Date;

import com.gruppe16.entities.Appointment;

public interface CalendarViewInterface {
	void showAppointments(Collection<Appointment> appointments);
	Date getDate();
	void setDate(Date date);
	void incDate();
	void decDate();
}
