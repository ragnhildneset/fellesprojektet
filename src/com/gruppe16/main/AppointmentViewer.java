package com.gruppe16.main;

import java.util.Collection;

import com.gruppe16.entities.Appointment;

public interface AppointmentViewer {
	void showAppointments(Collection<Appointment> appointments);
}
