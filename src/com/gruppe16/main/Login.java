package com.gruppe16.main;

import java.sql.PreparedStatement;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Employee;
import com.mysql.jdbc.ResultSet;

public abstract class Login {
	
	private static Employee login = null;
	
	public static boolean login(String username, String password){
		int id = -1;
		try{
			String q = "SELECT username, password, employeeid FROM UserAndID WHERE UserAndID.username=\'" + username + "\';";
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			String passString = null;
			while(rs.next()){
				passString = rs.getString("password");
				id = rs.getInt("employeeid");
			}
			if(password.equals(passString)){
				login = Employee.getEmployee(id);
				System.out.println("Hello, " + login.getFirstName() + "!");
				return true;
			}
			System.out.println("Wrong password or username.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		login = null;
		return false;
	}
	
	public static Employee getCurrentUser(){
		return login;
	}
	
	public static int getCurrentUserID() throws Exception {
		return login.getKey();
	}

}
