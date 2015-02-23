package com.gruppe16.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnect {
	
	private static String userid = "toraho", password = "gruppe16ftw";
	private static String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/toraho_fellesprosjektet";
	
	private static Connection con = null;
	
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
