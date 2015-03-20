package com.gruppe16.main;

import java.util.Collection;
import java.util.Date;

import com.gruppe16.entities.Appointment;

/**
 * The Interface CalendarViewInterface.
 * 
 * @author Gruppe 16
 */
public interface CalendarViewInterface {
	
	/**
	 * Show appointments. Shows the appointments inside the view.
	 *
	 * @param appointments the appointments to be shown.
	 */
	void showAppointments(Collection<Appointment> appointments);
	
	/**
	 * Gets the date that is displayed in the view.
	 *
	 * @return the date
	 */
	Date getDate();
	
	/**
	 * Sets the date to be displayed in the view.
	 *
	 * @param date the new date
	 */
	void setDate(Date date);
	
	/**
	 * Increments the date by 1.
	 */
	void incDate();
	
	/**
	 * Decrements the date by 1.
	 */
	void decDate();
}
