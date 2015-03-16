package com.gruppe16.entities;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import com.gruppe16.database.DBConnect;
import com.mysql.jdbc.ResultSet;
import com.gruppe16.main.RoomPicker;
import com.gruppe16.util.Tuple;

public class RoomReservation {
	
	SimpleObjectProperty<LocalDate> date;
	SimpleObjectProperty<LocalTime> fromTime;
	SimpleObjectProperty<LocalTime> toTime;
	SimpleIntegerProperty capacity;
	
	public RoomReservation(LocalDate date, LocalTime fromTime, LocalTime toTime, int capacity){

		this.date = new SimpleObjectProperty<LocalDate>(date);
		this.fromTime = new SimpleObjectProperty<LocalTime> (fromTime);
		this.toTime = new SimpleObjectProperty<LocalTime> (toTime);
		this.capacity = new SimpleIntegerProperty (capacity);
	
	}
	
}


	

