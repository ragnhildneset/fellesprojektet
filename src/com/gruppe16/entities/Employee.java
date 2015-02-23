package com.gruppe16.entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.gruppe16.database.DBConnect;
import com.mysql.jdbc.ResultSet;

public abstract class Employee {
	
	public String firstName;
	public String lastName;
	public String email;
	char[] phone;
	
	private static HashMap<Integer, ActualEmployee> employees = new HashMap<Integer, ActualEmployee>();
	
	public static void initialize(){
		String q = "SELECT * FROM Employee;";
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				new ActualEmployee((Integer.parseInt(rs.getString("EmployeeID"))), rs.getString("GivenName"), rs.getString("SurName"), rs.getString("Email"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addNew(String fname, String lname, String email){
		String q = "INSERT INTO Employee( GivenName, Surname, Email ) VALUES ( ?, ?, ? )";
		try {
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			s.setString(1, fname);
			s.setString(2, lname);
			s.setString(3, email);
			s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		employees.clear();
		initialize();
	}
	
	public static String[] getNames(){
		String[] ls = new String[employees.size()];
		int c = 0;
		for(Map.Entry<Integer, ActualEmployee> e : employees.entrySet()){
			ls[c] = e.getValue().getName();
			c++;
		}
		return ls;
	}
	
	private static class ActualEmployee {
		String firstName;
		String lastName;
		String email;
		int key;
		public ActualEmployee(int key, String firstName, String lastName, String email) {
			employees.put(key, this);
			this.key = key;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
		}
		public String getName(){
			return lastName + ", " + firstName;
		}
	}
	
}
