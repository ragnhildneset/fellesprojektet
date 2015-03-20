package com.gruppe16.main;

import java.sql.PreparedStatement;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Employee;
import com.mysql.jdbc.ResultSet;

/**
 * The Class Login. Used for logging into the calendar.
 * 
 * @author Gruppe 16
 */
public abstract class Login {
	
	/** The employee currently logged in.*/
	private static Employee login = null;
	
	/** Variable to validate if a user is admin. */
	private static boolean isAdmin = false;
	
	/**
	 * Uses username and password to log in to the calendar of an employee. 
	 * Compares the input username and password with the data stored in the database.
	 * Has a check if the user is admin, but is currently not used.
	 *
	 * @param username the username of the employee
	 * @param password the password of the employee
	 * @return true, if successful
	 */
	public static boolean login(String username, String password){
		int id = -1;
		try{
			String q = "SELECT username, password, employeeid, isAdmin FROM UserAndID WHERE UserAndID.username=\'" + username + "\';";
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			String passString = null;
			while(rs.next()){
				passString = rs.getString("password");
				id = rs.getInt("employeeid");
				if(rs.getBoolean("isAdmin")){
					isAdmin = true;
				}
			}
			if(password.equals(passString)){
				login = DBConnect.getEmployees().get(id);
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
	
	/**
	 * Gets the employee currently logged in
	 *
	 * @return the employee logged in
	 */
	public static Employee getCurrentUser(){
		return login;
	}
	
	/**
	 * Gets the current employee's employee ID.
	 *
	 * @return the current employee ID
	 */
	public static int getCurrentUserID() {
		return login.getID();
	}
	
	/**
	 * Checks if is admin.
	 *
	 * @return true, if is admin
	 */
	public static boolean isAdmin(){
		return isAdmin;
	}

}
