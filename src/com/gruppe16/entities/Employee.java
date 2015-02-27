package com.gruppe16.entities;
import java.util.HashMap;
import java.util.Map;

import com.gruppe16.database.DBConnect;
import com.mysql.jdbc.ResultSet;

public class Employee {
	
	
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
	
	String firstName;
	String lastName;
	String email;
	String username;
	int key;
	
	public Employee(int key, String firstName, String lastName, String email, String username) {
		employees.put(key, this);
		this.key = key;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
	}
	
	public String getName(){
		return lastName + ", " + firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public int getKey() {
		return key;
	}


	
}
