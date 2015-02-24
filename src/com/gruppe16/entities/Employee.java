package com.gruppe16.entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.gruppe16.database.DBConnect;
import com.mysql.jdbc.ResultSet;

public class Employee {
	
	private static HashMap<Integer, Employee> employees = new HashMap<Integer, Employee>();
	
	public static void initialize(){
		String q = "SELECT EmployeeID, GivenName, SurName, Email, Username FROM Employee;";
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				new Employee(Integer.parseInt(rs.getString("EmployeeID")), rs.getString("GivenName"), rs.getString("SurName"), rs.getString("Email"), rs.getString("Username"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addNew(String fname, String lname, String email, String username, byte[] password){
		String q = "INSERT INTO Employee( GivenName, Surname, Email, Username ) VALUES ( ?, ?, ?, ? )";
		try {
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			s.setString(1, fname);
			s.setString(2, lname);
			s.setString(3, email);
			s.setString(4, username);
//			s.setBytes(5, "passord");
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
		for(Map.Entry<Integer, Employee> e : employees.entrySet()){
			ls[c] = e.getValue().getName();
			c++;
		}
		return ls;
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

	
}
