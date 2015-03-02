package com.gruppe16.entities;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import com.gruppe16.database.DBConnect;
import com.mysql.jdbc.ResultSet;

public class Employee {
	

	SimpleStringProperty firstName;
	SimpleStringProperty lastName;
	SimpleStringProperty email;
	SimpleStringProperty username;
	SimpleIntegerProperty employeeid;
	
	private static HashMap<Integer, Employee> employees = new HashMap<Integer, Employee>();
	
	public static String[] getNames(){
		String[] ls = new String[employees.size()];
		int c = 0;
		for(Map.Entry<Integer, Employee> e : employees.entrySet()){
			ls[c] = e.getValue().getName();
			c++;
		}
		return ls;
	}
	
	public static Employee getEmployee(int key){
		return employees.get(key);
	}
	
	
	public Employee(int key, String firstName, String lastName, String email, String username) {
		employees.put(key, this);
		this.employeeid =  new SimpleIntegerProperty(key);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName =  new SimpleStringProperty(lastName);
		this.email =  new SimpleStringProperty(email);
		this.username = new SimpleStringProperty(username);
	}
	
	public String getName(){
		return lastName + ", " + firstName;
	}

	public String getFirstName() {
		return firstName.get();
	}

	public String getLastName() {
		return lastName.get();
	}

	public String getEmail() {
		return email.get();
	}

	public String getUsername() {
		return username.get();
	}

	public int getEmployeeID() {
		return employeeid.get();
	}


	
}
