package com.gruppe16.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.security.auth.login.Configuration;

public class DBConnect {
	
	private static String userid = "toraho", password = "gruppe16ftw";
	private static String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/toraho_fellesprosjektet";
	private static String driver = null;
	
	private static Connection con = null;
	
	public static void main(String[] args){
		
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
		
		String query = "INSERT INTO baresamme ( ettellerannet ) VALUES (?)";
		try{
			PreparedStatement p = con.prepareStatement(query);
			p.setInt(1, 5);
			p.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected static Connection getConnection(){
    	
        return con;
	}
	
}
