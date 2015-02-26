package com.gruppe16.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.gruppe16.entities.Employee;

public class DBConnect {
	
	private static String userid = "toraho", password = "gruppe16ftw";
	private static String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/toraho_fellesprosjektet";
	
	private static Connection con = null;
	
	public static HashMap<Integer,Employee> getEmployees(){
		String q = "select E.employeeid E.givenName, E.surname, E.email, U.username from Employee as E, UserAndID as U where U.employeeid = E.employeeid;";
		HashMap<Integer, Employee> map = new HashMap<Integer, Employee>();
		try{
			PreparedStatement p = getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) p.executeQuery();
			while(rs.next()){
				int key = rs.getInt("E.employeeid");
				Employee e = new Employee(
						key,rs.getString("E.givenName"), rs.getString("E.surname"), rs.getString("E.email"), rs.getString("username")
						);
				map.put(key, e);
			}
		return map;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Connection getConnection(){
		
		if(con == null){			
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			
			try {
				con = DriverManager.getConnection(url, userid, password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return con;
	}
	
}
